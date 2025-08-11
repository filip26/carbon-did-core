package com.apicatalog.did.primitive;

import java.util.Map;
import java.util.Objects;

import com.apicatalog.did.Did;
import com.apicatalog.did.DidUrl;
import com.apicatalog.did.document.VerificationMethod;

public class ImmutableVerificationMethod implements VerificationMethod {

    protected final DidUrl id;
    protected final String type;
    protected final Did controller;
    protected final String publicKeyMultibase;
    protected final Map<String, Object> publicKeyJwk;

    protected ImmutableVerificationMethod(
            final DidUrl id,
            final String type,
            final Did controller,
            final String publicKeyMultibase,
            final Map<String, Object> publicKeyJwk) {
        this.id = id;
        this.type = type;
        this.controller = controller;
        this.publicKeyMultibase = publicKeyMultibase;
        this.publicKeyJwk = publicKeyJwk;
    }

    public static final ImmutableVerificationMethod of(final DidUrl id,
            final String type,
            final Did controller) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(type);
        Objects.requireNonNull(controller);
        return new ImmutableVerificationMethod(id, type, controller, null, null);
    }

    public static final ImmutableVerificationMethod of(final DidUrl id,
            final String type,
            final Did controller,
            final String publicKeyMultibase) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(type);
        Objects.requireNonNull(controller);
        return new ImmutableVerificationMethod(id, type, controller, publicKeyMultibase, null);
    }

    public static final ImmutableVerificationMethod of(final DidUrl id,
            final String type,
            final Did controller,
            final Map<String, Object> publicKeyJwk) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(type);
        Objects.requireNonNull(controller);
        return new ImmutableVerificationMethod(id, type, controller, null, publicKeyJwk);
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
    public String publicKeyMultibase() {
        return publicKeyMultibase;
    }

    @Override
    public Map<String, Object> publicKeyJwk() {
        return publicKeyJwk;
    }
}
