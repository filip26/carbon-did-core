package com.apicatalog.did.resolver;

import com.apicatalog.did.document.DidDocument;

final class ImmutableResolvedDocument implements ResolvedDocument {

    final DidDocument document;
    final Metadata meta;
    
    ImmutableResolvedDocument(
            final DidDocument document,
            final Metadata meta
            ) {
        this.document = document;
        this.meta = meta;
    }
    
    @Override
    public DidDocument document() {
        return document;
    }
    
    @Override
    public Metadata metadata() {
        return meta;
    }
}
