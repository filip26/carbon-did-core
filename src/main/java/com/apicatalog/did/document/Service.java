package com.apicatalog.did.document;

import java.net.URI;
import java.util.Collection;

public interface Service {

    URI id();

    Collection<String> type();

    Collection<ServiceEndpoint> endpoint();
    
    default boolean hasRequiredProperties() {
        return id() != null && type() != null && endpoint() != null && !endpoint().isEmpty();
    }
}
