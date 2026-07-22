package com.apicatalog.cid.method;

import java.time.Instant;
import java.util.Map;

/**
 * Represents a JSON Web Key (JWK) verification method in accordance with the
 * W3C CID 1.0 specification.
 *
 * <p>
 * This record encapsulates cryptographic keys formatted as JSON Web Keys (RFC
 * 7517), enabling the representation of both public and private key material
 * along with decentralized identity metadata such as controller association and
 * lifecycle timestamps.
 * </p>
 *
 * <p>
 * Reference: <a href="https://www.w3.org/TR/cid-1.0/#JsonWebKey">JsonWebKey</a>
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
        String id,
        String controller,
        Instant expires,
        Instant revoked,
        Map<String, Object> publicKeyJwk,
        Map<String, Object> secretKeyJwk) implements VerificationMethod {

    /** Verification method type. */
    public static final String TYPE = "https://w3id.org/security#JsonWebKey";

    /** Verification method type. */
    public static final String TYPE_NAME = "JsonWebKey";

    /** JSON property containing the public JWK. */
    public static final String PUBLIC_KEY_JWK = "publicKeyJwk";

    /** JSON property containing the secret/private JWK. */
    public static final String SECRET_KEY_JWK = "secretKeyJwk";

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
     * @return parsed verification method
     * @throws IllegalArgumentException if the input contains invalid or unsupported
     *                                  properties
     */
    public static JsonWebKey from(Map<String, Object> compacted) {

        String id = null;
        String controller = null;
        Instant expires = null;
        Instant revoked = null;
        Map<String, Object> publicKeyJwk = null;
        Map<String, Object> secretKeyJwk = null;

        for (var entry : compacted.entrySet()) {

            if (entry.getValue() == null) {
                continue;
            }

            switch (entry.getKey()) {
            case ID_KEY -> id = MapAdapter.url(entry);
            case TYPE_KEY -> {
                if (!TYPE_NAME.equals(entry.getValue())) {
                    throw new IllegalArgumentException(
                            "Expected type '" + TYPE_NAME + "' but found '" + entry.getValue() + '\'');
                }
            }
            case CONTROLLER_KEY -> controller = MapAdapter.url(entry);
            case EXPIRES_KEY -> expires = MapAdapter.instant(entry);
            case REVOKED_KEY -> revoked = MapAdapter.instant(entry);
            case PUBLIC_KEY_JWK -> publicKeyJwk = MapAdapter.object(entry);
            case SECRET_KEY_JWK -> secretKeyJwk = MapAdapter.object(entry);
            default -> throw new IllegalArgumentException(
                    "Unsupported property: " + entry.getKey());
            }
        }
        return new JsonWebKey(id, controller, expires, revoked, publicKeyJwk, secretKeyJwk);
    }
}
