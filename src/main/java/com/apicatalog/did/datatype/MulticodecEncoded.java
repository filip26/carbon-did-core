package com.apicatalog.did.datatype;

/**
 * A value encoded using the
 * <a href="https://github.com/multiformats/multicodec">Multicodec</a> format.
 * <p>
 * Provides access to the codec code and the decoded (binary) value.
 * </p>
 */
public interface MulticodecEncoded {

    /**
     * Returns the numeric multicodec code identifying the content type.
     *
     * @return codec code
     */
    long codecCode();

    /**
     * Returns the decoded binary value.
     *
     * @return raw byte array
     */
    byte[] decoded();
}
