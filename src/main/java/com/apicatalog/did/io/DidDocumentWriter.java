package com.apicatalog.did.io;

import java.io.IOException;
import java.io.OutputStream;

import com.apicatalog.did.Document;

/**
 * Writer for serializing a {@link Document} to an {@link OutputStream}.
 * <p>
 * Implementations produce a specific DID document representation (e.g. JSON-LD,
 * CBOR).
 * </p>
 */
public interface DidDocumentWriter {

    /**
     * Returns the supported content type (e.g. {@code application/did+ld+json}).
     *
     * @return MIME type string
     */
    String contentType();

    /**
     * Writes the given DID Document to the provided output stream.
     *
     * @param document the DID Document to serialize (must not be {@code null})
     * @param os       the output stream to write to (must not be {@code null})
     * @throws IOException if a low-level I/O error occurs
     */
    void write(Document document, OutputStream os) throws IOException;
}
