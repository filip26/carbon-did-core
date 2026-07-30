package com.apicatalog.did.method;

import java.util.Map;
import java.util.Optional;

import com.apicatalog.did.Did;
import com.apicatalog.did.DidDocument;
import com.apicatalog.did.DidUrl;
import com.apicatalog.did.DidDocument.Relationship;

/**
 * A <a href=
 * "https://www.w3.org/TR/did-core/#verification-methods">verificationMethod</a>
 * entry within a DID Document.
 */
public interface VerificationMethod {

    @FunctionalInterface
    public interface Resolver {
        Optional<VerificationMethod> resolveMethod(DidUrl url, Relationship rel, Map<String, Object> options);
    }

    @FunctionalInterface
    public interface Dereferencer {
        Optional<VerificationMethod> findMethod(DidDocument document, DidUrl url, Relationship rel);
    }

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
     * Checks whether this verification method has the required properties:
     * {@code id}, {@code type}, and {@code controller}.
     *
     * @return {@code true} if valid
     */
    default boolean hasRequiredProperties() {
        return id() != null && type() != null && controller() != null;
    }
}
