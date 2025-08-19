package com.apicatalog.did.primitive;

import com.apicatalog.did.document.DidDocument;
import com.apicatalog.did.resolver.Metadata;
import com.apicatalog.did.resolver.ResolvedDocument;

public class ImmutableResolvedDocument implements ResolvedDocument {

    protected final DidDocument document;
    protected final Metadata meta;
    
    protected ImmutableResolvedDocument(
            final DidDocument document,
            final Metadata meta
            ) {
        this.document = document;
        this.meta = meta;
    }
    
    public static final ResolvedDocument of(DidDocument document) {
        return new ImmutableResolvedDocument(document, null);
    }

    public static final ResolvedDocument of(DidDocument document, Metadata meta) {
        return new ImmutableResolvedDocument(document, meta);
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
