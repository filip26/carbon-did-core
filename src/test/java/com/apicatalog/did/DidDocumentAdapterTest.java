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
import com.apicatalog.multibase.MultibaseDecoder;
import com.apicatalog.tree.io.Tree;
import com.apicatalog.tree.io.jakcson.Jackson2Parser;
import com.fasterxml.jackson.core.JsonFactory;

public class DidDocumentAdapterTest {

    static DidDocumentAdapter ADAPTER = DidDocumentAdapter.newBuilder()
            .context(_ -> true)
            .method(MultiKey.TYPE_NAME,
                    _ -> true,
                    new MultiKeyAdapter(MultibaseDecoder.getInstance()::decode))
            .method(JsonWebKey.TYPE_NAME,
                    _ -> true,
                    new JsonWebKeyAdapter())
            .build();
//    Map.of(/*TODO service adapters */))::readDocument,

    @ParameterizedTest(name = "{0}")
    @MethodSource({ "vectors" })
    void testRead(String uri, String resource) throws IOException {

        var did = Did.parse(uri);

        var doc = ADAPTER.readDocument(did, read(DidDocumentAdapterTest.class.getResourceAsStream(resource)));
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
