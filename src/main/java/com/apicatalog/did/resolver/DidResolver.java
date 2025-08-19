package com.apicatalog.did.resolver;

import com.apicatalog.did.Did;
import com.apicatalog.did.document.DidDocument;

/**
 * A <a href="https://www.w3.org/TR/did-core/#dfn-did-resolvers">DID
 * Resolver</a> expands a {@link Did} into a corresponding {@link DidDocument}.
 */
public interface DidResolver {

    /**
     * Resolves the given DID into a {@link DidDocument}.
     *
     * @param did the DID to resolve (must not be {@code null})
     * @return the resolution result as a {@link ResolvedDidDocument}
     * @throws DidResolutionException if resolution fails
     */
    ResolvedDidDocument resolve(Did did) throws DidResolutionException;
}
