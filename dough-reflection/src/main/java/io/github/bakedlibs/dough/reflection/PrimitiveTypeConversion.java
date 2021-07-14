package io.github.bakedlibs.dough.reflection;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

final class PrimitiveTypeConversion {

    private static final Map<Class<?>, Class<?>> primitiveTypes = new HashMap<>();

    static {
        primitiveTypes.put(Byte.class, Byte.TYPE);
        primitiveTypes.put(Short.class, Short.TYPE);
        primitiveTypes.put(Integer.class, Integer.TYPE);
        primitiveTypes.put(Long.class, Long.TYPE);
        primitiveTypes.put(Character.class, Character.TYPE);
        primitiveTypes.put(Float.class, Float.TYPE);
        primitiveTypes.put(Double.class, Double.TYPE);
        primitiveTypes.put(Boolean.class, Boolean.TYPE);
    }

    private PrimitiveTypeConversion() {}

    static @Nullable Class<?> convert(@Nonnull Class<?> boxedClassType) {
        return primitiveTypes.get(boxedClassType);
    }

    static @Nonnull Class<?> convertIfNecessary(@Nonnull Class<?> boxedClassType) {
        Class<?> primitiveType = convert(boxedClassType);
        return primitiveType != null ? primitiveType : boxedClassType;
    }

}
