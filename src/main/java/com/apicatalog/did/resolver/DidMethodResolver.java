package com.apicatalog.did.resolver;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.apicatalog.did.Did;

public class DidMethodResolver implements DidResolver {

    protected final Map<String, DidResolver> resolvers;

    protected DidMethodResolver(Map<String, DidResolver> resolvers) {
        this.resolvers = resolvers;
    }

    public DidResolver get(final String method) {
        return resolvers.get(method);
    }

    @Override
    public ResolvedDocument resolve(Did did, DocumentOptions options) {

        Objects.requireNonNull(did);

        final DidResolver resolver = resolvers.get(did.getMethod());

        if (resolver != null) {
            return resolver.resolve(did, options);
        }

        throw new IllegalArgumentException("The " + did.toString() + " method cannot be resolved.");
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {

        final Map<String, DidResolver> resolvers;

        public Builder() {
            resolvers = new HashMap<>();
        }

        public Builder add(String method, DidResolver resolver) {
            resolvers.put(method, resolver);
            return this;
        }

        public DidMethodResolver build() {
            return new DidMethodResolver(Collections.unmodifiableMap(resolvers));
        }
    }
}
