package com.apicatalog.controller.method;

import java.net.URI;
import java.time.Instant;
import java.util.Objects;

/**
 * Represents a verification method declaration.
 * 
 * https://www.w3.org/TR/controller-document/#verification-methods
 */
public interface VerificationMethod {

    URI id();

    String type();

    URI controller();

    Instant revoked();

    Instant expires();

    static boolean equals(VerificationMethod method1, VerificationMethod method2) {
        if (method1 == null || method2 == null) {
            return method1 == method2;
        }
        return Objects.equals(method1.id(), method2.id())
                && Objects.equals(method1.type(), method2.type())
                && Objects.equals(method1.controller(), method2.controller())
                && Objects.equals(method1.expires(), method2.expires())
                && Objects.equals(method1.revoked(), method2.revoked());
    }
}