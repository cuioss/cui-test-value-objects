package io.cui.test.valueobjects.testbeans.builder;

import io.cui.test.valueobjects.api.contracts.VerifyBuilder;
import io.cui.test.valueobjects.testbeans.builder.BuilderContractTestMinimalFactory.BuilderContractTestMinimalFactoryBuilder;

@VerifyBuilder(builderClass = BuilderContractTestMinimalFactoryBuilder.class,
        builderFactoryProvidingClass = BuilderContractTestMinimalFactory.class)
@SuppressWarnings("javadoc")
public class BuilderContractTestMinimalFactory {

    public static class BuilderContractTestMinimalFactoryBuilder {

        public BuilderContractTestMinimalFactory build() {
            return new BuilderContractTestMinimalFactory();
        }
    }

    public static BuilderContractTestMinimalFactoryBuilder builder() {
        return new BuilderContractTestMinimalFactoryBuilder();
    }
}
