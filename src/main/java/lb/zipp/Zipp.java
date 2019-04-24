package lb.zipp;

import com.sun.management.OperatingSystemMXBean;
import com.thedeanda.lorem.*;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.*;

/**
 * This class creates zip archives. Instead of directly using {@link java.util.zip.ZipOutputStream},
 * this implementation uses the jar {@link java.nio.file.FileSystem} available since Java 1.7.<p>
 * The advantage of using a {@code FileSystem} is that it can easily be processed in parallel.<p>
 * This class can create zip archives with parallel execution by combining parallel {@link Stream} processing
 * with the jar {@code FileSystem}.<p>
 * This class has a {@link #main(String[])} method which emulates a minimal command-line zip utility, i.e.
 * it can be used to create standard zip archives.
 *
 * @author Lennart Börjeson
 *
 */
@SuppressWarnings({"SpellCheckingInspection"})
public class Zipp implements Closeable {
    private static final int availCPUs = Runtime.getRuntime().availableProcessors();
    private final FileSystem zipArchive;
    private final boolean recursive;
    private final boolean parallel;

    private static String getMessage(final Message key) {
        return Message.getMessage(key);
    }


    /**
     * Creates and initialises a Zip archive.
     * @param archiveName name (file path) of the archive
     * @param options {@link Option}
     * @throws IOException Thrown on any underlying IO errors
     */
    private Zipp(final String archiveName, Option... options) throws IOException {
        Set<Option> options1 = Collections.unmodifiableSet(Stream.of(options).collect(toSet()));
        this.recursive = options1.contains(Option.RECURSIVE);
        this.parallel = options1.contains(Option.PARALLEL);

        final Path zipPath = Paths.get(archiveName);

        final Map<String, String> zipParams = new HashMap<>();
        zipParams.put("create", "true");
//        zipParams.put("useTempFile", "true");

        final URI resolvedFileURI = zipPath.toAbsolutePath().toUri();
        final URI zipURI = URI.create("jar:"+resolvedFileURI);

        System.out.printf(getMessage(Message.working), zipURI, options1);

        zipArchive = FileSystems.newFileSystem(zipURI, zipParams);
    }

    /**
     * Adds one file to the archive.
     *
     * @param f
     *            Path of file to add, not null
     */
    private void zipOneFile(final Path f) {
        try {
            final Path parent = f.getParent();
            if (parent != null && parent.getNameCount() > 0)
                Files.createDirectories(zipArchive.getPath(parent.toString()));
            final Path zipEntryPath = zipArchive.getPath(f.toString());
            String message = " "+getMessage(Message.adding)+": %s";
            if (Files.exists(zipEntryPath)) {
                Files.deleteIfExists(zipEntryPath);
                message = " "+getMessage(Message.updating)+": %s";
            }
            final StringBuilder logbuf = new StringBuilder();
            try (OutputStream out = Files.newOutputStream(zipEntryPath)) {
                logbuf.append(String.format(message, f));
                Files.copy(f, out);
                out.flush();
            } catch (Exception e) {
                System.err.printf("Error adding %s:%n", f);
                e.printStackTrace(System.err);
                return;
            }
            final long size = (long) Files.getAttribute(zipEntryPath, "zip:size");
            final long compressedSize = (long) Files.getAttribute(zipEntryPath, "zip:compressedSize");
            final double compression = (size-compressedSize)*100.0/size;
            final int method = (int) Files.getAttribute(zipEntryPath, "zip:method");
            final String methodName = method==0?"stored":method<8?getMessage(Message.compressed):getMessage(Message.deflated);
            logbuf.append(String.format(" (%4$s %3$.0f%%)", size, compressedSize, compression, methodName));
            synchronized(System.out) {
                System.out.println(logbuf);
            }
        } catch (Exception e1) {
            throw new RuntimeException(String.format(" Error accessing zip archive for %s:", f), e1);
        }
    }

    @Override
    public void close() throws IOException {
        zipArchive.close();
    }

    /**
     * Adds files, given as a {@link List} of file names, to this Zip archive.
     * <p>
     * If the option {@link Option#RECURSIVE} was specified in the constructor,
     * any directories specified will be traversed and all files found will be added.
     * <p>
     * If the option {@link Option#PARALLEL} was specified in the constructor,
     * all files found will be added in parallel.
     * @param fileNameArgs List of file names, not null
     */
    private void addFiles(final List<String> fileNameArgs) {

        final Stream<Path> distinctPaths =
                fileNameArgs.stream()			// Process file name list
                        .map(File::new) 				// String -> File
                        .flatMap(this::filesWalk)		// Find file, or, if recursive, files
                        .map(Path::normalize) 			// Ensure no contrived paths
                        .distinct();				    // Only distinct set (implies intermediate list collection)

        // If parallel processing requested, use parallel stream,
        // else use normal stream.
        final Stream<Path> streamOfPaths =
                parallel ? distinctPaths.parallel() : distinctPaths;

        streamOfPaths.forEach(this::zipOneFile); 		// zip them all!
    }

    /**
     * If the given {@link File} argument represents a real file (i.e.
     * {@link Files#isRegularFile(Path, LinkOption...)} returns {@code true}), converts the given file
     * argument to a {@link Stream} of a single {@link Path} (of the given file
     * argument).
     * <p>
     * Else, if {@link Option#RECURSIVE} was specified in the constructor
     * {@link Zipp#Zipp(String, Option...)}, assumes the file represents a directory and then uses
     * {@link Files#walk(Path, java.nio.file.FileVisitOption...)} to return a
     * {@code Stream} of all real files contained within this directory tree.
     * <p>
     * Returns an empty stream if any errors are encountered.
     *
     * @param f
     *            File, representing a file or directory.
     * @return Stream of all Paths resolved
     */
    private Stream<Path> filesWalk(final File f) {
        // If argument is a file, return directly as single-item stream
        if (f.isFile()) {
            return Stream.of(f.toPath());
        }

        // Check if argument is a directory and RECURSIVE option specified
        if (f.isDirectory() && this.recursive)
            try {
                // Traverse directory and return all files found
                return Files.walk(f.toPath(), FileVisitOption.FOLLOW_LINKS)
                        .filter(Files::isRegularFile); // Only return real files
            } catch (IOException e) {
                throw new RuntimeException(String.format(getMessage(Message.errtrav), f), e);
            }

        // Argument is neither file nor directory: Return empty stream
        return Stream.empty();
    }

    /**
     * Prints simple usage info.
     */
    private static void usage() {
        System.err.println();
        System.err.print(getMessage(Message.usage1));
        System.err.println();
        System.err.println();
    }

    /**
     * Main entry point, when launched as stand-alone application.
     * @param args Command-line arguments.
     */
    public static void main(final String[] args) {
        boolean test = false;
        boolean generate;
        long beginUserMillis, midUserMillis, endUserMillis;
        long beginCPUNanos, midCPUNanos, endCPUNanos;
        beginUserMillis = midUserMillis = System.currentTimeMillis();
        beginCPUNanos = midCPUNanos = ((OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean()).getProcessCpuTime();
        try {
            // This list will eventually contain only the file name arguments
            final LinkedList<String> fileArgs = Stream.of(args).collect(toCollection(LinkedList::new));

            // Collect all option arguments
            final List<String> optionArgs = fileArgs.stream().filter(s->s.startsWith("-")).collect(toList());

            // Remove option arguments from file name list
            fileArgs.removeAll(optionArgs);

            // Parse option arguments and convert to options array. Exceptions might be thrown here.
            final Option[] options =
                    optionArgs.stream()
                            .flatMap(Option::explodeSingleDashOptions)
                            .map(Option::parseOptionName)
                            .toArray(Option[]::new);

            // Check argument count. At least one zip file and one file/dir to be added to the zip is required.
            if (fileArgs.isEmpty()) {
                throw new NotEnoughArgumentsException(getMessage(Message.noargs));
            }

            final String zipName = fileArgs.removeFirst(); // Remove zip name argument

            generate = Arrays.binarySearch(options, Option.GENERATE) >= 0;
            test = Arrays.binarySearch(options, Option.TEST) >= 0;
            if (generate) {
                fileArgs.clear();
                int numTempFiles = availCPUs * 10;
                System.out.printf(getMessage(Message.cretemp), numTempFiles, availCPUs);
                Lorem loremIpsum = LoremIpsum.getInstance();
                Stream.generate(()->{
                    try{
                        return Files.createTempFile("ZippTest", ".txt").toFile();
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                }).limit(numTempFiles).parallel().forEach(tmpf-> {
                    synchronized (System.out) {
                        System.out.print(".");
                    }
                    tmpf.deleteOnExit();
                    try(FileWriter fw = new FileWriter(tmpf)) {
                         String txt = loremIpsum.getWords(7_000_000, 10_000_000);
                         fw.write(txt);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                    fileArgs.add(tmpf.getPath());
                });
                System.out.println(getMessage(Message.zipdone0));
            }

            // Check argument count. At least one zip file and one file/dir to be added to the zip is required.
            if (fileArgs.isEmpty()) {
                throw new NotEnoughArgumentsException(getMessage(Message.noargs));
            }

            beginUserMillis = midUserMillis = System.currentTimeMillis();
            beginCPUNanos = midCPUNanos = ((OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean()).getProcessCpuTime();

            try (Zipp zip = new Zipp(zipName, options)) {  // Initialise zip archive

                zip.addFiles(fileArgs);	// Add files

                if (test) {
                    midUserMillis = System.currentTimeMillis();
                    midCPUNanos = ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getProcessCpuTime();
                    long userMillis = midUserMillis - beginUserMillis;
                    double cpuMillis = (midCPUNanos - beginCPUNanos) / 1E6;
                    System.out.printf(getMessage(Message.tstadd), (long) cpuMillis, userMillis, cpuMillis / userMillis);
                }

                System.out.print(getMessage(Message.zpclos));
            }
            System.out.println(getMessage(Message.zipdone0));

            System.out.println(getMessage(Message.zpdone));

        } catch (NotEnoughArgumentsException | IllegalOptionException re) {
            System.err.println(re.getMessage());
            usage();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            System.exit(2);
        }
        if (test) {
            endUserMillis = System.currentTimeMillis();
            endCPUNanos = ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getProcessCpuTime();
            long userMillis = endUserMillis - midUserMillis;
            double cpuMillis = (endCPUNanos - midCPUNanos) / 1E6;
            System.out.printf(getMessage(Message.tstclose), (long) cpuMillis, userMillis, cpuMillis / userMillis);
            userMillis = endUserMillis - beginUserMillis;
            cpuMillis = (endCPUNanos - beginCPUNanos) / 1E6;
            System.out.printf(getMessage(Message.tsttotal), (long) cpuMillis, userMillis, cpuMillis / userMillis);
            System.out.printf(getMessage(Message.tstproc), availCPUs);
        }
    }
}
