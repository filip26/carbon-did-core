package com.apicatalog.did.document;

import java.util.Map;
import java.util.Objects;

import com.apicatalog.did.Did;
import com.apicatalog.did.DidUrl;
import com.apicatalog.did.datatype.MultibaseEncoded;

/**
 * A <a href=
 * "https://www.w3.org/TR/did-core/#verification-methods">verificationMethod</a>
 * entry within a DID Document.
 */
public interface DidVerificationMethod {

    /**
     * The unique identifier of this verification method.
     *
     * @return DID URL identifier
     */
    DidUrl id();

    /**
     * The type of this verification method.
     *
     * @return verification method type
     */
    String type();

    /**
     * The controlling DID of this verification method.
     *
     * @return controller DID
     */
    Did controller();

    /**
     * A {@code publicKeyMultibase} value if present.
     *
     * @return multibase-encoded public key, or {@code null}
     */
    MultibaseEncoded publicKeyMultibase();

    /**
     * A {@code publicKeyJwk} value if present.
     *
     * @return JWK representation of the key, or {@code null}
     */
    Map<String, Object> publicKeyJwk();

    /**
     * Checks whether this verification method has the required properties:
     * {@code id}, {@code type}, and {@code controller}.
     *
     * @return {@code true} if valid
     */
    default boolean hasRequiredProperties() {
        return id() != null && type() != null && controller() != null;
    }

    /**
     * Compares two verification methods for equality of {@code id}, {@code type},
     * {@code controller}, {@code publicKeyMultibase}, and {@code publicKeyJwk}.
     *
     * @param method1 first method (may be {@code null})
     * @param method2 second method (may be {@code null})
     * @return {@code true} if both are equal
     */
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

    /**
     * Creates a JWK-based verification method.
     *
     * @param id           method identifier (DID URL, not {@code null})
     * @param type         verification method type
     * @param controller   controlling DID
     * @param publicKeyJwk JWK-formatted key
     * @return a new {@code DidVerificationMethod}
     */
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

    /**
     * Creates a multibase-based verification method.
     *
     * @param id                 method identifier (DID URL, not {@code null})
     * @param type               verification method type
     * @param controller         controlling DID
     * @param publicKeyMultibase multibase-encoded key
     * @return a new {@code DidVerificationMethod}
     */
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
