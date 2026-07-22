package com.apicatalog.did;

import java.net.URI;
import java.util.Objects;

/**
 * Immutable record representing a standard implementation of a bare
 * {@link DidUrl}.
 * <p>
 * This class validates and preserves the method name and the raw
 * method-specific-id strictly adhering to the W3C DID Core specification ABNF.
 * </p>
 */
public record GenericDidUrl(
        String method,
        String methodSpecificId,
        String path,
        String query,
        String fragment) implements DidUrl {

    /**
     * Constructs a DID URL from validated DID parts.
     *
     * @param method           lowercase method name ({@code [a-z0-9]+})
     * @param methodSpecificId method-specific-id (percent-encoded as needed)
     * @param path             optional path (may be {@code null})
     * @param query            optional query (may be {@code null})
     * @param fragment         optional fragment (may be {@code null})
     */
    public GenericDidUrl {
    }

    /**
     * Creates a DID URL from separated parts. Values are preserved; only minimal
     * normalization of leading {@code /}, {@code ?}, and {@code #} markers is
     * applied.
     *
     * @param methodName       lowercase method name
     * @param methodSpecificId method-specific-id (percent-encoded as needed)
     * @param path             optional path (leading {@code '/'} added if missing;
     *                         {@code null} unchanged)
     * @param query            optional query (leading {@code '?'} removed if
     *                         present; {@code null} unchanged)
     * @param fragment         optional fragment (leading {@code '#'} removed if
     *                         present; {@code null} unchanged)
     * @return a new {@code DidUrl}
     */
    public static GenericDidUrl of(
            final String methodName,
            final String methodSpecificId,
            final String path,
            final String query,
            final String fragment) {
        return new GenericDidUrl(
                methodName,
                methodSpecificId,
                normalizePath(path),
                normalizeQuery(query),
                normalizeFragment(fragment));
    }

    /**
     * Creates a DID URL from a base {@link Did} and optional path, query, and
     * fragment. Percent-encoding is preserved.
     *
     * @param did      base DID (must not be {@code null})
     * @param path     optional path (see {@link #normalizePath(String)})
     * @param query    optional query (see {@link #normalizeQuery(String)})
     * @param fragment optional fragment (see {@link #normalizeFragment(String)})
     * @return a new {@code DidUrl}
     * @throws NullPointerException if {@code did} is {@code null}
     */
    public static GenericDidUrl of(final Did did, final String path, final String query, final String fragment) {
        Objects.requireNonNull(did);
        return new GenericDidUrl(
                did.method(),
                did.methodSpecificId(),
                normalizePath(path),
                normalizeQuery(query),
                normalizeFragment(fragment));
    }

    /**
     * Parses a DID URL from a {@link URI}. Percent-encoding is preserved.
     *
     * @param uri source URI
     * @return a new {@code DidUrl}
     * @throws NullPointerException     if {@code uri} is {@code null}
     * @throws IllegalArgumentException if the URI is not a valid DID URL
     */
    public static GenericDidUrl from(final URI uri) {

        Objects.requireNonNull(uri);

        if (!SCHEME.equalsIgnoreCase(uri.getScheme())) {
            throw new IllegalArgumentException("The URI [" + uri + "] is not a valid DID URL; must start with 'did:'.");
        }

        if (isNotBlank(uri.getAuthority())
                || isNotBlank(uri.getUserInfo())
                || isNotBlank(uri.getHost())) {
            throw new IllegalArgumentException(
                    "The URI [" + uri + "] is not a valid DID URL; authority is not allowed.");
        }

        final String ssp = uri.getRawSchemeSpecificPart();

        if (isNullOrBlank(ssp)) {
            throw new IllegalArgumentException(
                    "The URI [" + uri + "] is not a valid DID URL; expected 'did:method:method-specific-id'.");
        }

        final String[] parts = ssp.split(":", 2);

        if (parts.length != 2) {
            throw new IllegalArgumentException(
                    "The URI [" + uri + "] is not a valid DID URL; expected 'did:method:method-specific-id'.");
        }

        return of(
                parts[0],
                parts[1],
                uri.getRawFragment() // preserve raw pct-encoding
        );
    }

    /**
     * Parses a DID URL from a string. Percent-encoding is preserved.
     *
     * @param uri source string
     * @return a new {@code DidUrl}
     * @throws NullPointerException     if {@code uri} is {@code null}
     * @throws IllegalArgumentException if blank or not a DID URL
     */
    public static GenericDidUrl parse(final String uri) {

        Objects.requireNonNull(uri);

        if (uri.isEmpty()) {
            throw new IllegalArgumentException("DID URL string must not be blank.");
        }

        final String[] parts = uri.split(":", 3);

        if (parts.length != 3) {
            throw new IllegalArgumentException("The URI [" + uri + "] is not a valid DID URL.");
        }

        if (!Did.SCHEME.equals(parts[0])) {
            throw new IllegalArgumentException(
                    "The URI [" + uri + "] is not a valid DID URL; it must start with the 'did:' prefix.");
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

    /**
     * Creates a DID URL from a base {@link Did} with only a fragment component.
     * Percent-encoding is preserved.
     *
     * @param did      base DID (must not be {@code null})
     * @param fragment fragment (may be empty; leading {@code '#'} is removed if
     *                 present)
     * @return a new {@code DidUrl}
     * @throws NullPointerException if {@code fragment} is {@code null}
     */
    public static GenericDidUrl fragment(final Did did, final String fragment) {
        Objects.requireNonNull(fragment);
        return of(did, null, null, fragment);
    }

    /**
     * Returns whether the given {@link URI} is a syntactically valid DID URL.
     * Validation checks scheme, absence of authority/host, and that the method and
     * method-specific-id are valid per {@link Did}.
     *
     * @param uri candidate URI
     * @return {@code true} if valid; {@code false} otherwise
     */
    public static boolean isDidUrl(final URI uri) {
        if (uri == null
                || !SCHEME.equals(uri.getScheme())
                || isNullOrBlank(uri.getRawSchemeSpecificPart())
                || isNotBlank(uri.getRawAuthority())
                || isNotBlank(uri.getRawUserInfo())
                || isNotBlank(uri.getHost())) {
            return false;
        }

        final String[] parts = uri.getRawSchemeSpecificPart().split(":", 2);

        if (parts.length != 2) {
            return false;
        }

        if (!GenericDid.isValidMethodName(parts[0])) {
            return false;
        }

        int msiEndIndex = methodSpecificIdEndIndex(parts[1]);

        if (msiEndIndex == 0) {
            return false;
        }

        return GenericDid.isValidMethodSpecificId(parts[1].substring(0, msiEndIndex));
    }

    /**
     * Returns whether the given string is a syntactically valid DID URL.
     *
     * @param uri candidate string
     * @return {@code true} if valid; {@code false} otherwise
     */
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

    /**
     * Converts this DID URL to a {@link URI} by rendering {@link #toString()}.
     *
     * @return a {@code URI} equal to {@code URI.create(toString())}
     */
    public URI toUri() {
        // Preserve exact raw form produced by toString()
        return URI.create(toString());
    }

    /**
     * Returns the bare {@link Did} portion of this DID URL (method and
     * method-specific-id only).
     *
     * @return a new {@code Did}
     */
    public Did toDid() {
        return new GenericDid(method, methodSpecificId);
    }

    /**
     * Renders
     * {@code did:<method>:<method-specific-id>[<path>][?<query>][#<fragment>]} with
     * percent-encoding preserved.
     *
     * @return the canonical string form
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder()
                .append(SCHEME).append(':')
                .append(method).append(':')
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

    /**
     * Appends the path and query to the provided builder. Path is ensured to start
     * with {@code '/'} when present; query is appended after {@code '?'} even if
     * empty.
     *
     * @param builder target builder
     * @return the same {@code builder}
     */
    private StringBuilder appendPathAndQuery(final StringBuilder builder) {
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

    /**
     * Normalizes a path to start with {@code '/'}. An empty string remains empty
     * (renders as {@code "/"}).
     *
     * @param p input path (may be {@code null})
     * @return normalized path, empty string, or {@code null}
     */
    static final String normalizePath(final String p) {
        if (p == null) {
            return null;
        }
        if (p.isEmpty()) {
            return ""; // will render as "/"
        }
        return p.charAt(0) == '/' ? p : "/" + p;
    }

    /**
     * Removes a leading {@code '?'} from a query if present.
     *
     * @param q input query (may be {@code null})
     * @return query without leading {@code '?'}, or {@code null}
     */
    private static final String normalizeQuery(final String q) {
        if (q == null) {
            return null;
        }
        return (q.startsWith("?")) ? q.substring(1) : q;
    }

    /**
     * Removes a leading {@code '#'} from a fragment if present.
     *
     * @param f input fragment (may be {@code null})
     * @return fragment without leading {@code '#'}, or {@code null}
     */
    private static final String normalizeFragment(final String f) {
        if (f == null) {
            return null;
        }
        return (f.startsWith("#")) ? f.substring(1) : f;
    }

    /**
     * Returns the end index of the method-specific-id within the scheme-specific
     * part. The index is the position before the first {@code '/'} or {@code '?'},
     * or the end if neither is present.
     *
     * @param ssp scheme-specific part (must not be {@code null})
     * @return end index of the method-specific-id
     */
    private static final int methodSpecificIdEndIndex(String ssp) {
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

    /**
     * Internal parser from method name and scheme-specific part, plus optional
     * fragment. Extracts method-specific-id, path, and query. Percent-encoding is
     * preserved.
     *
     * @param methodName lowercase method name
     * @param ssp        scheme-specific part beginning with method-specific-id
     * @param fragment   optional fragment (may be {@code null})
     * @return a new {@code DidUrl}
     * @throws IllegalArgumentException if method-specific-id is empty or the tail
     *                                  is malformed
     */
    private static GenericDidUrl of(final String methodName,
            final String ssp,
            final String fragment) {

        // Determine end of method-specific-id (stop before first '/' or '?' or end)
        int msiEndIndex = methodSpecificIdEndIndex(ssp);

        if (msiEndIndex == 0) {
            throw new IllegalArgumentException("The URI is not a valid DID URL; method-specific-id is empty.");
        }

        final String methodSpecificId = ssp.substring(0, msiEndIndex);

        GenericDid.validate(methodName, methodSpecificId);

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
                throw new IllegalArgumentException(
                        "The URI is not a valid DID URL; malformed path/query [" + rest + "].");
            }
        }

        return new GenericDidUrl(
                methodName,
                methodSpecificId,
                path,
                query,
                fragment);
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
