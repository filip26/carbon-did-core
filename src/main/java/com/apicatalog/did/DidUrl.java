package com.apicatalog.did;

import java.net.URI;
import java.util.Objects;

public class DidUrl extends Did {

    private static final long serialVersionUID = -4371252270461483928L;

    protected final String path; // raw, may be null; when present, starts with '/'
    protected final String query; // raw, may be null; without leading '?'
    protected final String fragment; // raw, may be null; without leading '#'

    protected DidUrl(String methodName, String methodSpecificId, String path, String query, String fragment) {
        super(methodName, methodSpecificId);
        this.path = path;
        this.query = query;
        this.fragment = fragment;
    }

    /* no validation, just normalization */
    public static DidUrl of(
            final String methodName,
            final String methodSpecificId,
            final String path,
            final String query,
            final String fragment) {
        return new DidUrl(
                methodName,
                methodSpecificId,
                normalizePath(path),
                normalizeQuery(query),
                normalizeFragment(fragment));
    }

    public static DidUrl of(final Did did, final String path, final String query, final String fragment) {
        Objects.requireNonNull(did);
        return new DidUrl(
                did.methodName,
                did.methodSpecificId,
                normalizePath(path),
                normalizeQuery(query),
                normalizeFragment(fragment));
    }

    public static DidUrl of(final URI uri) {

        Objects.requireNonNull(uri);

        if (!SCHEME.equalsIgnoreCase(uri.getScheme())) {
            throw new IllegalArgumentException("The URI [" + uri + "] is not a valid DID URL; must start with 'did:'.");
        }

        if (isNotBlank(uri.getAuthority())
                || isNotBlank(uri.getUserInfo())
                || isNotBlank(uri.getHost())) {
            throw new IllegalArgumentException("The URI [" + uri + "] is not a valid DID URL; authority is not allowed.");
        }

        final String ssp = uri.getRawSchemeSpecificPart();

        if (isBlank(ssp)) {
            throw new IllegalArgumentException("The URI [" + uri + "] is not a valid DID URL; expected 'did:method:method-specific-id'.");
        }

        final String[] parts = ssp.split(":", 2);

        if (parts.length != 2) {
            throw new IllegalArgumentException("The URI [" + uri + "] is not a valid DID URL; expected 'did:method:method-specific-id'.");
        }

        return of(
                parts[0],
                parts[1],
                uri.getRawFragment() // preserve raw pct-encoding
        );
    }

    /** pct-encoded */
    public static DidUrl of(final String uri) {

        Objects.requireNonNull(uri);

        if (uri.isEmpty()) {
            throw new IllegalArgumentException("DID URL string must not be blank.");
        }

        final String[] parts = uri.split(":", 3);

        if (parts.length != 3) {
            throw new IllegalArgumentException("The URI [" + uri + "] is not a valid DID URL.");
        }

        if (!Did.SCHEME.equals(parts[0])) {
            throw new IllegalArgumentException("The URI [" + uri + "] is not a valid DID URL; it must start with the 'did:' prefix.");
        }

        String ssp = parts[2];
        String fragment = null;

        int fragmentIndex = parts[2].indexOf('#');

        if (fragmentIndex != -1) {
            ssp = parts[2].substring(0, fragmentIndex);
            fragment = parts[2].substring(fragmentIndex + 1);
        }

        return of(parts[1], ssp, fragment);
    }

    public static DidUrl fragment(final Did did, final String fragment) {
        Objects.requireNonNull(fragment);
        return of(did, null, null, fragment);
    }

    /** @deprecated use {@link DidUrl#of(String)} */
    @Deprecated
    public static DidUrl from(final String uri) {
        return of(uri);
    }

    /** @deprecated use {@link DidUrl#of(Did, String, String, String)} */
    @Deprecated
    public static DidUrl from(Did did, String path, String query, String fragment) {
        return of(did, path, query, fragment);
    }

    /** @deprecated use {@link DidUrl#of(URI)} */
    @Deprecated
    public static DidUrl from(final URI uri) {
        return of(uri);
    }

    public static boolean isDidUrl(final URI uri) {
        if (uri == null
                || !SCHEME.equals(uri.getScheme())
                || isBlank(uri.getRawSchemeSpecificPart())
                || isNotBlank(uri.getRawAuthority())
                || isNotBlank(uri.getRawUserInfo())
                || isNotBlank(uri.getHost())) {
            return false;
        }

        final String[] parts = uri.getRawSchemeSpecificPart().split(":", 2);

        if (parts.length != 2) {
            return false;
        }

        if (!isValidMethodName(parts[0])) {
            return false;
        }

        int msiEndIndex = methodSpecificIdEndIndex(parts[1]);

        if (msiEndIndex == 0) {
            throw new IllegalArgumentException("The URI [" + uri + "] is not a valid DID URL; method-specific-id is empty.");
        }

        return isValidMethodSpecificId(parts[1].substring(0, msiEndIndex));
    }

    public static boolean isDidUrl(final String uri) {
        if (uri == null || uri.isEmpty()) {
            return false;
        }
        try {
            return isDidUrl(URI.create(uri));
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public URI toUri() {
        // Preserve exact raw form produced by toString()
        return URI.create(toString());
    }

    @Override
    public boolean isDidUrl() {
        return true;
    }

    @Override
    public DidUrl asDidUrl() {
        return this;
    }

    public Did toDid() {
        return new Did(super.methodName, super.methodSpecificId);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder()
                .append(SCHEME).append(':')
                .append(methodName).append(':')
                .append(methodSpecificId);

        appendPathAndQuery(builder);

        if (fragment != null) {
            builder.append('#');
            if (!fragment.isEmpty()) {
                builder.append(fragment);
            }
        }

        return builder.toString();
    }

    protected StringBuilder appendPathAndQuery(final StringBuilder builder) {
        if (path != null) {
            if (path.isEmpty() || path.charAt(0) != '/') {
                builder.append('/');
            } else if (!path.isEmpty()) {
                builder.append(path);
            }
        }

        if (query != null) {
            builder.append('?');
            if (!query.isEmpty()) {
                builder.append(query);
            }
        }
        return builder;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(fragment, path, query);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }        
        if (getClass() != obj.getClass()) {
            return false;
        }        
        DidUrl other = (DidUrl) obj;
        return Objects.equals(fragment, other.fragment) && Objects.equals(path, other.path)
                && Objects.equals(query, other.query);
    }

    public String getFragment() {
        return fragment;
    }

    public String getPath() {
        return path;
    }

    public String getQuery() {
        return query;
    }

    static final String normalizePath(final String p) {
        if (p == null) {
            return null;
        }
        if (p.isEmpty()) {
            return ""; // will render as "/"
        }
        return p.charAt(0) == '/' ? p : "/" + p;
    }

    static final String normalizeQuery(final String q) {
        if (q == null) {
            return null;
        }
        return (q.startsWith("?")) ? q.substring(1) : q;
    }

    static final String normalizeFragment(final String f) {
        if (f == null) {
            return null;
        }
        return (f.startsWith("#")) ? f.substring(1) : f;
    }

    static final int methodSpecificIdEndIndex(String ssp) {
        int msiEndIndex = ssp.length();
        final int slashIndex = ssp.indexOf('/');
        final int qmarkIndex = ssp.indexOf('?');

        if (slashIndex != -1 && slashIndex < msiEndIndex) {
            msiEndIndex = slashIndex;
        }
        if (qmarkIndex != -1 && qmarkIndex < msiEndIndex) {
            msiEndIndex = qmarkIndex;
        }

        return msiEndIndex;
    }
    
    static DidUrl of(final String methodName,
            final String ssp,
            final String fragment) {

        // Determine end of method-specific-id (stop before first '/' or '?' or end)
        int msiEndIndex = methodSpecificIdEndIndex(ssp);

        if (msiEndIndex == 0) {
            throw new IllegalArgumentException("The URI is not a valid DID URL; method-specific-id is empty.");
        }

        final String methodSpecificId = ssp.substring(0, msiEndIndex);

        validate(methodName, methodSpecificId);

        final String rest = ssp.substring(msiEndIndex); // starts with '/' or '?' or is empty

        // Extract path and query from tail (both raw, without leading markers)
        String path = null;
        String query = null;

        if (!rest.isEmpty()) {
            if (rest.charAt(0) == '/') {
                // path-abempty: consume path, maybe followed by ?query
                final int queryIndex = rest.indexOf('?');
                if (queryIndex == -1) {
                    path = rest; // includes leading '/'
                } else {
                    path = rest.substring(0, queryIndex); // includes leading '/'
                    query = rest.substring(queryIndex + 1); // without '?'
                }
            } else if (rest.charAt(0) == '?') {
                query = rest.substring(1); // empty query allowed
            } else {
                // Should not happen (we only cut at '/' or '?')
                throw new IllegalArgumentException("The URI is not a valid DID URL; malformed path/query [" + rest + "].");
            }
        }

        return new DidUrl(
                methodName,
                methodSpecificId,
                path,
                query,
                fragment);
    }
}
