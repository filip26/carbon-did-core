package com.apicatalog.cid;

import java.util.Collection;

/**
 * Represents a generic <a href="https://www.w3.org/TR/cid-1.0/#services">CID
 * Service</a>.
 *
 * @param id        the service identifier
 * @param type      a collection of types defining the service
 * @param endpoints a collection of service endpoints; this collection can
 *                  contain {@link String}, {@link Map}, or both.
 */
public record GenericService(
        String id,
        Collection<String> type,
        Collection<Object> endpoints) implements Service {

    @Override
    public boolean hasEndpoint() {
        return endpoints != null && !endpoints.isEmpty();
    }
}
