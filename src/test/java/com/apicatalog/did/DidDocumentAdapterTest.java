package com.apicatalog.did;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.apicatalog.did.adapter.DidDocumentAdapter;
import com.apicatalog.did.adapter.JsonWebKeyAdapter;
import com.apicatalog.did.adapter.MultiKeyAdapter;
import com.apicatalog.did.method.JsonWebKey;
import com.apicatalog.did.method.MultiKey;
import com.apicatalog.multibase.Multibase;
import com.apicatalog.multibase.MultibaseDecoder;
import com.apicatalog.multicodec.codec.KeyCodec;
import com.apicatalog.tree.io.Tree;
import com.apicatalog.tree.io.jakcson.Jackson2Parser;
import com.fasterxml.jackson.core.JsonFactory;

public class DidDocumentAdapterTest {

    static DidDocumentAdapter ADAPTER = DidDocumentAdapter.newBuilder()
            .context(ctx -> ctx.contains("https://www.w3.org/ns/did/v1")
                    || ctx.contains("https://www.w3.org/ns/did/v1.1rc1"))

            .method(MultiKey.TYPE_NAME,
                    ctx -> ctx.contains("https://www.w3.org/ns/did/v1.1rc1")
                            || ctx.contains("https://w3id.org/security/multikey/v1"),
                    new MultiKeyAdapter(MultibaseDecoder.getInstance()::decode))

            .method("Ed25519VerificationKey2020",
                    ctx -> ctx.contains("https://w3id.org/security/suites/ed25519-2020/v1"),
                    new MultiKeyAdapter(
                            "Ed25519VerificationKey2020",
                            Multibase.BASE_58_BTC::decode,
                            KeyCodec.ED25519_PUBLIC::isEncoded))

            .method(JsonWebKey.TYPE_NAME,
                    ctx -> ctx.contains("https://www.w3.org/ns/did/v1.1rc1")
                            || ctx.contains("https://w3id.org/security/jwk/v1"),
                    new JsonWebKeyAdapter())

            .method("JsonWebKey2020",
                    ctx -> ctx.contains("https://w3id.org/security/suites/jws-2020/v1"),
                    new JsonWebKeyAdapter("JsonWebKey2020"))

            .genericServiceAdapter()

            .build();

    @ParameterizedTest(name = "{0}")
    @MethodSource({ "vectors" })
    void testRead(String uri, String resource) throws IOException {

        var did = Did.parse(uri);

        var doc = ADAPTER.mapDocument(did, read(DidDocumentAdapterTest.class.getResourceAsStream(resource)));
        assertNotNull(doc);
    }

    static Map<String, Object> read(InputStream is) throws IOException {

        Objects.requireNonNull(is);

        try (var parser = Jackson2Parser.newParser(is, JsonFactory.builder().build())) {
            return Tree.read(parser);
        }
    }

    static Stream<Arguments> vectors() {
        return Stream.of(
                Arguments.of(
                        "did:example:123456789abcdefghi",
                        "did-doc-1.json"));
    }
}
