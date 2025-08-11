package com.apicatalog.did;

import java.io.Serializable;
import java.net.URI;
import java.util.Objects;
import java.util.function.IntPredicate;

public class Did implements Serializable {

    private static final long serialVersionUID = -2933853082203788425L;

    public static final String SCHEME = "did";

    /*
     * method-char = %x61-7A / DIGIT
     */
    static final IntPredicate METHOD_CHAR = ch -> (0x61 <= ch && ch <= 0x7A)
            || ('0' <= ch && ch <= '9');

    /*
     * idchar = ALPHA / DIGIT / "." / "-" / "_" / pct-encoded
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

    protected final String methodName;
    protected final String specificId;

    protected Did(final String methodName, final String methodSpecificId) {
        this.methodName = methodName;
        this.specificId = methodSpecificId;
    }

    public static boolean isDid(final URI uri) {

        Objects.requireNonNull(uri);

        if (!Did.SCHEME.equalsIgnoreCase(uri.getScheme())
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

    public static boolean isDid(final String uri) {

        Objects.requireNonNull(uri);

        // "did:" method-name ":" method-specific-id
        final String[] parts = uri.split(":", 3);

        return parts.length == 3
                && Did.SCHEME.equalsIgnoreCase(parts[0])
                && isValidMethodName(parts[1])
                && isValidMethodSpecificId(parts[2]);
    }

    /**
     * Deprecated, use {@link Did#of(URI)}.
     * 
     * @param uri
     * @return
     * 
     */
    @Deprecated
    public static Did from(final URI uri) {
        return of(uri);
    }

    /**
     * Creates a new DID instance from the given {@link URI}.
     *
     * @param uri The source URI to be transformed into DID
     * @return The new DID
     *
     * @throws NullPointerException     If {@code uri} is {@code null}
     *
     * @throws IllegalArgumentException If the given {@code uri} is not valid DID
     */
    public static Did of(final URI uri) {

        Objects.requireNonNull(uri);
        if (!Did.SCHEME.equalsIgnoreCase(uri.getScheme())) {
            throw new IllegalArgumentException("The URI [" + uri + "] is not valid DID, must start with 'did:' prefix.");
        }

        if (isBlank(uri.getRawSchemeSpecificPart())
                || isNotBlank(uri.getAuthority())
                || isNotBlank(uri.getUserInfo())
                || isNotBlank(uri.getHost())
                || isNotBlank(uri.getPath())
                || isNotBlank(uri.getQuery())
                || uri.getFragment() != null) {
            throw new IllegalArgumentException("The URI [" + uri + "] is not valid DID, must be in form 'did:method:method-specific-id'.");
        }

        final String[] parts = uri.getRawSchemeSpecificPart().split(":", 2);

        if (parts.length != 2) {
            throw new IllegalArgumentException("The URI [" + uri + "] is not valid DID, must be in form 'did:method:method-specific-id'.");
        }

        return of(parts[0], parts[1]);
    }

    /**
     * Deprecated, use {@link Did#of(String)}
     * 
     * @param uri
     * @return
     */
    @Deprecated
    public static Did from(final String uri) {
        return of(uri);
    }

    /**
     * Creates a new DID instance from the given URI.
     *
     * @param uri The source URI to be transformed into DID
     * @return The new DID
     *
     * @throws NullPointerException     If {@code uri} is {@code null}
     *
     * @throws IllegalArgumentException If the given {@code uri} is not valid DID
     */
    public static Did of(final String uri) {
        if (uri == null || uri.length() == 0) {
            throw new IllegalArgumentException("The DID must not be null or blank string.");
        }

        final String[] parts = uri.split(":", 3);

        if (parts.length != 3) {
            throw new IllegalArgumentException("The URI [" + uri + "] is not valid DID, must be in form 'did:method:method-specific-id'.");
        }

        if (!Did.SCHEME.equalsIgnoreCase(parts[0])) {
            throw new IllegalArgumentException("The URI [" + uri + "] is not valid DID, must start with 'did:' prefix.");
        }
        return of(parts[1], parts[2]);
    }

    /**
     * 
     * @param methodName
     * @param methodSpecificId must be pct-encoded
     * @return
     */
    public static Did of(final String methodName, final String methodSpecificId) {

        // check method name
        if (methodName == null
                || !isValidMethodName(methodName)) {
            throw new IllegalArgumentException("The URI is not valid DID, method [" + methodName + "] syntax is blank or invalid.");
        }

        // check method specific id
        if (methodSpecificId == null
                || !isValidMethodSpecificId(methodSpecificId)) {
            throw new IllegalArgumentException("The URI is not valid DID, method specific id [" + methodSpecificId + "] is blank.");
        }

        return new Did(methodName, methodSpecificId);
    }

    public String getMethod() {
        return methodName;
    }

    /**
     * Raw pct encoded representations.
     */
    public String getMethodSpecificId() {
        return specificId;
    }

    public boolean isDidUrl() {
        return false;
    }

    public DidUrl asDidUrl() {
        throw new ClassCastException();
    }

    public URI toUri() {
        return URI.create(toString());
    }

    /**
     * Raw pct encoded representations.
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

    static final boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }

    static final boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    static boolean isValidMethodName(final String methodName) {
        return (methodName.length() > 0
                && methodName.codePoints().allMatch(METHOD_CHAR));
    }

    /*
     * method-specific-id = *( *idchar ":" ) 1*idchar - zero or more segments that
     * may be empty before ":" (i.e., "::" allowed) - final segment must contain at
     * least one idchar - idchar may be ALPHA / DIGIT / "." / "-" / "_" or
     * pct-encoded = "%" HEXDIG HEXDIG
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
