<h1 align="center">
	<br>
	<br>
	<img width="360" src="logo.png" alt="ulid">
	<br>
	<br>
	<br>
</h1>

<p align="center"><h1 style="text-align: center;">KUlid</h1></p>

<p align="center">
  <a href="https://jitpack.io/#GuepardoApps/KUlid"><img src="https://jitpack.io/v/GuepardoApps/KUlid.svg"/></a>
  <a href="https://opensource.org/licenses/MIT"><img src="https://img.shields.io/badge/License-MIT-blue.svg"/></a>
  <a href="http://makeapullrequest.com"><img src="https://img.shields.io/badge/PRs-welcome-brightgreen.svg"/></a>
  <a href="https://github.com/JonasSchubert/kulid/"><img src="https://img.shields.io/github/stars/JonasSchubert/kulid.svg"/></a>
</p>

<p align="center"><h6 style="text-align: center;">Unit test coverage</h6></p>

<p align="center">
  <a href="./test"><img src="https://img.shields.io/badge/coverage-100%25-green.svg"/></a>
</p>

<p align="center"><h6 style="text-align: center;">Support me</h6></p>

<p align="center">
  <a href="https://www.paypal.me/GuepardoApps"><img src="https://img.shields.io/badge/paypal-support-blue.svg"/></a>
</p>

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
    implementation 'com.github.guepardoapps:kulid:1.1.2.0'
}
```

## Usage

ULID generation examples:

```kotlin
val randomUlid = ULID.random()
val generateUlid = ULID.generate(System.currentTimeMillis(), byteArrayOf(0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9))
val stringUlid = ULID.fromString("003JZ9J6G80123456789ABCDEF")
```

ULID parsing examples:

```kotlin
val ulid = "003JZ9J6G80123456789ABCDEF"
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

## Contributors

| [<img alt="JonasSchubert" src="https://avatars0.githubusercontent.com/u/21952813?v=4&s=117" width="117"/>](https://github.com/JonasSchubert) |
| :---------------------------------------------------------------------------------------------------------------------------------------: |
| [Jonas Schubert](https://github.com/JonasSchubert) |

## License

KUlid is distributed under the MIT license. [See LICENSE](LICENSE.md) for details.

```
MIT License

Copyright (c) 2019 Jonas Schubert

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