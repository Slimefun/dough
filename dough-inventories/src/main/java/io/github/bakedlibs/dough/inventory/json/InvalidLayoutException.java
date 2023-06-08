package io.github.bakedlibs.dough.inventory.json;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;

import io.github.bakedlibs.dough.inventory.MenuLayout;

/**
 * An {@link InvalidLayoutException} is thrown when the {@link MenuLayout}
 * was not successfully read from a {@link JsonObject}.
 * 
 * @author TheBusyBiscuit
 *
 */
class InvalidLayoutException extends Exception {

    private static final long serialVersionUID = -1891678815608214476L;

    InvalidLayoutException(@Nonnull String message) {
        super(message);
    }

    InvalidLayoutException(@Nonnull Exception exception) {
        super(exception);
    }

}
