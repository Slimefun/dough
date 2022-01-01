package io.github.bakedlibs.dough.reflection;

import javax.annotation.Nullable;

class MockConstructors {

    public final String name;

    public MockConstructors() {
        this.name = "Mike";
    }

    public MockConstructors(@Nullable String name) {
        this.name = name;
    }

}
