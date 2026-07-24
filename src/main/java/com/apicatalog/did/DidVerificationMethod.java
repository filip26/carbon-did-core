package com.apicatalog.did;

/**
 * A <a href=
 * "https://www.w3.org/TR/did-core/#verification-methods">verificationMethod</a>
 * entry within a DID Document.
 */
public interface DidVerificationMethod extends DidResource {

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
