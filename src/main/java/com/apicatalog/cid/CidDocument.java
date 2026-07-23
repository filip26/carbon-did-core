package com.apicatalog.cid;

import java.util.Collection;

public class CidDocument implements IdentifierDocument<String, String> {

    private String id;
    private Collection<String> controller;
    private Collection<String> alsoKnownAs;
    
    @Override
    public String id() {
        return id;
    }

    @Override
    public Collection<String> controller() {
        return controller;
    }
    
    @Override
    public Collection<String> alsoKnownAs() {
        return alsoKnownAs;
    }
    
    @Override
    public Collection<Relationship> relationships() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public VerificationMethod methods(Relationship relationship) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<String> remoteMethods(Relationship relationship) {
        // TODO Auto-generated method stub
        return null;
    }

}
