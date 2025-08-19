package com.apicatalog.did.io;

import java.io.IOException;
import java.io.OutputStream;

import com.apicatalog.did.document.DidDocument;

public interface DidDocumentWriter {

    String contentType();

    void write(DidDocument document, OutputStream os) throws IOException;

}
