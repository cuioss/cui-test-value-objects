package de.cuioss.test.valueobjects.testbeans.builder;

import de.cuioss.test.valueobjects.api.contracts.VerifyBuilder;
import de.cuioss.test.valueobjects.testbeans.builder.BuilderMinimalTestClassSimple.BuilderMinimalTypeBuilder;

@VerifyBuilder(builderClass = BuilderMinimalTypeBuilder.class)
@SuppressWarnings("javadoc")
public class BuilderMinimalTestClassSimple {

    public static class BuilderMinimalTypeBuilder {

        public BuilderMinimalTestClassSimple build() {
            return new BuilderMinimalTestClassSimple();
        }
    }

    public static BuilderMinimalTypeBuilder builder() {
        return new BuilderMinimalTypeBuilder();
    }
}
