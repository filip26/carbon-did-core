package com.apicatalog.did;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.apicatalog.did.method.VerificationMethod;
import com.apicatalog.did.service.Service;

/**
 * A <a href="https://www.w3.org/TR/did-core/#did-document-properties">DID
 * Document</a>.
 * <p>
 * Models the top-level properties of a DID Document as defined in the W3C DID
 * Core specification. All accessors return empty sets by default.
 * </p>
 */
public interface DidDocument {

    @FunctionalInterface
    public interface Resolver {
        DidDocument.WithMetadata resolve(DidUrl url, Map<String, Object> options);
    }

    public enum Relationship {
        VERIFICATION(DidVocab.KEY_VERIFICATION_METHOD),
        AUTHENTICATION(DidVocab.KEY_AUTHENTICATION),
        ASSERTION(DidVocab.KEY_ASSERTION_METHOD),
        KEY_AGREEMENT(DidVocab.KEY_KEY_AGREEMENT),
        CAPABILITY_INVOCATION(DidVocab.KEY_CAPABILITY_INVOCATION),
        CAPABILITY_DELEGATION(DidVocab.KEY_CAPABILITY_DELEGATION);

        private final String name;
        private final String uri;

        private static final Map<String, Relationship> LOOKUP;

        static {
            Map<String, Relationship> map = HashMap.newHashMap(Relationship.values().length);
            for (Relationship rel : values()) {
                map.put(rel.name, rel);
                map.put(rel.uri, rel);
            }
            LOOKUP = Map.copyOf(map);
        }

        Relationship(String name) {
            this.name = name;
            this.uri = DidVocab.NS_SECURITY + (name.endsWith("Method") ? name : name + "Method");
        }

        public String getName() {
            return name;
        }

        public String getUri() {
            return uri;
        }

        public static Relationship from(String name) {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("Proof purpose cannot be null or blank");
            }

            Relationship rel = LOOKUP.get(name);

            if (rel == null) {
                throw new IllegalArgumentException("Unknown relationship: " + name);
            }

            return rel;
        }
    }

    /**
     * The {@code id} property: the primary identifier of the DID subject.
     *
     * @return the DID identifier, or {@code null} if absent
     */
    Did id();

    /**
     * The {@code controller} property: DIDs that control this DID.
     *
     * @return controller set, possibly empty
     */
    default Collection<Did> controller() {
        return List.of();
    }

    /**
     * The {@code alsoKnownAs} property: additional URIs that refer to the same
     * subject.
     *
     * @return URIs, possibly empty
     */
    default Collection<String> alsoKnownAs() {
        return List.of();
    }

    /**
     * The {@code service} property: service endpoints in this document.
     *
     * @return service definitions, possibly empty
     */
    default Collection<Service> service() {
        return List.of();
    }

    Set<Relationship> relationships();

    Collection<VerificationMethod> methods(Relationship rel);

    default Collection<DidUrl> remoteMethods(Relationship rel) {
        return List.of();
    }

    /**
     * Checks whether this document has the required {@code id} property.
     *
     * @return {@code true} if {@link #id()} is not {@code null}
     */
    default boolean hasRequiredProperties() {
        return id() != null;
    }

    /**
     * Result of a DID resolution process.
     * <p>
     * Contains the resolved {@link DidDocument} and optional
     * {@link DidDocument.Metadata}.
     * </p>
     *
     * @see <a href="https://www.w3.org/TR/did-core/#did-resolution">DID
     *      Resolution</a>
     * 
     * @param metadata the resolution metadata associated with the DID Document, or
     *                 {@code null} if none
     * @param document the DID Document (never {@code null})
     */
    public record WithMetadata(
            DidDocument.Metadata metadata,
            DidDocument document) {
    }

    /**
     * Metadata associated with a resolved DID Document, as defined in
     * <a href="https://www.w3.org/TR/did-core/#did-document-metadata">DID Core —
     * DID Document Metadata</a>.
     */
    public interface Metadata {

        /**
         * The timestamp when the DID Document was created.
         *
         * @return creation time, or {@code null} if not provided
         */
        default Instant created() {
            return null;
        }

        /**
         * The timestamp when the DID Document was last updated.
         *
         * @return last update time, or {@code null} if not provided
         */
        default Instant updated() {
            return null;
        }

        /**
         * Indicates whether the DID has been deactivated.
         *
         * @return {@code true} if deactivated, otherwise {@code false}
         */
        default boolean isDeactivated() {
            return false;
        }

        /**
         * A timestamp after which the DID Document should be refreshed.
         *
         * @return refresh time, or {@code null} if not specified
         */
        default Instant refresh() {
            return null;
        }

        /**
         * The identifier for the current version of the DID Document.
         *
         * @return version identifier, or {@code null} if not provided
         */
        default String versionId() {
            return null;
        }

        /**
         * The identifier of the next version of the DID Document.
         *
         * @return next version identifier, or {@code null} if not provided
         */
        default String nextVersionId() {
            return null;
        }

        /**
         * Equivalent identifiers for the DID, if any.
         *
         * @return a set of equivalent DIDs, never {@code null}
         */
        default Set<Did> equivalentId() {
            return Set.of();
        }

        /**
         * The canonical identifier for the DID.
         *
         * @return canonical DID, or {@code null} if not provided
         */
        default Did canonicalId() {
            return null;
        }
    }

    public static Builder newBuilder(Did did) {
        return new Builder(did);
    }

    public static class Builder {

        private Did id;
        private Collection<Did> controller;
        private Collection<String> alsoKnownAs;

        private List<Entry<Relationship, String>> references;
        private Map<String, VerificationMethod> methods;
        private Map<Relationship, Collection<VerificationMethod>> relations;

        private Collection<Service> service;
        
        public Builder(Did id) {
            this.id = id;
            this.controller = List.of();
            this.alsoKnownAs = List.of();
            this.service = List.of();
        }

        public void method(Relationship rel, VerificationMethod method) {
            if (methods == null) {
                methods = new HashMap<>();
                relations = new HashMap<>();
            }
            methods.put(method.id().toString(), method);
            relations.computeIfAbsent(rel, _ -> new ArrayList<>()).add(method);
        }

        public void reference(Relationship rel, String refId) {
            if (references == null) {
                references = new ArrayList<>();
            }

            references.add(Map.entry(rel,
                    refId.startsWith("#")
                            ? id.toString() + refId
                            : refId));
        }

        public void controller(Collection<Did> controller) {
            this.controller = controller;
        }

        public void service(Collection<Service> service) {
            this.service = service;
        }

        public void alsoKnownAs(Collection<String> alsoKnownAs) {
            this.alsoKnownAs = alsoKnownAs;
        }

        public DidDocument build() {

            if (references != null) {
                for (var ref : references) {
                    var method = methods.get(ref.getValue());
                    if (method == null) {
                        if (ref.getValue().startsWith(id.toString() + "#")) {
                            throw new IllegalArgumentException();
                        }

                        // TODO remote method

                    } else {
                        method(ref.getKey(), method);
                    }
                }
            }

            return new Document(
                    id,
                    controller,
                    alsoKnownAs,
                    relations != null
                            ? Map.copyOf(relations)
                            : Map.of(),
                    List.copyOf(service));
        }

        private static record Document(
                Did id,
                Collection<Did> controller,
                Collection<String> alsoKnownAs,
                Map<Relationship, Collection<VerificationMethod>> relations,
                Collection<Service> service) implements DidDocument {

            @Override
            public Set<Relationship> relationships() {
                return relations.keySet();
            }

            @Override
            public Collection<VerificationMethod> methods(Relationship rel) {
                return relations.get(rel);
            }
        }
    }
}
