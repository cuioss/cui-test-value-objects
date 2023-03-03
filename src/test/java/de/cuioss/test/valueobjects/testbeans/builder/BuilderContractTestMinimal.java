package de.cuioss.test.valueobjects.testbeans.builder;

import de.cuioss.test.valueobjects.api.contracts.VerifyBuilder;
import de.cuioss.test.valueobjects.testbeans.builder.BuilderContractTestMinimal.BuilderContractTestMinimalBuilder;

@VerifyBuilder(builderClass = BuilderContractTestMinimalBuilder.class)
@SuppressWarnings("javadoc")
public class BuilderContractTestMinimal {

    public static class BuilderContractTestMinimalBuilder {

        public BuilderContractTestMinimal build() {
            return new BuilderContractTestMinimal();
        }
    }

    public static BuilderContractTestMinimalBuilder builder() {
        return new BuilderContractTestMinimalBuilder();
    }
}
