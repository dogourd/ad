package top.ezttf.ad.exception;

/**
 * @author yuwen
 * @date 2019/1/19
 */
public class AdException extends Exception {

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public AdException(String message) {
        super(message);
    }
}
