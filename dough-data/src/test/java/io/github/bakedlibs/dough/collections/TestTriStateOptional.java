package io.github.bakedlibs.dough.collections;

import io.github.bakedlibs.dough.data.TriStateOptional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestTriStateOptional {

    @Test
    void testEmpty() {
        TriStateOptional<Object> empty = TriStateOptional.empty();
        TriStateOptional<Object> nullObject = TriStateOptional.ofNullable(null);
        Assertions.assertTrue(nullObject.isEmpty());

        Assertions.assertTrue(empty.isEmpty());
        Assertions.assertTrue(empty.isComputed());
        Assertions.assertFalse(empty.isPresent());
        Assertions.assertThrows(IllegalAccessError.class, empty::get, "This Optional has no value! Check .isPresent() first!");
        Assertions.assertNull(empty.getOrElse(null));
        Assertions.assertFalse(empty.getAsOptional().isPresent());
    }

    @Test
    void testComputed() {
        TriStateOptional<Object> computed = TriStateOptional.createNew();
        Assertions.assertFalse(computed.isEmpty());
        Assertions.assertFalse(computed.isComputed());
        Assertions.assertFalse(computed.isPresent());
        Assertions.assertThrows(IllegalAccessError.class, computed::get, "This Optional has no value! Check .isPresent() first!");
        Assertions.assertThrows(IllegalStateException.class, () -> computed.getOrElse(null), "This Optional has not yet been computed!");
        Assertions.assertThrows(IllegalStateException.class, computed::getAsOptional, "This Optional has not yet been computed!");
    }

    @Test
    void testPresent() {
        Object object = new Object();
        TriStateOptional<Object> present = TriStateOptional.of(object);
        Assertions.assertFalse(present.isEmpty());
        Assertions.assertTrue(present.isComputed());
        Assertions.assertSame(object, present.get());
        Assertions.assertSame(object, present.getOrElse(null));
        Assertions.assertTrue(present.getAsOptional().isPresent());
        Assertions.assertSame(object, present.getAsOptional().orElse(null));
        TriStateOptional<Object> presentFromNullable = TriStateOptional.ofNullable(object);
        Assertions.assertFalse(presentFromNullable.isEmpty());
        Assertions.assertTrue(presentFromNullable.isComputed());
        Assertions.assertSame(object, presentFromNullable.get());
        Assertions.assertSame(object, presentFromNullable.getOrElse(null));
        Assertions.assertTrue(presentFromNullable.getAsOptional().isPresent());
        Assertions.assertSame(object, presentFromNullable.getAsOptional().orElse(null));
    }

    @Test
    void testCompute() {
        Object computedValue = new Object();
        TriStateOptional<Object> computed = TriStateOptional.createNew();
        Assertions.assertFalse(computed.isComputed());
        computed.compute(computedValue);
        Assertions.assertTrue(computed.isComputed());
        Assertions.assertEquals(computedValue, computed.get());
        Assertions.assertEquals(computedValue, computed.getOrElse(null));
        Assertions.assertEquals(computedValue, computed.getAsOptional().orElse(null));
        Assertions.assertThrows(IllegalStateException.class, () -> computed.compute((Object) null), "This Optional has already been computed.");
        Object otherComputedValue = new Object();
        TriStateOptional<Object> empty = TriStateOptional.createNew();
        empty.compute(() -> otherComputedValue);
        Assertions.assertEquals(otherComputedValue, empty.get());
        TriStateOptional<Object> empty2 = TriStateOptional.createNew();
        empty2.compute((Object) null);
        Assertions.assertTrue(empty2.isComputed());
        Assertions.assertTrue(empty2.isEmpty());
    }

    @Test
    void testIfPresent() {
        TriStateOptional<Object> optional = TriStateOptional.of(new Object());
        Assertions.assertThrows(RuntimeException.class, () -> optional.ifPresent(x -> {
            throw new RuntimeException();
        }));
        TriStateOptional<Object> empty = TriStateOptional.empty();
        empty.ifPresent(x -> Assertions.fail("Optional is not present!"));
    }

}
