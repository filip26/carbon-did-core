package com.apicatalog.did.io;

import java.io.InputStream;

import com.apicatalog.did.document.DidDocument;

public interface DidDocumentReader {

    String contentType();

    DidDocument read(InputStream is);

}
