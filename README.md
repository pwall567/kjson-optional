# kjson-optional

[![Build Status](https://github.com/pwall567/kjson-optional/actions/workflows/build.yml/badge.svg)](https://github.com/pwall567/kjson-optional/actions/workflows/build.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Kotlin](https://img.shields.io/static/v1?label=Kotlin&message=v1.9.24&color=7f52ff&logo=kotlin&logoColor=7f52ff)](https://github.com/JetBrains/kotlin/releases/tag/v1.9.24)
[![Maven Central](https://img.shields.io/maven-central/v/io.kjson/kjson-optional?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.kjson%22%20AND%20a:%kjson-optional%22)

Optional property for [kjson](https://github.com/pwall567/kjson).

## IMPORTANT NOTE

There is a
[bug](https://youtrack.jetbrains.com/issue/KT-57357/Reflection-KotlinReflectionInternalError-when-using-callBy-on-constructor-that-has-inline-class-parameter-with-nullable-value)
in the Kotlin reflection subsystem relating to value classes that prevents this library working as intended.

The current version (1.2) uses a regular class instead of a [value class](#value-class), and will therefore not be as
efficient as planned.
A new version using a value class will be released as soon as the issue is resolved.

## Background

When deserializing JSON objects, the [kjson](https://github.com/pwall567/kjson) library will use the default value in
the constructor for any missing properties.
The convention is to use `null` as this default, but this makes it difficult to distinguish between a missing property
and one that has been set intentionally to `null`.

The `kjson-optional` library provides an `Opt` class which, when used in conjunction with the appropriate
deserialization functions, will provide the ability to determine whether a property was omitted or was supplied with the
value `null`.

## Quick Start

A good example of an optional property is the middle name of a name class:
```kotlin
data class PersonName(
    val firstName: String,
    val middleName: Opt<String> = Opt.unset(),
    val surname: String,
)
```

Then, to access the `middleName`:
```kotlin
    val displayName = buildString {
        append(firstName)
        append(' ')
        if (middleName.isSet) {
            append(middleName.value)
            append(' ')
        }
        append(surname)
    }
```

## Value Class

`Opt` is a value class, which means that in many cases the compiler will optimise away the instantiation of a separate
"holder" class.
The use of `Opt` will incur some overhead compared to the use of a raw type, but when used as described above, it will
not lead to the creation of a large number of small objects, with the associated garbage collection cost.

## Naming

The name `Opt` was chosen because a single data class is likely to contain several optional fields, and a long name
would be very obtrusive.
The package name `io.kjson.optional` provides a bit more explanation of the function of the class.

The `java.lang.Optional` class was considered for this use but ruled out because it does not allow null values, and
because it would not allow the use of a value class.

## Reference

### Creation

To construct an `Opt`:
```kotlin
    val optString = Opt.of("A string")
```
or:
```kotlin
    val optString = Opt.unset<String>()
```
Both of these will construct an `Opt<String>`.

To construct an `Opt` from a possibly-null value:
```kotlin
    val optString = Opt.ofNullable(nullableString)
```
The `Opt` will be unset if the parameter was `null`.

In all cases the `Opt` will be immutable.

### Access

To get the value:
```kotlin
    val str = optString.value
```
`str` will contain the value as a `String`; if the `Opt` was unset a `NotSetException` (a derived class of
`IllegalStateException`) will be thrown.

To test whether the value is set:
```kotlin
    if (optString.isSet) {
        // use optString.value
        println(optString.value)
    }
```

To test if the value is unset:
```kotlin
    if (optString.isUnset) {
        // do NOT use optString.value
        println("Value is not set")
    }
```

To get the value, or `null` if it was unset:
```kotlin
    val str = optString.orNull
```
In this case, `str` will be of type `String?`.

To get the value, or some other value if it was unset:
```kotlin
    val str = optString.orElse { "default" }
```
This can be used to throw an alternative exception type in place of `NotSetException`:
```kotlin
    val str = optString.orElse { throw StringMissingException() }
```

There is also a convenience function:
```kotlin
    optString.ifSet { println(it) }
```
The lambda will be executed only if the value is set, and the value will be passed in as the parameter.

## Dependency Specification

The latest version of the library is 1.2, and it may be obtained from the Maven Central repository.

### Maven
```xml
    <dependency>
      <groupId>io.kjson</groupId>
      <artifactId>kjson-optional</artifactId>
      <version>1.2</version>
    </dependency>
```
### Gradle
```groovy
    implementation "io.kjson:kjson-optional:1.2"
```
### Gradle (kts)
```kotlin
    implementation("io.kjson:kjson-optional:1.2")
```

Peter Wall

2024-07-22
