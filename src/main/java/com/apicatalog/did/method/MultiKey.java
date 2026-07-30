package com.apicatalog.did.method;

import java.time.Instant;

import com.apicatalog.did.Did;
import com.apicatalog.did.DidUrl;

/**
 * 
 * @param id
 * @param type
 * @param controller
 * @param expires
 * @param revoked
 * @param publicKey  multicodec-encoded public key, or {@code null}
 * @param secretKey  multicodec-encoded private key, or {@code null}
 * 
 */
public record MultiKey(
        DidUrl id,
        String type,
        Did controller,
        Instant expires,
        Instant revoked,
        byte[] publicKey,
        byte[] secretKey) implements VerificationMethod {

    public static final String TYPE = "https://w3id.org/security#Multikey";
    public static final String TYPE_NAME = "Multikey";
}
