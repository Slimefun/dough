package io.github.thebusybiscuit.cscorelib2.data;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import java.util.Optional;

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
     * @see #getByte(PersistentDataHolder, NamespacedKey)
     */
    public static Optional<Byte> getByteOpt(PersistentDataHolder holder, NamespacedKey key) {
        return Optional.of(getByte(holder, key));
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
     * @see #getByte(PersistentDataHolder, NamespacedKey, byte)
     */
    public static Optional<Byte> getByteOpt(PersistentDataHolder holder, NamespacedKey key, byte defaultVal) {
        return Optional.of(getByte(holder, key, defaultVal));
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
     * @see #getShort(PersistentDataHolder, NamespacedKey)
     */
    public static Optional<Short> getShortOpt(PersistentDataHolder holder, NamespacedKey key) {
        return Optional.of(getShort(holder, key));
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
     * @see #getShort(PersistentDataHolder, NamespacedKey, short)
     */
    public static Optional<Short> getShortOpt(PersistentDataHolder holder, NamespacedKey key, short defaultVal) {
        return Optional.of(getShort(holder, key, defaultVal));
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
     * @see #getShort(PersistentDataHolder, NamespacedKey)
     */
    public static Optional<Integer> getIntOpt(PersistentDataHolder holder, NamespacedKey key) {
        return Optional.of(getInt(holder, key));
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
     * @see #getInt(PersistentDataHolder, NamespacedKey, int)
     */
    public static Optional<Integer> getIntOpt(PersistentDataHolder holder, NamespacedKey key, int defaultVal) {
        return Optional.of(getInt(holder, key, defaultVal));
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
     * @see #getLong(PersistentDataHolder, NamespacedKey)
     */
    public static Optional<Long> getLongOpt(PersistentDataHolder holder, NamespacedKey key) {
        return Optional.of(getLong(holder, key));
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
     * @see #getLong(PersistentDataHolder, NamespacedKey, long)
     */
    public static Optional<Long> getLongOpt(PersistentDataHolder holder, NamespacedKey key, long defaultVal) {
        return Optional.of(getLong(holder, key, defaultVal));
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
     * @see #getFloat(PersistentDataHolder, NamespacedKey)
     */
    public static Optional<Float> getFloatOpt(PersistentDataHolder holder, NamespacedKey key) {
        return Optional.of(getFloat(holder, key));
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
     * @see #getFloat(PersistentDataHolder, NamespacedKey, float)
     */
    public static Optional<Float> getFloatOpt(PersistentDataHolder holder, NamespacedKey key, float defaultVal) {
        return Optional.of(getFloat(holder, key, defaultVal));
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
     * @see #getDouble(PersistentDataHolder, NamespacedKey)
     */
    public static Optional<Double> getDoubleOpt(PersistentDataHolder holder, NamespacedKey key) {
        return Optional.of(getDouble(holder, key));
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
     * @see #getDouble(PersistentDataHolder, NamespacedKey, double)
     */
    public static Optional<Double> getDoubleOpt(PersistentDataHolder holder, NamespacedKey key, double defaultVal) {
        return Optional.of(getDouble(holder, key, defaultVal));
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
     * @see #getString(PersistentDataHolder, NamespacedKey)
     */
    public static Optional<String> getStringOpt(PersistentDataHolder holder, NamespacedKey key) {
        return Optional.of(getString(holder, key));
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
     * @see #getString(PersistentDataHolder, NamespacedKey, String)
     */
    public static Optional<String> getStringOpt(PersistentDataHolder holder, NamespacedKey key, String defaultVal) {
        return Optional.of(getString(holder, key, defaultVal));
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
     * @see #getByteArray(PersistentDataHolder, NamespacedKey)
     */
    public static Optional<byte[]> getByteArrayOpt(PersistentDataHolder holder, NamespacedKey key) {
        return Optional.of(getByteArray(holder, key));
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
     * @see #getByteArray(PersistentDataHolder, NamespacedKey, byte...)
     */
    public static Optional<byte[]> getByteArrayOpt(PersistentDataHolder holder, NamespacedKey key, byte... defaultVal) {
        return Optional.of(getByteArray(holder, key, defaultVal));
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
     * @see #getIntArray(PersistentDataHolder, NamespacedKey)
     */
    public static Optional<int[]> getIntArrayOpt(PersistentDataHolder holder, NamespacedKey key) {
        return Optional.of(getIntArray(holder, key));
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
     * @see #getIntArray(PersistentDataHolder, NamespacedKey, int...)
     */
    public static Optional<int[]> getIntArrayOpt(PersistentDataHolder holder, NamespacedKey key, int... defaultVal) {
        return Optional.of(getIntArray(holder, key, defaultVal));
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
     * @see #getLongArray(PersistentDataHolder, NamespacedKey)
     */
    public static Optional<long[]> getLongArrayOpt(PersistentDataHolder holder, NamespacedKey key) {
        return Optional.of(getLongArray(holder, key));
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
     * @see #getLongArray(PersistentDataHolder, NamespacedKey, long...)
     */
    public static Optional<long[]> getLongArrayOpt(PersistentDataHolder holder, NamespacedKey key, long... defaultVal) {
        return Optional.of(getLongArray(holder, key, defaultVal));
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
     * @see #getContainer(PersistentDataHolder, NamespacedKey)
     */
    public static Optional<PersistentDataContainer> getContainerOpt(PersistentDataHolder holder, NamespacedKey key) {
        return Optional.of(getContainer(holder, key));
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
    public static PersistentDataContainer getContainer(PersistentDataHolder holder, NamespacedKey key,
                                                       PersistentDataContainer defaultVal) {
        return holder.getPersistentDataContainer().getOrDefault(key, PersistentDataType.TAG_CONTAINER, defaultVal);
    }

    /**
     * @see #getContainer(PersistentDataHolder, NamespacedKey, PersistentDataContainer defaultVal)
     */
    public static Optional<PersistentDataContainer> getContainerOpt(PersistentDataHolder holder, NamespacedKey key,
                                                   PersistentDataContainer defaultVal) {
        return Optional.of(getContainer(holder, key, defaultVal));
    }
}