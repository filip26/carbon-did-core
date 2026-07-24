package com.apicatalog.did.resolver;

/**
 * Exception thrown during DID resolution.
 * <p>
 * Indicates resolution failures such as invalid input, not found, unsupported
 * representation, or internal errors.
 * </p>
 */
public class DidResolutionException extends Exception {

    private static final long serialVersionUID = -7104603698482015381L;

    /**
     * Standard resolution error codes.
     */
    public enum ErrorCode {
        /** The DID syntax is invalid and cannot be parsed. */
        INVALID_DID,

        /** The DID method resolution is not supported. */
        UNSUPPORTED_METHOD,

        /** The DID could not be found. */
        NOT_FOUND,

        /** An internal error occurred. */
        INTERNAL,

    }

    protected final String did;
    protected final ErrorCode code;

    /**
     * Creates a new resolution exception with a DID and code.
     *
     * @param did  the DID being resolved (may be {@code null})
     * @param code error code
     */
    public DidResolutionException(String did, ErrorCode code) {
        this.did = did;
        this.code = code;
    }

    /**
     * Creates a new resolution exception with a message.
     *
     * @param did     the DID being resolved (may be {@code null})
     * @param code    error code
     * @param message detail message
     */
    public DidResolutionException(String did, ErrorCode code, String message) {
        super(message);
        this.did = did;
        this.code = code;
    }

    /**
     * Creates a new resolution exception with a cause.
     * <p>
     * Code is set to {@link ErrorCode#INTERNAL}.
     * </p>
     *
     * @param did the DID being resolved (may be {@code null})
     * @param e   the cause
     */
    public DidResolutionException(String did, Throwable e) {
        super(e);
        this.did = did;
        this.code = ErrorCode.INTERNAL;
    }

    /**
     * Creates a new resolution exception with a message and cause.
     * <p>
     * Code is set to {@link ErrorCode#INTERNAL}.
     * </p>
     *
     * @param did     the DID being resolved (may be {@code null})
     * @param message detail message
     * @param e       the cause
     */
    public DidResolutionException(String did, String message, Throwable e) {
        super(message, e);
        this.did = did;
        this.code = ErrorCode.INTERNAL;
    }

    /**
     * Creates a new resolution exception.
     * <p>
     * Code is set to {@link ErrorCode#INTERNAL}.
     * </p>
     *
     * @param did     the DID being resolved (may be {@code null})
     * @param code    error code
     * @param message detail message
     * @param e       the cause
     */
    public DidResolutionException(String did, ErrorCode code, String message, Throwable e) {
        super(message, e);
        this.did = did;
        this.code = code;
    }

    /**
     * Returns the DID that failed to resolve.
     *
     * @return the DID, or {@code null}
     */
    public String getDid() {
        return did;
    }

    /**
     * Returns the resolution error code.
     *
     * @return error code
     */
    public ErrorCode getCode() {
        return code;
    }
}
