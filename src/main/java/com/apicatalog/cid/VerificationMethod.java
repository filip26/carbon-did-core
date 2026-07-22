package com.apicatalog.cid;

import java.time.Instant;

public interface VerificationMethod {

    static final String ID_KEY = "id";
    static final String TYPE_KEY = "type";
    static final String CONTROLLER_KEY = "controller";
    static final String EXPIRES_KEY = "expires";
    static final String REVOKED_KEY = "revoked";

    
    String id();
    String type();
    String controller();
    
    Instant expires();
    Instant revoked();
    
}
