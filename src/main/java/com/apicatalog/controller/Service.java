package com.apicatalog.controller;

import java.net.URI;
import java.util.Collection;

public interface Service {

    URI id();

//    FragmentType type();

    Collection<ServiceEndpoint> endpoint();
}
