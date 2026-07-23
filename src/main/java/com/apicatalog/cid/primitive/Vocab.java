package com.apicatalog.cid.primitive;

class Vocab {

    public static final String MULTIKEY_TYPE = "https://w3id.org/security#Multikey";
    public static final String MULTIKEY_TYPE_NAME = "Multikey";

    public static final String JSON_WEB_KEY_TYPE = "https://w3id.org/security#JsonWebKey";
    public static final String JSON_WEB_KEY_TYPE_NAME = "JsonWebKey";

    static final String KEY_ID = "id";
    static final String KEY_TYPE = "type";
    static final String KEY_CONTROLLER = "controller";
    static final String KEY_EXPIRES = "expires";
    static final String KEY_REVOKED = "revoked";

    static final String KEY_SERVICE_ENDPOINT = "serviceEndpoint";

    static final String KEY_PUBLIC_KEY_MULTIBASE = "publicKeyMultibase";
    static final String KEY_SECRET_KEY_MULTIBASE = "secretKeyMultibase";

    /** Property containing the public JWK. */
    static final String KEY_PUBLIC_KEY_JWK = "publicKeyJwk";
    /** Property containing the secret/private JWK. */
    static final String KEY_SECRET_KEY_JWK = "secretKeyJwk";

}
