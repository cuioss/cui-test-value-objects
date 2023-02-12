package io.cui.test.valueobjects.testbeans.builder;

import io.cui.test.valueobjects.api.contracts.VerifyBuilder;
import io.cui.test.valueobjects.api.property.PropertyBuilderConfig;
import io.cui.test.valueobjects.testbeans.builder.BuilderMinimalTestClassComplexSample.BuilderMinimalTypeBuilder;

@VerifyBuilder(builderClass = BuilderMinimalTypeBuilder.class, exclude = "name",
        defaultValued = "generator", readOnly = "generator",
        required = "collectionType",
        transientProperties = "propertyMemberInfo")
@SuppressWarnings("javadoc")
@PropertyBuilderConfig(name = "generator")
public class BuilderMinimalTestClassComplexSample {

    public static class BuilderMinimalTypeBuilder {

        public BuilderMinimalTestClassComplexSample build() {
            return new BuilderMinimalTestClassComplexSample();
        }
    }

    public static BuilderMinimalTypeBuilder builder() {
        return new BuilderMinimalTypeBuilder();
    }
}
