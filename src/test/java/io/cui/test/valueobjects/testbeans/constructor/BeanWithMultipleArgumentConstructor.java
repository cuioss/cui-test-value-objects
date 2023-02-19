package io.cui.test.valueobjects.testbeans.constructor;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.Formattable;
import java.util.Set;

import io.cui.test.valueobjects.objects.BuilderInstantiator;
import io.cui.tools.property.PropertyMemberInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("javadoc")
@RequiredArgsConstructor
public class BeanWithMultipleArgumentConstructor implements Serializable {

    private static final long serialVersionUID = -7914292255779711820L;

    @Getter
    private final String name;

    @Getter
    private final PropertyMemberInfo propertyMemberInfo;

    @Getter
    private final Set<String> nameSet;

    @Getter
    private final BuilderInstantiator<String> builderInstantiator;

    @Getter
    private final Formattable formattable;

    @Getter
    private final AbstractList<String> abstractList;
}
