# DeVerify

Disable java runtime verifier per-class on the fly only using java.

## Info

This library uses `Unsafe` to unset the `_misc_flags_should_verify` flag in the `Class` object found in OpenJDK based JVMs.
Therefore, this library will only work on OpenJDK based JVMs.

A writeup on what it is based on can be found [here](https://blog.skidfuscator.dev/disabling-the-verifier-in-java/) which
explains the process in more detail.

### Tested JDKs
- Oracle OpenJDK 8-17
- AdoptOpenJDK 8-17
- Amazon Corretto 8-17
- Zulu OpenJDK 8-17
- Eclipse Temurin 8-20

### Non working JDKs
- Anything J9 based (IBM, OpenJ9, etc.)
- GraalVM
- Anything that doesn't fork off of OpenJDK

## Usage
Simply call `DeVerifier.disableVerifier(clazz)` to disable the verifier for this class.

## Limitations
- The library requires `Unsafe` to be accessible.
- The library assumes that the `Class` object is laid out in memory the same way as in OpenJDK.
- The library cannot disable verification of classes before they are loaded.