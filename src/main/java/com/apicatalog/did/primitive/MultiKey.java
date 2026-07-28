package com.apicatalog.did.primitive;

import java.time.Instant;

import com.apicatalog.did.Did;
import com.apicatalog.did.DidUrl;
import com.apicatalog.did.VerificationMethod;

/**
 * 
 * @param id
 * @param controller
 * @param expires
 * @param revoked
 * @param publicKey  multicodec-encoded public key, or {@code null}
 * @param secretKey  multicodec-encoded private key, or {@code null}
 * 
 */
public record MultiKey(
        DidUrl id,
        Did controller,
        Instant expires,
        Instant revoked,
        byte[] publicKey,
        byte[] secretKey) implements VerificationMethod {

    public static final String TYPE = "https://w3id.org/security#Multikey";
    public static final String TYPE_NAME = "Multikey";

    @Override
    public String type() {
        return TYPE_NAME;
    }
}
