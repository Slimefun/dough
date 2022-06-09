package io.github.bakedlibs.dough.collections;

import org.junit.jupiter.api.Assertions;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;

public class CollectionTestUtils {

    static <T> void assertHaveEqualElements(@Nonnull Collection<T> c1, @Nonnull Collection<T> c2) {
        Assertions.assertEquals(c1.size(), c2.size());
        Assertions.assertTrue(c1.containsAll(c2));
    }

    static <K, V> void assertHaveEqualElements(@Nonnull Map<K, V> m1, @Nonnull Map<K, V> m2) {
        Assertions.assertEquals(m1.size(), m2.size());
        for (Map.Entry<K, V> entry : m1.entrySet()) {
            Assertions.assertEquals(m2.get(entry.getKey()), entry.getValue());
        }
    }

    static <T> void assertHaveClonedElements(@Nonnull Collection<T> c1, @Nonnull Collection<T> c2) {
        Assertions.assertEquals(c1.size(), c2.size());
        for (T t1 : c1) {
            for (T t2 : c2) {
                // If they are the same, it did not clone and thus we fail.
                Assertions.assertNotSame(t1, t2);
                if (t1.equals(t2)) {
                    // If they are equal, the element has been cloned, thus, we break and check the next element.
                    break;
                }
            }
        }
    }

    static <K, V> void assertHaveClonedElements(@Nonnull Map<K, V> m1, @Nonnull Map<K, V> m2) {
        Assertions.assertEquals(m1.size(), m2.size());
        for (Map.Entry<K, V> entry : m1.entrySet()) {
            V otherValue = m2.get(entry.getKey());
            // If they are the same, it did not clone and thus we fail.
            Assertions.assertNotSame(entry.getValue(), otherValue);
            if (entry.getValue().equals(otherValue)) {
                // If they are equal, the element has been cloned, thus, we break and check the next element.
                break;
            }
        }
    }

}
