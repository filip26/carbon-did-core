package com.apicatalog.did.resolver;

import java.io.InputStream;

@FunctionalInterface
public interface PresentationReader {

    void read(String contentType, InputStream is);
    
}
