# nerd4j-core
Core library with classes that represent concepts common to all nerd4j projects but also generic enough to be used in any code.

## Content of this library
The classes of this library are divided into several concepts:

* Concurrent: currently contains just the class `KeyLock` that handles the lock and release of a pool of resources.
* Converter: classes and interfaces for the conversion of simple data beans (POJOs) from one format to another.
* Exception: currently contains just the class `DataConsistencyException` that represents a failure in checking data consistency performed by the utility class `DataConsistency`.
* Format: contains an abstract class and the reflection support needed to autogenerate the `toString()` method of a data bean using the `@Formatted` annotation on the fields that you want to print.
* Lang: useful classes that are missing in the package `java.lang`.
* Net: currently contains just the class `IPv4Address` that represents a lightweight and compatible version of `java.net.InetAddress`.
* Time: useful classes to deal with `java.util.Date` and `java.util.Calendar`.
* Util: utility classes like the `Require` checker that allows to make assertions on data fields, the `HashCoder` that provides an easy way to generate `hashcode`s from the internal fields of a bean, the `CommandIterator` that allows to apply the same operation to all the items of a collection or an array.


This library is also available on Maven Central [here] (http://search.maven.org/#artifactdetails|org.nerd4j|nerd4j-core|1.1.1|jar "Maven Central: nerd4j-core") and can be used with the following dependecy declaration:
```xml
<dependency>
 <groupId>org.nerd4j</groupId>
 <artifactId>nerd4j-core</artifactId>
 <version>1.1.1</version>
</dependency>
```
