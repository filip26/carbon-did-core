package com.apicatalog.did;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.URI;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("DID")
@TestMethodOrder(OrderAnnotation.class)
class DidTest {

    @DisplayName("of(String)")
    @ParameterizedTest(name = "{0}")
    @MethodSource({ "positiveVectors" })
    void ofString(String uri, String method, String specificId) {
        final Did did = Did.of(uri);

        assertNotNull(did);
        assertFalse(did.isDidUrl());
        assertEquals(method, did.getMethod());
        assertEquals(specificId, did.getMethodSpecificId());
    }

    @DisplayName("!of(String)")
    @ParameterizedTest()
    @MethodSource({ "negativeVectors" })
    void ofStringNegative(String uri) {
        try {
            Did.of(uri);
            fail();
        } catch (IllegalArgumentException e) {
            /* expected */ }
    }

    @DisplayName("of(URI)")
    @ParameterizedTest(name = "{0}")
    @MethodSource({ "positiveVectors" })
    void ofUri(String input, String method, String specificId) {
        final Did did = Did.of(URI.create(input));

        assertNotNull(did);
        assertFalse(did.isDidUrl());
        assertEquals(method, did.getMethod());
        assertEquals(specificId, did.getMethodSpecificId());
    }

    @DisplayName("toString()")
    @ParameterizedTest(name = "{0}")
    @MethodSource({ "positiveVectors" })
    void toString(String input, String method, String specificId) {
        final Did did = Did.of(input);

        assertNotNull(did);
        assertEquals(input, did.toString());
    }

    @DisplayName("!of(URI)")
    @ParameterizedTest()
    @MethodSource({ "negativeVectors" })
    void ofUriNegative(String uri) {
        try {
            Did.of(URI.create(uri));
            fail();
        } catch (IllegalArgumentException | NullPointerException e) {
            /* expected */ }
    }

    @DisplayName("toUri()")
    @ParameterizedTest(name = "{0}")
    @MethodSource({ "positiveVectors" })
    void toUri(String input, String method, String specificId) {
        final Did did = Did.of(URI.create(input));

        assertNotNull(did);
        assertEquals(URI.create(input), did.toUri());
    }

    @DisplayName("isDid(String)")
    @ParameterizedTest(name = "{0}")
    @MethodSource({ "positiveVectors" })
    void stringIsDid(String uri) {
        assertTrue(Did.isDid(uri));
    }

    @DisplayName("isNotDid(String)")
    @ParameterizedTest()
    @MethodSource({ "negativeVectors" })
    void stringIsNotDid(String uri) {
        assertFalse(Did.isDid(uri));
    }

    @DisplayName("isDid(URI)")
    @ParameterizedTest(name = "{0}")
    @MethodSource({ "positiveVectors" })
    void uriIsDid(String uri) {
        assertTrue(Did.isDid(URI.create(uri)));
    }

    static Stream<Arguments> positiveVectors() {
        return Stream.of(
                Arguments.of(
                        "did:key:z6MkpTHR8VNsBxYAAWHut2Geadd9jSwuBV8xRoAnwWsdvktH",
                        "key",
                        "z6MkpTHR8VNsBxYAAWHut2Geadd9jSwuBV8xRoAnwWsdvktH"),
                Arguments.of("did:example:z6MkicdicToW5HbxPP7zZV1H7RHvXgRMhoujWAF2n5WQkdd2",
                        "example",
                        "z6MkicdicToW5HbxPP7zZV1H7RHvXgRMhoujWAF2n5WQkdd2"),
                Arguments.of("did:key:1.1:z6MkicdicToW5HbxPP7zZV1H7RHvXgRMhoujWAF2n5WQkdd2",
                        "key",
                        "1.1:z6MkicdicToW5HbxPP7zZV1H7RHvXgRMhoujWAF2n5WQkdd2"),
                Arguments.of("did:web:method:specific:identifier",
                        "web",
                        "method:specific:identifier"),
                Arguments.of("did:tdw:example.com:dids:12345",
                        "tdw",
                        "example.com:dids:12345"),
                Arguments.of("did:tdw:12345.example.com",
                        "tdw",
                        "12345.example.com"),
                Arguments.of("did:tdw:example.com_12345",
                        "tdw",
                        "example.com_12345"),
                Arguments.of("did:0:%E2%9C%93",
                        "0",
                        "%E2%9C%93"
                        ));
    }

    static Stream<Arguments> negativeVectors() {
        return Stream.of(
                Arguments.of("did:example:123456/path"),
                Arguments.of("did:example:123456?versionId=1"),
                Arguments.of("did:example:123#public-key-0"),
                Arguments.of("did:example:123?service=agent&relativeRef=/credentials#degree"),
                Arguments.of("did:example:123?service=files&relativeRef=/resume.pdf"),
                Arguments.of("did:example:123#"),
                Arguments.of("did:example:123?"),
                Arguments.of("did:example:123/"),
                Arguments.of(""),
                Arguments.of("https://example.com"),
                Arguments.of("irc:example:channel"),
                Arguments.of("did:example.com:channel"),
                Arguments.of("did:example: "),
                Arguments.of("did:example:"),
                Arguments.of("did:example"),
                Arguments.of("did:"),
                Arguments.of("did"),
                Arguments.of(":example:channel"),
                Arguments.of(" :example:channel"),
                Arguments.of("did::channel"),
                Arguments.of("did: :channel"),
                Arguments.of(" did:method:id"),
                Arguments.of("did:method:id ")
                );
    }

}
