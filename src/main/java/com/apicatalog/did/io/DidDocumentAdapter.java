package com.apicatalog.did.io;

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
import com.apicatalog.did.VerificationMethod;

public class DidDocumentAdapter {
   
    @FunctionalInterface
    public interface MethodAdapter {
        VerificationMethod readMethod(Collection<String> context, Map<String, Object> method);
    }
        
    private final Predicate<Collection<String>> isAccepted;
    private final Map<String, Entry<Predicate<Collection<String>>, MethodAdapter>> methodAdapters;

    public DidDocumentAdapter(
            Predicate<Collection<String>> isAccepted,
            Map<String, Entry<Predicate<Collection<String>>, MethodAdapter>> methodAdapters) {
        this.isAccepted = isAccepted;
        this.methodAdapters = methodAdapters;
    }

//    @Override
    public DidDocument readDocument(Did did, Map<String, Object> document) {

        var context = getContexts(document);

        if (!isAccepted.test(context)) {
            throw new IllegalArgumentException();
        }

        if (!did.toString().equals(document.get("id"))) {
            throw new IllegalArgumentException();
        }

        var builder = DidDocument.builder(did);

        for (var entry : document.entrySet()) {

            switch (entry.getKey()) {
            case "id":
                break;

            case "authentication",
                    "verificationMethod",
                    "assertionMethod",
                    "keyAgreement",
                    "capabilityInvocation",
                    "capabilityDelegation":

                var methods = asList(entry.getValue());
                for (var method : methods) {

                    var rel = Relationship.from(entry.getKey());

                    if (method instanceof String stringValue) {
                        builder.reference(rel, stringValue);

                    } else if (method instanceof Map mapValue) {

                        var methodAdapter = methodAdapters.get(mapValue.get("type"));

                        if (methodAdapter == null) {
                            throw new IllegalArgumentException(
                                    "No adapter is configured for type '" + mapValue.get("type") + "'.");
                        }

                        if (!methodAdapter.getKey().test(context)) {
                            throw new IllegalArgumentException();
                        }

                        if (mapValue.get("id") instanceof String idValue && idValue.startsWith("#")) {

                            var mapClone = HashMap.<String, Object>newHashMap(mapValue.size());
                            mapClone.putAll(mapValue);
                            mapClone.put("id", did.toString() + idValue);

                            builder.method(rel, methodAdapter.getValue().readMethod(context, mapClone));

                        } else {
                            builder.method(rel, methodAdapter.getValue().readMethod(context, mapValue));
                        }

                    } else {
                        throw new IllegalArgumentException();
                    }
                }
                IO.println(entry);

                break;

            case "service":
                break;

            case "controller":
                var controllers = asList(entry.getValue());
                if (!controllers.isEmpty()) {
                    var controllerValue = new ArrayList<Did>(controllers.size());
                    for (var controller : controllers) {
                        if (controller instanceof String stringValue) {
                            controllerValue.add(Did.parse(stringValue));

                        } else {
                            throw new IllegalArgumentException();
                        }
                    }
                    builder.controller(controllerValue);
                }
                break;

            case "alsoKnownAs":
                var aliases = asList(entry.getValue());
                if (!aliases.isEmpty()) {
                    var alsoKnownAsValue = new ArrayList<String>(aliases.size());
                    for (var alias : aliases) {
                        if (alias instanceof String stringValue) {
                            alsoKnownAsValue.add(stringValue);

                        } else {
                            throw new IllegalArgumentException();
                        }
                    }
                    builder.alsoKnownAs(alsoKnownAsValue);
                }
                break;
            }
        }

        return builder.build();
    }

    static Collection<String> getContexts(Map<String, Object> document) {
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

    static Collection<?> asList(Object value) {
        return (value instanceof Collection<?> col) ? col : (value != null ? List.of(value) : List.of());
    }
}
