package me.sirimperivm.spigot.nms;
/**
 * Thrown to indicate that the invoked NMS operation failed.
 */

@SuppressWarnings("all")
public class NmsOperationException extends Exception{

    /**
     * Creates a new {@code NmsOperationException}.
     *
     * @param message the exception detail message
     */
    public NmsOperationException(String message) {
        super(message);
    }

    /**
     * Creates a new {@code NmsOperationException}.
     *
     * @param cause the cause of the exception
     */
    public NmsOperationException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new {@code NmsOperationException}.
     *
     * @param message the exception detail message
     * @param cause the cause of the exception
     */
    public NmsOperationException(String message, Throwable cause) {
        super(message, cause);
    }

}
