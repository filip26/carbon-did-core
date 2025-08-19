package com.apicatalog.did.document;

import java.util.Map;

import com.apicatalog.did.Did;
import com.apicatalog.did.DidUrl;
import com.apicatalog.did.datatype.MultibaseEncoded;

final class ImmutableJwkMethod implements DidVerificationMethod {

    final DidUrl id;
    final String type;
    final Did controller;
    final Map<String, Object> publicKeyJwk;

    ImmutableJwkMethod(
            final DidUrl id,
            final String type,
            final Did controller,
            final Map<String, Object> publicKeyJwk) {
        this.id = id;
        this.type = type;
        this.controller = controller;
        this.publicKeyJwk = publicKeyJwk;
    }

    @Override
    public DidUrl id() {
        return id;
    }

    @Override
    public String type() {
        return type;
    }

    @Override
    public Did controller() {
        return controller;
    }

    @Override
    public MultibaseEncoded publicKeyMultibase() {
        return null;
    }

    @Override
    public Map<String, Object> publicKeyJwk() {
        return publicKeyJwk;
    }
}
