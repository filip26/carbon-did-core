package com.apicatalog.did.resolver;

import com.apicatalog.did.document.DidDocument;
import com.apicatalog.did.primitive.ImmutableResolvedDocument;

public interface ResolvedDocument {

    DidDocument document();

    static ResolvedDocument immutable(DidDocument document) {
        return ImmutableResolvedDocument.of(document);
    }
}
