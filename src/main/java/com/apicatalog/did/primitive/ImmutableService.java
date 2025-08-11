package com.apicatalog.did.primitive;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;

import com.apicatalog.did.document.Service;
import com.apicatalog.did.document.ServiceEndpoint;

public class ImmutableService implements Service {

    protected final URI id;

    protected final Collection<String> type;

    protected final Collection<ServiceEndpoint> endpoint;

    protected ImmutableService(
            final URI id,
            final Collection<String> type,
            final Collection<ServiceEndpoint> endpoint) {
        this.id = id;
        this.type = type;
        this.endpoint = endpoint;
    }

    public static final Service of(URI id, String type, ServiceEndpoint endpoint) {
        return new ImmutableService(id, Collections.singleton(type), Collections.singleton(endpoint));
    }

    public static final Service of(URI id, String type, Collection<ServiceEndpoint> endpoint) {
        return new ImmutableService(id, Collections.singleton(type), endpoint);
    }

    public static final Service of(URI id, Collection<String> type, Collection<ServiceEndpoint> endpoint) {
        return new ImmutableService(id, type, endpoint);
    }

    @Override
    public URI id() {
        return id;
    }

    @Override
    public Collection<String> type() {
        return type;
    }

    @Override
    public Collection<ServiceEndpoint> endpoint() {
        return endpoint;
    }
}
