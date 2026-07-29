package com.apicatalog.did.service;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a generic DID Service.
 * 
 * <p>
 * This implementation stores service information without imposing a specific
 * endpoint model. Endpoint values may be strings, maps, or materialized
 * instances.
 * </p>
 * 
 * @param id        the service identifier (optional)
 * @param type      a collection of types defining the service
 * @param endpoints a collection of service endpoints
 */
public record GenericService<T>(
        String id,
        Collection<String> type,
        Collection<T> endpoints) implements Service {

    /**
     * Creates a {@code DidService}.
     *
     * @param id        service id
     * @param type      service type
     * @param endpoints service endpoints
     */
    public GenericService {
        type = Objects.requireNonNull(type);
        endpoints = Objects.requireNonNull(endpoints);
    }

    @Override
    public boolean hasEndpoint() {
        return endpoints != null && !endpoints.isEmpty();
    }
}
