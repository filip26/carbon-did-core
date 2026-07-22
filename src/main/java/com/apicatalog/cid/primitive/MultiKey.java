package com.apicatalog.cid.primitive;

import java.time.Instant;
import java.util.Map;
import java.util.function.Function;

import com.apicatalog.cid.VerificationMethod;

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
     * Creates a {@link MultiKey} verification method from a compacted object.
     *
     * @param compacted        compacted verification method object
     * @param multibaseDecoder
     * @return verification method
     * @throws IllegalArgumentException if the input contains invalid or unsupported
     *                                  properties
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
            case Vocab.ID_KEY -> id = MapAdapter.url(entry);
            case Vocab.TYPE_KEY -> {
                if (!TYPE_NAME.equals(entry.getValue())) {
                    throw new IllegalArgumentException(
                            "Expected type '" + TYPE_NAME + "' but found '" + entry.getValue() + '\'');
                }
            }
            case Vocab.CONTROLLER_KEY -> controller = MapAdapter.url(entry);
            case Vocab.EXPIRES_KEY -> expires = MapAdapter.instant(entry);
            case Vocab.REVOKED_KEY -> revoked = MapAdapter.instant(entry);
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
