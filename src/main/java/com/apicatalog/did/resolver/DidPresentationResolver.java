package com.apicatalog.did.resolver;

import com.apicatalog.did.Did;


public interface DidPresentationResolver {

    interface PresentationOptions {
        String accept();
    }
    
    ResolvedPresentation resolveRepresentation(Did did, PresentationOptions options);    
}
