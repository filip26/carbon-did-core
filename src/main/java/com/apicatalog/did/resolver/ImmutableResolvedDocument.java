package com.apicatalog.did.resolver;

import com.apicatalog.did.document.DidDocument;

final class ImmutableResolvedDocument implements ResolvedDidDocument {

    final DidDocument document;
    final DidDocumentMetadata meta;
    
    ImmutableResolvedDocument(
            final DidDocument document,
            final DidDocumentMetadata meta
            ) {
        this.document = document;
        this.meta = meta;
    }
    
    @Override
    public DidDocument document() {
        return document;
    }
    
    @Override
    public DidDocumentMetadata metadata() {
        return meta;
    }
}
