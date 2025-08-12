package com.apicatalog.did.resolver;

import com.apicatalog.did.Did;

public interface DidPresenter {

    interface Options {

        String accept();
    }
    
    void present(Did did, Options options) throws DidResolutionException;
}
