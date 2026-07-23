package com.apicatalog.cid;

import java.time.Instant;

public interface VerificationMethod<I, L> {

    L id();
    String type();
    I controller();
    
    Instant expires();
    Instant revoked();
    
}
