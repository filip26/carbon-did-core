package com.apicatalog.did.adapter;

import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

import com.apicatalog.did.Did;
import com.apicatalog.did.DidUrl;
import com.apicatalog.did.primitive.MultiKey;

public class MultiKeyAdapter implements DidDocumentAdapter.MethodAdapter {

    private final Function<String, byte[]> multibaseDecoder;

    public MultiKeyAdapter(Function<String, byte[]> multibaseDecoder) {
        this.multibaseDecoder = multibaseDecoder;
    }

    /**
     * Creates a {@link MultiKey} verification method from a compacted object.
     *
     * @param context
     * @param compacted compacted verification method object
     * @return verification method
     * @throws IllegalArgumentException if the input contains invalid or unsupported
     *                                  properties
     */
    public MultiKey readMethod(Collection<String> context, Map<String, Object> compacted) {

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
                if (!MultiKey.TYPE_NAME.equals(entry.getValue())) {
                    throw new IllegalArgumentException(
                            "Expected type '" + MultiKey.TYPE_NAME + "' but found '" + entry.getValue() + '\'');
                }
            }
            case Vocab.KEY_CONTROLLER -> controller = MapEntryAdapter.did(entry);
            case Vocab.KEY_EXPIRES -> expires = MapEntryAdapter.instant(entry);
            case Vocab.KEY_REVOKED -> revoked = MapEntryAdapter.instant(entry);
            case Vocab.KEY_PUBLIC_KEY_MULTIBASE ->
                publicKey = multibaseDecoder.apply(MapEntryAdapter.string(entry));
            case Vocab.KEY_SECRET_KEY_MULTIBASE ->
                secretKey = multibaseDecoder.apply(MapEntryAdapter.string(entry));

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