package com.apicatalog.did.datatype;

/**
 * A value encoded using the <a href=
 * "https://datatracker.ietf.org/doc/html/draft-multiformats-multibase">Multibase</a>
 * format.
 * <p>
 * Provides access to the encoding base and the decoded (binary) value.
 * </p>
 */
public interface MultibaseEncoded {

    /**
     * Returns the name of the encoding base (e.g. {@code base58btc},
     * {@code base64url}).
     *
     * @return multibase name
     */
    String baseName();

    /**
     * Returns the decoded binary value.
     *
     * @return raw byte array
     */
    byte[] debased();
}
