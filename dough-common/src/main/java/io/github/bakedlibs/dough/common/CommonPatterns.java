package io.github.bakedlibs.dough.common;

import java.util.regex.Pattern;

/**
 * This class is created for common-use patterns used in things such as {@link String#split(String)}. <br>
 * Every time something like {@link String#split(String)} is called it will compile a {@link Pattern},
 * for code that is called often this can be a massive performance loss.
 * This class solves that, one compile but many uses!
 * 
 * @author Walshy
 * @author TheBusyBiscuit
 * 
 */
public final class CommonPatterns {

    private CommonPatterns() {}

    /**
     * {@link Pattern} for {@literal ':'}
     */
    public static final Pattern COLON = Pattern.compile(":");

    /**
     * {@link Pattern} for {@literal ';'}
     */
    public static final Pattern SEMICOLON = Pattern.compile(";");

    /**
     * {@link Pattern} for {@literal '#'}
     */
    public static final Pattern HASH = Pattern.compile("#");

    /**
     * {@link Pattern} for {@literal ','}
     */
    public static final Pattern COMMA = Pattern.compile(",");

    /**
     * {@link Pattern} for {@literal '-'}
     */
    public static final Pattern DASH = Pattern.compile("-");

    /**
     * {@link Pattern} for {@literal '_'}
     */
    public static final Pattern UNDERSCORE = Pattern.compile("_");

    /**
     * {@link Pattern} for {@literal '/'}
     */
    public static final Pattern SLASH = Pattern.compile("\\/");

    /**
     * {@link Pattern} for {@literal '[A-Za-z "_]+'}
     */
    public static final Pattern ASCII = Pattern.compile("[A-Za-z \"_]+");

    /**
     * {@link Pattern} for {@literal '[A-Fa-f0-9]+'}
     */
    public static final Pattern HEXADECIMAL = Pattern.compile("[A-Fa-f0-9]+");

    /**
     * {@link Pattern} for {@literal '[0-9]+'}
     */
    public static final Pattern NUMERIC = Pattern.compile("[0-9]+");

    /**
     * {@link Pattern} for {@literal '[,.]'}
     */
    public static final Pattern NUMBER_SEPARATOR = Pattern.compile("[,.]");

    /**
     * {@link Pattern} for {@literal 'minecraft:[a-z_]+'}
     */
    public static final Pattern MINECRAFT_NAMESPACEDKEY = Pattern.compile("minecraft:[a-z_]+");
}
