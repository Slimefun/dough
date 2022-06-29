/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.bakedlibs.dough.common;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * <p>This class is modified from Apache's commons-lang3 package.
 * Removed default messages.</p>
 *
 * <p>This class assists in validating arguments. The validation methods are
 * based along the following principles:
 * <ul>
 *   <li>An invalid {@code null} argument causes a {@link NullPointerException}.</li>
 *   <li>A non-{@code null} argument causes an {@link IllegalArgumentException}.</li>
 *   <li>An invalid index into an array/collection/map/string causes an {@link IndexOutOfBoundsException}.</li>
 * </ul>
 *
 * <p>All exceptions messages are
 * <a href="http://docs.oracle.com/javase/1.5.0/docs/api/java/util/Formatter.html#syntax">format strings</a>
 * as defined by the Java platform. For example:</p>
 *
 * <pre>
 * Validate.isTrue(i &gt; 0, "The value must be greater than zero: %d", i);
 * Validate.notNull(surname, "The surname must not be %s", null);
 * </pre>
 *
 * <p>#ThreadSafe#</p>
 * @see java.lang.String#format(String, Object...)
 */
@SuppressWarnings("unused")
public class Validate {
    /**
     * Constructor. This class should not normally be instantiated.
     */
    private Validate() {
        throw new UnsupportedOperationException("Utility class");
    }

    // isTrue
    //---------------------------------------------------------------------------------

    /**
     * <p>Validate that the argument condition is {@code true}; otherwise
     * throwing an exception with the specified message. This method is useful when
     * validating according to an arbitrary boolean expression, such as validating a
     * primitive number or using your own custom validation expression.</p>
     *
     * <pre>Validate.isTrue(i &gt; 0.0, "The value must be greater than zero: &#37;d", i);</pre>
     *
     * <p>For performance reasons, the long value is passed as a separate parameter and
     * appended to the exception message only in the case of an error.</p>
     *
     * @param expression  the boolean expression to check
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param value  the value to append to the message when invalid
     * @throws IllegalArgumentException if expression is {@code false}
     * @see #isTrue(boolean, String, double)
     * @see #isTrue(boolean, String, Object...)
     */
    public static void isTrue(final boolean expression, final String message, final long value) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(message, Long.valueOf(value)));
        }
    }

    /**
     * <p>Validate that the argument condition is {@code true}; otherwise
     * throwing an exception with the specified message. This method is useful when
     * validating according to an arbitrary boolean expression, such as validating a
     * primitive number or using your own custom validation expression.</p>
     *
     * <pre>Validate.isTrue(d &gt; 0.0, "The value must be greater than zero: &#37;s", d);</pre>
     *
     * <p>For performance reasons, the double value is passed as a separate parameter and
     * appended to the exception message only in the case of an error.</p>
     *
     * @param expression  the boolean expression to check
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param value  the value to append to the message when invalid
     * @throws IllegalArgumentException if expression is {@code false}
     * @see #isTrue(boolean, String, long)
     * @see #isTrue(boolean, String, Object...)
     */
    public static void isTrue(final boolean expression, final String message, final double value) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(message, Double.valueOf(value)));
        }
    }

    /**
     * <p>Validate that the argument condition is {@code true}; otherwise
     * throwing an exception with the specified message. This method is useful when
     * validating according to an arbitrary boolean expression, such as validating a
     * primitive number or using your own custom validation expression.</p>
     *
     * <pre>
     * Validate.isTrue(i &gt;= min &amp;&amp; i &lt;= max, "The value must be between &#37;d and &#37;d", min, max);
     * Validate.isTrue(myObject.isOk(), "The object is not okay");</pre>
     *
     * @param expression  the boolean expression to check
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @throws IllegalArgumentException if expression is {@code false}
     * @see #isTrue(boolean, String, long)
     * @see #isTrue(boolean, String, double)
     */
    public static void isTrue(final boolean expression, final String message, final Object... values) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    // notNull
    //---------------------------------------------------------------------------------

    /**
     * <p>Validate that the specified argument is not {@code null};
     * otherwise throwing an exception with the specified message.
     *
     * <pre>Validate.notNull(myObject, "The object must not be null");</pre>
     *
     * @param <T> the object type
     * @param object  the object to check
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message
     * @return the validated object (never {@code null} for method chaining)
     * @throws NullPointerException if the object is {@code null}
     */
    public static <T> T notNull(final T object, final String message, final Object... values) {
        return Objects.requireNonNull(object, () -> String.format(message, values));
    }

    // notEmpty array
    //---------------------------------------------------------------------------------

    /**
     * <p>Validate that the specified argument array is neither {@code null}
     * nor a length of zero (no elements); otherwise throwing an exception
     * with the specified message.
     *
     * <pre>Validate.notEmpty(myArray, "The array must not be empty");</pre>
     *
     * @param <T> the array type
     * @param array  the array to check, validated not null by this method
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @return the validated array (never {@code null} method for chaining)
     * @throws NullPointerException if the array is {@code null}
     * @throws IllegalArgumentException if the array is empty
     */
    public static <T> T[] notEmpty(final T[] array, final String message, final Object... values) {
        Objects.requireNonNull(array, () -> String.format(message, values));
        if (array.length == 0) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return array;
    }

    // notEmpty collection
    //---------------------------------------------------------------------------------

    /**
     * <p>Validate that the specified argument collection is neither {@code null}
     * nor a size of zero (no elements); otherwise throwing an exception
     * with the specified message.
     *
     * <pre>Validate.notEmpty(myCollection, "The collection must not be empty");</pre>
     *
     * @param <T> the collection type
     * @param collection  the collection to check, validated not null by this method
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @return the validated collection (never {@code null} method for chaining)
     * @throws NullPointerException if the collection is {@code null}
     * @throws IllegalArgumentException if the collection is empty
     */
    public static <T extends Collection<?>> T notEmpty(final T collection, final String message, final Object... values) {
        Objects.requireNonNull(collection, () -> String.format(message, values));
        if (collection.isEmpty()) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return collection;
    }

    // notEmpty map
    //---------------------------------------------------------------------------------

    /**
     * <p>Validate that the specified argument map is neither {@code null}
     * nor a size of zero (no elements); otherwise throwing an exception
     * with the specified message.
     *
     * <pre>Validate.notEmpty(myMap, "The map must not be empty");</pre>
     *
     * @param <T> the map type
     * @param map  the map to check, validated not null by this method
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @return the validated map (never {@code null} method for chaining)
     * @throws NullPointerException if the map is {@code null}
     * @throws IllegalArgumentException if the map is empty
     */
    public static <T extends Map<?, ?>> T notEmpty(final T map, final String message, final Object... values) {
        Objects.requireNonNull(map, () -> String.format(message, values));
        if (map.isEmpty()) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return map;
    }

    // notEmpty string
    //---------------------------------------------------------------------------------

    /**
     * <p>Validate that the specified argument character sequence is
     * neither {@code null} nor a length of zero (no characters);
     * otherwise throwing an exception with the specified message.
     *
     * <pre>Validate.notEmpty(myString, "The string must not be empty");</pre>
     *
     * @param <T> the character sequence type
     * @param chars  the character sequence to check, validated not null by this method
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @return the validated character sequence (never {@code null} method for chaining)
     * @throws NullPointerException if the character sequence is {@code null}
     * @throws IllegalArgumentException if the character sequence is empty
     */
    public static <T extends CharSequence> T notEmpty(final T chars, final String message, final Object... values) {
        Objects.requireNonNull(chars, () -> String.format(message, values));
        if (chars.length() == 0) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return chars;
    }

    // notBlank string
    //---------------------------------------------------------------------------------

    /**
     * <p>Validate that the specified argument character sequence is
     * neither {@code null}, a length of zero (no characters), empty
     * nor whitespace; otherwise throwing an exception with the specified
     * message.
     *
     * <pre>Validate.notBlank(myString, "The string must not be blank");</pre>
     *
     * @param <T> the character sequence type
     * @param chars  the character sequence to check, validated not null by this method
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @return the validated character sequence (never {@code null} method for chaining)
     * @throws NullPointerException if the character sequence is {@code null}
     * @throws IllegalArgumentException if the character sequence is blank
     *
     * @since 3.0
     */
    public static <T extends CharSequence> T notBlank(final T chars, final String message, final Object... values) {
        Objects.requireNonNull(chars, () -> String.format(message, values));
        int strLen = chars.length();
        if (strLen != 0) {
            for(int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(chars.charAt(i))) {
                    return chars;
                }
            }
        }
        throw new IllegalArgumentException(String.format(message, values));
    }


    // noNullElements array
    //---------------------------------------------------------------------------------

    /**
     * <p>Validate that the specified argument array is neither
     * {@code null} nor contains any elements that are {@code null};
     * otherwise throwing an exception with the specified message.
     *
     * <pre>Validate.noNullElements(myArray, "The array contain null at position %d");</pre>
     *
     * <p>If the array is {@code null}, then the message in the exception
     * is &quot;The validated object is null&quot;.</p>
     *
     * <p>If the array has a {@code null} element, then the iteration
     * index of the invalid element is appended to the {@code values}
     * argument.</p>
     *
     * @param <T> the array type
     * @param array  the array to check, validated not null by this method
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @return the validated array (never {@code null} method for chaining)
     * @throws NullPointerException if the array is {@code null}
     * @throws IllegalArgumentException if an element is {@code null}
     */
    public static <T> T[] noNullElements(final T[] array, final String message, final Object... values) {
        notNull(array, "Array cannot be null");
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                final Object[] newValues = Arrays.copyOf(values, values.length + 1);
                newValues[values.length] = Integer.valueOf(i);
                throw new IllegalArgumentException(String.format(message, newValues));
            }
        }
        return array;
    }

    // noNullElements iterable
    //---------------------------------------------------------------------------------

    /**
     * <p>Validate that the specified argument iterable is neither
     * {@code null} nor contains any elements that are {@code null};
     * otherwise throwing an exception with the specified message.
     *
     * <pre>Validate.noNullElements(myCollection, "The collection contains null at position %d");</pre>
     *
     * <p>If the iterable is {@code null}, then the message in the exception
     * is &quot;The validated object is null&quot;.</p>
     *
     * <p>If the iterable has a {@code null} element, then the iteration
     * index of the invalid element is appended to the {@code values}
     * argument.</p>
     *
     * @param <T> the iterable type
     * @param iterable  the iterable to check, validated not null by this method
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @return the validated iterable (never {@code null} method for chaining)
     * @throws NullPointerException if the array is {@code null}
     * @throws IllegalArgumentException if an element is {@code null}
     */
    public static <T extends Iterable<?>> T noNullElements(final T iterable, final String message, final Object... values) {
        notNull(iterable, "The iterable cannot be null");
        int i = 0;
        for (final Iterator<?> it = iterable.iterator(); it.hasNext(); i++) {
            if (it.next() == null) {
                final Object[] newValues = Arrays.copyOf(values, values.length + 1);
                newValues[values.length] = Integer.valueOf(i);
                throw new IllegalArgumentException(String.format(message, newValues));
            }
        }
        return iterable;
    }

    // validIndex array
    //---------------------------------------------------------------------------------

    /**
     * <p>Validates that the index is within the bounds of the argument
     * array; otherwise throwing an exception with the specified message.</p>
     *
     * <pre>Validate.validIndex(myArray, 2, "The array index is invalid: ");</pre>
     *
     * <p>If the array is {@code null}, then the message of the exception
     * is &quot;The validated object is null&quot;.</p>
     *
     * @param <T> the array type
     * @param array  the array to check, validated not null by this method
     * @param index  the index to check
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @return the validated array (never {@code null} for method chaining)
     * @throws NullPointerException if the array is {@code null}
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public static <T> T[] validIndex(final T[] array, final int index, final String message, final Object... values) {
        notNull(array, "Array cannot be null");
        if (index < 0 || index >= array.length) {
            throw new IndexOutOfBoundsException(String.format(message, values));
        }
        return array;
    }

    // validIndex collection
    //---------------------------------------------------------------------------------

    /**
     * <p>Validates that the index is within the bounds of the argument
     * collection; otherwise throwing an exception with the specified message.</p>
     *
     * <pre>Validate.validIndex(myCollection, 2, "The collection index is invalid: ");</pre>
     *
     * <p>If the collection is {@code null}, then the message of the
     * exception is &quot;The validated object is null&quot;.</p>
     *
     * @param <T> the collection type
     * @param collection  the collection to check, validated not null by this method
     * @param index  the index to check
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @return the validated collection (never {@code null} for chaining)
     * @throws NullPointerException if the collection is {@code null}
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public static <T extends Collection<?>> T validIndex(final T collection, final int index, final String message, final Object... values) {
        notNull(collection, "The collection cannot be null");
        if (index < 0 || index >= collection.size()) {
            throw new IndexOutOfBoundsException(String.format(message, values));
        }
        return collection;
    }

    // validIndex string
    //---------------------------------------------------------------------------------

    /**
     * <p>Validates that the index is within the bounds of the argument
     * character sequence; otherwise throwing an exception with the
     * specified message.</p>
     *
     * <pre>Validate.validIndex(myStr, 2, "The string index is invalid: ");</pre>
     *
     * <p>If the character sequence is {@code null}, then the message
     * of the exception is &quot;The validated object is null&quot;.</p>
     *
     * @param <T> the character sequence type
     * @param chars  the character sequence to check, validated not null by this method
     * @param index  the index to check
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @return the validated character sequence (never {@code null} for method chaining)
     * @throws NullPointerException if the character sequence is {@code null}
     * @throws IndexOutOfBoundsException if the index is invalid
     *
     * @since 3.0
     */
    public static <T extends CharSequence> T validIndex(final T chars, final int index, final String message, final Object... values) {
        notNull(chars, "CharSequence cannot be null");
        if (index < 0 || index >= chars.length()) {
            throw new IndexOutOfBoundsException(String.format(message, values));
        }
        return chars;
    }

    // validState
    //---------------------------------------------------------------------------------

    /**
     * <p>Validate that the stateful condition is {@code true}; otherwise
     * throwing an exception with the specified message. This method is useful when
     * validating according to an arbitrary boolean expression, such as validating a
     * primitive number or using your own custom validation expression.</p>
     *
     * <pre>Validate.validState(this.isOk(), "The state is not OK: %s", myObject);</pre>
     *
     * @param expression  the boolean expression to check
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @throws IllegalStateException if expression is {@code false}
     *
     * @since 3.0
     */
    public static void validState(final boolean expression, final String message, final Object... values) {
        if (!expression) {
            throw new IllegalStateException(String.format(message, values));
        }
    }

    // matchesPattern
    //---------------------------------------------------------------------------------

    /**
     * <p>Validate that the specified argument character sequence matches the specified regular
     * expression pattern; otherwise throwing an exception with the specified message.</p>
     *
     * <pre>Validate.matchesPattern("hi", "[a-z]*", "%s does not match %s", "hi" "[a-z]*");</pre>
     *
     * <p>The syntax of the pattern is the one used in the {@link Pattern} class.</p>
     *
     * @param input  the character sequence to validate, not null
     * @param pattern  the regular expression pattern, not null
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @throws IllegalArgumentException if the character sequence does not match the pattern
     *
     * @since 3.0
     */
    public static void matchesPattern(final CharSequence input, final String pattern, final String message, final Object... values) {
        // TODO when breaking BC, consider returning input
        if (!Pattern.matches(pattern, input)) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    // notNaN
    //---------------------------------------------------------------------------------

    /**
     * <p>Validates that the specified argument is not {@code NaN}; otherwise
     * throwing an exception with the specified message.</p>
     *
     * <pre>Validate.notNaN(myDouble, "The value must be a number");</pre>
     *
     * @param value  the value to validate
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message
     * @throws IllegalArgumentException if the value is not a number
     *
     * @since 3.5
     */
    public static void notNaN(final double value, final String message, final Object... values) {
        if (Double.isNaN(value)) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    // finite
    //---------------------------------------------------------------------------------

    /**
     * <p>Validates that the specified argument is not infinite or {@code NaN};
     * otherwise throwing an exception with the specified message.</p>
     *
     * <pre>Validate.finite(myDouble, "The argument must contain a numeric value");</pre>
     *
     * @param value the value to validate
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message
     * @throws IllegalArgumentException if the value is infinite or {@code NaN}
     *
     * @since 3.5
     */
    public static void finite(final double value, final String message, final Object... values) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    // inclusiveBetween
    //---------------------------------------------------------------------------------

    /**
     * <p>Validate that the specified argument object fall between the two
     * inclusive values specified; otherwise, throws an exception with the
     * specified message.</p>
     *
     * <pre>Validate.inclusiveBetween(0, 2, 1, "Not in boundaries");</pre>
     *
     * @param <T> the type of the argument object
     * @param start  the inclusive start value, not null
     * @param end  the inclusive end value, not null
     * @param value  the object to validate, not null
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @throws IllegalArgumentException if the value falls outside the boundaries
     *
     * @since 3.0
     */
    public static <T> void inclusiveBetween(final T start, final T end, final Comparable<T> value, final String message, final Object... values) {
        // TODO when breaking BC, consider returning value
        if (value.compareTo(start) < 0 || value.compareTo(end) > 0) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    /**
     * Validate that the specified primitive value falls between the two
     * inclusive values specified; otherwise, throws an exception with the
     * specified message.
     *
     * <pre>Validate.inclusiveBetween(0, 2, 1, "Not in range");</pre>
     *
     * @param start the inclusive start value
     * @param end   the inclusive end value
     * @param value the value to validate
     * @param message the exception message if invalid, not null
     *
     * @throws IllegalArgumentException if the value falls outside the boundaries
     *
     * @since 3.3
     */
    public static void inclusiveBetween(final long start, final long end, final long value, final String message) {
        // TODO when breaking BC, consider returning value
        if (value < start || value > end) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Validate that the specified primitive value falls between the two
     * inclusive values specified; otherwise, throws an exception with the
     * specified message.
     *
     * <pre>Validate.inclusiveBetween(0.1, 2.1, 1.1, "Not in range");</pre>
     *
     * @param start the inclusive start value
     * @param end   the inclusive end value
     * @param value the value to validate
     * @param message the exception message if invalid, not null
     *
     * @throws IllegalArgumentException if the value falls outside the boundaries
     *
     * @since 3.3
     */
    public static void inclusiveBetween(final double start, final double end, final double value, final String message) {
        // TODO when breaking BC, consider returning value
        if (value < start || value > end) {
            throw new IllegalArgumentException(message);
        }
    }

    // exclusiveBetween
    //---------------------------------------------------------------------------------

    /**
     * <p>Validate that the specified argument object fall between the two
     * exclusive values specified; otherwise, throws an exception with the
     * specified message.</p>
     *
     * <pre>Validate.exclusiveBetween(0, 2, 1, "Not in boundaries");</pre>
     *
     * @param <T> the type of the argument object
     * @param start  the exclusive start value, not null
     * @param end  the exclusive end value, not null
     * @param value  the object to validate, not null
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @throws IllegalArgumentException if the value falls outside the boundaries
     *
     * @since 3.0
     */
    public static <T> void exclusiveBetween(final T start, final T end, final Comparable<T> value, final String message, final Object... values) {
        // TODO when breaking BC, consider returning value
        if (value.compareTo(start) <= 0 || value.compareTo(end) >= 0) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    /**
     * Validate that the specified primitive value falls between the two
     * exclusive values specified; otherwise, throws an exception with the
     * specified message.
     *
     * <pre>Validate.exclusiveBetween(0, 2, 1, "Not in range");</pre>
     *
     * @param start the exclusive start value
     * @param end   the exclusive end value
     * @param value the value to validate
     * @param message the exception message if invalid, not null
     *
     * @throws IllegalArgumentException if the value falls outside the boundaries
     *
     * @since 3.3
     */
    public static void exclusiveBetween(final long start, final long end, final long value, final String message) {
        // TODO when breaking BC, consider returning value
        if (value <= start || value >= end) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Validate that the specified primitive value falls between the two
     * exclusive values specified; otherwise, throws an exception with the
     * specified message.
     *
     * <pre>Validate.exclusiveBetween(0.1, 2.1, 1.1, "Not in range");</pre>
     *
     * @param start the exclusive start value
     * @param end   the exclusive end value
     * @param value the value to validate
     * @param message the exception message if invalid, not null
     *
     * @throws IllegalArgumentException if the value falls outside the boundaries
     *
     * @since 3.3
     */
    public static void exclusiveBetween(final double start, final double end, final double value, final String message) {
        // TODO when breaking BC, consider returning value
        if (value <= start || value >= end) {
            throw new IllegalArgumentException(message);
        }
    }

    // isInstanceOf
    //---------------------------------------------------------------------------------

    /**
     * <p>Validate that the argument is an instance of the specified class; otherwise
     * throwing an exception with the specified message. This method is useful when
     * validating according to an arbitrary class</p>
     *
     * <pre>Validate.isInstanceOf(OkClass.class, object, "Wrong class, object is of class %s",
     *   object.getClass().getName());</pre>
     *
     * @param type  the class the object must be validated against, not null
     * @param obj  the object to check, null throws an exception
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @throws IllegalArgumentException if argument is not of specified class
     *
     * @since 3.0
     */
    public static void isInstanceOf(final Class<?> type, final Object obj, final String message, final Object... values) {
        // TODO when breaking BC, consider returning obj
        if (!type.isInstance(obj)) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    // isAssignableFrom
    //---------------------------------------------------------------------------------

    /**
     * Validates that the argument can be converted to the specified class, if not throws an exception.
     *
     * <p>This method is useful when validating if there will be no casting errors.</p>
     *
     * <pre>Validate.isAssignableFrom(SuperClass.class, object.getClass());</pre>
     *
     * <p>The message of the exception is &quot;The validated object can not be converted to the&quot;
     * followed by the name of the class and &quot;class&quot;</p>
     *
     * @param superType  the class the class must be validated against, not null
     * @param type  the class to check, not null
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @throws IllegalArgumentException if argument can not be converted to the specified class
     */
    public static void isAssignableFrom(final Class<?> superType, final Class<?> type, final String message, final Object... values) {
        // TODO when breaking BC, consider returning type
        if (!superType.isAssignableFrom(type)) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }
}
