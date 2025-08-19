package com.apicatalog.did.document;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;

public interface DidService {

    URI id();

    Collection<String> type();

    Collection<DidServiceEndpoint> endpoint();

    default boolean hasRequiredProperties() {
        return id() != null && type() != null && endpoint() != null && !endpoint().isEmpty();
    }

    static DidService of(URI id, String type, DidServiceEndpoint endpoint) {
        return new ImmutableService(id, Collections.singleton(type), Collections.singleton(endpoint));
    }

    static DidService of(URI id, String type, Collection<DidServiceEndpoint> endpoint) {
        return new ImmutableService(id, Collections.singleton(type), endpoint);
    }

    static DidService of(URI id, Collection<String> type, Collection<DidServiceEndpoint> endpoint) {
        return new ImmutableService(id, type, endpoint);
    }

}
