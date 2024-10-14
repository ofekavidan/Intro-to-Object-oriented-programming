package ascii_art;


/**
 * The ResolutionOutOfBoundsException class represents an exception that is thrown
 * when a resolution is out of bounds in an image.
 *
 * <p>This exception extends IndexOutOfBoundsException.
 */
public class ResolutionOutOfBoundsException extends IndexOutOfBoundsException {

    private static final String ERROR_MESSAGE_PREFIX = "Resolution out of image boundaries: ";

    /**
     * Constructs an ResolutionOutOfBoundsException with no detail message.
     */
    public ResolutionOutOfBoundsException() {
        super();
    }

    /**
     * Constructs an ResolutionOutOfBoundsException with the specified detail message.
     * @param s the detail message
     */
    public ResolutionOutOfBoundsException(String s) {
        super(s);
    }

    /**
     * Constructs a new ResolutionOutOfBoundsException class with an
     * argument indicating the illegal resolution.
     * <p> The resolution is included in this exception's detail message.  The
     * exact presentation format of the detail message is unspecified.
     *
     * @param res the illegal resolution.
     */
    public ResolutionOutOfBoundsException(int res) {
        super(ERROR_MESSAGE_PREFIX + res);
    }
}
