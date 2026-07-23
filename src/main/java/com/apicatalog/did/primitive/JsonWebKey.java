package com.apicatalog.did.primitive;

import java.time.Instant;
import java.util.Map;

import com.apicatalog.did.Did;
import com.apicatalog.did.DidUrl;
import com.apicatalog.did.DidVerificationMethod;

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
        Map<String, Object> secretKeyJwk) implements DidVerificationMethod {

    /** Verification method type. */
    public static final String TYPE = "https://w3id.org/security#JsonWebKey";

    /** Verification method type. */
    public static final String TYPE_NAME = "JsonWebKey";

    @Override
    public String type() {
        return TYPE;
    }

    /**
     * Creates a {@code JsonWebKey} from a compacted object.
     *
     * <p>
     * The supplied map must contain only properties defined by the
     * {@code JsonWebKey} verification method. Unknown properties or values of an
     * unexpected type result in an {@link IllegalArgumentException}.
     * </p>
     *
     * @param compacted compacted object
     * @return verification method
     * @throws IllegalArgumentException if the input contains invalid or unsupported
     *                                  properties
     */
    public static JsonWebKey from(Map<String, Object> compacted) {

        DidUrl id = null;
        Did controller = null;
        Instant expires = null;
        Instant revoked = null;
        Map<String, Object> publicKeyJwk = null;
        Map<String, Object> secretKeyJwk = null;

        for (var entry : compacted.entrySet()) {

            if (entry.getValue() == null) {
                continue;
            }

            switch (entry.getKey()) {
            case Vocab.KEY_ID -> id = MapAdapter.didUrl(entry);
            case Vocab.KEY_TYPE -> {
                if (!TYPE_NAME.equals(entry.getValue())) {
                    throw new IllegalArgumentException(
                            "Expected type '" + TYPE_NAME + "' but found '" + entry.getValue() + '\'');
                }
            }
            case Vocab.KEY_CONTROLLER -> controller = MapAdapter.did(entry);
            case Vocab.KEY_EXPIRES -> expires = MapAdapter.instant(entry);
            case Vocab.KEY_REVOKED -> revoked = MapAdapter.instant(entry);
            case Vocab.KEY_PUBLIC_KEY_JWK -> publicKeyJwk = MapAdapter.object(entry);
            case Vocab.KEY_SECRET_KEY_JWK -> secretKeyJwk = MapAdapter.object(entry);
            default -> throw new IllegalArgumentException(
                    "Unsupported property: " + entry.getKey());
            }
        }
        return new JsonWebKey(id, controller, expires, revoked, publicKeyJwk, secretKeyJwk);
    }
}
