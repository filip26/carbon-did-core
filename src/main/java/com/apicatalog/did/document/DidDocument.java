package com.apicatalog.did.document;

import java.net.URI;
import java.util.Collections;
import java.util.Set;

import com.apicatalog.did.Did;

/**
 * DID Document
 * 
 * @see <a href="https://www.w3.org/TR/did-core/#did-document-properties">DID
 *      document properties</a>
 */
public interface DidDocument {

    Did id();

    default Set<Did> controller() {
        return Collections.emptySet();
    }

    default Set<DidVerificationMethod> verification() {
        return Collections.emptySet();
    }

    default Set<URI> alsoKnownAs() {
        return Collections.emptySet();
    }

    default Set<DidVerificationMethod> authentication() {
        return Collections.emptySet();
    }

    default Set<DidVerificationMethod> assertion() {
        return Collections.emptySet();
    }

    default Set<DidVerificationMethod> keyAgreement() {
        return Collections.emptySet();
    }

    default Set<DidVerificationMethod> capabilityInvocation() {
        return Collections.emptySet();
    }

    default Set<DidVerificationMethod> capabilityDelegation() {
        return Collections.emptySet();
    }

    default Set<DidService> service() {
        return Collections.emptySet();
    }

    default boolean hasRequiredProperties() {
        return id() != null;
    }

}
