package com.apicatalog.did;

/**
 * Immutable value object for a <a href="https://www.w3.org/TR/did-1.0/">DID
 * URL</a>.
 * <p>
 * Extends {@link Did} with optional {@code path}, {@code query}, and
 * {@code fragment} components. Values are preserved exactly as supplied,
 * including any percent-encoding (no decoding).
 * </p>
 * <p>
 * The W3C DID Core specification mandates that the exact percent-encoded string
 * is passed to DID resolvers. Modifying or decoding the method-specific-id
 * before resolution can break cryptographic proofs, content hashes, or
 * method-specific routing that rely on the exact original string.
 * </p>
 *
 * <h2>Form</h2>
 * 
 * {@code
 * did-url            = did path-abempty [ "?" query ] [ "#" fragment ]
 * }
 */
public interface DidUrl extends Did {

    /**
     * Returns the fragment (percent-encoded if applicable), without a leading
     * {@code '#'}.
     *
     * @return fragment or {@code null} if absent
     */
    String fragment();

    /**
     * Returns the path (percent-encoded if applicable). When present it starts with
     * {@code '/'}.
     *
     * @return path or {@code null} if absent
     */
    String path();

    /**
     * Returns the query (percent-encoded if applicable), without a leading
     * {@code '?'}.
     *
     * @return query or {@code null} if absent
     */
    String query();
}
