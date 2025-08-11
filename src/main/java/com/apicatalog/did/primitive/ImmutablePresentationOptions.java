package com.apicatalog.did.primitive;

import java.util.Objects;

import com.apicatalog.did.resolver.PresentationResolver;

public class ImmutablePresentationOptions implements PresentationResolver.Options {

    protected final String accept;

    protected ImmutablePresentationOptions(String accept) {
        this.accept = accept;
    }

    public static final PresentationResolver.Options of(String accept) {
        Objects.requireNonNull(accept);
        return new ImmutablePresentationOptions(accept);
    }

    @Override
    public String accept() {
        return accept;
    }

}
