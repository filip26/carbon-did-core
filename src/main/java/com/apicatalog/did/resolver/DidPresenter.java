package com.apicatalog.did.resolver;

import com.apicatalog.did.Did;

public interface DidPresenter {

    void present(Did did, String accept, PresentationWriterProvider writerProvider) throws DidResolutionException;
}
