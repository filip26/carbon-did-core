package com.apicatalog.did.primitive;

import java.net.URI;
import java.util.Collection;

import com.apicatalog.did.DidService;


public class ImmutableService implements DidService {

    final URI id;

    final Collection<String> type;

    final Collection<DidServiceEndpoint> endpoint;

    public ImmutableService(
            final URI id,
            final Collection<String> type,
            final Collection<DidServiceEndpoint> endpoint) {
        this.id = id;
        this.type = type;
        this.endpoint = endpoint;
    }

    @Override
    public URI id() {
        return id;
    }

    @Override
    public Collection<String> type() {
        return type;
    }

    @Override
    public Collection<DidServiceEndpoint> endpoint() {
        return endpoint;
    }
}
