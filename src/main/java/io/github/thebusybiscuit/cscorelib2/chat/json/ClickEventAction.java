package io.github.thebusybiscuit.cscorelib2.chat.json;

import java.util.Locale;
/**
 * 
 * @deprecated Honestly, Kyori-adventure is infinite times better than this, please use that.
 *
 */
@Deprecated
public enum ClickEventAction {

    SUGGEST_COMMAND,
    RUN_COMMAND,
    OPEN_URL,
    COPY_TO_CLIPBOARD;

    @Override
    public String toString() {
        return name().toLowerCase(Locale.ROOT);
    }

}
