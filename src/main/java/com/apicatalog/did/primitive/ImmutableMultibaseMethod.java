package com.apicatalog.did.primitive;

import java.util.Map;
import java.util.Objects;

import com.apicatalog.did.Did;
import com.apicatalog.did.DidUrl;
import com.apicatalog.did.datatype.MultibaseEncoded;
import com.apicatalog.did.document.VerificationMethod;

public class ImmutableMultibaseMethod implements VerificationMethod {

    protected final DidUrl id;
    protected final String type;
    protected final Did controller;
    protected final MultibaseEncoded publicKeyMultibase;

    protected ImmutableMultibaseMethod(
            final DidUrl id,
            final String type,
            final Did controller,
            final MultibaseEncoded publicKeyMultibase) {
        this.id = id;
        this.type = type;
        this.controller = controller;
        this.publicKeyMultibase = publicKeyMultibase;
    }

    public static final VerificationMethod of(
            final DidUrl id) {
        Objects.requireNonNull(id);
        return new ImmutableMultibaseMethod(id, null, null, null);
    }

    public static final VerificationMethod of(
            final DidUrl id,
            final String type,
            final Did controller,
            final MultibaseEncoded publicKeyMultibase) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(type);
        Objects.requireNonNull(controller);
        return new ImmutableMultibaseMethod(id, type, controller, publicKeyMultibase);
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
        return publicKeyMultibase;
    }

    @Override
    public Map<String, Object> publicKeyJwk() {
        return null;
    }
}
