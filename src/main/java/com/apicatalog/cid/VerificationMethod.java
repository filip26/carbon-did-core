package com.apicatalog.cid;

import java.time.Instant;

public interface VerificationMethod {

    String id();
    String type();
    String controller();
    
    Instant expires();
    Instant revoked();
    
}
