package com.apicatalog.did.primitive;

import java.time.Instant;
import java.util.Map;

import com.apicatalog.did.Did;
import com.apicatalog.did.DidUrl;
import com.apicatalog.did.VerificationMethod;

/**
 * Represents a JSON Web Key (JWK) verification method in accordance with the
 * W3C DID 1.1 specification.
 *
 * <p>
 * This record encapsulates cryptographic keys formatted as JSON Web Keys (RFC
 * 7517), enabling the representation of both public and private key material
 * along with decentralized identity metadata such as controller association and
 * lifecycle timestamps.
 * </p>
 *
 * @param id           The unique identifier of the verification method.
 * @param controller   The entity controlling this verification method.
 * @param expires      The timestamp when this key expires, or null if not
 *                     applicable.
 * @param revoked      The timestamp when this key was revoked, or null if not
 *                     applicable.
 * @param publicKeyJwk A map representing the public key properties in JWK
 *                     format.
 * @param secretKeyJwk A map representing the secret key properties in JWK
 *                     format.
 */
public record JsonWebKey(
        DidUrl id,
        Did controller,
        Instant expires,
        Instant revoked,
        Map<String, Object> publicKeyJwk,
        Map<String, Object> secretKeyJwk) implements VerificationMethod {

    /** Verification method type. */
    public static final String TYPE = "https://w3id.org/security#JsonWebKey";

    /** Verification method type. */
    public static final String TYPE_NAME = "JsonWebKey";

    @Override
    public String type() {
        return TYPE;
    }
}
