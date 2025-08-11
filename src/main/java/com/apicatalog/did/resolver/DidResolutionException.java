package com.apicatalog.did.resolver;

import com.apicatalog.did.Did;

public class DidResolutionException extends Exception {

    private static final long serialVersionUID = -7104603698482015381L;

    public enum Code {
        InvalidDid,
        NotFound,
        RepresentationNotSupported,
        Internal,
    }

    protected final Did did;
    protected final Code code;

    public DidResolutionException(Did did, Code code) {
        this.did = did;
        this.code = code;
    }

    public DidResolutionException(Did did, Code code, String message) {
        super(message);
        this.did = did;
        this.code = code;
    }

    public DidResolutionException(Did did, Throwable e) {
        super(e);
        this.did = did;
        this.code = Code.Internal;
    }

    public DidResolutionException(Did did, String message, Throwable e) {
        super(message, e);
        this.did = did;
        this.code = Code.Internal;
    }

    public Did getDid() {
        return did;
    }

    public Code getCode() {
        return code;
    }
}
