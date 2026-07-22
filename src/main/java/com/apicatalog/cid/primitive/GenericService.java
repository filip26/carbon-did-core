package com.apicatalog.cid.primitive;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.apicatalog.cid.Service;

/**
 * Represents a generic <a href="https://www.w3.org/TR/cid-1.0/#services">CID
 * Service</a>.
 * 
 * <p>
 * This implementation stores service information without imposing a specific
 * endpoint model. Endpoint values may be strings and maps.
 * </p>
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

    /**
     * Creates a {@link GenericService} from a compacted object.
     *
     * @param compacted compacted service representation
     * @return parsed generic service
     * @throws IllegalArgumentException if an unsupported property or invalid value
     *                                  is encountered
     */
    public static GenericService from(Map<String, Object> compacted) {

        String id = null;
        Collection<String> type = null;
        Collection<Object> endpoints = List.of();

        for (var entry : compacted.entrySet()) {

            if (entry.getValue() == null) {
                continue;
            }

            switch (entry.getKey()) {
            case Vocab.ID_KEY -> id = MapAdapter.url(entry);
            case Vocab.TYPE_KEY -> type = MapAdapter.stringCollection(entry);
            case Vocab.SERVICE_ENDPOINT_KEY -> endpoints = MapAdapter.collection(entry);

            default -> throw new IllegalArgumentException(
                    "Unsupported property: " + entry.getKey());
            }
        }
        return new GenericService(id, type, endpoints);
    }
}
