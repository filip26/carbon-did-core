package com.apicatalog.did.document;

import java.net.URI;
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

    Set<Did> controller();

    Set<VerificationMethod> verification();

    Set<URI> alsoKnownAs();

    Set<VerificationMethod> authentication();

    Set<VerificationMethod> assertion();

    Set<VerificationMethod> keyAgreement();

    Set<VerificationMethod> capabilityInvocation();

    Set<VerificationMethod> capabilityDelegation();

    Set<Service> service();
}
