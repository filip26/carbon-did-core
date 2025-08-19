package com.apicatalog.did.document;

import java.net.URI;

public interface DidServiceEndpoint {

    URI id();

    default boolean hasRequiredProperties() {
        return id() != null;
    }
}
