package top.ezttf.ad.exception;

/**
 * @author yuwen
 * @date 2019/1/20
 */
public class AdUnitException extends AdException {
    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public AdUnitException(String message) {
        super(message);
    }
}
