package com.apicatalog.did.document;

import java.net.URI;
import java.util.Objects;

final class ImmutableServiceEndpoint implements DidServiceEndpoint {

    final URI id;

    ImmutableServiceEndpoint(URI id) {
        this.id = id;
    }

    public static final DidServiceEndpoint of(URI id) {
        Objects.requireNonNull(id);
        return new ImmutableServiceEndpoint(id);
    }

    @Override
    public URI id() {
        return id;
    }

}
