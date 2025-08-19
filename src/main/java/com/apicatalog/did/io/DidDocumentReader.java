package com.apicatalog.did.io;

import java.io.IOException;
import java.io.InputStream;

import com.apicatalog.did.document.DidDocument;

/**
 * Reader for parsing a {@link DidDocument} from an {@link InputStream}.
 * <p>
 * Implementations handle a specific DID document representation (e.g. JSON-LD,
 * CBOR).
 * </p>
 */
public interface DidDocumentReader {

    /**
     * Returns the supported content type (e.g. {@code application/did+ld+json}).
     *
     * @return MIME type string
     */
    String contentType();

    /**
     * Reads and parses a DID Document from the given input stream.
     *
     * @param is input stream (must not be {@code null})
     * @return parsed {@code DidDocument}
     * @throws IOException if reading or parsing fails
     */
    DidDocument read(InputStream is) throws IOException;
}
