package com.apicatalog.did.document;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;

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

    public static final DidService of(URI id, String type, DidServiceEndpoint endpoint) {
        return new ImmutableService(id, Collections.singleton(type), Collections.singleton(endpoint));
    }

    public static final DidService of(URI id, String type, Collection<DidServiceEndpoint> endpoint) {
        return new ImmutableService(id, Collections.singleton(type), endpoint);
    }

    public static final DidService of(URI id, Collection<String> type, Collection<DidServiceEndpoint> endpoint) {
        return new ImmutableService(id, type, endpoint);
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
