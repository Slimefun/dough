package io.github.bakedlibs.dough.inventory.factory;

import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import javax.annotation.ParametersAreNonnullByDefault;

class MockExceptionHandler extends Handler {

    private final AtomicReference<Throwable> ref;

    @ParametersAreNonnullByDefault
    MockExceptionHandler(AtomicReference<Throwable> ref) {
        this.ref = ref;
    }

    @Override
    public void publish(LogRecord record) {
        if (record.getLevel() == Level.SEVERE) {
            ref.set(record.getThrown());
        }
    }

    @Override
    public void flush() {}

    @Override
    public void close() throws SecurityException {}

}
