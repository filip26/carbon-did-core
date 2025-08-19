package com.apicatalog.did.document;

import java.net.URI;
import java.util.Collection;

final class ImmutableService implements DidService {

    final URI id;

    final Collection<String> type;

    final Collection<DidServiceEndpoint> endpoint;

    ImmutableService(
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
