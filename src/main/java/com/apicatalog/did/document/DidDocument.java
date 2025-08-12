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

    default Set<VerificationMethod> verification() {
        return Collections.emptySet();
    }

    default Set<URI> alsoKnownAs() {
        return Collections.emptySet();
    }

    default Set<VerificationMethod> authentication() {
        return Collections.emptySet();
    }

    default Set<VerificationMethod> assertion() {
        return Collections.emptySet();
    }

    default Set<VerificationMethod> keyAgreement() {
        return Collections.emptySet();
    }

    default Set<VerificationMethod> capabilityInvocation() {
        return Collections.emptySet();
    }

    default Set<VerificationMethod> capabilityDelegation() {
        return Collections.emptySet();
    }

    default Set<Service> service() {
        return Collections.emptySet();
    }

    default boolean hasRequiredProperties() {
        return id() != null;
    }

}
