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

    interface Options {
        static final Options EMPTY = new Options() {
        };
    }

    default ResolvedDocument resolve(Did did) throws DidResolutionException {
        return resolve(did, Options.EMPTY);
    }

    /**
     * Resolves the given {@link Did} into {@link DidDocument}
     *
     * @param did     To resolve
     * @param options
     * @return a new {@link DidDocument} instance
     * 
     * @throws IllegalArgumentException if the given DID cannot be resolved
     * @throws DidResolutionException 
     */
    ResolvedDocument resolve(Did did, Options options) throws DidResolutionException;
}
