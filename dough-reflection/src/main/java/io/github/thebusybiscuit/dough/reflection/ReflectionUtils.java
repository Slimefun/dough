package io.github.thebusybiscuit.dough.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;

/**
 * This class provides some useful static methods to perform reflection.
 * 
 * @author TheBusyBiscuit
 *
 */
@SuppressWarnings("java:S3011")
public final class ReflectionUtils {

    private ReflectionUtils() {}

    private static int majorVersion;
    private static String currentVersion;

    static {
        currentVersion =  Bukkit.getServer().getClass().getPackage().getName()
            .substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf('.') + 1);
        // Turn v1_17_R1 -> 17
        majorVersion = Integer.parseInt(currentVersion.replace("v1_", "").replace("_R1", ""));
    }


    /**
     * Returns a certain Method in the specified Class
     *
     * @param c
     *            The Class in which the Method is in
     * @param method
     *            The Method you are looking for
     * @return The found Method
     */
    @Nullable
    public static Method getMethod(@Nonnull Class<?> c, @Nonnull String method) {
        for (Method m : c.getMethods()) {
            if (m.getName().equals(method))
                return m;
        }

        return null;
    }

    /**
     * Returns the Method with certain Parameters
     *
     * @param c
     *            The Class in which the Method is in
     * @param method
     *            The Method you are looking for
     * @param paramTypes
     *            The Types of the Parameters
     * @return The found Method
     */
    @Nullable
    public static Method getMethod(@Nonnull Class<?> c, @Nonnull String method, Class<?>... paramTypes) {
        Class<?>[] t = toPrimitiveTypeArray(paramTypes);

        for (Method m : c.getMethods()) {
            Class<?>[] types = toPrimitiveTypeArray(m.getParameterTypes());

            if ((m.getName().equals(method)) && (equalsTypeArray(types, t))) {
                return m;
            }
        }

        return null;
    }

    /**
     * Returns the Field of a Class
     *
     * @param c
     *            The Class conating this Field
     * @param field
     *            The name of the Field you are looking for
     * @return The found Field
     *
     * @throws NoSuchFieldException
     *             If the field could not be found.
     */
    @Nonnull
    public static Field getField(@Nonnull Class<?> c, @Nonnull String field) throws NoSuchFieldException {
        return c.getDeclaredField(field);
    }

    /**
     * Modifies a Field in an Object
     *
     * @param <T>
     *            The type of the specified field
     * @param object
     *            The Object containing the Field
     * @param c
     *            The Class in which we are looking for this field
     * @param field
     *            The Name of that Field
     * @param value
     *            The Value for that Field
     * 
     * @throws NoSuchFieldException
     *             If the field could not be found.
     * @throws IllegalAccessException
     *             If the field could not be modified.
     */
    public static <T> void setFieldValue(@Nonnull T object, @Nonnull Class<?> c, @Nonnull String field, @Nullable Object value) throws NoSuchFieldException, IllegalAccessException {
        Field f = getField(c, field);
        f.setAccessible(true);
        f.set(object, value);
    }

    /**
     * Modifies a Field in an Object
     *
     * @param <T>
     *            The type of the specified field
     * @param object
     *            The Object containing the Field
     * @param field
     *            The Name of that Field
     * @param value
     *            The Value for that Field
     * 
     * @throws NoSuchFieldException
     *             If the field could not be found.
     * @throws IllegalAccessException
     *             If the field could not be modified.
     */
    public static <T> void setFieldValue(@Nonnull T object, @Nonnull String field, @Nullable Object value) throws NoSuchFieldException, IllegalAccessException {
        Field f = getField(object.getClass(), field);
        f.setAccessible(true);
        f.set(object, value);
    }

    /**
     * Returns the Value of a Field in an Object
     *
     * @param object
     *            The Object containing the Field
     * @param field
     *            The Name of that Field
     * 
     * @throws NoSuchFieldException
     *             If the field could not be found.
     * @throws IllegalAccessException
     *             If the field could not be queried.
     * 
     * @return The Value of a Field
     */
    @Nullable
    public static <T> T getFieldValue(@Nonnull Object object, @Nonnull Class<T> fieldType, @Nonnull String field) throws NoSuchFieldException, IllegalAccessException {
        Field f = getField(object.getClass(), field);
        f.setAccessible(true);
        return fieldType.cast(f.get(object));
    }

    /**
     * Converts the Classes to a Primitive Type Array
     * in order to be used as paramaters
     *
     * @param classes
     *            The Types you want to convert
     * 
     * @return An Array of primitive Types
     */
    @Nonnull
    public static Class<?>[] toPrimitiveTypeArray(@Nonnull Class<?>[] classes) {
        int size = classes.length;

        Class<?>[] types = new Class[size];

        for (int i = 0; i < size; i++) {
            types[i] = PrimitiveTypeConversion.convertIfNecessary(classes[i]);
        }

        return types;
    }

    /**
     * Converts the Classes of the specified Objects
     * to a Primitive Type Array
     * in order to be used as paramaters
     *
     * @param objects
     *            The Types you want to convert
     * 
     * @return An Array of primitive Types
     */
    @Nonnull
    public static Class<?>[] toPrimitiveTypeArray(@Nonnull Object[] objects) {
        int size = objects.length;

        Class<?>[] types = new Class[size];

        for (int i = 0; i < size; i++) {
            types[i] = PrimitiveTypeConversion.convertIfNecessary(objects[i].getClass());
        }

        return types;
    }

    /**
     * Returns the Constructor of a Class with the specified Parameters
     *
     * @param <T>
     *            The Type argument for the class of this constructor
     * @param c
     *            The Class containing the Constructor
     * @param paramTypes
     *            The Parameters for that Constructor
     * @return The Constructor for that Class
     */
    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> Constructor<T> getConstructor(@Nonnull Class<T> c, Class<?>... paramTypes) {
        Class<?>[] t = toPrimitiveTypeArray(paramTypes);

        for (Constructor<?> constructor : c.getConstructors()) {
            Class<?>[] types = toPrimitiveTypeArray(constructor.getParameterTypes());

            if (equalsTypeArray(types, t)) {
                return (Constructor<T>) constructor;
            }
        }

        return null;
    }

    /**
     * Returns an NMS Class inside a Class
     *
     * @param name
     *            The Name of the Class your Inner class is located in
     * @param subname
     *            The Name of the inner Class you are looking for
     * 
     * @throws ClassNotFoundException
     *             If the class could not be found.
     * 
     * @return The Class in your specified Class
     */
    @Nonnull
    public static Class<?> getInnerNMSClass(@Nonnull String name, @Nonnull String subname) throws ClassNotFoundException {
        return getNMSClass(name + '$' + subname);
    }

    /**
     * Returns a `net.minecraft` class via Reflection.
     *
     * @param name The class name of which to fetch
     * @return The `net.minecraft` class.
     * @throws ClassNotFoundException If the class does not exist
     */
    public static Class<?> getNetMinecraftClass(@Nonnull String name) throws ClassNotFoundException {
        return Class.forName("net.minecraft." + (majorVersion <= 16 ? getVersion() + '.' : "") + name);
    }

    /**
     * Returns an NMS Class via Reflection
     *
     * @param name
     *            The Name of the Class you are looking for
     * 
     * @throws ClassNotFoundException
     *             If the class could not be found.
     * 
     * @return The Class in that Package
     */
    @Nonnull
    public static Class<?> getNMSClass(@Nonnull String name) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + (majorVersion <= 16 ? getVersion() + '.' : "") + name);
    }

    /**
     * Returns an OBC Class inside a Class
     *
     * @param name
     *            The Name of the Class your Inner class is located in
     * @param subname
     *            The Name of the inner Class you are looking for
     * 
     * @throws ClassNotFoundException
     *             If the class could not be found.
     * 
     * @return The Class in your specified Class
     */
    @Nonnull
    public static Class<?> getInnerOBCClass(@Nonnull String name, @Nonnull String subname) throws ClassNotFoundException {
        return getOBCClass(name + '$' + subname);
    }

    /**
     * Returns an OBC Class via Reflection
     *
     * @param name
     *            The Name of the Class you are looking for
     * 
     * @throws ClassNotFoundException
     *             If the class could not be found.
     * 
     * @return The Class in that Package
     */
    @Nonnull
    public static Class<?> getOBCClass(@Nonnull String name) throws ClassNotFoundException {
        return Class.forName("org.bukkit.craftbukkit." + getVersion() + '.' + name);
    }

    /**
     * Returns the formatted Server Version usable for Reflection
     *
     * @return The formatted Server Version
     */
    @Nonnull
    private static String getVersion() {
        if (currentVersion == null) {
            currentVersion = Bukkit.getServer().getClass().getPackage().getName().substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf('.') + 1);
        }

        return currentVersion;
    }

    /**
     * Return the Minecraft Major Version (15, 16, 17).
     */
    public static int getMajorVersion() {
        return majorVersion;
    }

    /**
     * This checks if the current Server instance is a mock (MockBukkit) and
     * whether we are in a Unit Test environment.
     * 
     * @return Whether the current Server instance is a mock
     */
    public static boolean isUnitTestEnvironment() {
        return getVersion().equals("mockbukkit");
    }

    /**
     * Compares multiple Type Arrays
     *
     * @param a
     *            The first Array for comparison
     * @param o
     *            All following Arrays you want to compare
     * @return Whether they equal each other
     */
    private static boolean equalsTypeArray(@Nonnull Class<?>[] a, Class<?>... o) {
        if (a.length != o.length) {
            return false;
        }

        for (int i = 0; i < a.length; i++) {
            if ((!a[i].equals(o[i])) && (!a[i].isAssignableFrom(o[i]))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns all Enum Constants in an Enum
     *
     * @param <T>
     *            The Type argument of the enum we are querying
     * @param c
     *            The Enum you are targeting
     * @return An ArrayList of all Enum Constants in that Enum
     */
    @Nonnull
    public static <T> List<T> getEnumConstants(@Nonnull Class<T> c) {
        return Arrays.asList(c.getEnumConstants());
    }

    /**
     * Returns a specific Enum Constant in an Enum
     *
     * @param <T>
     *            The Type argument of the enum we are querying
     * @param c
     *            The Enum you are targeting
     * @param name
     *            The Name of the Constant you are targeting
     * @return The found Enum Constant
     */
    @Nullable
    public static <T> T getEnumConstant(@Nonnull Class<T> c, @Nonnull String name) {
        for (T field : c.getEnumConstants()) {
            if (field.toString().equals(name)) {
                return field;
            }
        }

        return null;
    }
}
