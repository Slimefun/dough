package io.github.thebusybiscuit.cscorelib2.chat.json;
/**
 * 
 * @deprecated Honestly, Kyori-adventure is infinite times better than this, please use that.
 *
 */
@Deprecated
public enum ChatComponentColor {

    WHITE,
    BLACK,
    YELLOW,
    GOLD,
    AQUA,
    DARK_AQUA,
    BLUE,
    DARK_BLUE,
    LIGHT_PURPLE,
    DARK_PURPLE,
    RED,
    DARK_RED,
    GREEN,
    DARK_GREEN,
    GRAY,
    DARK_GRAY;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

}
