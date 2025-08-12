package com.apicatalog.did.resolver;

import java.io.Writer;

import com.apicatalog.did.Did;

@FunctionalInterface
public interface PresentationWriterProvider {

    Writer forContentType(Did did, String contentType);

}
