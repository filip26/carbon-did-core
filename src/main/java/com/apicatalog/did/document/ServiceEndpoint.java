package com.apicatalog.did.document;

import java.net.URI;

public interface ServiceEndpoint {

    URI id();

    default boolean hasRequiredProperties() {
        return id() != null;
    }
}
