package com.apicatalog.did.resolver;

import java.util.Map;

import com.apicatalog.did.DidResource;
import com.apicatalog.did.DidUrl;

@FunctionalInterface
public interface DidResourceResolver {

    DidResource resolve(DidUrl url, Map<String, Object> options) throws DidResolutionException;
}
