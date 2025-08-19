package com.apicatalog.did.resolver;

import com.apicatalog.did.document.DidDocument;

public interface ResolvedDidDocument {

    DidDocument document();
    
    default DidDocumentMetadata metadata() {
        return null;
    }

    static ResolvedDidDocument of(DidDocument document) {
        return new ImmutableResolvedDocument(document, null);
    }
    
    static ResolvedDidDocument of(DidDocument document, DidDocumentMetadata meta) {
        return new ImmutableResolvedDocument(document, meta);
    }
}
