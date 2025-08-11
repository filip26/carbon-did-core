package com.apicatalog.did.resolver;

import com.apicatalog.did.Did;

public interface PresentationResolver {

    interface Options {

        String accept();
    }

    ResolvedPresentation resolveRepresentation(Did did, Options options) throws DidResolutionException;;
}
