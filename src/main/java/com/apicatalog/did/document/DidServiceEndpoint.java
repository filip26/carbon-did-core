package com.apicatalog.did.document;

import java.net.URI;
import java.util.Objects;

public interface DidServiceEndpoint {

    URI id();

    default boolean hasRequiredProperties() {
        return id() != null;
    }

    static DidServiceEndpoint of(URI id) {
        Objects.requireNonNull(id);
        return new ImmutableServiceEndpoint(id);
    }
}
