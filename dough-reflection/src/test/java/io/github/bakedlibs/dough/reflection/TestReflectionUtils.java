package io.github.bakedlibs.dough.reflection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class TestReflectionUtils {

    @Test
    void testGetMethodByName() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        MockMethods obj = new MockMethods();
        Method method = ReflectionUtils.getMethod(obj.getClass(), "returnFortyTwo");

        assertNotNull(method);
        assertEquals(42, method.invoke(obj));
    }

    @Test
    void testGetMethodInvalidName() {
        MockMethods obj = new MockMethods();
        Method method = ReflectionUtils.getMethod(obj.getClass(), "notExisting");

        assertNull(method);
    }

    @Test
    void testGetMethodWithParams() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        MockMethods obj = new MockMethods();
        Method method = ReflectionUtils.getMethod(obj.getClass(), "returnArg", String.class);

        assertNotNull(method);

        String arg = "Hello World";
        assertEquals(arg, method.invoke(obj, arg));
    }

    @Test
    void testGetMethodWithPrimitiveParams() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        MockMethods obj = new MockMethods();
        Method method = ReflectionUtils.getMethod(obj.getClass(), "squaredPrimitiveInt", Integer.class);

        assertNotNull(method);

        int arg = 1000;
        assertEquals(arg * arg, method.invoke(obj, arg));
    }

    @Test
    void testGetMethodWithInvalidParams() {
        MockMethods obj = new MockMethods();
        Method method = ReflectionUtils.getMethod(obj.getClass(), "returnInteger", String.class);

        assertNull(method);
    }

    @Test
    void testGetFieldByName() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        MockFields obj = new MockFields();
        Field field = ReflectionUtils.getField(obj.getClass(), "name");

        assertNotNull(field);
        assertEquals("Joe", field.get(obj));
    }

    @Test
    void testSetFieldValue() throws NoSuchFieldException, IllegalAccessException {
        MockFields obj = new MockFields();

        assertEquals("Joe", obj.name);

        ReflectionUtils.setFieldValue(obj, "name", "Cool Joe");

        assertEquals("Cool Joe", obj.name);
    }

    @Test
    void testGetFieldValue() throws NoSuchFieldException, IllegalAccessException {
        MockFields obj = new MockFields();

        assertEquals("Joe", ReflectionUtils.getFieldValue(obj, String.class, "name"));
    }

    @Test
    void testGetConstructor() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<MockConstructors> constructor = ReflectionUtils.getConstructor(MockConstructors.class);

        MockConstructors obj = constructor.newInstance();

        assertNotNull(obj);
        assertEquals("Mike", obj.name);
    }

    @Test
    void testGetConstructorWithParams() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<MockConstructors> constructor = ReflectionUtils.getConstructor(MockConstructors.class, String.class);

        MockConstructors obj = constructor.newInstance("Cool Mike");

        assertNotNull(obj);
        assertEquals("Cool Mike", obj.name);
    }

    @Test
    void testGetConstructorWithInvalidParams() {
        Constructor<MockConstructors> constructor = ReflectionUtils.getConstructor(MockConstructors.class, Float.class, Byte.class);

        assertNull(constructor);
    }

    @Test
    void testGetEnumConstants() {
        List<MockEnum> constants = ReflectionUtils.getEnumConstants(MockEnum.class);

        assertEquals(4, constants.size());
    }

    @ParameterizedTest
    @EnumSource(value = MockEnum.class)
    void testGetEnumConstant(MockEnum enumValue) {
        MockEnum constant = ReflectionUtils.getEnumConstant(MockEnum.class, enumValue.name());

        assertNotNull(constant);

        assertEquals(enumValue.name(), constant.name());
        assertEquals(enumValue, constant);
    }

    @Test
    void testGetNotExistingEnumConstant() {
        MockEnum constant = ReflectionUtils.getEnumConstant(MockEnum.class, "CHOCOLATE");

        assertNull(constant);
    }

}
