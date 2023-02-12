package io.cui.test.valueobjects.testbeans.builder;

@SuppressWarnings("javadoc")
public class BadBuilderAlwaysFails {

    public static class BadBuilderAlwaysFailsBuilder {

        public BadBuilderAlwaysFails build() {
            throw new IllegalStateException("Bad boy");

        }
    }

    public static BadBuilderAlwaysFailsBuilder builder() {
        return new BadBuilderAlwaysFailsBuilder();
    }

}
