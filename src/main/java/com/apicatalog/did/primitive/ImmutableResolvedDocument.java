package com.apicatalog.did.primitive;

import com.apicatalog.did.document.DidDocument;
import com.apicatalog.did.resolver.ResolvedDocument;

public class ImmutableResolvedDocument implements ResolvedDocument {

    protected final DidDocument document;
    
    protected ImmutableResolvedDocument(
            DidDocument document
            ) {
        this.document = document;
    }
    
    public static final ResolvedDocument of(DidDocument document) {
        return new ImmutableResolvedDocument(document);
    }
    
    @Override
    public DidDocument document() {
        return document;
    }

}
