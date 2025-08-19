package com.apicatalog.did.document;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;

/**
 * A <a href="https://www.w3.org/TR/did-core/#services">DID Document
 * service</a>.
 * <p>
 * Represents a service entry in a DID Document, consisting of an {@code id},
 * one or more {@code type} values, and one or more {@code serviceEndpoint}
 * values.
 * </p>
 */
public interface DidService {

    /**
     * The {@code id} of this service entry.
     *
     * @return the unique service identifier
     */
    URI id();

    /**
     * The {@code type} values of this service.
     *
     * @return one or more type strings
     */
    Collection<String> type();

    /**
     * The {@code serviceEndpoint} values of this service.
     *
     * @return one or more endpoints
     */
    Collection<DidServiceEndpoint> endpoint();

    /**
     * Checks whether this service has the required properties: {@code id},
     * {@code type}, and at least one {@code serviceEndpoint}.
     *
     * @return {@code true} if valid
     */
    default boolean hasRequiredProperties() {
        return id() != null && type() != null && endpoint() != null && !endpoint().isEmpty();
    }

    /**
     * Creates a {@code DidService} with a single type and a single endpoint.
     *
     * @param id       service id
     * @param type     service type
     * @param endpoint service endpoint
     * @return a new {@code DidService}
     */
    static DidService of(URI id, String type, DidServiceEndpoint endpoint) {
        return new ImmutableService(id, Collections.singleton(type), Collections.singleton(endpoint));
    }

    /**
     * Creates a {@code DidService} with a single type and multiple endpoints.
     *
     * @param id       service id
     * @param type     service type
     * @param endpoint service endpoints
     * @return a new {@code DidService}
     */
    static DidService of(URI id, String type, Collection<DidServiceEndpoint> endpoint) {
        return new ImmutableService(id, Collections.singleton(type), endpoint);
    }

    /**
     * Creates a {@code DidService} with multiple types and endpoints.
     *
     * @param id       service id
     * @param type     service types
     * @param endpoint service endpoints
     * @return a new {@code DidService}
     */
    static DidService of(URI id, Collection<String> type, Collection<DidServiceEndpoint> endpoint) {
        return new ImmutableService(id, type, endpoint);
    }

}
