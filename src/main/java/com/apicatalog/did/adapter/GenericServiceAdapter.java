package com.apicatalog.did.adapter;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.apicatalog.did.DidVocab;
import com.apicatalog.did.primitive.GenericService;

public class GenericServiceAdapter {


    /**
     * Creates a {@link GenericService} from a compacted object.
     *
     * @param document compacted service representation
     * @return parsed generic service
     * @throws IllegalArgumentException if an unsupported property or invalid value
     *                                  is encountered
     */
    public static GenericService from(Map<String, Object> document) {

        String id = null;
        Collection<String> type = null;
        Collection<Object> endpoints = List.of();

        for (var entry : document.entrySet()) {

            if (entry.getValue() == null) {
                continue;
            }

            switch (entry.getKey()) {
            case DidVocab.KEY_ID -> id = MapEntryAdapter.url(entry);
            case DidVocab.KEY_TYPE -> type = MapEntryAdapter.stringCollection(entry);
            case DidVocab.KEY_SERVICE_ENDPOINT -> endpoints = MapEntryAdapter.toCollection(entry);

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

}
