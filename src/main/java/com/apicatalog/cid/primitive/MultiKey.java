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
            case Vocab.KEY_ID -> id = MapAdapter.url(entry);
            case Vocab.KEY_TYPE -> {
                if (!TYPE_NAME.equals(entry.getValue())) {
                    throw new IllegalArgumentException(
                            "Expected type '" + TYPE_NAME + "' but found '" + entry.getValue() + '\'');
                }
            }
            case Vocab.KEY_CONTROLLER -> controller = MapAdapter.url(entry);
            case Vocab.KEY_EXPIRES -> expires = MapAdapter.instant(entry);
            case Vocab.KEY_REVOKED -> revoked = MapAdapter.instant(entry);
            case Vocab.KEY_PUBLIC_KEY_MULTIBASE -> publicKey = multibaseDecoder.apply(MapAdapter.string(entry));
            case Vocab.KEY_SECRET_KEY_MULTIBASE -> secretKey = multibaseDecoder.apply(MapAdapter.string(entry));

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
