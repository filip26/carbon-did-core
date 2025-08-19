package com.apicatalog.did;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("DID URL")
@TestMethodOrder(OrderAnnotation.class)
class DidUrlTest {

    @DisplayName("of(String)")
    @ParameterizedTest(name = "{0}")
    @MethodSource({ "positiveVectors" })
    void ofString(String uri, String method, String specificId, String path, String query, String fragment) {
        final DidUrl didUrl = DidUrl.of(uri);

        assertNotNull(didUrl);
        assertTrue(didUrl.isDidUrl());
        assertEquals(method, didUrl.getMethod());
        assertEquals(specificId, didUrl.getMethodSpecificId());
        assertEquals(path, didUrl.getPath());
        assertEquals(query, didUrl.getQuery());
        assertEquals(fragment, didUrl.getFragment());
    }

    @DisplayName("of(URI)")
    @ParameterizedTest(name = "{0}")
    @MethodSource({ "positiveVectors" })
    void ofUri(String input, String method, String specificId, String path, String query, String fragment) {
        final DidUrl didUrl = DidUrl.of(URI.create(input));

        assertNotNull(didUrl);
        assertTrue(didUrl.isDidUrl());
        assertEquals(method, didUrl.getMethod());
        assertEquals(specificId, didUrl.getMethodSpecificId());
        assertEquals(path, didUrl.getPath());
        assertEquals(query, didUrl.getQuery());
        assertEquals(fragment, didUrl.getFragment());
    }

    @DisplayName("toUri()")
    @ParameterizedTest(name = "{0}")
    @MethodSource({ "positiveVectors" })
    void toUri(String input, String method, String specificId, String path, String query, String fragment) {
        final DidUrl didUrl = DidUrl.of(URI.create(input));

        assertNotNull(didUrl);
        assertEquals(URI.create(input), didUrl.toUri());
    }

    @DisplayName("isDidUrl(String)")
    @ParameterizedTest(name = "{0}")
    @MethodSource({ "positiveVectors" })
    void stringIsDid(String uri) {
        assertTrue(DidUrl.isDidUrl(uri));
    }

    @DisplayName("isDidUrl(URI)")
    @ParameterizedTest(name = "{0}")
    @MethodSource({ "positiveVectors" })
    void uriIsDid(String uri) {
        assertTrue(DidUrl.isDidUrl(URI.create(uri)));
    }

    @DisplayName("toString()")
    @ParameterizedTest(name = "{0}")
    @MethodSource({ "positiveVectors" })
    void toString(String input) {
        final DidUrl didUrl = DidUrl.of(input);

        assertNotNull(didUrl);
        assertEquals(input, didUrl.toString());
    }

    static Stream<Arguments> positiveVectors() {
        return Stream.of(
                Arguments.of(
                        "did:example:z6MkicdicToW5HbxPP7zZV1H7RHvXgRMhoujWAF2n5WQkdd2",
                        "example",
                        "z6MkicdicToW5HbxPP7zZV1H7RHvXgRMhoujWAF2n5WQkdd2",
                        null,
                        null,
                        null),
                Arguments.of("did:key:1.1:z6MkicdicToW5HbxPP7zZV1H7RHvXgRMhoujWAF2n5WQkdd2",
                        "key",
                        "1.1:z6MkicdicToW5HbxPP7zZV1H7RHvXgRMhoujWAF2n5WQkdd2",
                        null,
                        null,
                        null),
                Arguments.of("did:web:method:specific:identifier",
                        "web",
                        "method:specific:identifier",
                        null,
                        null,
                        null),
                Arguments.of("did:example:123456/path",
                        "example",
                        "123456",
                        "/path",
                        null,
                        null),
                Arguments.of("did:example:123456?versionId=1",
                        "example",
                        "123456",
                        null,
                        "versionId=1",
                        null),
                Arguments.of("did:example:123#public-key-0",
                        "example",
                        "123",
                        null,
                        null,
                        "public-key-0"),
                Arguments.of("did:example:123?service=agent&relativeRef=/credentials#degree",
                        "example",
                        "123",
                        null,
                        "service=agent&relativeRef=/credentials",
                        "degree"),
                Arguments.of("did:example:123?service=files&relativeRef=/resume.pdf",
                        "example",
                        "123",
                        null,
                        "service=files&relativeRef=/resume.pdf",
                        null),
                Arguments.of("did:example:1?",
                        "example",
                        "1",
                        null,
                        "",
                        null),
                Arguments.of("did:example:a#",
                        "example",
                        "a",
                        null,
                        null,
                        ""),
                Arguments.of("did:example:a/",
                        "example",
                        "a",
                        "/",
                        null,
                        null),
                Arguments.of("did:example:a/?#",
                        "example",
                        "a",
                        "/",
                        "",
                        ""),

                // Basic valid DID URLs
                Arguments.of(
                        "did:key:z6MkpTHR8VNsBxYAAWHut2Geadd9jSwuBV8xRoAnwWsdvktH",
                        "key",
                        "z6MkpTHR8VNsBxYAAWHut2Geadd9jSwuBV8xRoAnwWsdvktH",
                        null, null, null),

                Arguments.of(
                        "did:key:z6MkhwTbtMsvDSB8z2pF8A4DRNirMRHkRkNvztNFVQVw1W8H",
                        "key",
                        "z6MkhwTbtMsvDSB8z2pF8A4DRNirMRHkRkNvztNFVQVw1W8H",
                        null, null, null),

                // Valid DID URL with path
                Arguments.of(
                        "did:web:example.com/users/alice",
                        "web",
                        "example.com",
                        "/users/alice", null, null),

                // Valid DID URL with path and query
                Arguments.of(
                        "did:web:example.com/users/alice?role=admin&active=true",
                        "web",
                        "example.com",
                        "/users/alice", "role=admin&active=true", null),

                // Valid DID URL with fragment
                Arguments.of(
                        "did:web:example.com/users/alice#section1",
                        "web",
                        "example.com",
                        "/users/alice", null, "section1"),

                // DID URL with encoded characters in path
                Arguments.of(
                        "did:web:example.com/files%2Fmy%2Fdoc%3Fv=1#overview",
                        "web",
                        "example.com",
                        "/files%2Fmy%2Fdoc%3Fv=1", null, "overview"),

                Arguments.of(
                        "did:web:example.com%3A8443/files%2Fmy%2Fdoc%3Fv=1#overview",
                        "web",
                        "example.com%3A8443",
                        "/files%2Fmy%2Fdoc%3Fv=1", null, "overview"),

                // DID URL with an empty path and query
                Arguments.of(
                        "did:web:example.com?#section1",
                        "web",
                        "example.com",
                        "", null, "section1"),

                // DID URL with only query and fragment
                Arguments.of(
                        "did:web:example.com?role=admin&active=true#section1",
                        "web",
                        "example.com",
                        null, "role=admin&active=true", "section1"),

                // DID URL with multiple segments and pct-encoded
                Arguments.of(
                        "did:web:example.com/path%2Fto%2Ffile%3Fv%3D1#fragment1",
                        "web",
                        "example.com",
                        "/path%2Fto%2Ffile%3Fv%3D1", null, "fragment1"),

                // Valid DID URL with port encoded in the method
                Arguments.of(
                        "did:web:example.com%3A8080/path/to/resource",
                        "web",
                        "example.com%3A8080",
                        "/path/to/resource", null, null),

                // Valid DID URL with multiple path segments and query parameters
                Arguments.of(
                        "did:example:abc%2Fdef:1234/segment1/segment2?param1=value1&param2=value2#fragment",
                        "example",
                        "abc%2Fdef:1234",
                        "/segment1/segment2", "param1=value1&param2=value2", "fragment"),

                // Valid DID URL with long segments
                Arguments.of(
                        "did:web:example.com/path/to/long/segment/with/many/parts/inside",
                        "web",
                        "example.com",
                        "/path/to/long/segment/with/many/parts/inside", null, null),

                // DID URL with query-only (no path)
                Arguments.of(
                        "did:web:example.com?query=only#end",
                        "web",
                        "example.com",
                        null, "query=only", "end"),

                Arguments.of(
                        "did:web:example.com#section#end",
                        "web",
                        "example.com",
                        null, null, "section#end"),

                // Valid DID URL with a mix of encoded and unencoded parts
                Arguments.of(
                        "did:web:example.com/users%2Fjohn%3Fstatus=active#profile",
                        "web",
                        "example.com",
                        "/users%2Fjohn%3Fstatus=active", null, "profile"));
    }
}
