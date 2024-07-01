package me.sirimperivm.spigot.nms;

/**
 * Thrown to indicate failure when creating an {@link NmsAdapter}.
 */
@SuppressWarnings("all")
public class CreateNmsAdapterException extends Exception {

    /**
     * Creates a new {@code CreateNmsAdapterException}.
     *
     * @param message the exception detail message
     */
    public CreateNmsAdapterException(String message) {
        super(message);
    }

    /**
     * Creates a new {@code CreateNmsAdapterException}.
     *
     * @param message the exception detail message
     * @param cause the cause of the exception
     */
    public CreateNmsAdapterException(String message, Throwable cause) {
        super(message, cause);
    }

}

