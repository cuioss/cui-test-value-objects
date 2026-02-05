# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

`cui-test-value-objects` is a Java testing library that provides infrastructure for testing value objects, beans, builders, constructors, factory methods, and comparators. Unlike behavior-oriented testing frameworks (EasyMock, Mockito), this library focuses on testing properties, object contracts, and canonical object methods (equals, hashCode, toString, serialization).

## Build and Test Commands

### Standard Maven Commands
- **Build and run all tests**: `./mvnw verify`
- **Run tests only**: `./mvnw test`
- **Run single test class**: `./mvnw test -Dtest=ClassName`
- **Run single test method**: `./mvnw test -Dtest=ClassName#methodName`
- **Clean build**: `./mvnw clean verify`
- **Skip tests**: `./mvnw verify -DskipTests`

### Maven Wrapper
Always use `./mvnw` (or `./mvnw.cmd` on Windows) instead of `mvn` to ensure consistent Maven version.

### Java Version Requirements
- Primary development: Java 21
- CI/CD tests against: Java 21 and 25
- Minimum release target: Configurable via `-Dmaven.compiler.release=<version>`

### Quality and Deployment
- **SonarCloud analysis**: `./mvnw -B verify -Psonar sonar:sonar` (requires SONAR_TOKEN)
- **Deploy snapshot**: `./mvnw -B -Prelease-snapshot deploy -Dmaven.test.skip=true`
- **Generate Javadoc**: `./mvnw -Prelease-snapshot javadoc:aggregate`


## Git Workflow

This repository has branch protection on `main`. Direct pushes to `main` are never allowed. Always use this workflow:

1. Create a feature branch: `git checkout -b <branch-name>`
2. Commit changes: `git add <files> && git commit -m "<message>"`
3. Push the branch: `git push -u origin <branch-name>`
4. Create a PR: `gh pr create --head <branch-name> --base main --title "<title>" --body "<body>"`
5. Enable auto-merge: `gh pr merge --auto --squash --delete-branch`
6. Wait for merge (check every ~60s): `while gh pr view --json state -q '.state' | grep -q OPEN; do sleep 60; done`
7. Return to main: `git checkout main && git pull`

## Architecture Overview

### Core Architecture Pattern
The library uses an **annotation-driven contract testing** approach where test classes extend base classes and use annotations to declare what should be tested.

### Key Components

#### 1. Base Test Classes
- **`ValueObjectTest<T>`**: Main entry point for value object testing. Extends `PropertyAwareTest<T>` and implements `ObjectContractTestSupport`. Automatically runs object contract tests and configured test contracts.
- **`PropertyAwareTest<T>`**: Base for property-aware testing, handles property metadata and generators.
- **`MapperTest<S, T>`**: Specialized for testing mapper classes that transform between types.

#### 2. Contract System (`de.cuioss.test.valueobjects.contract`)
Contract implementations test specific patterns:
- **`BeanPropertyContractImpl`**: Tests JavaBean property accessors (getters/setters)
- **`BuilderContractImpl`**: Tests builder pattern implementations
- **`CopyConstructorContractImpl`**: Tests copy constructor behavior
- **`ObjectCreatorContractImpl`**: Tests factory methods and constructors
- **`EqualsAndHashcodeContractImpl`**: Tests equals/hashCode contract compliance
- **`ToStringContractImpl`**: Tests toString implementations
- **`SerializableContractImpl`**: Tests serialization/deserialization
- **`MapperContractImpl`**: Tests mapper transformations

#### 3. Object Contract Testing (`de.cuioss.test.valueobjects.objects`)
- **`ParameterizedInstantiator<T>`**: Creates instances with various property combinations
- **`AbstractInlineInstantiator<T>`**: Base for inline instantiator implementations
- Contract tests verify: equals symmetry/transitivity, hashCode consistency, serialization roundtrip

#### 4. Property Metadata System (`de.cuioss.test.valueobjects.property`)
- **`PropertyMetadata`**: Describes bean properties (name, type, required/optional, read/write access)
- Supports reflection-based property scanning and manual property configuration
- Handles property generators for test data

#### 5. Generator System (`de.cuioss.test.valueobjects.generator`)
- **`TypedGenerator<T>`**: Interface for generating test values
- Integrates with `de.cuioss.test.generator` (cui-test-generator dependency)
- Extensible via `@PropertyGenerator` and `@PropertyGeneratorHint` annotations

#### 6. API Annotations (`de.cuioss.test.valueobjects.api`)
Test behavior is controlled via annotations:
- **`@VerifyBeanProperty`**: Test JavaBean properties
- **`@VerifyBuilder`**: Test builder pattern
- **`@VerifyConstructor`**: Test constructor with specified parameters
- **`@VerifyFactoryMethod`**: Test factory method
- **`@VerifyCopyConstructor`**: Test copy constructor
- **`@VetoObjectTestContract`**: Exclude specific object contract tests (e.g., TO_STRING, SERIALIZABLE)
- **`@ObjectTestConfig`**: Configure object contract testing (e.g., exclude fields from equals/hashCode)
- **`@PropertyReflectionConfig`**: Control property reflection (skip scanning, mark required/default-valued properties)
- **`@PropertyConfig`**: Manually declare properties
- **`@PropertyGenerator`**: Register custom generators
- **`@PropertyGeneratorHint`**: Map abstract types to concrete implementations

### Typical Test Structure

```java
@VerifyBeanProperty  // or @VerifyBuilder, @VerifyConstructor, etc.
@VetoObjectTestContract(ObjectTestContracts.SERIALIZABLE)  // Optional: skip contracts
@ObjectTestConfig(equalsAndHashCodeExclude = "transientField")  // Optional: config
class MyValueObjectTest extends ValueObjectTest<MyValueObject> {
    // Test infrastructure handles property scanning and contract execution
    // Override anyValueObject() only if no contracts are configured
}
```

## Package Structure

- `de.cuioss.test.valueobjects`: Main entry classes (`ValueObjectTest`, `MapperTest`, `PropertyAwareTest`)
- `de.cuioss.test.valueobjects.api`: Annotations and interfaces for contract testing
- `de.cuioss.test.valueobjects.contract`: Contract implementations
- `de.cuioss.test.valueobjects.objects`: Instantiator and object creation infrastructure
- `de.cuioss.test.valueobjects.property`: Property metadata and property-based testing
- `de.cuioss.test.valueobjects.generator`: Test data generation system
- `de.cuioss.test.valueobjects.util`: Utility classes
- `de.cuioss.test.valueobjects.junit5`: JUnit 5 integration

## Dependencies

Key dependencies defined in parent POM (`cui-java-parent:1.3.3`):
- **JUnit Jupiter**: Test framework (compile scope - this is a testing library)
- **Lombok**: Annotations for boilerplate reduction
- **cui-java-tools**: CUI utility library
- **cui-test-generator**: Test data generator system
- **Javassist**: Bytecode manipulation for advanced testing scenarios

## Development Notes

### Extension Points
- **Custom generators**: Implement `TypedGenerator<T>` and register via `@PropertyGenerator`
- **Custom contracts**: Implement `TestContract<T>` interface
- **Custom instantiators**: Extend `AbstractInlineInstantiator<T>` or implement `ParameterizedInstantiator<T>`

### Property Scanning
By default, the framework uses reflection to scan JavaBean properties. Use `@PropertyReflectionConfig(skip = true)` and `@PropertyConfig` annotations when:
- Properties don't follow JavaBean conventions
- Getters are missing
- Manual control is needed over property metadata

### Object Contract Testing
Object contract tests run automatically unless vetoed:
- **EQUALS_AND_HASHCODE**: Tests equals/hashCode contract
- **TO_STRING**: Tests toString produces non-null output
- **SERIALIZABLE**: Tests serialization roundtrip (only if type implements Serializable)

Use `@VetoObjectTestContract` to skip specific contracts.

## CI/CD Pipeline

The project uses GitHub Actions (`.github/workflows/maven.yml`):
1. **Build job**: Tests against Java 21 and 25
2. **Sonar-build job**: Runs SonarCloud analysis
3. **Deploy-snapshot job**: Deploys snapshots to Maven Central (main branch only)

All jobs use Maven wrapper and the Temurin JDK distribution.

## Documentation

- Detailed feature documentation in `src/site/asciidoc/`
- Generated documentation: https://cuioss.github.io/cui-test-value-objects/about.html
- Package-level Javadoc in `package-info.java` files contains extensive examples
