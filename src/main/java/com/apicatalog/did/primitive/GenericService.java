package com.apicatalog.did.primitive;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.apicatalog.did.DidService;

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
        Collection<Object> endpoints) implements DidService {

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
            case Vocab.KEY_ID -> id = MapEntryAdapter.url(entry);
            case Vocab.KEY_TYPE -> type = MapEntryAdapter.stringCollection(entry);
            case Vocab.KEY_SERVICE_ENDPOINT -> endpoints = MapEntryAdapter.collection(entry);

            default -> throw new IllegalArgumentException(
                    "Unsupported property: " + entry.getKey());
            }
        }
        return new GenericService(id, type, endpoints);
    }

//    /**
//     * Creates a {@code DidService} with a single type and multiple endpoints.
//     *
//     * @param id       service id
//     * @param type     service type
//     * @param endpoint service endpoints
//     * @return a new {@code DidService}
//     */
//    static DidService of(String id, String type, Collection<DidServiceEndpoint> endpoint) {
//        return new GenericService(id, List.of(type), endpoint);
//    }
//
//    /**
//     * Creates a {@code DidService} with single type and endpoint.
//     *
//     * @param id       service id
//     * @param type     service types
//     * @param endpoint service endpoint
//     * @return a new {@code DidService}
//     */
//    static DidService of(URI id, String type, DidServiceEndpoint endpoint) {
//        return new GenericService(id, List.of(type), List.of(endpoint));
//    }

    @Override
    public boolean hasEndpoint() {
        return endpoints != null && !endpoints.isEmpty();
    }
}
