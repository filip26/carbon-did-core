package com.apicatalog.did.resolver;

import java.io.InputStream;

public interface ResolvedPresentation {

    String contentType();
    
    InputStream stream();

}
