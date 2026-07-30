package com.apicatalog.did.adapter;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.apicatalog.did.DidVocab;
import com.apicatalog.did.service.GenericService;
import com.apicatalog.did.service.Service;

public class GenericServiceAdapter implements DidDocumentAdapter.ServiceAdapter {

    /**
     * Creates a {@link GenericService} from a compacted object.
     *
     * @param document compacted service representation
     * @return parsed generic service
     * @throws IllegalArgumentException if an unsupported property or invalid value
     *                                  is encountered
     */
    @Override
    public Service readService(Collection<String> context, Map<String, Object> document) {

        String id = null;
        String type = null;
        Collection<Object> endpoints = List.of();

        for (var entry : document.entrySet()) {

            if (entry.getValue() == null) {
                continue;
            }

            switch (entry.getKey()) {
            case DidVocab.KEY_ID -> id = MapEntryAdapter.url(entry);
            case DidVocab.KEY_TYPE -> type = MapEntryAdapter.string(entry);
            case DidVocab.KEY_SERVICE_ENDPOINT -> endpoints = MapEntryAdapter.toCollection(entry);

            default -> throw new IllegalArgumentException(
                    "Unsupported property: " + entry.getKey());
            }
        }
        return new GenericService(id, type, List.copyOf(endpoints));
    }
}
