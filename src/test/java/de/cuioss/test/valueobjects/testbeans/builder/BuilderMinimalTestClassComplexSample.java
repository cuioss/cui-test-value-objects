package de.cuioss.test.valueobjects.testbeans.builder;

import de.cuioss.test.valueobjects.api.contracts.VerifyBuilder;
import de.cuioss.test.valueobjects.api.property.PropertyBuilderConfig;
import de.cuioss.test.valueobjects.testbeans.builder.BuilderMinimalTestClassComplexSample.BuilderMinimalTypeBuilder;

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
