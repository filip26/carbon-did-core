package com.apicatalog.did;

import java.util.Collection;
import java.util.Map;

import com.apicatalog.did.Document.Relationship;

/**
 * A <a href="https://www.w3.org/TR/did-core/#services">DID Document
 * service</a>.
 * <p>
 * Represents a service entry in a DID Document, consisting of an {@code id},
 * one or more {@code type} values, and one or more {@code serviceEndpoint}
 * values.
 * </p>
 */
public interface Service {

    @FunctionalInterface
    public interface Resolver {
        Collection<Service> resolve(DidUrl url, Map<String, Object> options);
    }

    @FunctionalInterface
    public interface Dereferencer {
        Collection<Service> dereference(DidUrl url, Document document, Relationship rel);
    }

    /**
     * The {@code id} of this service entry.
     *
     * @return the unique service identifier
     */
    String id();

    /**
     * The {@code type} values of this service.
     *
     * @return one or more type strings
     */
    Collection<String> type();

    /**
     * Determines if the service has at least one endpoint.
     *
     * @return true if endpoints is not null and not empty, false otherwise
     */
    boolean hasEndpoint();

    /**
     * Checks whether this service has the required properties: {@code id},
     * {@code type}, and at least one {@code serviceEndpoint}.
     *
     * @return {@code true} if valid
     */
    default boolean hasRequiredProperties() {
        return id() != null
                && type() != null && !type().isEmpty()
                && hasEndpoint();
    }
}
