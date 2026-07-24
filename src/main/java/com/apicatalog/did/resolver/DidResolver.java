package com.apicatalog.did.resolver;

import java.util.Map;

import com.apicatalog.did.Did;
import com.apicatalog.did.DidDocument;

@FunctionalInterface
public interface DidResolver {

    /**
     * Resolves the given DID into a {@link DidDocument}.
     *
     * @param did     the DID to resolve (must not be {@code null})
     * @param options
     * @return the resolution result as a {@link ResolvedDidDocument}
     * @throws DidResolutionException if resolution fails
     */
    ResolvedDidDocument resolve(Did did, Map<String, Object> options) throws DidResolutionException;
}
