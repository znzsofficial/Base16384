# Base16384 for Kotlin

A Kotlin implementation of Base16384 encoding and decoding, inspired by the TypeScript library [base16384.js](https://github.com/shigma/base16384.js). 

## Features

- Encode `String` or `ByteArray` data into Base16384 format using Unicode characters.
- Decode Base16384-encoded strings back to the original data.

## Installation

Include the library in your Kotlin project.

## Usage

```kotlin
import Base16384

fun main() {
    val input = "Hello, Base16384!"

    // Encoding
    val encoded = Base16384.encodeToString(input)
    println("Encoded: $encoded")

    // Decoding
    val decoded = Base16384.decodeFromString(encoded)
    println("Decoded: $decoded")
}
```
