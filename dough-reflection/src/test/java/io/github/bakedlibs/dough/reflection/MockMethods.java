package io.github.bakedlibs.dough.reflection;

import javax.annotation.Nonnull;

class MockMethods {

    public int returnFortyTwo() {
        return 42;
    }

    public @Nonnull String returnArg(@Nonnull String arg) {
        return arg;
    }

    public int squaredPrimitiveInt(int arg) {
        return arg * arg;
    }

    public int returnInteger(int arg) {
        return arg;
    }

}
