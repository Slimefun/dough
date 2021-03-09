package io.github.thebusybiscuit.cscorelib2.data;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.apache.commons.lang.Validate;

import javax.annotation.Nonnull;
import java.util.UUID;

public final class PersistentUUIDDataType implements PersistentDataType<int[], UUID> {

    public static final PersistentDataType<int[], UUID> TYPE = new PersistentUUIDDataType();

    private PersistentUUIDDataType() {}

    @Nonnull
    @Override
    public Class<int[]> getPrimitiveType() {
        return int[].class;
    }

    @Nonnull
    @Override
    public Class<UUID> getComplexType() {
        return UUID.class;
    }

    @Nonnull
    @Override
    public int[] toPrimitive(@Nonnull UUID complex, @Nonnull PersistentDataAdapterContext context) {
        return toIntArray(complex);
    }

    @Nonnull
    @Override
    public UUID fromPrimitive(@Nonnull int[] primitive, @Nonnull PersistentDataAdapterContext context) {
        return fromIntArray(primitive);
    }
    
    @Nonnull
    public static UUID fromIntArray(@Nonnull int[] ints) {
        Validate.notNull(ints, "The provided integer array cannot be null!");
        Validate.isTrue(ints.length == 4, "The integer array must have a length of 4.");

        return new UUID(ints[0] << 32L | ints[1] & 0xFFFFFFFFL, ints[2] << 32L | ints[3] & 0xFFFFFFFFL);
    }

    @Nonnull
    public static int[] toIntArray(@Nonnull UUID uuid) {
        Validate.notNull(ints, "The provided uuid cannot be null!");

        long mostSig = uuid.getMostSignificantBits();
        long leastSig = uuid.getLeastSignificantBits();
        return new int[] { (int) (mostSig >> 32L), (int) mostSig, (int) (leastSig >> 32L), (int) leastSig };
    }
}
