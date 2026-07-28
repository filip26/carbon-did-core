package com.apicatalog.did.primitive;

import java.util.Collection;
import java.util.Objects;

import com.apicatalog.did.Service;

/**
 * Represents a generic <a href="https://www.w3.org/TR/cid-1.0/#services">CID
 * Service</a>.
 * 
 * <p>
 * This implementation stores service information without imposing a specific
 * endpoint model. Endpoint values may be strings and maps.
 * </p>
 * 
 * @param id        the service identifier (optional)
 * @param type      a collection of types defining the service
 * @param endpoints a collection of service endpoints; this collection can
 *                  contain strings and maps
 */
public record GenericService(
        String id,
        Collection<String> type,
        Collection<Object> endpoints) implements Service {

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
