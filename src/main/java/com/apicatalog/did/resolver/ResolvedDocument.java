package com.apicatalog.did.resolver;

import com.apicatalog.did.document.DidDocument;

public interface ResolvedDocument {

    DidDocument document();
    
    default Metadata metadata() {
        return null;
    }

    static ResolvedDocument of(DidDocument document) {
        return new ImmutableResolvedDocument(document, null);
    }
    
    static ResolvedDocument of(DidDocument document, Metadata meta) {
        return new ImmutableResolvedDocument(document, meta);
    }
}
