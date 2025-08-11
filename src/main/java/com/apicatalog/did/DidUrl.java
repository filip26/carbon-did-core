package com.apicatalog.did;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public class DidUrl extends Did {

    private static final long serialVersionUID = -4371252270461483928L;
    
    protected final String path;
    protected final String query;
    protected final String fragment;

    protected DidUrl(Did did, String path, String query, String fragment) {
        super(did.methodName, did.specificId);
        this.path = path;
        this.query = query;
        this.fragment = fragment;
    }

    /**
     * @deprecated use {@link DidUrl#of(Did, String, String, String)}
     * @param did
     * @param path
     * @param query
     * @param fragment
     * @return
     */
    @Deprecated
    public static DidUrl from(Did did, String path, String query, String fragment) {
        return of(did, path, query, fragment);
    }

    public static DidUrl of(Did did, String path, String query, String fragment) {
        return new DidUrl(did, path, query, fragment);
    }

    /**
     * @deprecated use {@link DidUrl#of(URI)}
     * @param uri
     * @return
     */
    @Deprecated
    public static DidUrl from(final URI uri) {
        return of(uri);
    }

    public static DidUrl of(final URI uri) {

        Objects.requireNonNull(uri);

        if (!Did.SCHEME.equalsIgnoreCase(uri.getScheme())) {
            throw new IllegalArgumentException("The URI [" + uri + "] is not valid DID URL, must start with 'did:' prefix.");
        }

        final String[] didParts = uri.getRawSchemeSpecificPart().split(":", 2);

        if (didParts.length != 2) {
            throw new IllegalArgumentException("The URI [" + uri + "] is not valid DID, must be in form 'did:method:method-specific-id'.");
        }

        return of(uri, didParts[0], didParts[1], uri.getFragment());
    }

    /**
     * @deprecated use {@link DidUrl#of(String)}
     * @param uri
     * @return
     */
    @Deprecated
    public static DidUrl from(final String uri) {
        return of(uri);
    }

    public static DidUrl of(final String uri) {

        Objects.requireNonNull(uri);

        if (uri.length() == 0) {
            throw new IllegalArgumentException("The DID must not be null or blank string.");
        }

        final String[] parts = uri.split(":", 3);

        if (parts.length != 3) {
            throw new IllegalArgumentException("The URI [" + uri + "] is not valid DID, must be in form 'did:method:method-specific-id'.");
        }

        if (!Did.SCHEME.equalsIgnoreCase(parts[0])) {
            throw new IllegalArgumentException("The URI [" + uri + "] is not valid DID, must start with 'did:' prefix.");
        }

        String rest = parts[2];
        String fragment = null;

        int fragmentIndex = rest.indexOf('#');
        if (fragmentIndex != -1) {
            fragment = rest.substring(fragmentIndex + 1);
            rest = rest.substring(0, fragmentIndex);
        }

        return of(uri, parts[1], rest, fragment);
    }

    protected static DidUrl of(Object uri, final String method, final String rest, final String fragment) {
        String specificId = rest;

        String path = null;
        String query = null;

        int urlPartIndex = specificId.indexOf('?');
        if (urlPartIndex != -1) {
            query = specificId.substring(urlPartIndex + 1);
            specificId = specificId.substring(0, urlPartIndex);
        }

        urlPartIndex = specificId.indexOf('/');
        if (urlPartIndex != -1) {
            path = specificId.substring(urlPartIndex);
            specificId = specificId.substring(0, urlPartIndex);
        }

        Did did = of(method, specificId);

        return new DidUrl(did, path, query, fragment);
    }

    public static boolean isDidUrl(final URI uri) {
        if (!Did.SCHEME.equalsIgnoreCase(uri.getScheme())
                || isBlank(uri.getSchemeSpecificPart())
                || isNotBlank(uri.getAuthority())
                || isNotBlank(uri.getUserInfo())
                || isNotBlank(uri.getHost())) {
            return false;
        }

        final String[] parts = uri.getSchemeSpecificPart().split(":", 2);

        return parts.length == 2
                && parts[0].length() > 0
                && parts[1].length() > 0
                && parts[0].codePoints().allMatch(METHOD_CHAR);
    }

    public static boolean isDidUrl(final String uri) {
        if (uri == null) {
            return false;
        }

        final String[] parts = uri.split(":", 3);

        return parts.length == 3
                && Did.SCHEME.equalsIgnoreCase(parts[0])
                && parts[1].length() > 0
                && parts[2].length() > 0
                && parts[1].codePoints().allMatch(METHOD_CHAR);
    }

    @Override
    public URI toUri() {
        try {
            return new URI(SCHEME,
                    appendPathQuery(new StringBuilder()
                            .append(methodName)
                            .append(':')
                            .append(specificId)).toString(),
                    fragment);
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean isDidUrl() {
        return true;
    }

    @Override
    public DidUrl asDidUrl() {
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(super.toString());

        appendPathQuery(builder);

        if (fragment != null) {
            if (fragment.length() == 0 || fragment.charAt(0) != '#') {
                builder.append('#');
            }
            if (fragment.length() > 0) {
                builder.append(fragment);
            }
        }

        return builder.toString();
    }

    protected StringBuilder appendPathQuery(final StringBuilder builder) {
        if (path != null) {
            if (path.length() == 0 || path.charAt(0) != '/') {
                builder.append('/');
            }
            if (path.length() > 0) {
                builder.append(path);
            }
        }

        if (query != null) {
            if (query.length() == 0 || query.charAt(0) != '?') {
                builder.append('?');
            }
            if (query.length() > 0) {
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
}
