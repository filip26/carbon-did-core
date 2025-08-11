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
                        "%E2%9C%93"),
                Arguments.of(
                        "did:key:z6MkpTHR8VNsBxYAAWHut2Geadd9jSwuBV8xRoAnwWsdvktH",
                        "key",
                        "z6MkpTHR8VNsBxYAAWHut2Geadd9jSwuBV8xRoAnwWsdvktH"),

                Arguments.of(
                        "did:key:z6MkhwTbtMsvDSB8z2pF8A4DRNirMRHkRkNvztNFVQVw1W8H",
                        "key",
                        "z6MkhwTbtMsvDSB8z2pF8A4DRNirMRHkRkNvztNFVQVw1W8H"),

                Arguments.of(
                        "did:web:example.com",
                        "web",
                        "example.com"),

                Arguments.of(
                        "did:web:example.com:user:alice",
                        "web",
                        "example.com:user:alice"),

                /* pct-encoded: space */
                Arguments.of(
                        "did:web:example.com:users:alice%20smith",
                        "web",
                        "example.com:users:alice%20smith"),

                /* pct-encoded: colon (port-like), plus another segment */
                Arguments.of(
                        "did:web:example.com%3A8443:users:alice",
                        "web",
                        "example.com%3A8443:users:alice"),

                /* pct-encoded: slash inside a single segment */
                Arguments.of(
                        "did:web:example.com:dir%2Fsubdir%2Ffile",
                        "web",
                        "example.com:dir%2Fsubdir%2Ffile"),

                /* pct-encoded: percent sign */
                Arguments.of(
                        "did:web:example.com:foo%25bar",
                        "web",
                        "example.com:foo%25bar"),

                /* pct-encoded: question + hash */
                Arguments.of(
                        "did:web:example.com:ticket%3F42:ref%23A",
                        "web",
                        "example.com:ticket%3F42:ref%23A"),

                /* pct-encoded: multi-byte UTF-8 (✓) */
                Arguments.of(
                        "did:web:example.com:unicode%E2%9C%93",
                        "web",
                        "example.com:unicode%E2%9C%93"),

                /* pct-encoded: emoji (😀) */
                Arguments.of(
                        "did:web:example.com:emoji%F0%9F%98%80",
                        "web",
                        "example.com:emoji%F0%9F%98%80"),

                /* pct-encoded: tilde (unreserved but here encoded for coverage) */
                Arguments.of(
                        "did:web:sub.example.com%3A8443:%7Ealice",
                        "web",
                        "sub.example.com%3A8443:%7Ealice"),

                Arguments.of(
                        "did:pkh:eip155:1:0xF39Fd6e51aad88F6F4ce6aB8827279cffFb92266",
                        "pkh",
                        "eip155:1:0xF39Fd6e51aad88F6F4ce6aB8827279cffFb92266"),

                Arguments.of(
                        "did:ethr:0x5:0x90f8bf6a479f320ead074411a4b0e7944ea8c9c1",
                        "ethr",
                        "0x5:0x90f8bf6a479f320ead074411a4b0e7944ea8c9c1"),

                Arguments.of(
                        "did:sov:2wJPyULfLLnYTEFYzByfUR",
                        "sov",
                        "2wJPyULfLLnYTEFYzByfUR"),

                Arguments.of(
                        "did:uuid:123e4567-e89b-12d3-a456-426614174000",
                        "uuid",
                        "123e4567-e89b-12d3-a456-426614174000"),

                Arguments.of(
                        "did:jwk:eyJrdHkiOiJFQyIsImNydiI6IlAtMjU2In0",
                        "jwk",
                        "eyJrdHkiOiJFQyIsImNydiI6IlAtMjU2In0"),

                Arguments.of(
                        "did:btcr:xz35-jzv2-qqs2-7x4",
                        "btcr",
                        "xz35-jzv2-qqs2-7x4"));
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
                Arguments.of("did:example:123456/path"),
                Arguments.of("did:example:123 456"),
                Arguments.of("did:example:"), // missing final 1*idchar
                Arguments.of("did:example"), // missing method-specific-id
                Arguments.of("did:"), // missing method and msi
                Arguments.of("did"), // not a URI
                Arguments.of("did:EXAMPLE:abc"), // uppercase in method-name
                Arguments.of("did:ex_ample:abc"), // '_' not allowed in method-name
                Arguments.of("did:ex.ample:abc"), // '.' not allowed in method-name
                Arguments.of("did:%65xample:abc"), // pct-encoding not allowed in method-name
                Arguments.of("did:ExAmple:abc"), // mixed case method-name
                Arguments.of("did::abc"), // empty method-name
                Arguments.of("did:example:abc:"), // final empty segment
                Arguments.of("did:example::"), // final empty segment (last is empty)
                Arguments.of("did:web:example.com:"), // final empty segment
                Arguments.of("did:web:example.com::"), // final empty segment
                Arguments.of("did:web:example.com:users:"), // final empty segment
                Arguments.of("did:web:example.com/path"), // path not allowed in bare DID
                Arguments.of("did:web:example.com?query"), // query not allowed in bare DID
                Arguments.of("did:web:example.com#frag"), // fragment not allowed in bare DID
                Arguments.of("did:web:example.com#"), // trailing '#'
                Arguments.of("did://example.com:abc"), // authority present (//...)
                Arguments.of("did:example:abc/def"), // raw '/' not allowed (must be %2F)
                Arguments.of("did:example:alice@corp"), // '@' not allowed
                Arguments.of("did:example:alice,corp"), // ',' not allowed
                Arguments.of("did:example:;alice"), // ';' not allowed
                Arguments.of("did:example:(alice)"), // parentheses not allowed
                Arguments.of("did:example:~alice"), // '~' not allowed unescaped
                Arguments.of("did:example:%"), // lone '%'
                Arguments.of("did:example:%2"), // incomplete pct-encoded
                Arguments.of("did:example:%ZZ"), // non-hex pct-encoded
                Arguments.of("did:example:foo%2Gbar"), // bad hex digit
                Arguments.of("did:web:example.com:users:alice%2"), // incomplete pct-encoded
                Arguments.of("did:web:example.com%3G:users"), // invalid pct-encoded in segment
                Arguments.of("did:example:ä"), // non-ASCII unescaped
                Arguments.of("did:examplé:abc"), // non-ASCII in method-name
                Arguments.of("did%3Aexample:abc"), // encoded ':' in scheme
                Arguments.of(" did:example:abc"), // leading space
                Arguments.of("did:example:abc ") // trailing space
        );
    }

}
