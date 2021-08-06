package io.github.bakedlibs.dough.collections;

import org.junit.jupiter.api.Assertions;

public class PairUtils {

    static <P, S> void assertValues(Pair<P, S> pair, P expectedPrimary, S expectedSecondary) {
        Assertions.assertSame(expectedPrimary, pair.getFirstValue());
        Assertions.assertSame(expectedSecondary, pair.getSecondValue());
    }

    static <P, S> void assertValues(OptionalPair<P, S> pair, P expectedPrimary, S expectedSecondary) {
        if (expectedPrimary == null) {
            Assertions.assertFalse(pair.getFirstValue().isPresent());
        } else {
            Assertions.assertEquals(expectedPrimary, pair.getFirstValue().orElse(null));
        }
        if (expectedSecondary == null) {
            Assertions.assertFalse(pair.getSecondValue().isPresent());
        } else {
            Assertions.assertEquals(expectedSecondary, pair.getSecondValue().orElse(null));
        }
    }

}
