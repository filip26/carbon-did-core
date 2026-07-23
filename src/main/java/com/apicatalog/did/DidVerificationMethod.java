package com.apicatalog.did;

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
     * Checks whether this verification method has the required properties:
     * {@code id}, {@code type}, and {@code controller}.
     *
     * @return {@code true} if valid
     */
    default boolean hasRequiredProperties() {
        return id() != null && type() != null && controller() != null;
    }

//    /**
//     * Compares two verification methods for equality of {@code id}, {@code type},
//     * {@code controller}, {@code publicKeyMultibase}, and {@code publicKeyJwk}.
//     *
//     * @param method1 first method (may be {@code null})
//     * @param method2 second method (may be {@code null})
//     * @return {@code true} if both are equal
//     */
//    static boolean equals(final DidVerificationMethod method1, final DidVerificationMethod method2) {
//        if (method1 == null || method2 == null) {
//            return method1 == method2;
//        }
//        return Objects.equals(method1.id(), method2.id())
//                && Objects.equals(method1.type(), method2.type())
//                && Objects.equals(method1.controller(), method2.controller())
    //// && Objects.equals(method1.publicKeyMultibase(),
    /// method2.publicKeyMultibase()) / && Objects.equals(method1.publicKeyJwk(),
    /// method2.publicKeyJwk()) /
//                ;
//    }
}
