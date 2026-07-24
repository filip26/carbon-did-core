package com.apicatalog.did.resolver;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import com.apicatalog.did.Did;
import com.apicatalog.did.resolver.DidResolutionException.ErrorCode;

public class DidMethodRouter implements DidResolver {

    protected final Map<String, DidResolver> resolvers;

    protected DidMethodRouter(final Map<String, DidResolver> resolvers) {
        this.resolvers = resolvers;
    }

    @Override
    public ResolvedDidDocument resolve(Did did, Map<String, Object> options) throws DidResolutionException {

        Objects.requireNonNull(did);

        final DidResolver resolver = resolvers.get(did.method());

        if (resolver == null) {
            throw new DidResolutionException(did.toString(), ErrorCode.UNSUPPORTED_METHOD);
        }
        return resolver.resolve(did, options);
    }

    public static Builder with(String method, DidResolver resolver) {
        return (new Builder()).with(method, resolver);
    }

    public static class Builder {

        final Map<String, DidResolver> resolvers;

        Builder() {
            this.resolvers = new LinkedHashMap<>();
        }

        public Builder with(String method, DidResolver resolver) {
            resolvers.put(method, resolver);
            return this;
        }

        public DidResolver build() {
            if (resolvers.size() == 1) {
                return resolvers.values().iterator().next();
            }
            return new DidMethodRouter(Map.copyOf(resolvers));
        }
    }

}
