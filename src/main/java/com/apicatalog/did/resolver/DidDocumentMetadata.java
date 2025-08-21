package com.apicatalog.did.resolver;

import java.time.Instant;
import java.util.Collections;
import java.util.Set;

import com.apicatalog.did.Did;

/**
 * Metadata associated with a resolved DID Document, as defined in
 * <a href="https://www.w3.org/TR/did-core/#did-document-metadata">DID Core —
 * DID Document Metadata</a>.
 */
public interface DidDocumentMetadata {

    /**
     * The timestamp when the DID Document was created.
     *
     * @return creation time, or {@code null} if not provided
     */
    default Instant created() {
        return null;
    }

    /**
     * The timestamp when the DID Document was last updated.
     *
     * @return last update time, or {@code null} if not provided
     */
    default Instant updated() {
        return null;
    }

    /**
     * Indicates whether the DID has been deactivated.
     *
     * @return {@code true} if deactivated, otherwise {@code false}
     */
    default boolean deactivated() {
        return false;
    }

    /**
     * A timestamp after which the DID Document should be refreshed.
     *
     * @return refresh time, or {@code null} if not specified
     */
    default Instant refresh() {
        return null;
    }

    /**
     * The identifier for the current version of the DID Document.
     *
     * @return version identifier, or {@code null} if not provided
     */
    default String versionId() {
        return null;
    }

    /**
     * The identifier of the next version of the DID Document.
     *
     * @return next version identifier, or {@code null} if not provided
     */
    default String nextVersionId() {
        return null;
    }

    /**
     * Equivalent identifiers for the DID, if any.
     *
     * @return a set of equivalent DIDs, never {@code null}
     */
    default Set<Did> equivalentId() {
        return Collections.emptySet();
    }

    /**
     * The canonical identifier for the DID.
     *
     * @return canonical DID, or {@code null} if not provided
     */
    default Did canonicalId() {
        return null;
    }
}
