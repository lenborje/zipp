package lb.zipp;

/**
 * Thrown when {@link Option#parseOptionName(String)} can't recognise the given argument.
 *
 * @author Lennart Börjeson
 *
 */
public class IllegalOptionException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates this exception.
     * @param s Reason message
     */
    public IllegalOptionException(final String s) {
        super(s);
    }

}

