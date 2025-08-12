package com.apicatalog.did.resolver;

import com.apicatalog.did.Did;

public interface DidPresenter {

    interface Options {

        String accept();
    }
    
    DocumentPresentation present(Did did, Options options) throws DidResolutionException;
}
