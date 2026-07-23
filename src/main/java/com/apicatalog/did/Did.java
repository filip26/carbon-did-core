package com.apicatalog.did;

import java.net.URI;
import java.util.Objects;
import java.util.function.IntPredicate;

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
 * 
 * @param method           the DID method name (lowercase ASCII).
 * @param methodSpecificId the raw method-specific-id as provided (may contain
 *                         pct-encoded octets).
 */
public record Did(
        String method,
        String methodSpecificId) {

    /** DID URI scheme literal: {@code "did"}. */
    public static final String SCHEME = "did";

    /*
     * method-char = %x61-7A / DIGIT
     */
    static final IntPredicate METHOD_CHAR = ch -> (0x61 <= ch && ch <= 0x7A)
            || ('0' <= ch && ch <= '9');

    /*
     * idchar = ALPHA / DIGIT / "." / "-" / "_" / pct-encoded
     *
     * This predicate intentionally covers only the single-codepoint, unescaped part
     * (ALPHA / DIGIT / "." / "-" / "_"). pct-encoded is validated in the scanner.
     */
    static final IntPredicate ID_CHAR = ch -> ch >= 'a' && ch <= 'z'
            || 'A' <= ch && ch <= 'Z'
            || '0' <= ch && ch <= '9'
            || ch == '.'
            || ch == '-'
            || ch == '_';

    /*
     * HEXDIG = 0-9 / A-F / a-f
     */
    static final IntPredicate HEXDIG = ch -> ('0' <= ch && ch <= '9') ||
            ('A' <= ch && ch <= 'F') ||
            ('a' <= ch && ch <= 'f');

    /**
     * Creates a DID with validated components.
     *
     * @param method           validated method name
     * @param methodSpecificId validated, raw pct-encoded method-specific-id
     * @throws NullPointerException     if either parameter is null
     * @throws IllegalArgumentException if components are syntactically invalid
     */
    public Did {
        Objects.requireNonNull(method, "Method must not be null.");
        Objects.requireNonNull(methodSpecificId, "Method-specific-id must not be null.");
    }

    /**
     * Tests whether the given URI is a syntactically valid bare DID.
     *
     * @param uri candidate URI
     * @return true if the URI is a valid DID, otherwise false
     * @throws NullPointerException if uri is null
     */
    public static boolean isDid(final URI uri) {

        Objects.requireNonNull(uri);

        if (!Did.SCHEME.equals(uri.getScheme())
                || isNullOrBlank(uri.getRawSchemeSpecificPart())
                || isNotBlank(uri.getAuthority())
                || isNotBlank(uri.getUserInfo())
                || isNotBlank(uri.getHost())
                || isNotBlank(uri.getRawPath())
                || isNotBlank(uri.getRawQuery())
                || uri.getRawFragment() != null) {
            return false;
        }

        final String[] parts = uri.getRawSchemeSpecificPart().split(":", 2);

        return parts.length == 2
                && isValidMethodName(parts[0])
                && isValidMethodSpecificId(parts[1]);
    }

    /**
     * Tests whether the given string is a syntactically valid bare DID.
     *
     * @param uri candidate string
     * @return true if valid, otherwise false
     * @throws NullPointerException if uri is null
     */
    public static boolean isDid(final String uri) {

        Objects.requireNonNull(uri);

        // "did:" method-name ":" method-specific-id
        final String[] parts = uri.split(":", 3);

        return parts.length == 3
                && Did.SCHEME.equals(parts[0])
                && isValidMethodName(parts[1])
                && isValidMethodSpecificId(parts[2]);
    }

    /**
     * Returns a {@code Did} from the given {@link URI}.
     * <p>
     * The URI must be a bare DID: {@code did:method:method-specific-id}. The
     * method-specific-id is treated as raw pct-encoded data and is <em>not</em>
     * decoded.
     * </p>
     *
     * @param uri source URI
     * @return a new {@code Did}
     * @throws NullPointerException     if {@code uri} is {@code null}
     * @throws IllegalArgumentException if the URI is not a syntactically valid DID
     */
    public static Did from(final URI uri) {

        Objects.requireNonNull(uri);

        if (!Did.SCHEME.equals(uri.getScheme())) {
            throw new IllegalArgumentException(
                    "The URI [" + uri + "] is not a valid DID; it must start with the 'did:' prefix.");
        }

        if (isNullOrBlank(uri.getRawSchemeSpecificPart())
                || isNotBlank(uri.getAuthority())
                || isNotBlank(uri.getUserInfo())
                || isNotBlank(uri.getHost())
                || isNotBlank(uri.getRawPath())
                || isNotBlank(uri.getRawQuery())
                || uri.getRawFragment() != null) {
            throw new IllegalArgumentException("The URI [" + uri
                    + "] is not a valid DID; it must be in the form 'did:method:method-specific-id'.");
        }

        final String[] parts = uri.getRawSchemeSpecificPart().split(":", 2);

        if (parts.length != 2) {
            throw new IllegalArgumentException(
                    "The URI [" + uri + "] is not valid DID, must be in form 'did:method:method-specific-id'.");
        }

        return of(parts[0], parts[1]);
    }

    /**
     * Parses and returns a {@code Did} from the given string.
     * <p>
     * The string must be a bare DID: {@code did:method:method-specific-id}. The
     * method-specific-id is treated as raw pct-encoded data and is <em>not</em>
     * decoded.
     * </p>
     *
     * @param uri source string
     * @return a new {@code Did}
     * @throws IllegalArgumentException if {@code uri} is blank, or not a valid DID
     * @throws NullPointerException     if {@code uri} is {@code null}
     */
    public static Did parse(final String uri) {

        Objects.requireNonNull(uri);

        if (uri.length() == 0) {
            throw new IllegalArgumentException("DID string must not be blank.");
        }

        final String[] parts = uri.split(":", 3);

        if (parts.length != 3) {
            throw new IllegalArgumentException("The URI [" + uri
                    + "] is not a valid DID; it must be in the form 'did:method:method-specific-id'.");
        }

        if (!Did.SCHEME.equals(parts[0])) {
            throw new IllegalArgumentException(
                    "The URI [" + uri + "] is not a valid DID; it must start with the 'did:' prefix.");
        }

        return of(parts[1], parts[2]);
    }

    /**
     * Creates a {@code Did} from already-separated components.
     *
     * @param methodName       the method name (ASCII {@code [a-z0-9]+})
     * @param methodSpecificId the raw method-specific-id (must follow the ABNF and
     *                         use pct-encoding where required); no decoding is
     *                         performed
     * @return a new {@code Did}
     * @throws NullPointerException if {@code methodName} or
     *                              {@code methodSpecificId} is {@code null}
     */
    public static Did of(final String methodName, final String methodSpecificId) {

        Objects.requireNonNull(methodName);
        Objects.requireNonNull(methodSpecificId);

        validate(methodName, methodSpecificId);

        return new Did(methodName, methodSpecificId);
    }

    public static void validate(final String methodName, final String methodSpecificId) {
        // check method name
        if (!isValidMethodName(methodName)) {
            throw new IllegalArgumentException(
                    "Not a valid DID: method name [" + methodName + "] is blank or invalid.");
        }

        // check method specific id
        if (!isValidMethodSpecificId(methodSpecificId)) {
            throw new IllegalArgumentException(
                    "Not a valid DID: method-specific-id [" + methodSpecificId + "] is blank or invalid.");
        }
    }

    /**
     * Validates the method name: {@code 1*(%x61-7A / DIGIT)} i.e.
     * {@code [a-z0-9]+}.
     *
     * @param methodName candidate method name
     * @return {@code true} if valid
     */
    public static boolean isValidMethodName(final String methodName) {
        return (methodName.length() > 0
                && methodName.codePoints().allMatch(METHOD_CHAR));
    }

    /**
     * Validates the method-specific-id using a single-pass scanner that enforces:
     * <ul>
     * <li>{@code method-specific-id = *( *idchar ":" ) 1*idchar}</li>
     * <li>{@code idchar = ALPHA / DIGIT / "." / "-" / "_" / pct-encoded}</li>
     * <li>{@code pct-encoded = "%" HEXDIG HEXDIG}</li>
     * </ul>
     * Empty segments before {@code ':'} are allowed; the final segment must contain
     * at least one {@code idchar}.
     *
     * @param methodSpecificId candidate method-specific-id (raw pct-encoded)
     * @return {@code true} if valid
     */
    public static boolean isValidMethodSpecificId(final String methodSpecificId) {
        if (methodSpecificId.isEmpty()) {
            return false;
        }

        boolean lastSegHasIdChar = false;

        for (int i = 0; i < methodSpecificId.length();) {
            final char c = methodSpecificId.charAt(i);

            if (c == ':') {
                // Empty segments are allowed; reset for next segment.
                lastSegHasIdChar = false;
                i++;
                continue;
            }

            if (c == '%') {
                // pct-encoded = "%" HEXDIG HEXDIG
                if ((i + 2 >= methodSpecificId.length())
                        || !HEXDIG.test(methodSpecificId.charAt(i + 1))
                        || !HEXDIG.test(methodSpecificId.charAt(i + 2))) {
                    return false;
                }
                i += 3;
                lastSegHasIdChar = true;
                continue;
            }

            final int cp = methodSpecificId.codePointAt(i);
            if (!ID_CHAR.test(cp)) {
                return false;
            }

            i += Character.charCount(cp);

            lastSegHasIdChar = true;
        }

        // final segment must have at least one idchar
        return lastSegHasIdChar;
    }

    /**
     * Converts this DID to a {@link URI} by rendering {@link #toString()}.
     *
     * @return a {@code URI} equal to {@code URI.create(toString())}
     */
    public URI toUri() {
        return URI.create(toString());
    }

    /**
     * Renders the bare DID in its wire form:
     * {@code did:<method>:<method-specific-id>}.
     * <p>
     * The {@code method-specific-id} is returned exactly as stored (pct-encoded as
     * needed).
     * </p>
     */
    @Override
    public String toString() {
        return SCHEME + ":" + method + ":" + methodSpecificId;
    }

    /**
     * @return {@code true} if the value is non-null and not blank after
     *         {@code trim()}
     */
    private static final boolean isNotBlank(String value) {
        return value != null && !value.isBlank();
    }

    /**
     * @return {@code true} if the value is {@code null} or blank after
     *         {@code trim()}
     */
    private static final boolean isNullOrBlank(String value) {
        return value == null || value.isBlank();
    }
}
