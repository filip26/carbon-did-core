package com.apicatalog.did.resolver;

import java.util.Map;

import com.apicatalog.did.DidResource;
import com.apicatalog.did.DidUrl;

@FunctionalInterface
public interface DidResolver {

    public static final String OPTION_DOCUMENT_AND_METADATA = "withMetadata";

    DidResource resolve(DidUrl url, Map<String, Object> options) throws DidResolutionException;
}
