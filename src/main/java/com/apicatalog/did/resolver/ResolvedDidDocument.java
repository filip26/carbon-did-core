package com.apicatalog.did.resolver;

import com.apicatalog.did.document.DidDocument;

/**
 * Result of a DID resolution process.
 * <p>
 * Contains the resolved {@link DidDocument} and optional
 * {@link DidDocumentMetadata}.
 * </p>
 *
 * @see <a href="https://www.w3.org/TR/did-core/#did-resolution">DID
 *      Resolution</a>
 */
public interface ResolvedDidDocument {

    /**
     * Returns the resolved DID Document.
     *
     * @return the DID Document (never {@code null})
     */
    DidDocument document();

    /**
     * Returns resolution metadata associated with the DID Document.
     *
     * @return metadata, or {@code null} if none
     */
    default DidDocumentMetadata metadata() {
        return null;
    }

    /**
     * Creates a resolution result containing only a document.
     *
     * @param document the resolved DID Document
     * @return a new {@code ResolvedDidDocument}
     */
    static ResolvedDidDocument of(DidDocument document) {
        return new ImmutableResolvedDocument(document, null);
    }

    /**
     * Creates a resolution result containing a document and metadata.
     *
     * @param document the resolved DID Document
     * @param meta     associated metadata (may be {@code null})
     * @return a new {@code ResolvedDidDocument}
     */
    static ResolvedDidDocument of(DidDocument document, DidDocumentMetadata meta) {
        return new ImmutableResolvedDocument(document, meta);
    }
}
