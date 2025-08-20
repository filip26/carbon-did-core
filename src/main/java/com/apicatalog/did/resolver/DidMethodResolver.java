package com.apicatalog.did.resolver;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import com.apicatalog.did.Did;
import com.apicatalog.did.resolver.DidResolutionException.Code;

public class DidMethodResolver implements DidResolver {

    protected final Map<String, DidResolver> resolvers;

    protected DidMethodResolver(final Map<String, DidResolver> resolvers) {
        this.resolvers = resolvers;
    }

    @Override
    public ResolvedDidDocument resolve(Did did) throws DidResolutionException {

        Objects.requireNonNull(did);

        final DidResolver resolver = resolvers.get(did.getMethod());

        if (resolver == null) {
            throw new DidResolutionException(did, Code.UnsupportedMethod);
        }
        return resolver.resolve(did);
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
            return new DidMethodResolver(Collections.unmodifiableMap(resolvers));
        }
    }

}
