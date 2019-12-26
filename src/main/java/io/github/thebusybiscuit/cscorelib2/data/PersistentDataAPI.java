package io.github.thebusybiscuit.cscorelib2.data;

import java.util.Optional;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

/**
 * This is a utility class that allows you to modify data for any Objects
 * that implement {@link PersistentDataHolder}.
 * 
 * @author Walshy
 * @author TheBusyBiscuit
 *
 */
public final class PersistentDataAPI {

    private PersistentDataAPI() {
        // This is a utility class, we don't want any instances.
    }

    /////////////////////////////////////
    // Setters
    /////////////////////////////////////
    public static void setByte(PersistentDataHolder holder, NamespacedKey key, byte value) {
        holder.getPersistentDataContainer().set(key, PersistentDataType.BYTE, value);
    }

    public static void setShort(PersistentDataHolder holder, NamespacedKey key, short value) {
        holder.getPersistentDataContainer().set(key, PersistentDataType.SHORT, value);
    }

    public static void setInt(PersistentDataHolder holder, NamespacedKey key, int value) {
        holder.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, value);
    }

    public static void setLong(PersistentDataHolder holder, NamespacedKey key, long value) {
        holder.getPersistentDataContainer().set(key, PersistentDataType.LONG, value);
    }

    public static void setFloat(PersistentDataHolder holder, NamespacedKey key, float value) {
        holder.getPersistentDataContainer().set(key, PersistentDataType.FLOAT, value);
    }

    public static void setDouble(PersistentDataHolder holder, NamespacedKey key, double value) {
        holder.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, value);
    }

    public static void setString(PersistentDataHolder holder, NamespacedKey key, String value) {
        holder.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
    }

    public static void setByteArray(PersistentDataHolder holder, NamespacedKey key, byte... value) {
        holder.getPersistentDataContainer().set(key, PersistentDataType.BYTE_ARRAY, value);
    }

    public static void setIntArray(PersistentDataHolder holder, NamespacedKey key, int... value) {
        holder.getPersistentDataContainer().set(key, PersistentDataType.INTEGER_ARRAY, value);
    }

    public static void setLongArray(PersistentDataHolder holder, NamespacedKey key, long... value) {
        holder.getPersistentDataContainer().set(key, PersistentDataType.LONG_ARRAY, value);
    }

    public static void setContainer(PersistentDataHolder holder, NamespacedKey key, PersistentDataContainer value) {
        holder.getPersistentDataContainer().set(key, PersistentDataType.TAG_CONTAINER, value);
    }

    /////////////////////////////////////
    // Has
    /////////////////////////////////////

    /**
     * Checks if the specified {@link PersistentDataHolder} has a byte with the specified key.
     *
     * @param holder
     *            The {@link PersistentDataHolder} to check
     * @param key
     *            The key to check for
     * @return {@code true} if the holder has a byte with the specified key.
     */
    public static boolean hasByte(PersistentDataHolder holder, NamespacedKey key) {
        return holder.getPersistentDataContainer().has(key, PersistentDataType.BYTE);
    }

    /**
     * Checks if the specified {@link PersistentDataHolder} has a short with the specified key.
     *
     * @param holder
     *            The {@link PersistentDataHolder} to check
     * @param key
     *            The key to check for
     * @return {@code true} if the holder has a short with the specified key.
     */
    public static boolean hasShort(PersistentDataHolder holder, NamespacedKey key) {
        return holder.getPersistentDataContainer().has(key, PersistentDataType.SHORT);
    }

    /**
     * Checks if the specified {@link PersistentDataHolder} has an integer with the specified key.
     *
     * @param holder
     *            The {@link PersistentDataHolder} to check
     * @param key
     *            The key to check for
     * @return {@code true} if the holder has an integer with the specified key.
     */
    public static boolean hasInt(PersistentDataHolder holder, NamespacedKey key) {
        return holder.getPersistentDataContainer().has(key, PersistentDataType.INTEGER);
    }

    /**
     * Checks if the specified {@link PersistentDataHolder} has a long with the specified key.
     *
     * @param holder
     *            The {@link PersistentDataHolder} to check
     * @param key
     *            The key to check for
     * @return {@code true} if the holder has a long with the specified key.
     */
    public static boolean hasLong(PersistentDataHolder holder, NamespacedKey key) {
        return holder.getPersistentDataContainer().has(key, PersistentDataType.LONG);
    }

    /**
     * Checks if the specified {@link PersistentDataHolder} has a float with the specified key.
     *
     * @param holder
     *            The {@link PersistentDataHolder} to check
     * @param key
     *            The key to check for
     * @return {@code true} if the holder has a float with the specified key.
     */
    public static boolean hasFloat(PersistentDataHolder holder, NamespacedKey key) {
        return holder.getPersistentDataContainer().has(key, PersistentDataType.FLOAT);
    }

    /**
     * Checks if the specified {@link PersistentDataHolder} has a double with the specified key.
     *
     * @param holder
     *            The {@link PersistentDataHolder} to check
     * @param key
     *            The key to check for
     * @return {@code true} if the holder has a double with the specified key.
     */
    public static boolean hasDouble(PersistentDataHolder holder, NamespacedKey key) {
        return holder.getPersistentDataContainer().has(key, PersistentDataType.DOUBLE);
    }

    /**
     * Checks if the specified {@link PersistentDataHolder} has a String with the specified key.
     *
     * @param holder
     *            The {@link PersistentDataHolder} to check
     * @param key
     *            The key to check for
     * @return {@code true} if the holder has a String with the specified key.
     */
    public static boolean hasString(PersistentDataHolder holder, NamespacedKey key) {
        return holder.getPersistentDataContainer().has(key, PersistentDataType.STRING);
    }

    /**
     * Checks if the specified {@link PersistentDataHolder} has a byte array with the specified key.
     *
     * @param holder
     *            The {@link PersistentDataHolder} to check
     * @param key
     *            The key to check for
     * @return {@code true} if the holder has a byte array with the specified key.
     */
    public static boolean hasByteArray(PersistentDataHolder holder, NamespacedKey key) {
        return holder.getPersistentDataContainer().has(key, PersistentDataType.BYTE_ARRAY);
    }

    /**
     * Checks if the specified {@link PersistentDataHolder} has an integer array with the specified key.
     *
     * @param holder
     *            The {@link PersistentDataHolder} to check
     * @param key
     *            The key to check for
     * @return {@code true} if the holder has an integer array with the specified key.
     */
    public static boolean hasIntArray(PersistentDataHolder holder, NamespacedKey key) {
        return holder.getPersistentDataContainer().has(key, PersistentDataType.INTEGER_ARRAY);
    }

    /**
     * Checks if the specified {@link PersistentDataHolder} has a long array with the specified key.
     *
     * @param holder
     *            The {@link PersistentDataHolder} to check
     * @param key
     *            The key to check for
     * @return {@code true} if the holder has a long array with the specified key.
     */
    public static boolean hasLongArray(PersistentDataHolder holder, NamespacedKey key) {
        return holder.getPersistentDataContainer().has(key, PersistentDataType.LONG_ARRAY);
    }

    /**
     * Checks if the specified {@link PersistentDataHolder} has a {@link PersistentDataContainer} with the specified
     * key.
     *
     * @param holder
     *            The {@link PersistentDataHolder} to check
     * @param key
     *            The key to check for
     * @return {@code true} if the holder has a {@link PersistentDataContainer} with the specified key.
     */
    public static boolean hasContainer(PersistentDataHolder holder, NamespacedKey key) {
        return holder.getPersistentDataContainer().has(key, PersistentDataType.TAG_CONTAINER);
    }

    /////////////////////////////////////
    // Getters
    /////////////////////////////////////

    /**
     * Get a byte value in a {@link PersistentDataContainer}, if the key doesn't exist it returns -1.
     *
     * @param holder
     *            The {@link PersistentDataHolder} to retrieve the data from
     * @param key
     *            The key of the data to retrieve
     * @return The byte associated with this key or -1 if it doesn't exist
     */
    public static byte getByte(PersistentDataHolder holder, NamespacedKey key) {
        return holder.getPersistentDataContainer().getOrDefault(key, PersistentDataType.BYTE, (byte) -1);
    }

    /**
     * This method returns an {@link Optional} describing the {@link Byte} found under the given key.
     * An empty {@link Optional} will be returned if no value has been found.
     * 
     * @see PersistentDataAPI#getByte(PersistentDataHolder, NamespacedKey)
     * 
     * @param holder
     *            The {@link PersistentDataHolder} to retrieve the data from
     * @param key
     *            The key of the data to retrieve
     * @return An Optional describing the result
     */
    public static Optional<Byte> getOptionalByte(PersistentDataHolder holder, NamespacedKey key) {
    	return !hasByte(holder, key) ? Optional.empty(): Optional.of(getByte(holder, key));
    }

    /**
     * Get a byte value in a {@link PersistentDataContainer} or the default value passed if no key exists.
     *
     * @param holder
     *            The {@link PersistentDataHolder} to retrieve the data from
     * @param key
     *            The key of the data to retrieve
     * @param defaultVal
     *            The default value to use if no key is found
     * @return The byte associated with this key or the default value if it doesn't exist
     */
    public static byte getByte(PersistentDataHolder holder, NamespacedKey key, byte defaultVal) {
        return holder.getPersistentDataContainer().getOrDefault(key, PersistentDataType.BYTE, defaultVal);
    }

    /**
     * Get a short value in a {@link PersistentDataContainer}, if the key doesn't exist it returns -1.
     *
     * @param holder
     *            The {@link PersistentDataHolder} to retrieve the data from
     * @param key
     *            The key of the data to retrieve
     * @return The short associated with this key or -1 if it doesn't exist
     */
    public static short getShort(PersistentDataHolder holder, NamespacedKey key) {
        return holder.getPersistentDataContainer().getOrDefault(key, PersistentDataType.SHORT, (short) -1);
    }

    /**
     * This method returns an {@link Optional} describing the {@link Short} found under the given key.
     * An empty {@link Optional} will be returned if no value has been found.
     * 
     * @see PersistentDataAPI#getShort(PersistentDataHolder, NamespacedKey)
     * 
     * @param holder
     *            The {@link PersistentDataHolder} to retrieve the data from
     * @param key
     *            The key of the data to retrieve
     * @return An Optional describing the result
     */
    public static Optional<Short> getOptionalShort(PersistentDataHolder holder, NamespacedKey key) {
        return !hasShort(holder, key) ? Optional.empty(): Optional.of(getShort(holder, key));
    }

    /**
     * Get a short value in a {@link PersistentDataContainer} or the default value passed if no key exists.
     *
     * @param holder
     *            The {@link PersistentDataHolder} to retrieve the data from
     * @param key
     *            The key of the data to retrieve
     * @param defaultVal
     *            The default value to use if no key is found
     * @return The short associated with this key or the default value if it doesn't exist
     */
    public static short getShort(PersistentDataHolder holder, NamespacedKey key, short defaultVal) {
        return holder.getPersistentDataContainer().getOrDefault(key, PersistentDataType.SHORT, defaultVal);
    }

    /**
     * Get an integer value in a {@link PersistentDataContainer}, if the key doesn't exist it returns -1.
     *
     * @param holder
     *            The {@link PersistentDataHolder} to retrieve the data from
     * @param key
     *            The key of the data to retrieve
     * @return The integer associated with this key or -1 if it doesn't exist
     */
    public static int getInt(PersistentDataHolder holder, NamespacedKey key) {
        return holder.getPersistentDataContainer().getOrDefault(key, PersistentDataType.INTEGER, -1);
    }

    /**
     * This method returns an {@link Optional} describing the {@link Integer} found under the given key.
     * An empty {@link Optional} will be returned if no value has been found.
     * 
     * @see PersistentDataAPI#getInt(PersistentDataHolder, NamespacedKey)
     * 
     * @param holder
     *            The {@link PersistentDataHolder} to retrieve the data from
     * @param key
     *            The key of the data to retrieve
     * @return An Optional describing the result
     */
    public static Optional<Integer> getOptionalInt(PersistentDataHolder holder, NamespacedKey key) {
        return !hasInt(holder, key) ? Optional.empty(): Optional.of(getInt(holder, key));
    }

    /**
     * Get an integer value in a {@link PersistentDataContainer} or the default value passed if no key exists.
     *
     * @param holder
     *            The {@link PersistentDataHolder} to retrieve the data from
     * @param key
     *            The key of the data to retrieve
     * @param defaultVal
     *            The default value to use if no key is found
     * @return The integer associated with this key or the default value if it doesn't exist
     */
    public static int getInt(PersistentDataHolder holder, NamespacedKey key, int defaultVal) {
        return holder.getPersistentDataContainer().getOrDefault(key, PersistentDataType.INTEGER, defaultVal);
    }

    /**
     * Get a long value in a {@link PersistentDataContainer}, if the key doesn't exist it returns -1.
     *
     * @param holder
     *            The {@link PersistentDataHolder} to retrieve the data from
     * @param key
     *            The key of the data to retrieve
     * @return The long associated with this key or -1 if it doesn't exist
     */
    public static long getLong(PersistentDataHolder holder, NamespacedKey key) {
        return holder.getPersistentDataContainer().getOrDefault(key, PersistentDataType.LONG, (long) -1);
    }

    /**
     * This method returns an {@link Optional} describing the {@link Long} found under the given key.
     * An empty {@link Optional} will be returned if no value has been found.
     * 
     * @see PersistentDataAPI#getLong(PersistentDataHolder, NamespacedKey)
     * 
     * @param holder
     *            The {@link PersistentDataHolder} to retrieve the data from
     * @param key
     *            The key of the data to retrieve
     * @return An Optional describing the result
     */
    public static Optional<Long> getOptionalLong(PersistentDataHolder holder, NamespacedKey key) {
        return !hasLong(holder, key) ? Optional.empty(): Optional.of(getLong(holder, key));
    }

    /**
     * Get a long value in a {@link PersistentDataContainer} or the default value passed if no key exists.
     *
     * @param holder
     *            The {@link PersistentDataHolder} to retrieve the data from
     * @param key
     *            The key of the data to retrieve
     * @param defaultVal
     *            The default value to use if no key is found
     * @return The long associated with this key or the default value if it doesn't exist
     */
    public static long getLong(PersistentDataHolder holder, NamespacedKey key, long defaultVal) {
        return holder.getPersistentDataContainer().getOrDefault(key, PersistentDataType.LONG, defaultVal);
    }

    /**
     * Get a float value in a {@link PersistentDataContainer}, if the key doesn't exist it returns -1.
     *
     * @param holder
     *            The {@link PersistentDataHolder} to retrieve the data from
     * @param key
     *            The key of the data to retrieve
     * @return The float associated with this key or -1 if it doesn't exist
     */
    public static float getFloat(PersistentDataHolder holder, NamespacedKey key) {
        return holder.getPersistentDataContainer().getOrDefault(key, PersistentDataType.FLOAT, (float) -1);
    }

    /**
     * This method returns an {@link Optional} describing the {@link Float} found under the given key.
     * An empty {@link Optional} will be returned if no value has been found.
     * 
     * @see PersistentDataAPI#getFloat(PersistentDataHolder, NamespacedKey)
     * 
     * @param holder
     *            The {@link PersistentDataHolder} to retrieve the data from
     * @param key
     *            The key of the data to retrieve
     * @return An Optional describing the result
     */
    public static Optional<Float> getOptionalFloat(PersistentDataHolder holder, NamespacedKey key) {
        return !hasFloat(holder, key) ? Optional.empty(): Optional.of(getFloat(holder, key));
    }

    /**
     * Get a float value in a {@link PersistentDataContainer} or the default value passed if no key exists.
     *
     * @param holder
     *            The {@link PersistentDataHolder} to retrieve the data from
     * @param key
     *            The key of the data to retrieve
     * @param defaultVal
     *            The default value to use if no key is found
     * @param defaultVal
     *            The default value to use if no key is found
     * @return The float associated with this key or the default value if it doesn't exist
     */
    public static float getFloat(PersistentDataHolder holder, NamespacedKey key, float defaultVal) {
        return holder.getPersistentDataContainer().getOrDefault(key, PersistentDataType.FLOAT, defaultVal);
    }

    /**
     * Get a double value in a {@link PersistentDataContainer}, if the key doesn't exist it returns -1.
     *
     * @param holder
     *            The {@link PersistentDataHolder} to retrieve the data from
     * @param key
     *            The key of the data to retrieve
     * @return The double associated with this key or -1 if it doesn't exist
     */
    public static double getDouble(PersistentDataHolder holder, NamespacedKey key) {
        return holder.getPersistentDataContainer().getOrDefault(key, PersistentDataType.DOUBLE, (double) -1);
    }

    /**
     * This method returns an {@link Optional} describing the {@link Double} found under the given key.
     * An empty {@link Optional} will be returned if no value has been found.
     * 
     * @see PersistentDataAPI#getDouble(PersistentDataHolder, NamespacedKey)
     * 
     * @param holder
     *            The {@link PersistentDataHolder} to retrieve the data from
     * @param key
     *            The key of the data to retrieve
     * @return An Optional describing the result
     */
    public static Optional<Double> getOptionalDouble(PersistentDataHolder holder, NamespacedKey key) {
        return !hasDouble(holder, key) ? Optional.empty(): Optional.of(getDouble(holder, key));
    }

    /**
     * Get a double value in a {@link PersistentDataContainer} or the default value passed if no key exists.
     *
     * @param holder
     *            The {@link PersistentDataHolder} to retrieve the data from
     * @param key
     *            The key of the data to retrieve
     * @param defaultVal
     *            The default value to use if no key is found
     * @return The double associated with this key or the default value if it doesn't exist
     */
    public static double getDouble(PersistentDataHolder holder, NamespacedKey key, double defaultVal) {
        return holder.getPersistentDataContainer().getOrDefault(key, PersistentDataType.DOUBLE, defaultVal);
    }

    /**
     * Get a String value in a {@link PersistentDataContainer}, if the key doesn't exist it returns null.
     *
     * @param holder
     *            The {@link PersistentDataHolder} to retrieve the data from
     * @param key
     *            The key of the data to retrieve
     * @return The String associated with this key or null if it doesn't exist
     */
    public static String getString(PersistentDataHolder holder, NamespacedKey key) {
        return holder.getPersistentDataContainer().get(key, PersistentDataType.STRING);
    }

    /**
     * This method returns an {@link Optional} describing the {@link String} found under the given key.
     * An empty {@link Optional} will be returned if no value has been found.
     * 
     * @see PersistentDataAPI#getString(PersistentDataHolder, NamespacedKey)
     * 
     * @param holder
     *            The {@link PersistentDataHolder} to retrieve the data from
     * @param key
     *            The key of the data to retrieve
     * @return An Optional describing the result
     */
    public static Optional<String> getOptionalString(PersistentDataHolder holder, NamespacedKey key) {
        return Optional.ofNullable(getString(holder, key));
    }

    /**
     * Get a String value in a {@link PersistentDataContainer} or the default value passed if no key exists.
     *
     * @param holder
     *            The {@link PersistentDataHolder} to retrieve the data from
     * @param key
     *            The key of the data to retrieve
     * @param defaultVal
     *            The default value to use if no key is found
     * @return The String associated with this key or the default value if it doesn't exist
     */
    public static String getString(PersistentDataHolder holder, NamespacedKey key, String defaultVal) {
        return holder.getPersistentDataContainer().getOrDefault(key, PersistentDataType.STRING, defaultVal);
    }

    /**
     * Get a byte array in a {@link PersistentDataContainer}, if the key doesn't exist it returns null.
     *
     * @param holder
     *            The {@link PersistentDataHolder} to retrieve the data from
     * @param key
     *            The key of the data to retrieve
     * @return The byte array associated with this key or null if it doesn't exist
     */
    public static byte[] getByteArray(PersistentDataHolder holder, NamespacedKey key) {
        return holder.getPersistentDataContainer().get(key, PersistentDataType.BYTE_ARRAY);
    }

    /**
     * This method returns an {@link Optional} describing the Byte Array found under the given key.
     * An empty {@link Optional} will be returned if no value has been found.
     * 
     * @see PersistentDataAPI#getByteArray(PersistentDataHolder, NamespacedKey)
     * 
     * @param holder
     *            The {@link PersistentDataHolder} to retrieve the data from
     * @param key
     *            The key of the data to retrieve
     * @return An Optional describing the result
     */
    public static Optional<byte[]> getOptionalByteArray(PersistentDataHolder holder, NamespacedKey key) {
        return Optional.ofNullable(getByteArray(holder, key));
    }

    /**
     * Get a byte array in a {@link PersistentDataContainer} or the default value passed if no key exists.
     *
     * @param holder
     *            The {@link PersistentDataHolder} to retrieve the data from
     * @param key
     *            The key of the data to retrieve
     * @param defaultVal
     *            The default value to use if no key is found
     * @return The byte array associated with this key or the default value if it doesn't exist
     */
    public static byte[] getByteArray(PersistentDataHolder holder, NamespacedKey key, byte... defaultVal) {
        return holder.getPersistentDataContainer().getOrDefault(key, PersistentDataType.BYTE_ARRAY, defaultVal);
    }

    /**
     * Get a integer array in a {@link PersistentDataContainer}, if the key doesn't exist it returns null.
     *
     * @param holder
     *            The {@link PersistentDataHolder} to retrieve the data from
     * @param key
     *            The key of the data to retrieve
     * @return The integer array associated with this key or null if it doesn't exist
     */
    public static int[] getIntArray(PersistentDataHolder holder, NamespacedKey key) {
        return holder.getPersistentDataContainer().get(key, PersistentDataType.INTEGER_ARRAY);
    }

    /**
     * This method returns an {@link Optional} describing the Integer Array found under the given key.
     * An empty {@link Optional} will be returned if no value has been found.
     * 
     * @see PersistentDataAPI#getIntArray(PersistentDataHolder, NamespacedKey)
     * 
     * @param holder
     *            The {@link PersistentDataHolder} to retrieve the data from
     * @param key
     *            The key of the data to retrieve
     * @return An Optional describing the result
     */
    public static Optional<int[]> getOptionalIntArray(PersistentDataHolder holder, NamespacedKey key) {
        return Optional.ofNullable(getIntArray(holder, key));
    }

    /**
     * Get a byte array in a {@link PersistentDataContainer} or the default value passed if no key exists.
     *
     * @param holder
     *            The {@link PersistentDataHolder} to retrieve the data from
     * @param key
     *            The key of the data to retrieve
     * @param defaultVal
     *            The default value to use if no key is found
     * @return The byte associated with this key or the default value if it doesn't exist
     */
    public static int[] getIntArray(PersistentDataHolder holder, NamespacedKey key, int... defaultVal) {
        return holder.getPersistentDataContainer().getOrDefault(key, PersistentDataType.INTEGER_ARRAY, defaultVal);
    }

    /**
     * Get a long array in a {@link PersistentDataContainer}, if the key doesn't exist it returns null.
     *
     * @param holder
     *            The {@link PersistentDataHolder} to retrieve the data from
     * @param key
     *            The key of the data to retrieve
     * @return The long array associated with this key or null if it doesn't exist
     */
    public static long[] getLongArray(PersistentDataHolder holder, NamespacedKey key) {
        return holder.getPersistentDataContainer().get(key, PersistentDataType.LONG_ARRAY);
    }

    /**
     * This method returns an {@link Optional} describing the Long Array found under the given key.
     * An empty {@link Optional} will be returned if no value has been found.
     * 
     * @see PersistentDataAPI#getLongArray(PersistentDataHolder, NamespacedKey)
     * 
     * @param holder
     *            The {@link PersistentDataHolder} to retrieve the data from
     * @param key
     *            The key of the data to retrieve
     * @return An Optional describing the result
     */
    public static Optional<long[]> getOptionalLongArray(PersistentDataHolder holder, NamespacedKey key) {
        return Optional.ofNullable(getLongArray(holder, key));
    }

    /**
     * Get a long array in a {@link PersistentDataContainer} or the default value passed if no key exists.
     *
     * @param holder
     *            The {@link PersistentDataHolder} to retrieve the data from
     * @param key
     *            The key of the data to retrieve
     * @param defaultVal
     *            The default value to use if no key is found
     * @return The long array associated with this key or the default value if it doesn't exist
     */
    public static long[] getLongArray(PersistentDataHolder holder, NamespacedKey key, long... defaultVal) {
        return holder.getPersistentDataContainer().getOrDefault(key, PersistentDataType.LONG_ARRAY, defaultVal);
    }

    /**
     * Get a nested {@link PersistentDataContainer}, if the key doesn't exist it returns null.
     *
     * @param holder
     *            The {@link PersistentDataHolder} to retrieve the data from
     * @param key
     *            The key of the data to retrieve
     * @return The byte associated with this key or null if it doesn't exist
     */
    public static PersistentDataContainer getContainer(PersistentDataHolder holder, NamespacedKey key) {
        return holder.getPersistentDataContainer().get(key, PersistentDataType.TAG_CONTAINER);
    }

    /**
     * This method returns an {@link Optional} describing the {@link PersistentDataContainer} found under the given key.
     * An empty {@link Optional} will be returned if no value has been found.
     * 
     * @see PersistentDataAPI#getContainer(PersistentDataHolder, NamespacedKey)
     * 
     * @param holder
     *            The {@link PersistentDataHolder} to retrieve the data from
     * @param key
     *            The key of the data to retrieve
     * @return An Optional describing the result
     */
    public static Optional<PersistentDataContainer> getOptionalContainer(PersistentDataHolder holder, NamespacedKey key) {
        return Optional.ofNullable(getContainer(holder, key));
    }

    /**
     * Get a nested {@link PersistentDataContainer} or the default value passed if no key exists.
     *
     * @param holder
     *            The {@link PersistentDataHolder} to retrieve the data from
     * @param key
     *            The key of the data to retrieve
     * @param defaultVal
     *            The default value to use if no key is found
     * @return The byte associated with this key or the default value if it doesn't exist
     */
    public static PersistentDataContainer getContainer(PersistentDataHolder holder, NamespacedKey key, PersistentDataContainer defaultVal) {
        return holder.getPersistentDataContainer().getOrDefault(key, PersistentDataType.TAG_CONTAINER, defaultVal);
    }
    
    /**
     * This removes the data stored with the given key on the given data holder
     * 
     * @param holder
     *            The {@link PersistentDataHolder} to remove the data from
     * @param key
     *            The key of the data to remove
     */
    public static void remove(PersistentDataHolder holder, NamespacedKey key) {
    	holder.getPersistentDataContainer().remove(key);
    }
}