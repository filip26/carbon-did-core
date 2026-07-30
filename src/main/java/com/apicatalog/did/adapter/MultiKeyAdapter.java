package com.apicatalog.did.adapter;

import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import com.apicatalog.did.Did;
import com.apicatalog.did.DidUrl;
import com.apicatalog.did.DidVocab;
import com.apicatalog.did.method.MultiKey;

public class MultiKeyAdapter implements DidDocumentAdapter.MethodAdapter {

    private final String typeName;
    private final Function<String, byte[]> multibaseDecoder;
    private final Predicate<byte[]> isCodecAccepted;

    public MultiKeyAdapter(Function<String, byte[]> multibaseDecoder) {
        this(MultiKey.TYPE_NAME, multibaseDecoder, _ -> true);
    }

    public MultiKeyAdapter(String typeName, Function<String, byte[]> multibaseDecoder) {
        this(typeName, multibaseDecoder, _ -> true);
    }

    public MultiKeyAdapter(
            String typeName,
            Function<String, byte[]> multibaseDecoder,
            Predicate<byte[]> isCodecAccepted) {
        this.typeName = typeName;
        this.multibaseDecoder = multibaseDecoder;
        this.isCodecAccepted = isCodecAccepted;
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
    public MultiKey mapMethod(Collection<String> context, Map<String, Object> compacted) {

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
            case DidVocab.KEY_ID -> id = MapEntryAdapter.didUrl(entry);
            case DidVocab.KEY_TYPE -> {
                if (!typeName.equals(entry.getValue())) {
                    throw new IllegalArgumentException(
                            "Expected type '" + MultiKey.TYPE_NAME + "' but found '" + entry.getValue() + '\'');
                }
            }
            case DidVocab.KEY_CONTROLLER -> controller = MapEntryAdapter.did(entry);
            case DidVocab.KEY_EXPIRES -> expires = MapEntryAdapter.instant(entry);
            case DidVocab.KEY_REVOKED -> revoked = MapEntryAdapter.instant(entry);
            case DidVocab.KEY_PUBLIC_KEY_MULTIBASE -> {
                var encodedPublicKey = MapEntryAdapter.string(entry);
                publicKey = multibaseDecoder.apply(encodedPublicKey);
                if (!isCodecAccepted.test(publicKey)) {
                    throw new IllegalArgumentException("Unsupported public key multicodec " + encodedPublicKey);
                }
            }
            case DidVocab.KEY_SECRET_KEY_MULTIBASE -> {
                var encodedSecretKey = MapEntryAdapter.string(entry);
                secretKey = multibaseDecoder.apply(encodedSecretKey);
                if (!isCodecAccepted.test(publicKey)) {
                    throw new IllegalArgumentException("Unsupported private key multicodec " + encodedSecretKey);
                }
            }
            default -> throw new IllegalArgumentException(
                    "Unsupported property: " + entry.getKey());
            }
        }

        return new MultiKey(
                id,
                typeName,
                controller,
                expires,
                revoked,
                publicKey,
                secretKey);
    }
}