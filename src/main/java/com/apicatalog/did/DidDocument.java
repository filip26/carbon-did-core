package com.apicatalog.did;

import java.util.Collection;
import java.util.Collections;

/**
 * A <a href="https://www.w3.org/TR/did-core/#did-document-properties">DID
 * Document</a>.
 * <p>
 * Models the top-level properties of a DID Document as defined in the W3C DID
 * Core specification. All accessors return empty sets by default.
 * </p>
 */
public interface DidDocument {

    public enum Relationship {
        VERIFICATION, // generic
        AUTHENTICATION,
        ASSERTION,
        KEY_AGREEMENT,
        CAPABILITY_INVOCATION,
        CAPABILITY_DELETATION
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
        return Collections.emptySet();
    }

    /**
     * The {@code alsoKnownAs} property: additional URIs that refer to the same
     * subject.
     *
     * @return URIs, possibly empty
     */
    default Collection<String> alsoKnownAs() {
        return Collections.emptySet();
    }

    /**
     * The {@code service} property: service endpoints in this document.
     *
     * @return service definitions, possibly empty
     */
    default Collection<DidService> service() {
        return Collections.emptySet();
    }

    Collection<Relationship> relationships();

    Collection<DidVerificationMethod> methods(Relationship relationship);

    Collection<DidUrl> remoteMethods(Relationship relationship);

    /**
     * Checks whether this document has the required {@code id} property.
     *
     * @return {@code true} if {@link #id()} is not {@code null}
     */
    default boolean hasRequiredProperties() {
        return id() != null;
    }

}
