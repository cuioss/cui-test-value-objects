package io.cui.test.valueobjects.testbeans.builder;

import io.cui.test.valueobjects.api.contracts.VerifyBuilder;
import io.cui.test.valueobjects.testbeans.builder.BuilderContractTestMinimal.BuilderContractTestMinimalBuilder;

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
