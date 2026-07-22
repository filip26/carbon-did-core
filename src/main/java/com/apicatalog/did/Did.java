package com.apicatalog.did;

import java.io.Serializable;

/**
 * Immutable value object representing a
 * <a href="https://www.w3.org/TR/did-1.0/">Decentralized Identifier (DID)</a>.
 * <p>
 * This type models a <em>bare DID</em> (not a DID URL). It holds and preserves
 * the {@code method} and the {@code method-specific-id} exactly as supplied,
 * including any percent-encoding (no decoding or normalization is performed).
 * </p>
 * <p>
 * The W3C DID Core specification mandates that the exact percent-encoded string
 * is passed to DID resolvers. Modifying or decoding the method-specific-id
 * before resolution can break cryptographic proofs, content hashes, or
 * method-specific routing that rely on the exact original string.
 * </p>
 *
 * <h2>Syntax</h2>
 * 
 * {@snippet :
 * did                = "did:" method-name ":" method-specific-id
 * method-name        = 1*method-char
 * method-char        = %x61-7A / DIGIT          ; "a"-"z" or "0"-"9"
 * method-specific-id = *( *idchar ":" ) 1*idchar
 * idchar             = ALPHA / DIGIT / "." / "-" / "_" / pct-encoded
 * pct-encoded        = "%" HEXDIG HEXDIG
 * }
 *
 * <p>
 * <strong>Notes</strong>
 * </p>
 * <ul>
 * <li>Authority, path, query, and fragment are not allowed for a bare DID
 * (e.g., {@code did:example:123#frag} and {@code did:example:123/path} are
 * invalid).</li>
 * <li>The final segment of the {@code method-specific-id} MUST contain at least
 * one {@code idchar}; earlier segments may be empty (i.e., {@code "::"} is
 * allowed) per the ABNF above.</li>
 * <li>Percent-encoded octets ({@code %HH}) are validated for shape only and are
 * not decoded.</li>
 * </ul>
 */
public interface Did extends Serializable {

    /** DID URI scheme literal: {@code "did"}. */
    String SCHEME = "did";

    /**
     * Returns the DID method name (lowercase ASCII).
     *
     * @return method name
     */
    String method();

    /**
     * Returns the raw method-specific-id as provided (may contain pct-encoded
     * octets).
     *
     * @return raw, pct-encoded method-specific-id
     */
    String methodSpecificId();

}
