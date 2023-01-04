# cui-test-value-objects-junit5

## What is it?
A library to support creating unit-tests and much more. Compared to other, more behavior-oriented frameworks like 
[EasyMock](https://easymock.org/) or [Mockito](https://site.mockito.org/), it focuses, as its name 
implies, on testing and generating Value-Objects of all sorts. Therefore it is no replacement of said frameworks but 
a supplement. Testing values-objects is about checking properties, constructors, factories, builder,... and the 
dependent canonical object-methods. In addition, the library provides a feature rich generator system, sitting on 
top of the quickcheck library. 

## Maven Coordinates
```xml
<dependency>
    <groupId>de.icw.cui.test</groupId>
    <artifactId>cui-test-value-objects-junit5</artifactId>
    <scope>test</scope>
</dependency>
```

## Understanding Value-Object testing
There are a number of features that can be used. Therefore, there are different documentation parts for the specific
 features:
* [Value-Object-Testing](docs/testing-value-objects.md)
* [Testing Mapper Classes](docs/testing-mapper.md)
* [Managing Metadata](docs/managing-metadata.md)
* [Generator System](docs/generator-system.md)
* [Reflection System](docs/reflection-system.md)
* [Simplified Testing of Canonical Object Methods](docs/simple-canonical-object-methods.md)
