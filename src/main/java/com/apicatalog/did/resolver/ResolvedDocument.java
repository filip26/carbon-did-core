package com.apicatalog.did.resolver;

import com.apicatalog.did.document.DidDocument;
import com.apicatalog.did.primitive.ImmutableResolvedDocument;

public interface ResolvedDocument {

    DidDocument document();
    
    default Metadata metadata() {
        return null;
    }

    static ResolvedDocument immutable(DidDocument document) {
        return ImmutableResolvedDocument.of(document);
    }
    
    static ResolvedDocument immutable(DidDocument document, Metadata meta) {
        return ImmutableResolvedDocument.of(document, meta);
    }
}
