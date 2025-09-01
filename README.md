# Carbon Decentralized Identifiers

Decentralized Identifiers (DIDs) are a W3C standard for creating and resolving 
persistent, verifiable identifiers without centralized registries or authorities.  
They enable secure, portable, and self-sovereign identity for people, 
organizations, and devices.  

Carbon provides a Java implementation of the 
[Decentralized Identifiers (DIDs) v1.0](https://www.w3.org/TR/did-core/) 
specification, making it easier to build and integrate DID-based solutions 
in Java applications.

The library defines core primitives such as `Did` and `DidUrl` for working with 
identifiers, together with interfaces for resolving DIDs into DID Documents. 
These components form the foundation for parsing, constructing, and resolving 
identifiers in a modular and extensible way, supporting multiple DID methods 
and resolution strategies.

[![Java 8 CI](https://github.com/filip26/carbon-did-core/actions/workflows/java8-build.yml/badge.svg)](https://github.com/filip26/carbon-did-core/actions/workflows/java8-build.yml)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/dd79aafc6eb14ed18f2217de62585ba7)](https://app.codacy.com/gh/filip26/carbon-did-core/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
[![Maven Central](https://img.shields.io/maven-central/v/com.apicatalog/carbon-did.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:com.apicatalog%20AND%20a:carbon-did)
[![javadoc](https://javadoc.io/badge2/com.apicatalog/carbon-did/javadoc.svg)](https://javadoc.io/doc/com.apicatalog/carbon-did)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## ✨ Features

* DID, DID URL primitives
* DID Document API & primitives
* DID Resolver API
  * [`did:key`](https://github.com/filip26/carbon-did-key) method

## Installation

### Maven

```xml
<dependency>
    <groupId>com.apicatalog</groupId>
    <artifactId>carbon-did</artifactId>
    <version>0.9.2</version>
</dependency>
```

## Contributing

All PR's welcome!


### Building

Fork and clone the project repository.

```bash
> cd carbon-did-core
> mvn clean package
```

## Resources

- [W3C Decentralized Identifiers (DIDs) v1.0](https://www.w3.org/TR/did-core/)
- [W3C Controlled Identifiers v1.0](https://www.w3.org/TR/cid-1.0/)
- [Carbon DID Key Method](https://github.com/filip26/carbon-did-key)
- [Carbon Controlled Identifiers](https://github.com/filip26/carbon-cid)

## Sponsors

<a href="https://github.com/digitalbazaar">
  <img src="https://avatars.githubusercontent.com/u/167436?s=200&v=4" width="40" />
</a> 

## Commercial Support

Commercial support and consulting are available.  
For inquiries, please contact: filip26@gmail.com

