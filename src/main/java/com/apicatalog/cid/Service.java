package com.apicatalog.cid;

import java.util.Collection;

/**
 * A <a href="https://www.w3.org/TR/cid-1.0/#services">CID Service</a>.
 */
public interface Service {

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
