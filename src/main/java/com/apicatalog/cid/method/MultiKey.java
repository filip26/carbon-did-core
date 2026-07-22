package com.apicatalog.cid.method;

import java.time.Instant;
import java.util.Map;
import java.util.function.Function;

public record MultiKey(
        String id,
        String controller,
        Instant expires,
        Instant revoked,
        byte[] publicKey,
        byte[] secretKey) implements VerificationMethod {

    public static final String TYPE = "https://w3id.org/security#Multikey";
    public static final String TYPE_NAME = "Multikey";

    static final String PUBLIC_KEY_MULTIBASE_KEY = "publicKeyMultibase";
    static final String SECRET_KEY_MULTIBASE_KEY = "secretKeyMultibase";

    @Override
    public String type() {
        return TYPE_NAME;
    }

    /**
     * Creates a {@link MultiKey} verification method from a compacted JSON-LD
     * representation.
     *
     * @param compacted        compacted verification method object
     * @param multibaseDecoder
     * @return parsed multikey verification method
     * @throws IllegalArgumentException if a property has an invalid type
     */
    public static MultiKey from(Map<String, Object> compacted, Function<String, byte[]> multibaseDecoder) {

        String id = null;
        String controller = null;
        Instant expires = null;
        Instant revoked = null;
        byte[] publicKey = null;
        byte[] secretKey = null;

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
            case PUBLIC_KEY_MULTIBASE_KEY -> publicKey = multibaseDecoder.apply(MapAdapter.string(entry));
            case SECRET_KEY_MULTIBASE_KEY -> secretKey = multibaseDecoder.apply(MapAdapter.string(entry));

            default -> throw new IllegalArgumentException(
                    "Unsupported property: " + entry.getKey());
            }
        }

        return new MultiKey(
                id,
                controller,
                expires,
                revoked,
                publicKey,
                secretKey);
    }
}
