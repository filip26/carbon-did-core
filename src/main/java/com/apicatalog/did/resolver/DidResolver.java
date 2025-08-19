package com.apicatalog.did.resolver;

import com.apicatalog.did.Did;
import com.apicatalog.did.document.DidDocument;

/**
 * Performs {@link Did} resolution by expanding {@link Did} into
 * {@link DidDocument}.
 *
 * @see <a href="https://www.w3.org/TR/did-core/#dfn-did-resolvers">DID
 *      resolvers</a>
 */
public interface DidResolver {

    /**
     * Resolves the given {@link Did} into {@link DidDocument}
     *
     * @param did To resolve
     * @return a new {@link DidDocument} instance
     * 
     * @throws IllegalArgumentException if the given DID cannot be resolved
     * @throws DidResolutionException
     */
    ResolvedDidDocument resolve(Did did) throws DidResolutionException;
}
