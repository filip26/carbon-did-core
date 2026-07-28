package com.apicatalog.did.adapter;

import java.time.Instant;
import java.util.Collection;
import java.util.Map;

import com.apicatalog.did.Did;
import com.apicatalog.did.DidUrl;
import com.apicatalog.did.DidVocab;
import com.apicatalog.did.primitive.JsonWebKey;

public class JsonWebKeyAdapter implements DidDocumentAdapter.MethodAdapter {

    private final String typeName;

    public JsonWebKeyAdapter() {
        this(JsonWebKey.TYPE_NAME);
    }

    public JsonWebKeyAdapter(String typeName) {
        this.typeName = typeName;
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
     * @param context
     * @param compacted compacted object
     * @return verification method
     * @throws IllegalArgumentException if the input contains invalid or unsupported
     *                                  properties
     */
    public JsonWebKey readMethod(Collection<String> context, Map<String, Object> compacted) {

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
            case DidVocab.KEY_ID -> id = MapEntryAdapter.didUrl(entry);
            case DidVocab.KEY_TYPE -> {
                if (!typeName.equals(entry.getValue())) {
                    throw new IllegalArgumentException(
                            "Expected type '" + typeName + "' but found '" + entry.getValue() + '\'');
                }
            }
            case DidVocab.KEY_CONTROLLER -> controller = MapEntryAdapter.did(entry);
            case DidVocab.KEY_EXPIRES -> expires = MapEntryAdapter.instant(entry);
            case DidVocab.KEY_REVOKED -> revoked = MapEntryAdapter.instant(entry);
            case DidVocab.KEY_PUBLIC_KEY_JWK -> publicKeyJwk = MapEntryAdapter.object(entry);
            case DidVocab.KEY_SECRET_KEY_JWK -> secretKeyJwk = MapEntryAdapter.object(entry);
            default -> throw new IllegalArgumentException(
                    "Unsupported property: " + entry.getKey());
            }
        }
        return new JsonWebKey(id, controller, expires, revoked, publicKeyJwk, secretKeyJwk);
    }
}
