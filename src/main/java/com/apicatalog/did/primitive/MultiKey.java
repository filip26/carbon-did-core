package com.apicatalog.did.primitive;

import java.time.Instant;
import java.util.Map;
import java.util.function.Function;

import com.apicatalog.did.Did;
import com.apicatalog.did.DidUrl;
import com.apicatalog.did.DidVerificationMethod;

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
        byte[] secretKey) implements DidVerificationMethod {

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

        DidUrl id = null;
        Did controller = null;
        Instant expires = null;
        Instant revoked = null;
        byte[] publicKey = null;
        byte[] secretKey = null;

        for (var entry : compacted.entrySet()) {

            if (entry.getValue() == null) {
                continue;
            }

            switch (entry.getKey()) {
            case Vocab.KEY_ID -> id = MapEntryAdapter.didUrl(entry);
            case Vocab.KEY_TYPE -> {
                if (!TYPE_NAME.equals(entry.getValue())) {
                    throw new IllegalArgumentException(
                            "Expected type '" + TYPE_NAME + "' but found '" + entry.getValue() + '\'');
                }
            }
            case Vocab.KEY_CONTROLLER -> controller = MapEntryAdapter.did(entry);
            case Vocab.KEY_EXPIRES -> expires = MapEntryAdapter.instant(entry);
            case Vocab.KEY_REVOKED -> revoked = MapEntryAdapter.instant(entry);
            case Vocab.KEY_PUBLIC_KEY_MULTIBASE -> publicKey = multibaseDecoder.apply(MapEntryAdapter.string(entry));
            case Vocab.KEY_SECRET_KEY_MULTIBASE -> secretKey = multibaseDecoder.apply(MapEntryAdapter.string(entry));

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
