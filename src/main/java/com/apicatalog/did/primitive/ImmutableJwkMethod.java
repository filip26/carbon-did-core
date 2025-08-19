package com.apicatalog.did.primitive;

import java.util.Map;
import java.util.Objects;

import com.apicatalog.did.Did;
import com.apicatalog.did.DidUrl;
import com.apicatalog.did.datatype.MultibaseEncoded;
import com.apicatalog.did.document.VerificationMethod;

public class ImmutableJwkMethod implements VerificationMethod {

    protected final DidUrl id;
    protected final String type;
    protected final Did controller;
    protected final Map<String, Object> publicKeyJwk;

    protected ImmutableJwkMethod(
            final DidUrl id,
            final String type,
            final Did controller,
            final Map<String, Object> publicKeyJwk) {
        this.id = id;
        this.type = type;
        this.controller = controller;
        this.publicKeyJwk = publicKeyJwk;
    }

    public static final VerificationMethod of(
            final DidUrl id) {
        Objects.requireNonNull(id);
        return new ImmutableJwkMethod(id, null, null, null);
    }

    public static final VerificationMethod of(
            final DidUrl id,
            final String type,
            final Did controller,
            final Map<String, Object> publicKeyJwk) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(type);
        Objects.requireNonNull(controller);
        return new ImmutableJwkMethod(id, type, controller, publicKeyJwk);
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
