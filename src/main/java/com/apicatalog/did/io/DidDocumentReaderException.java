package com.apicatalog.did.io;

/**
 * Exception thrown when a DID Document cannot be read or parsed.
 */
public class DidDocumentReaderException extends Exception {

    private static final long serialVersionUID = 2956582473611314812L;

    /**
     * Creates a new exception with the specified detail message.
     *
     * @param message the detail message
     */
    public DidDocumentReaderException(String message) {
        super(message);
    }

    /**
     * Creates a new exception with the specified cause.
     *
     * @param cause the cause of this exception
     */
    public DidDocumentReaderException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new exception with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of this exception
     */
    public DidDocumentReaderException(String message, Throwable cause) {
        super(message, cause);
    }
}
