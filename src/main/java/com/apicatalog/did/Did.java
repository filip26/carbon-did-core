package com.apicatalog.did;

import java.io.Serializable;
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
 *
 * <h2>Syntax</h2>
 * 
 * <pre>{@code
 * did                = "did" ":" method-name ":" method-specific-id
 * method-name        = 1*method-char
 * method-char        = %x61-7A / DIGIT          ; "a"–"z" or "0"–"9"
 * method-specific-id = *( *idchar ":" ) 1*idchar
 * idchar             = ALPHA / DIGIT / "." / "-" / "_" / pct-encoded
 * pct-encoded        = "%" HEXDIG HEXDIG
 * }</pre>
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
public class Did implements Serializable {

    private static final long serialVersionUID = -2933853082203788425L;

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

    /** Lowercase method name. */
    protected final String methodName;
    /** Raw (pct-encoded) method-specific-id, preserved as provided. */
    protected final String specificId;

    /**
     * Creates a DID with already-validated components.
     *
     * @param methodName       validated method name
     * @param methodSpecificId validated, raw pct-encoded method-specific-id
     */
    protected Did(final String methodName, final String methodSpecificId) {
        this.methodName = methodName;
        this.specificId = methodSpecificId;
    }

    /**
     * Tests whether the given {@link URI} is a syntactically valid <em>bare
     * DID</em>.
     * <p>
     * Validation enforces the ABNF in the class Javadoc and additionally requires:
     * </p>
     * <ul>
     * <li>scheme is {@code did} (case-sensitive)</li>
     * <li>no authority, user-info, host, path, query, or fragment</li>
     * </ul>
     *
     * @param uri candidate URI
     * @return {@code true} if the URI is a valid DID, otherwise {@code false}
     * @throws NullPointerException if {@code uri} is {@code null}
     */
    public static boolean isDid(final URI uri) {

        Objects.requireNonNull(uri);

        if (!Did.SCHEME.equals(uri.getScheme())
                || isBlank(uri.getRawSchemeSpecificPart())
                || isNotBlank(uri.getAuthority())
                || isNotBlank(uri.getUserInfo())
                || isNotBlank(uri.getHost())
                || isNotBlank(uri.getPath())
                || isNotBlank(uri.getQuery())
                || uri.getFragment() != null) {
            return false;
        }

        final String[] parts = uri.getRawSchemeSpecificPart().split(":", 2);

        return parts.length == 2
                && isValidMethodName(parts[0])
                && isValidMethodSpecificId(parts[1]);
    }

    /**
     * Tests whether the given string is a syntactically valid <em>bare DID</em>.
     *
     * @param uri candidate string (e.g., {@code "did:example:123"})
     * @return {@code true} if valid, otherwise {@code false}
     * @throws NullPointerException if {@code uri} is {@code null}
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
     * @deprecated Use {@link Did#of(URI)}.
     */
    @Deprecated
    public static Did from(final URI uri) {
        return of(uri);
    }

    /**
     * Parses and returns a {@code Did} from the given {@link URI}.
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
    public static Did of(final URI uri) {

        Objects.requireNonNull(uri);

        if (!Did.SCHEME.equals(uri.getScheme())) {
            throw new IllegalArgumentException("The URI [" + uri + "] is not a valid DID; it must start with the 'did:' prefix.");
        }

        if (isBlank(uri.getRawSchemeSpecificPart())
                || isNotBlank(uri.getAuthority())
                || isNotBlank(uri.getUserInfo())
                || isNotBlank(uri.getHost())
                || isNotBlank(uri.getPath())
                || isNotBlank(uri.getQuery())
                || uri.getFragment() != null) {
            throw new IllegalArgumentException("The URI [" + uri + "] is not a valid DID; it must be in the form 'did:method:method-specific-id'.");
        }

        final String[] parts = uri.getRawSchemeSpecificPart().split(":", 2);

        if (parts.length != 2) {
            throw new IllegalArgumentException("The URI [" + uri + "] is not valid DID, must be in form 'did:method:method-specific-id'.");
        }

        return of(parts[0], parts[1]);
    }

    /**
     * @deprecated Use {@link Did#of(String)}.
     */
    @Deprecated
    public static Did from(final String uri) {
        return of(uri);
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
    public static Did of(final String uri) {

        Objects.requireNonNull(uri);

        if (uri.length() == 0) {
            throw new IllegalArgumentException("DID string must not be blank.");
        }

        final String[] parts = uri.split(":", 3);

        if (parts.length != 3) {
            throw new IllegalArgumentException("The URI [" + uri + "] is not a valid DID; it must be in the form 'did:method:method-specific-id'.");
        }

        if (!Did.SCHEME.equals(parts[0])) {
            throw new IllegalArgumentException("The URI [" + uri + "] is not a valid DID; it must start with the 'did:' prefix.");
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
     * @throws IllegalArgumentException if either component is invalid
     * @throws NullPointerException     if {@code methodName} or
     *                                  {@code methodSpecificId} is {@code null}
     */
    public static Did of(final String methodName, final String methodSpecificId) {

        Objects.requireNonNull(methodName);
        Objects.requireNonNull(methodSpecificId);

        // check method name
        if (!isValidMethodName(methodName)) {
            throw new IllegalArgumentException("Not a valid DID: method name [" + methodName + "] is blank or invalid.");
        }

        // check method specific id
        if (!isValidMethodSpecificId(methodSpecificId)) {
            throw new IllegalArgumentException("Not a valid DID: method-specific-id [" + methodSpecificId + "] is blank or invalid.");
        }

        return new Did(methodName, methodSpecificId);
    }

    /**
     * Returns the DID method name (lowercase ASCII).
     *
     * @return method name
     */
    public String getMethod() {
        return methodName;
    }

    /**
     * Returns the raw method-specific-id as provided (may contain pct-encoded
     * octets).
     *
     * @return raw, pct-encoded method-specific-id
     */
    public String getMethodSpecificId() {
        return specificId;
    }

    /**
     * Indicates whether this instance represents a DID URL (always {@code false}
     * here).
     * <p>
     * This class models <em>bare</em> DIDs only. Use {@link #asDidUrl()} in
     * implementations that support DID URLs.
     * </p>
     *
     * @return {@code false}
     */
    public boolean isDidUrl() {
        return false;
    }

    /**
     * Casts this instance to a DID URL.
     *
     * @return never returns; this class does not represent DID URLs
     * @throws ClassCastException always
     */
    public DidUrl asDidUrl() {
        throw new ClassCastException();
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
        return new StringBuilder()
                .append(SCHEME)
                .append(':')
                .append(methodName)
                .append(':')
                .append(specificId).toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(methodName, specificId);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Did other = (Did) obj;
        return Objects.equals(methodName, other.methodName) && Objects.equals(specificId, other.specificId);

    }

    /**
     * @return {@code true} if the value is non-null and not blank after
     *         {@code trim()}
     */
    static final boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * @return {@code true} if the value is {@code null} or blank after
     *         {@code trim()}
     */
    static final boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * Validates the method name: {@code 1*(%x61-7A / DIGIT)} i.e.
     * {@code [a-z0-9]+}.
     *
     * @param methodName candidate method name
     * @return {@code true} if valid
     */
    static boolean isValidMethodName(final String methodName) {
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
    static boolean isValidMethodSpecificId(final String methodSpecificId) {
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
}
