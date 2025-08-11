package com.apicatalog.did;

import java.io.Serializable;
import java.net.URI;
import java.util.Objects;
import java.util.function.IntPredicate;

public class Did implements Serializable {

    private static final long serialVersionUID = 8800667526627004412L;

    /*
     * method-char = %x61-7A / DIGIT
     */
    static final IntPredicate METHOD_CHAR = ch -> (0x61 <= ch && ch <= 0x7A)
            || ('0' <= ch && ch <= '9');

    /*
     * idchar = ALPHA / DIGIT / "." / "-" / "_" / pct-encoded
     */
    static final IntPredicate ID_CHAR = ch -> Character.isLetter(ch)
            || Character.isDigit(ch)
            || ch == '.'
            || ch == '-'
            || ch == '_';

    public static final String SCHEME = "did";

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
                && parts[0].length() > 0
                && parts[1].length() > 0
                && parts[0].codePoints().allMatch(METHOD_CHAR)
                // FIXME does not validate pct-encoded correctly
                && parts[1].codePoints().allMatch(ID_CHAR.or(ch -> ch == ':' || ch == '%'));

    }

    public static boolean isDid(final String uri) {

        Objects.requireNonNull(uri);

        // "did:" method-name ":" method-specific-id
        final String[] parts = uri.split(":", 3);

        return parts.length == 3
                && Did.SCHEME.equalsIgnoreCase(parts[0])
                && parts[1].length() > 0
                && parts[2].length() > 0
                && parts[1].codePoints().allMatch(METHOD_CHAR)
                // FIXME does not validate pct-encoded correctly
                && parts[2].codePoints().allMatch(ID_CHAR.or(ch -> ch == ':' || ch == '%'));
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

        return of(uri, parts[0], parts[1]);
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

        return of(uri, parts[1], parts[2]);
    }

    protected static Did of(final Object uri, final String methodName, final String methodSpecificId) {

        // check method name
        if (methodName == null
                || methodName.length() == 0
                || !methodName.codePoints().allMatch(METHOD_CHAR)) {
            throw new IllegalArgumentException("The URI [" + uri + "] is not valid DID, method [" + methodName + "] syntax is blank or invalid.");
        }

        // check method specific id
        if (methodSpecificId == null
                || methodSpecificId.length() == 0
                // FIXME does not validate pct-encoded correctly
                || !methodSpecificId.codePoints().allMatch(ID_CHAR.or(ch -> ch == ':' || ch == '%'))) {
            throw new IllegalArgumentException("The URI [" + uri + "] is not valid DID, method specific id [" + methodSpecificId + "] is blank.");
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
}
