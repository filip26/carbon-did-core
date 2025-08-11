package com.apicatalog.controller.method;

import java.net.URI;
import java.time.Instant;

/**
 * Represents a signature method declaration.
 */
public interface SignatureMethod {

    URI id();

    String type();

    URI controller();

    Instant revoked();
}