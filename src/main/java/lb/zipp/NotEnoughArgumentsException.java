package lb.zipp;

/**
 * Thrown by {@link Zipp#main(String[])} when not enough arguments were given.
 *
 * @author Lennart Börjeson
 *
 */
class NotEnoughArgumentsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates this exception with the given reason string.
     * @param string Reason message
     */
    NotEnoughArgumentsException(final String string) {
        super(string);
    }

}
