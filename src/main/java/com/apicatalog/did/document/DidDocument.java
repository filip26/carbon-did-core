package com.apicatalog.did.document;

import java.net.URI;
import java.util.Collections;
import java.util.Set;

import com.apicatalog.did.Did;

/**
 * A <a href="https://www.w3.org/TR/did-core/#did-document-properties">DID
 * Document</a>.
 * <p>
 * Models the top-level properties of a DID Document as defined in the W3C DID
 * Core specification. All accessors return empty sets by default.
 * </p>
 */
public interface DidDocument {

    /**
     * The {@code id} property: the primary identifier of the DID subject.
     *
     * @return the DID identifier, or {@code null} if absent
     */
    Did id();

    /**
     * The {@code controller} property: DIDs that control this DID.
     *
     * @return controller set, possibly empty
     */
    default Set<Did> controller() {
        return Collections.emptySet();
    }

    /**
     * The {@code verificationMethod} property: verification methods defined in this
     * document.
     *
     * @return verification methods, possibly empty
     */
    default Set<DidVerificationMethod> verification() {
        return Collections.emptySet();
    }

    /**
     * The {@code alsoKnownAs} property: additional URIs that refer to the same
     * subject.
     *
     * @return URIs, possibly empty
     */
    default Set<URI> alsoKnownAs() {
        return Collections.emptySet();
    }

    /**
     * The {@code authentication} relationship: methods that can authenticate as the
     * DID subject.
     *
     * @return authentication methods, possibly empty
     */
    default Set<DidVerificationMethod> authentication() {
        return Collections.emptySet();
    }

    /**
     * The {@code assertionMethod} relationship: methods for asserting claims.
     *
     * @return assertion methods, possibly empty
     */
    default Set<DidVerificationMethod> assertion() {
        return Collections.emptySet();
    }

    /**
     * The {@code keyAgreement} relationship: methods for key agreement.
     *
     * @return key agreement methods, possibly empty
     */
    default Set<DidVerificationMethod> keyAgreement() {
        return Collections.emptySet();
    }

    /**
     * The {@code capabilityInvocation} relationship: methods for invoking
     * capabilities.
     *
     * @return invocation methods, possibly empty
     */
    default Set<DidVerificationMethod> capabilityInvocation() {
        return Collections.emptySet();
    }

    /**
     * The {@code capabilityDelegation} relationship: methods for delegating
     * capabilities.
     *
     * @return delegation methods, possibly empty
     */
    default Set<DidVerificationMethod> capabilityDelegation() {
        return Collections.emptySet();
    }

    /**
     * The {@code service} property: service endpoints in this document.
     *
     * @return service definitions, possibly empty
     */
    default Set<DidService> service() {
        return Collections.emptySet();
    }

    /**
     * Checks whether this document has the required {@code id} property.
     *
     * @return {@code true} if {@link #id()} is not {@code null}
     */
    default boolean hasRequiredProperties() {
        return id() != null;
    }

}
