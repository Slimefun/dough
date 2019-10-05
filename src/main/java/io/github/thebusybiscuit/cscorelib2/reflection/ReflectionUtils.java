package io.github.thebusybiscuit.cscorelib2.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;

public final class ReflectionUtils {

	private ReflectionUtils() {}
	
	private static final Map<Class<?>, Class<?>> conversion = new HashMap<>();
	private static String currentVersion;

	static {
		conversion.put(Byte.class, Byte.TYPE);
	    conversion.put(Short.class, Short.TYPE);
	    conversion.put(Integer.class, Integer.TYPE);
	    conversion.put(Long.class, Long.TYPE);
	    conversion.put(Character.class, Character.TYPE);
	    conversion.put(Float.class, Float.TYPE);
	    conversion.put(Double.class, Double.TYPE);
	    conversion.put(Boolean.class, Boolean.TYPE);
	}

	/**
	 * Returns a certain Method in the specified Class
	 *
	 * @param  c The Class in which the Method is in
	 * @param  method The Method you are looking for
	 * @return      The found Method
	 */
	public static Method getMethod(Class<?> c, String method) {
		for (Method m : c.getMethods()) {
			if (m.getName().equals(method)) return m;
		}
        return null;
	}

	/**
	 * Returns the Method with certain Parameters
	 *
	 * @param  c The Class in which the Method is in
	 * @param  method The Method you are looking for
	 * @param  paramTypes The Types of the Parameters
	 * @return      The found Method
	 */
	public static Method getMethod(Class<?> c, String method, Class<?>... paramTypes) {
	    Class<?>[] t = toPrimitiveTypeArray(paramTypes);
	    for (Method m : c.getMethods()) {
	      Class<?>[] types = toPrimitiveTypeArray(m.getParameterTypes());
	      if ((m.getName().equals(method)) && (equalsTypeArray(types, t)))
	        return m;
	    }
	    return null;
	}

	/**
	 * Returns the Field of a Class
	 *
	 * @param  c The Class conating this Field
	 * @param  field The name of the Field you are looking for
	 * @return      The found Field
	 *
	 * @throws Exception If there's an issue getting a field
	 */
	public static Field getField(Class<?> c, String field) throws Exception {
		return c.getDeclaredField(field);
	}

	/**
	 * Modifies a Field in an Object
	 *
	 * @param <T> The type of the specified field
	 * @param  object The Object containing the Field
	 * @param  c The Class in which we are looking for this field
	 * @param  field The Name of that Field
	 * @param  value The Value for that Field
	 * @throws Exception If there was an issue setting a field value
	 */
	public static <T> void setFieldValue(T object, Class<?> c, String field, Object value) throws Exception {
		Field f = getField(c, field);
		f.setAccessible(true);
		f.set(object, value);
	}

	/**
	 * Modifies a Field in an Object
	 *
	 * @param <T> The type of the specified field
	 * @param  object The Object containing the Field
	 * @param  field The Name of that Field
	 * @param  value The Value for that Field
	 * @throws Exception If there was an issue setting a field value
	 */
	public static <T> void setFieldValue(T object, String field, Object value) throws Exception {
		Field f = getField(object.getClass(), field);
		f.setAccessible(true);
		f.set(object, value);
	}

	/**
	 * Returns the Value of a Field in an Object
	 *
	 * @param  object The Object containing the Field
	 * @param  field The Name of that Field
	 * @throws Exception If an issue occurred while getting the field value
	 * @return      The Value of a Field
	 */
	public static Object getFieldValue(Object object, String field) throws Exception {
	    Field f = getField(object.getClass(), field);
	    f.setAccessible(true);
	    return f.get(object);
	}

	/**
	 * Converts the Classes to a Primitive Type Array
	 * in order to be used as paramaters
	 *
	 * @param  classes The Types you want to convert
	 * @return      An Array of primitive Types
	 */
	public static Class<?>[] toPrimitiveTypeArray(Class<?>... classes) {
		int a = classes != null ? classes.length : 0;
	    Class<?>[] types = new Class[a];
	    for (int i = 0; i < a; i++) {
	    	types[i] = conversion.containsKey(classes[i]) ? conversion.get(classes[i]): classes[i];
	    }
	    return types;
	}

	/**
	 * Converts the Classes of the specified Objects
	 *  to a Primitive Type Array
	 * in order to be used as paramaters
	 *
	 * @param  objects The Types you want to convert
	 * @return      An Array of primitive Types
	 */
	public static Class<?>[] toPrimitiveTypeArray(Object... objects) {
	    int a = objects != null ? objects.length : 0;
	    Class<?>[] types = new Class[a];
	    for (int i = 0; i < a; i++)
	    	types[i] = conversion.containsKey(objects[i].getClass()) ? conversion.get(objects[i].getClass()): objects[i].getClass();
	    return types;
	}

	/**
	 * Returns the Constructor of a Class with the specified Parameters
	 *
	 * @param  c The Class containing the Constructor
	 * @param  paramTypes The Parameters for that Constructor
	 * @return      The Constructor for that Class
	 */
	public static Constructor<?> getConstructor(Class<?> c, Class<?>... paramTypes) {
	    Class<?>[] t = toPrimitiveTypeArray(paramTypes);
	    for (Constructor<?> con : c.getConstructors()) {
	    	Class<?>[] types = toPrimitiveTypeArray(con.getParameterTypes());
	    	if (equalsTypeArray(types, t)) return con;
	    }
	    return null;
	}

	/**
	 * Returns an NMS Class inside a Class
	 *
	 * @param  name The Name of the Class your Inner class is located in
	 * @param  subname The Name of the inner Class you are looking for
	 * @throws Exception If there was an issue getting the specified inner class.
	 * @return      The Class in your specified Class
	 */
	public static Class<?> getInnerNMSClass(String name, String subname) throws Exception {
		return getNMSClass(name + "$" + subname);
	}

	/**
	 * Returns an NMS Class via Reflection
	 *
	 * @param  name The Name of the Class you are looking for
	 * @throws Exception If there was an issue getting the specified class
	 * @return      The Class in that Package
	 */
	public static Class<?> getNMSClass(String name) throws Exception {
	    return Class.forName(new StringBuilder().append("net.minecraft.server.").append(getVersion()).append(".").append(name).toString());
	}

	/**
	 * Returns an OBC Class inside a Class
	 *
	 * @param  name The Name of the Class your Inner class is located in
	 * @param  subname The Name of the inner Class you are looking for
	 * @throws Exception If there was an issue getting the specified inner class.
	 * @return      The Class in your specified Class
	 */
	public static Class<?> getInnerOBCClass(String name, String subname) throws Exception {
		return getOBCClass(name + "$" + subname);
	}

	/**
	 * Returns an OBC Class via Reflection
	 *
	 * @param  name The Name of the Class you are looking for
	 * @throws Exception If there was an issue getting the specified class.
	 * @return      The Class in that Package
	 */
	public static Class<?> getOBCClass(String name) throws Exception {
	    return Class.forName(new StringBuilder().append("org.bukkit.craftbukkit.").append(getVersion()).append(".").append(name).toString());
	}

	/**
	 * Returns the formatted Server Version usable for Reflection
	 *
	 * @return      The formatted Server Version
	 */
	public static String getVersion() {
		if (currentVersion == null) {
			currentVersion = Bukkit.getServer().getClass().getPackage().getName().substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf('.') + 1);
		}
		
		return currentVersion;
	}
	
	public static boolean isVersion(String... prefixes) {
		String version = getVersion();
		
		for (String prefix: prefixes) {
			if (version.startsWith(prefix)) return true;
		}
		
		return false;
	}

	/**
	 * Compares multiple Type Arrays
	 *
	 * @param  a The first Array for comparison
	 * @param  o All following Arrays you want to compare
	 * @return      Whether they equal each other
	 */
	private static boolean equalsTypeArray(Class<?>[] a, Class<?>... o) {
	    if (a.length != o.length)
	      return false;
	    for (int i = 0; i < a.length; i++)
	      if ((!a[i].equals(o[i])) && (!a[i].isAssignableFrom(o[i])))
	        return false;
	    return true;
	}

	/**
	 * Returns all Enum Constants in an Enum
	 *
	 * @param  c The Enum you are targeting
	 * @return      An ArrayList of all Enum Constants in that Enum
	 */
	public static List<?> getEnumConstants(Class<?> c) {
		return Arrays.asList(c.getEnumConstants());
	}

	/**
	 * Returns a specific Enum Constant in an Enum
	 *
	 * @param  c The Enum you are targeting
	 * @param  name The Name of the Constant you are targeting
	 * @return      The found Enum Constant
	 */
	public static Object getEnumConstant(Class<?> c, String name) {
		for (Object o: c.getEnumConstants()) {
			if (o.toString().equals(name)) return o;
		}
		return null;
	}
}
