package io.cui.test.valueobjects.testbeans.builder;

import io.cui.test.valueobjects.api.contracts.VerifyBuilder;
import io.cui.test.valueobjects.testbeans.builder.BuilderMinimalTestClassSimple.BuilderMinimalTypeBuilder;

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
