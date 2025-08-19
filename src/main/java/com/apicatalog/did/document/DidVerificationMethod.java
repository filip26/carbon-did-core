package com.apicatalog.did.document;

import java.util.Map;
import java.util.Objects;

import com.apicatalog.did.Did;
import com.apicatalog.did.DidUrl;
import com.apicatalog.did.datatype.MultibaseEncoded;

public interface DidVerificationMethod {

    DidUrl id();

    String type();

    Did controller();

    MultibaseEncoded publicKeyMultibase();

    Map<String, Object> publicKeyJwk();

    default boolean hasRequiredProperties() {
        return id() != null && type() != null && controller() != null;
    }

    static boolean equals(final DidVerificationMethod method1, final DidVerificationMethod method2) {
        if (method1 == null || method2 == null) {
            return method1 == method2;
        }
        return Objects.equals(method1.id(), method2.id())
                && Objects.equals(method1.type(), method2.type())
                && Objects.equals(method1.controller(), method2.controller())
                && Objects.equals(method1.publicKeyMultibase(), method2.publicKeyMultibase())
                && Objects.equals(method1.publicKeyJwk(), method2.publicKeyJwk());
    }
    
    static DidVerificationMethod jwk(
            final DidUrl id,
            final String type,
            final Did controller,
            final Map<String, Object> publicKeyJwk) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(type);
        Objects.requireNonNull(controller);
        return new ImmutableJwkMethod(id, type, controller, publicKeyJwk);
    }
    
    static DidVerificationMethod multibase(
            final DidUrl id,
            final String type,
            final Did controller,
            final MultibaseEncoded publicKeyMultibase) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(type);
        Objects.requireNonNull(controller);
        return new ImmutableMultibaseMethod(id, type, controller, publicKeyMultibase);
    }
}