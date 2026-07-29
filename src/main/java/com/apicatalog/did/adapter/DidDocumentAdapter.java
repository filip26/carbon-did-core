package com.apicatalog.did.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;

import com.apicatalog.did.Did;
import com.apicatalog.did.DidDocument;
import com.apicatalog.did.DidDocument.Relationship;
import com.apicatalog.did.method.VerificationMethod;
import com.apicatalog.did.service.Service;
import com.apicatalog.did.DidVocab;

public final class DidDocumentAdapter {

    @FunctionalInterface
    public interface MethodAdapter {
        VerificationMethod readMethod(Collection<String> context, Map<String, Object> method);
    }

    @FunctionalInterface
    public interface ServiceAdapter {
        Service readService(Collection<String> context, Map<String, Object> method);
    }

    private final Predicate<Collection<String>> isAccepted;

    private final Map<String, Entry<Predicate<Collection<String>>, MethodAdapter>> methodAdapters;
    private final Map<String, Entry<Predicate<Collection<String>>, ServiceAdapter>> serviceAdapters;

    private DidDocumentAdapter(
            Predicate<Collection<String>> isAccepted,
            Map<String, Entry<Predicate<Collection<String>>, MethodAdapter>> methodAdapters,
            Map<String, Entry<Predicate<Collection<String>>, ServiceAdapter>> serviceAdapters) {
        this.isAccepted = isAccepted;
        this.methodAdapters = methodAdapters;
        this.serviceAdapters = serviceAdapters;
    }

    public DidDocument readDocument(Did did, Map<String, Object> document) {

        var context = getContexts(document);

        if (!isAccepted.test(context)) {
            throw new IllegalArgumentException();
        }

        if (!did.toString().equals(document.get(DidVocab.KEY_ID))) {
            throw new IllegalArgumentException();
        }

        var builder = DidDocument.newBuilder(did);

        for (var entry : document.entrySet()) {

            switch (entry.getKey()) {
            case DidVocab.KEY_ID, "@context":
                break;

            case DidVocab.KEY_AUTHENTICATION,
                    DidVocab.KEY_VERIFICATION_METHOD,
                    DidVocab.KEY_ASSERTION_METHOD,
                    DidVocab.KEY_KEY_AGREEMENT,
                    DidVocab.KEY_CAPABILITY_INVOCATION,
                    DidVocab.KEY_CAPABILITY_DELEGATION:

                for (var method : MapEntryAdapter.toCollection(entry)) {

                    var rel = Relationship.from(entry.getKey());

                    if (method instanceof String stringValue) {
                        builder.reference(rel, stringValue);

                    } else if (method instanceof Map mapValue) {

                        var methodAdapter = methodAdapters.get(mapValue.get(DidVocab.KEY_TYPE));

                        if (methodAdapter == null) {
                            throw new IllegalArgumentException(
                                    "No adapter is configured for type '" + mapValue.get(DidVocab.KEY_TYPE) + "'.");
                        }

                        if (!methodAdapter.getKey().test(context)) {
                            throw new IllegalArgumentException();
                        }

                        if (mapValue.get(DidVocab.KEY_ID) instanceof String idValue && idValue.startsWith("#")) {

                            var mapClone = HashMap.<String, Object>newHashMap(mapValue.size());
                            mapClone.putAll(mapValue);
                            mapClone.put(DidVocab.KEY_ID, did.toString() + idValue);

                            builder.method(rel, methodAdapter.getValue().readMethod(context, mapClone));

                        } else {
                            builder.method(rel, methodAdapter.getValue().readMethod(context, mapValue));
                        }

                    } else {
                        throw new IllegalArgumentException();
                    }
                }
                break;

            case DidVocab.KEY_SERVICE:
                break;

            case DidVocab.KEY_CONTROLLER:
                var controllers = MapEntryAdapter.toCollection(entry);
                if (!controllers.isEmpty()) {
                    var controllerContainer = new ArrayList<Did>(controllers.size());
                    for (var controller : controllers) {
                        if (controller instanceof String stringValue) {
                            controllerContainer.add(Did.parse(stringValue));

                        } else {
                            throw new IllegalArgumentException();
                        }
                    }
                    builder.controller(controllerContainer);
                }
                break;

            case DidVocab.KEY_ALSO_KNOWN_AS:
                builder.alsoKnownAs(MapEntryAdapter.stringCollection(entry));
                break;

            default:
                throw new IllegalArgumentException("Unrecognized DID document property '" + entry.getKey() + "'.");
            }
        }

        return builder.build();
    }

    private static Collection<String> getContexts(Map<String, Object> document) {
        return switch (document.get("@context")) {
        case Collection<?> col -> col.stream()
                .map(item -> {
                    if (item instanceof String s) {
                        return s;
                    }
                    throw new IllegalArgumentException(
                            "The @context collection contains one or more non-string elements");
                })
                .toList();
        case String context -> List.of(context);
        case null -> List.of();
        default ->
            throw new IllegalArgumentException("Invalid @context type: expected a string or a collection of strings");
        };
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {

        private Predicate<Collection<String>> isAccepted;

        private Map<String, Entry<Predicate<Collection<String>>, MethodAdapter>> methodAdapters;
        private Map<String, Entry<Predicate<Collection<String>>, ServiceAdapter>> serviceAdapters;

        public Builder context(Predicate<Collection<String>> accepts) {
            this.isAccepted = accepts;
            this.methodAdapters = new HashMap<>();
            this.serviceAdapters = new HashMap<>();
            return this;
        }

        public Builder method(String typeName, Predicate<Collection<String>> context, MethodAdapter adapter) {
            this.methodAdapters.put(typeName, Map.entry(context, adapter));
            return this;
        }

        public Builder service(String typeName, Predicate<Collection<String>> context, ServiceAdapter adapter) {
            this.serviceAdapters.put(typeName, Map.entry(context, adapter));
            return this;
        }

        public DidDocumentAdapter build() {

            if (methodAdapters.isEmpty() && serviceAdapters.isEmpty()) {
                throw new IllegalArgumentException();
            }

            return new DidDocumentAdapter(isAccepted, Map.copyOf(methodAdapters), Map.copyOf(serviceAdapters));
        }

    }
}
