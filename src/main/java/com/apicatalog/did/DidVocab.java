package com.apicatalog.did;

public final class DidVocab {

    private DidVocab() {
        // no instance allowed, just container
    }
    
    public static final String NS_SECURITY = "https://w3id.org/security#";
    public static final String NS_ACTIVITYSTREAMS = "https://www.w3.org/ns/activitystreams#";
    public static final String NS_DID = "https://www.w3.org/ns/did#";

    public static final String KEY_ID = "id";
    public static final String KEY_TYPE = "type";
    public static final String KEY_CONTROLLER = "controller";
    public static final String KEY_ALSO_KNOWN_AS = "alsoKnownAs";

    public static final String KEY_AUTHENTICATION = "authentication";
    public static final String KEY_VERIFICATION_METHOD = "verificationMethod";
    public static final String KEY_ASSERTION_METHOD = "assertionMethod";
    public static final String KEY_KEY_AGREEMENT = "keyAgreement";
    public static final String KEY_CAPABILITY_INVOCATION = "capabilityInvocation";
    public static final String KEY_CAPABILITY_DELEGATION = "capabilityDelegation";

    public static final String KEY_SERVICE = "service";
    public static final String KEY_SERVICE_ENDPOINT = "serviceEndpoint";

    public static final String KEY_EXPIRES = "expires";
    public static final String KEY_REVOKED = "revoked";

    public static final String KEY_PUBLIC_KEY_MULTIBASE = "publicKeyMultibase";
    public static final String KEY_SECRET_KEY_MULTIBASE = "secretKeyMultibase";

    public static final String KEY_PUBLIC_KEY_JWK = "publicKeyJwk";
    public static final String KEY_SECRET_KEY_JWK = "secretKeyJwk";

}
