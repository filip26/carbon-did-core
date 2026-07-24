package com.apicatalog.did.resolver;

import com.apicatalog.did.DidDocument;
import com.apicatalog.did.DidResource;

/**
 * Result of a DID resolution process.
 * <p>
 * Contains the resolved {@link DidDocument} and optional
 * {@link DidDocumentMetadata}.
 * </p>
 *
 * @see <a href="https://www.w3.org/TR/did-core/#did-resolution">DID
 *      Resolution</a>
 * 
 * @param metadata the resolution metadata associated with the DID Document, or
 *                 {@code null} if none
 * @param document the DID Document (never {@code null})
 */
public record DidDocumentWithMetadata(
        DidDocumentMetadata metadata,
        DidDocument document) implements DidResource {

}
