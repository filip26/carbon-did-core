# Carbon Decentralized Identifiers

[Decentralized Identifiers (DIDs) v1.0](https://www.w3.org/TR/did-core/) support identity systems where identifiers are controlled by their owners rather than by centralized authorities.

DIDs allow entities to create persistent, globally resolvable identifiers and prove control over them using cryptographic keys. This enables portable and verifiable identity without depending on a single identity provider, registry, or intermediary.

Carbon provides primitives for creating, resolving, and verifying DID documents and DID-based identities.


[![Java 25 CI](https://github.com/filip26/carbon-did-core/actions/workflows/java25-build.yml/badge.svg)](https://github.com/filip26/carbon-did-core/actions/workflows/java25-build.yml)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/dd79aafc6eb14ed18f2217de62585ba7)](https://app.codacy.com/gh/filip26/carbon-did-core/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
[![Maven Central](https://img.shields.io/maven-central/v/com.apicatalog/carbon-did.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:com.apicatalog%20AND%20a:carbon-did)
[![javadoc](https://javadoc.io/badge2/com.apicatalog/carbon-did/javadoc.svg)](https://javadoc.io/doc/com.apicatalog/carbon-did)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## ✨ Features

- DID, DID URL, and DID Document primitives
- Resolvers
  - [`did:key`](https://github.com/filip26/carbon-did-key) method
  - [`did:cel`](https://github.com/filip26/iron-did-cel) method
- Modular, fully configurable, and extensible
- Zero dependencies for a lightweight, self-contained implementation
- Deliberate engineering; zero vibe coding

## Installation

### Maven

```xml
<dependency>
    <groupId>com.apicatalog</groupId>
    <artifactId>carbon-did</artifactId>
    <version>${did.version}</version>
</dependency>
```

## 🤝 Contributing

Contributions of all kinds are welcome - whether it’s code, documentation, testing, or community support! Please open PR or issue to get started.

## 📚 Resources

- [W3C Decentralized Identifiers (DIDs) v1.0](https://www.w3.org/TR/did-core/)
- [Decentralized Identifier Resolution (DID Resolution) v1](https://w3c.github.io/did-resolution/)
- [W3C Controlled Identifiers v1.0](https://www.w3.org/TR/cid-1.0/)
- [Carbon DID Key Method](https://github.com/filip26/carbon-did-key)

## 💼 Commercial Support

Commercial support and consulting are available.
For inquiries, please contact: filip26@gmail.com


