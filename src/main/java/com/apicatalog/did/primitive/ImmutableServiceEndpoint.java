package com.apicatalog.did.primitive;

import java.net.URI;
import java.util.Objects;

import com.apicatalog.did.document.ServiceEndpoint;

public class ImmutableServiceEndpoint implements ServiceEndpoint {

    protected final URI id;

    protected ImmutableServiceEndpoint(URI id) {
        this.id = id;
    }

    public static final ImmutableServiceEndpoint of(URI id) {
        Objects.requireNonNull(id);
        return new ImmutableServiceEndpoint(id);
    }

    @Override
    public URI id() {
        return id;
    }

}
