package com.apicatalog.did.io;

import java.io.IOException;
import java.io.OutputStream;

import com.apicatalog.did.document.DidDocument;

/**
 * Writer for serializing a {@link DidDocument} to an {@link OutputStream}.
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
     * @throws IOException if writing fails
     */
    void write(DidDocument document, OutputStream os) throws IOException;
}
