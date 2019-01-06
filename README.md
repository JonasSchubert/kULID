<h1 align="center">
	<br>
	<br>
	<img width="360" src="logo.png" alt="ulid">
	<br>
	<br>
	<br>
</h1>

# KUlid

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)
<a target="_blank" href="https://www.paypal.me/GuepardoApps" title="Donate using PayPal"><img src="https://img.shields.io/badge/paypal-donate-blue.svg" /></a>

[![](https://jitpack.io/v/GuepardoApps/KUlid.svg)](https://jitpack.io/#GuepardoApps/KUlid)
[![Version](https://img.shields.io/badge/version-v1.1.0.0-blue.svg)](https://github.com/GuepardoApps/KUlid/releases/tag/1.1.0.0)
[![Build](https://img.shields.io/badge/build-success-green.svg)](kulid)
[![CodeCoverage](https://img.shields.io/badge/codeCoverage-95-green.svg)](kulid)

[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](http://makeapullrequest.com)

ULID (Universally Unique Lexicographically Sortable Identifier) generator and parser for Kotlin.

Refer the [ULID spec](https://github.com/ulid/spec) for a more detailed ULID specification.

## Installation

Add the JitPack repository to your `build.gradle`:

```groovy
allprojects {
   repositories {
      ...
      maven { url 'https://jitpack.io' }
   }
}
```

Add the dependency to your `build.gradle`:

```groovy
dependencies {
    implementation 'com.github.GuepardoApps:KUlid:1.1.0.0'
}
```

## Usage

ULID generation examples:

```kotlin
val randomUlid = ULID.random()
val generateUlid = ULID.generate(System.currentTimeMillis(), byteArrayOf(0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9))
val stringUlid = ULID.fromString("003JZ9J6G80123456789abcdef")
```

ULID parsing examples:

```kotlin
val ulid = "003JZ9J6G80123456789abcdef"
val isValid = ULID.isValid(ulid)        // returns a Boolean indicating if the ULID is valid
val timestamp = ULID.getTimestamp(ulid) // returns a Long
val entropy = ULID.getEntropy(ulid)     // returns a ByteArray
```

## Prior Projects

- [azam/ulidj](https://github.com/azam/ulidj)
- [Lewiscowles1986/jULID](https://github.com/Lewiscowles1986/jULID)
- [alizain/ulid](https://github.com/alizain/ulid)

## Requirements

- Use at least JVM 1.8

## License

KUlid is distributed under the MIT license. [See LICENSE](LICENSE.md) for details.

```
MIT License

Copyright (c) 2019 GuepardoApps (Jonas Schubert)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```