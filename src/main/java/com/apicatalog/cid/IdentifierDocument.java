package com.apicatalog.cid;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;

public interface IdentifierDocument<I, L> {

    public enum Relationship {
        VERIFICATION, // generic
        AUTHENTICATION,
        ASSERTION,
        KEY_AGREEMENT,
        CAPABILITY_INVOCATION,
        CAPABILITY_DELETATION
    }
    
    interface Method<I, L> {

        L id();
        String type();
        I controller();
        
        Instant expires();
        Instant revoked();
        
    }

    /**
     * The {@code id} property: the primary identifier of the DID subject.
     *
     * @return the DID identifier, or {@code null} if absent
     */
    I id();

    /**
     * The {@code controller} property: DIDs that control this DID.
     *
     * @return controller set, possibly empty
     */
    default Collection<I> controller() {
        return Collections.emptySet();
    }

    /**
     * The {@code alsoKnownAs} property: additional URIs that refer to the same
     * subject.
     *
     * @return URIs, possibly empty
     */
    default Collection<I> alsoKnownAs() {
        return Collections.emptySet();
    }

    Collection<Relationship> relationships();

    Method<I, L> methods(Relationship relationship);

    Collection<L> remoteMethods(Relationship relationship);

    /**
     * The {@code service} property: service endpoints in this document.
     *
     * @return service definitions, possibly empty
     */
    default Collection<Service> service() {
        return Collections.emptySet();
    }

    /**
     * Checks whether this document has the required {@code id} property.
     *
     * @return {@code true} if {@link #id()} is not {@code null}
     */
    default boolean hasRequiredProperties() {
        return id() != null;
    }

}
