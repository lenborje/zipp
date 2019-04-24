package lb.zipp;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

/**
 * Represents Zip processing options. (Internal to this application; not needed by the jar FileSystem.)
 * @author Lennart Börjeson
 *
 */
public enum Option {
    /**
     * Requests that all file additions should be executed in parallel.
     */
    PARALLEL,

    /**
     * Requests that any directory specified as input should be
     * recursively traversed and all files found added individually to the
     * Zip archive. Paths will be preserved.
     */
    RECURSIVE,

    /**
     * Prints test timings.
     */
    TEST,

    /**
     * Creates a number of temporary test files which are then added to the target archive.
     */
    GENERATE;

    /**
     * Maps names to options
     */
    private static final Map<String, Option> name2option = new HashMap<>();
    private final String shortName;
    private final String longName;

    private static void register(final Option o) {
        name2option.put(o.shortName, o);
        name2option.put(o.longName, o);
    }

    /*
     * Collect and register all options
     */
    static {
        for (Option o : values()) {
            register(o);
        }
    }

    /**
     * Creates an lb.zipp.Option with the given short and long names. Don't specify any hyphen/dash in the name!
     * @param shortName Short name
     * @param longName Long name, without any leading dashes or hyphens
     */
    Option(final char shortName, final String longName) {
        this.shortName = "-"+shortName;
        this.longName = "--"+longName;
    }

    /**
     * Creates an lb.zipp.Option with the given long name. The short name will be the first character of the long name.
     * @param longName Long name, without any leading dashes or hyphens
     */
    Option(final String longName) {
        this(longName.charAt(0), longName);
    }

    /**
     * Creates an lb.zipp.Option, using the lower case of the declared name as the long name. The short name will be the first character of the long name.
     */
    Option() {
        this.longName = "--"+name().toLowerCase();
        this.shortName = "-"+name().toLowerCase().charAt(0);
    }

    /**
     * Parses a string as either the long or short representation of an lb.zipp.Option.
     * @param optionName Name of the option
     * @return Parsed lb.zipp.Option
     * @throws IllegalOptionException If string argument isn't recognised as an option.
     */
    public static Option parseOptionName(final String optionName) {
        Option result = name2option.get(optionName);
        if (result == null) {
            throw new IllegalOptionException(String.format("Unrecognised option '%s'", optionName));
        }
        return result;
    }

    /**
     * Prepend a single char (specified as int, as {@link String#chars()} returns integers) with
     * a dash, and return this string.
     * @param c char, specified as int
     * @return "-" + given char
     */
    private static String singleCharToOptionName(final int c) {
        return "-" + (char) c;
    }
    /**
     * Explode a given single-dash option sequence (e.g. "-rp") to a stream of
     * corresponding stream of single-character options (e.g. "-r", "-p").
     * @param optionName Name of option
     * @return Stream of single-character optionNames
     */
    public static Stream<String> explodeSingleDashOptions(final String optionName) {
        if (optionName.startsWith("--"))
            return Stream.of(optionName); // Do nothing, just return this option name
        // Else explode concatenated single-character options to individual options
        return optionName.substring(1) // Remove first dash
                .chars() // explode to single char stream
                .mapToObj(Option::singleCharToOptionName); // Convert to option name
    }

    /**
     * Returns a syntax string for this Options, e.g. "[-p|--parallel]"
     * @return Single option syntax string
     */
    private String syntax() {
        return String.format("[%s|%s]", shortName, longName);
    }

    /**
     * Returns the combined syntax string for all Options.
     * @return Options syntax string
     */
    public static String optionsSyntax() {
        return
                Stream.of(values())
                        .map(Option::syntax)
                        .collect(joining(" "));
    }
}
