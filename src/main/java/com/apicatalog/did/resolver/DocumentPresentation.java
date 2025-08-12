package com.apicatalog.did.resolver;

import java.io.InputStream;

public interface DocumentPresentation {

    String contentType();
    
    InputStream stream();

}
