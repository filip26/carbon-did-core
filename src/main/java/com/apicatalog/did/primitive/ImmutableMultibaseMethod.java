package com.apicatalog.did.primitive;

import java.util.Map;

import com.apicatalog.did.Did;
import com.apicatalog.did.DidUrl;

final class ImmutableMultibaseMethod implements DidVerificationMethod {

    final DidUrl id;
    final String type;
    final Did controller;
    final byte[] publicKeyMultibase;

    ImmutableMultibaseMethod(
            final DidUrl id,
            final String type,
            final Did controller,
            final byte[] publicKeyMultibase) {
        this.id = id;
        this.type = type;
        this.controller = controller;
        this.publicKeyMultibase = publicKeyMultibase;
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
    public byte[] publicKeyMultibase() {
        return publicKeyMultibase;
    }

    @Override
    public Map<String, Object> publicKeyJwk() {
        return null;
    }
}
