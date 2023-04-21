package io.shiromi.saml.types;

import io.shiromi.saml.tools.MathUtils;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Collection;

public class GenericListType extends ListType<GenericType> {
    public GenericListType(@Range(from = 0, to = MathUtils.INT_MAX) int initialCapacity) {
        super(initialCapacity);
    }

    public GenericListType(@NotNull @Flow(sourceIsContainer = true, targetIsContainer = true)Collection<GenericType> collection) {
        super(collection);
    }

    public GenericListType() {
        super();
    }
}
