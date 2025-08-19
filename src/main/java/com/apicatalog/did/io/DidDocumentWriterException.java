package com.apicatalog.did.io;

/**
 * Exception thrown when a DID Document cannot be written or serialized.
 */
public class DidDocumentWriterException extends Exception {

    private static final long serialVersionUID = -3790735310020905272L;

    /**
     * Creates a new exception with the specified detail message.
     *
     * @param message the detail message
     */
    public DidDocumentWriterException(String message) {
        super(message);
    }

    /**
     * Creates a new exception with the specified cause.
     *
     * @param cause the cause of this exception
     */
    public DidDocumentWriterException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new exception with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of this exception
     */
    public DidDocumentWriterException(String message, Throwable cause) {
        super(message, cause);
    }
}
