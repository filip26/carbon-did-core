package com.apicatalog.did.primitive;

import java.net.URI;

final class ImmutableServiceEndpoint implements DidServiceEndpoint {

    final URI id;

    ImmutableServiceEndpoint(URI id) {
        this.id = id;
    }

    @Override
    public URI id() {
        return id;
    }
}
