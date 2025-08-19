package com.apicatalog.did.document;

import java.net.URI;
import java.util.Objects;

/**
 * A <a href="https://www.w3.org/TR/did-core/#services">serviceEndpoint</a>
 * entry within a DID Document service.
 */
public interface DidServiceEndpoint {

    /**
     * The {@code id} of this service endpoint.
     *
     * @return endpoint identifier
     */
    URI id();

    /**
     * Checks whether this endpoint has the required {@code id} property.
     *
     * @return {@code true} if valid
     */
    default boolean hasRequiredProperties() {
        return id() != null;
    }

    /**
     * Creates a {@code DidServiceEndpoint} with the given {@code id}.
     *
     * @param id endpoint identifier (must not be {@code null})
     * @return a new {@code DidServiceEndpoint}
     * @throws NullPointerException if {@code id} is {@code null}
     */
    static DidServiceEndpoint of(URI id) {
        Objects.requireNonNull(id);
        return new ImmutableServiceEndpoint(id);
    }
}
