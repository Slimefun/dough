package io.github.bakedlibs.dough.data.persistent;

import java.util.UUID;

import javax.annotation.Nonnull;

import org.apache.commons.lang.Validate;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

/**
 * A {@link PersistentDataType} for {@link UUID}s which uses an
 * {@link Integer} array for storage purposes.
 * 
 * @author Sfiguz7
 * @author Walshy
 *
 */
public final class PersistentUUIDDataType implements PersistentDataType<int[], UUID> {

    public static final PersistentDataType<int[], UUID> TYPE = new PersistentUUIDDataType();

    private PersistentUUIDDataType() {}

    @Override
    public @Nonnull Class<int[]> getPrimitiveType() {
        return int[].class;
    }

    @Override
    public @Nonnull Class<UUID> getComplexType() {
        return UUID.class;
    }

    @Override
    public @Nonnull int[] toPrimitive(@Nonnull UUID complex, @Nonnull PersistentDataAdapterContext context) {
        return toIntArray(complex);
    }

    @Override
    public @Nonnull UUID fromPrimitive(@Nonnull int[] primitive, @Nonnull PersistentDataAdapterContext context) {
        return fromIntArray(primitive);
    }

    public static @Nonnull UUID fromIntArray(@Nonnull int[] ints) {
        Validate.notNull(ints, "The provided integer array cannot be null!");
        Validate.isTrue(ints.length == 4, "The integer array must have a length of 4.");

        return new UUID(ints[0] | ints[1] & 0xFFFFFFFFL, ints[2] | ints[3] & 0xFFFFFFFFL);
    }

    public static @Nonnull int[] toIntArray(@Nonnull UUID uuid) {
        Validate.notNull(uuid, "The provided uuid cannot be null!");

        long mostSig = uuid.getMostSignificantBits();
        long leastSig = uuid.getLeastSignificantBits();
        return new int[] { (int) (mostSig >> 32L), (int) mostSig, (int) (leastSig >> 32L), (int) leastSig };
    }
}
