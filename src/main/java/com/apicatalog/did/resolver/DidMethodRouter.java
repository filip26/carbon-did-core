package com.apicatalog.did.resolver;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import com.apicatalog.did.DidResource;
import com.apicatalog.did.DidUrl;
import com.apicatalog.did.resolver.DidResolutionException.ErrorCode;

public final class DidMethodRouter implements DidResolver {

    private final Map<String, DidResolver> resolvers;

    private DidMethodRouter(final Map<String, DidResolver> resolvers) {
        this.resolvers = resolvers;
    }

    @Override
    public DidResource resolve(DidUrl did, Map<String, Object> options) throws DidResolutionException {

        Objects.requireNonNull(did);

        final DidResolver resolver = resolvers.get(did.method());

        if (resolver == null) {
            throw new DidResolutionException(did.toString(), ErrorCode.UNSUPPORTED_METHOD);
        }
        return resolver.resolve(did, options);
    }

    public static Builder createBuilder() {
        return new Builder();
    }

    public static class Builder {

        final Map<String, DidResolver> resolvers;

        Builder() {
            this.resolvers = new LinkedHashMap<>();
        }

        public Builder method(String method, DidResolver resolver) {
            resolvers.put(method, resolver);
            return this;
        }

        public DidResolver build() {
            if (resolvers.isEmpty()) {
                throw new IllegalStateException();
            }

            if (resolvers.size() == 1) {
                return resolvers.values().iterator().next();
            }
            return new DidMethodRouter(Map.copyOf(resolvers));
        }
    }

}
