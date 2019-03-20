import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Walk {

	public static void main(String[] args) {
		Stream<Path> paths = Stream.of(args).map(Paths::get);
		Stream<Path> allFiles = walk(paths);
		allFiles.forEach(Walk::checkPath);
	}
	
	public static void checkPath(Path path) {
		System.out.printf("Path: %s (%s)%n", path, Files.exists(path));
		URI uri = path.toUri();
		System.out.printf(" URI: %s%n", uri);
		Path path2 = uri2Path(uri);
		System.out.printf("back: %s (%s)%n", path2, Files.exists(path2));
		System.out.printf("2URI: %s%n", path2.toUri());
		System.out.println();
	}
	
	private static Path uri2Path(URI uri) {
		String scheme = uri.getScheme();
		if (scheme.startsWith("jar")) {
			String[] parts = uri.toString().split("!");
			FileSystem jarFS = FileSystems.newFileSystem(path, loader)
		}
	}

	private static Stream<Path> walk(Stream<Path> paths) {
		return paths.flatMap(Walk::walk);
	}
	
	private static Stream<Path> walk(Path path) {
		if (Files.isRegularFile(path)) {
			if (path.getFileName().toString().toLowerCase().endsWith(".zip"))
			try {
				// Try for zip
				return walk(zipList(path));
			} catch (Exception e) {
				// Fall back to regular file
			}
			return Stream.of(path);
		} else if (Files.isDirectory(path)) {
			Stream<Path> list;
			try {
				list = Files.list(path);
			} catch (IOException e) {
//				e.printStackTrace(System.err);
				list = Stream.empty();
			}
			return Stream.concat(Stream.of(path), walk(list));
		}
		return Stream.empty();
	}

	private static Stream<Path> zipList(Path zipPath) throws IOException, URISyntaxException {
		FileSystem zipArchive = FileSystems.newFileSystem(zipPath, null);
		Iterable<Path> rootDirectories = zipArchive.getRootDirectories();
		Spliterator<Path> roots = Spliterators.spliteratorUnknownSize(rootDirectories.iterator(),0);
		return StreamSupport.stream(roots, false);
	}
	
	

}
