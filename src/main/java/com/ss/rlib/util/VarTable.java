package com.ss.rlib.util;

import static com.ss.rlib.util.ObjectUtils.notNull;
import static java.lang.Float.parseFloat;
import com.ss.rlib.geom.Quaternion4f;
import com.ss.rlib.geom.Vector3f;
import com.ss.rlib.util.dictionary.DictionaryFactory;
import com.ss.rlib.util.dictionary.ObjectDictionary;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * THe utility class to contain different properties.
 *
 * @author JavaSaBr
 */
public class VarTable {

    public static @NotNull VarTable newInstance() {
        return new VarTable();
    }

    /**
     * New instance var table.
     *
     * @param node the xml node.
     * @return the new table with attributes of the node.
     */
    public static @NotNull VarTable newInstance(@Nullable final Node node) {
        return newInstance().parse(node);
    }

    /**
     * New instance var table.
     *
     * @param node      the node
     * @param childName the child name
     * @param nameType  the name type
     * @param nameValue the name value
     * @return the var table
     */
    public static @NotNull VarTable newInstance(@Nullable final Node node, @NotNull final String childName,
                                                @NotNull final String nameType, @NotNull final String nameValue) {
        return newInstance().parse(node, childName, nameType, nameValue);
    }

    /**
     * The table with values.
     */
    @NotNull
    private final ObjectDictionary<String, Object> values;

    private VarTable() {
        this.values = DictionaryFactory.newObjectDictionary();
    }

    /**
     * Clear this table.
     */
    public void clear() {
        values.clear();
    }

    /**
     * Get the value by the key.
     *
     * @param <T> the type parameter
     * @param key the key.
     * @return the t
     */
    public <T> @NotNull T get(@NotNull final String key) {
        final T result = ClassUtils.unsafeCast(values.get(key));
        if (result != null) return result;
        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Get the value by the key.
     *
     * @param <T>  the type parameter
     * @param key  the key.
     * @param type the type.
     * @return the value.
     */
    public <T> @NotNull T get(@NotNull final String key, @NotNull final Class<T> type) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException();
        } else if (type.isInstance(object)) {
            return type.cast(object);
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Get the value by the key.
     *
     * @param <T>  the type parameter
     * @param <E>  the type parameter
     * @param key  the key.
     * @param type the type.
     * @param def  the default value.
     * @return the value.
     */
    public <T, E extends T> @NotNull T get(@NotNull final String key, @NotNull final Class<T> type,
                                           @NotNull final E def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (type.isInstance(object)) {
            return type.cast(object);
        }

        return def;
    }

    /**
     * Get the value by the key.
     *
     * @param <T>  the type parameter
     * @param <E>  the type parameter
     * @param key  the key.
     * @param type the type.
     * @param def  the default value.
     * @return the value.
     */
    public <T, E extends T> @NotNull T getNullable(@NotNull final String key, @NotNull final Class<T> type,
                                                   @Nullable final E def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (type.isInstance(object)) {
            return type.cast(object);
        }

        return def;
    }

    /**
     * Get the value by the key.
     *
     * @param <T> the type parameter
     * @param key the key.
     * @param def the default value.
     * @return the value.
     */
    public <T> @NotNull T get(@NotNull final String key, @NotNull final T def) {

        final Object object = values.get(key);
        if (object == null) return def;

        final Class<?> type = def.getClass();

        if (type.isInstance(object)) {
            return notNull(ClassUtils.unsafeCast(object));
        }

        return def;
    }

    /**
     * Get the value by the key.
     *
     * @param <T> the type parameter
     * @param key the key.
     * @param def the default value.
     * @return the value.
     */
    public <T> @NotNull T getNullable(@NotNull final String key, @Nullable final T def) {

        final Object object = values.get(key);
        if (object == null || def == null) return def;

        final Class<?> type = def.getClass();

        if (type.isInstance(object)) {
            return notNull(ClassUtils.unsafeCast(object));
        }

        return def;
    }

    /**
     * Get an array by the key.
     *
     * @param <T>  the type parameter
     * @param key  the key.
     * @param type the type.
     * @return the array.
     */
    public <T> @NotNull T[] getArray(@NotNull final String key, @NotNull final Class<T[]> type) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (type.isInstance(object)) {
            return notNull(ClassUtils.unsafeCast(object));
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Get an array of the key.
     *
     * @param <T>  the type parameter
     * @param key  the key.
     * @param type the type.
     * @param def  the default array.
     * @return the array.
     */
    @SafeVarargs
    public final <T> @NotNull T[] getArray(@NotNull final String key, @NotNull final Class<T[]> type,
                                           @NotNull final T... def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (type.isInstance(object)) {
            return notNull(ClassUtils.unsafeCast(object));
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Get a boolean value by the key.
     *
     * @param key the key.
     * @return the value.
     */
    public boolean getBoolean(@NotNull final String key) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof Boolean) {
            return (Boolean) object;
        } else if (object instanceof String) {
            return Boolean.parseBoolean(object.toString());
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Get a boolean value by the key.
     *
     * @param key the key.
     * @param def the default value.
     * @return the value.
     */
    public boolean getBoolean(@NotNull final String key, final boolean def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof Boolean) {
            return (Boolean) object;
        } else if (object instanceof String) {
            return Boolean.parseBoolean(object.toString());
        }

        return def;
    }

    /**
     * Get a boolean array by the key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @return the boolean array.
     */
    public @NotNull boolean[] getBooleanArray(@NotNull final String key, @NotNull final String regex) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof boolean[]) {
            return (boolean[]) object;
        } else if (object instanceof String) {
            return parseBooleanArray(regex, object);
        }

        throw new IllegalArgumentException("not found " + key);
    }

    private boolean[] parseBooleanArray(@NotNull final String regex, @NotNull final Object object) {

        final String[] strings = object.toString().split(regex);
        final boolean[] result = new boolean[strings.length];

        for (int i = 0, length = strings.length; i < length; i++) {
            result[i] = Boolean.parseBoolean(strings[i]);
        }

        return result;
    }

    /**
     * Get a boolean array by the key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @param def   the default array.
     * @return the boolean array.
     */
    public @NotNull boolean[] getBooleanArray(@NotNull final String key, @NotNull final String regex,
                                              @NotNull final boolean... def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof boolean[]) {
            return (boolean[]) object;
        } else if (object instanceof String) {
            return parseBooleanArray(regex, object);
        }

        return def;
    }

    /**
     * Get a byte value by the key.
     *
     * @param key the key.
     * @return the byte.
     */
    public byte getByte(@NotNull final String key) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof Byte) {
            return (Byte) object;
        } else if (object instanceof String) {
            return Byte.parseByte(object.toString());
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Get a byte value by the key.
     *
     * @param key the key.
     * @param def the default value.
     * @return the byte.
     */
    public byte getByte(@NotNull final String key, final byte def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof Byte) {
            return (Byte) object;
        } else if (object instanceof String) {
            return Byte.parseByte(object.toString());
        }

        return def;
    }

    /**
     * Get a byte array by the key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @return the byte array.
     */
    public @NotNull byte[] getByteArray(@NotNull final String key, @NotNull final String regex) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof byte[]) {
            return (byte[]) object;
        } else if (object instanceof String) {
            return parseByteArray(regex, object);
        }

        throw new IllegalArgumentException("not found " + key);
    }

    private byte[] parseByteArray(@NotNull final String regex, @NotNull final Object object) {

        final String[] strings = object.toString().split(regex);
        final byte[] result = new byte[strings.length];

        for (int i = 0, length = strings.length; i < length; i++) {
            result[i] = Byte.parseByte(strings[i]);
        }

        return result;
    }

    /**
     * Get a byte array by the key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @param def   the default byte array.
     * @return the byte array.
     */
    public @NotNull byte[] getByteArray(@NotNull final String key, @NotNull final String regex,
                                        @NotNull final byte... def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof byte[]) {
            return (byte[]) object;
        } else if (object instanceof String) {
            return parseByteArray(regex, object);
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Get a double value by the key.
     *
     * @param key the key.
     * @return the value.
     */
    public double getDouble(@NotNull final String key) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof Double) {
            return (Double) object;
        } else if (object instanceof String) {
            return Double.parseDouble(object.toString());
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Get a double value by the key.
     *
     * @param key the key.
     * @param def the default value.
     * @return the value.
     */
    public double getDouble(@NotNull final String key, final double def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof Double) {
            return (Double) object;
        } else if (object instanceof String) {
            return Double.parseDouble(object.toString());
        }

        return def;
    }

    /**
     * Get a double array by the key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @return the double array.
     */
    public @NotNull double[] getDoubleArray(@NotNull final String key, @NotNull final String regex) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof double[]) {
            return (double[]) object;
        } else if (object instanceof String) {
            return parseDoubleArray(regex, object);
        }

        throw new IllegalArgumentException("not found " + key);
    }

    private double[] parseDoubleArray(@NotNull final String regex, @NotNull final Object object) {

        final String[] strings = object.toString().split(regex);
        final double[] result = new double[strings.length];

        for (int i = 0, length = strings.length; i < length; i++) {
            result[i] = Double.parseDouble(strings[i]);
        }

        return result;
    }

    /**
     * Get a double array by the key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @param def   the default array.
     * @return the double array.
     */
    public @NotNull double[] getDoubleArray(@NotNull final String key, @NotNull final String regex,
                                            @NotNull final double... def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof double[]) {
            return (double[]) object;
        } else if (object instanceof String) {
            return parseDoubleArray(regex, object);
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Get an enum value by the key.
     *
     * @param <T>  the type parameter
     * @param key  the key.
     * @param type the type of enum.
     * @return the value.
     */
    public @NotNull <T extends Enum<T>> T getEnum(@NotNull final String key, @NotNull final Class<T> type) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (type.isInstance(object)) {
            return type.cast(object);
        } else if (object instanceof String) {
            return Enum.valueOf(type, object.toString());
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Get an enum value by the key.
     *
     * @param <T>  the type parameter
     * @param key  the key.
     * @param type the type of enum.
     * @param def  the default value.
     * @return the value.
     */
    public <T extends Enum<T>> @NotNull T getEnum(@NotNull final String key, @NotNull final Class<T> type,
                                                  @NotNull final T def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (type.isInstance(object)) {
            return type.cast(object);
        } else if (object instanceof String) {
            return Enum.valueOf(type, object.toString());
        }

        return def;
    }

    /**
     * Get an enum array by the key.
     *
     * @param <T>   the type parameter
     * @param key   the key.
     * @param type  the type of enum.
     * @param regex the regex to split if a value is string.
     * @return the enum array.
     */
    public <T extends Enum<T>> @NotNull T[] getEnumArray(@NotNull final String key, @NotNull final Class<T> type,
                                                         @NotNull final String regex) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof Enum[]) {
            return notNull(ClassUtils.unsafeCast(object));
        } else if (object instanceof String) {
            return parseEnumArray(type, regex, object);
        }

        throw new IllegalArgumentException("not found " + key);
    }

    private <T extends Enum<T>> @NotNull T[] parseEnumArray(@NotNull final Class<T> type, @NotNull final String regex,
                                                            @NotNull final Object object) {

        final String[] strings = object.toString().split(regex);
        final T[] result = notNull(ClassUtils.unsafeCast(ArrayUtils.create(type, strings.length)));

        for (int i = 0, length = strings.length; i < length; i++) {
            result[i] = Enum.valueOf(type, strings[i]);
        }

        return result;
    }

    /**
     * Get an enum array by the key.
     *
     * @param <T>   the type parameter
     * @param key   the key.
     * @param type  the type of enum.
     * @param regex the regex to split if a value is string.
     * @param def   the default array.
     * @return the enum array.
     */

    public <T extends Enum<T>> @NotNull T[] getEnumArray(@NotNull final String key, @NotNull final Class<T> type,
                                                         @NotNull final String regex, @NotNull final T... def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof Enum[]) {
            return notNull(ClassUtils.unsafeCast(object));
        } else if (object instanceof String) {
            return parseEnumArray(type, regex, object);
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Get a float value by the key.
     *
     * @param key the key.
     * @return the float value.
     */
    public float getFloat(@NotNull final String key) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof Float) {
            return (Float) object;
        } else if (object instanceof String) {
            return Float.parseFloat(object.toString());
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Get a float value by the key.
     *
     * @param key the key.
     * @param def the default value.
     * @return the float value.
     */
    public float getFloat(@NotNull final String key, final float def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof Float) {
            return (Float) object;
        } else if (object instanceof String) {
            return Float.parseFloat(object.toString());
        }

        return def;
    }

    /**
     * Get a float array by the key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @return the float array.
     */
    public @NotNull float[] getFloatArray(@NotNull final String key, @NotNull final String regex) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException();
        } else if (object instanceof float[]) {
            return (float[]) object;
        } else if (object instanceof String) {
            return parseFloatArray(regex, object);
        }

        throw new IllegalArgumentException("not found " + key);
    }

    private @NotNull float[] parseFloatArray(@NotNull final String regex, @NotNull final Object object) {

        final String[] strings = object.toString().split(regex);
        final float[] result = new float[strings.length];

        for (int i = 0, length = strings.length; i < length; i++) {
            result[i] = Float.parseFloat(strings[i]);
        }

        return result;
    }

    /**
     * Get a float array by the key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @param def   the default array.
     * @return the float array.
     */
    public @NotNull float[] getFloatArray(@NotNull final String key, @NotNull final String regex,
                                          @NotNull final float... def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof float[]) {
            return (float[]) object;
        } else if (object instanceof String) {
            return parseFloatArray(regex, object);
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Get an integer value by the key.
     *
     * @param key the key.
     * @return the integer value.
     */
    public int getInteger(@NotNull final String key) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof Integer) {
            return (Integer) object;
        } else if (object instanceof String) {
            return Integer.parseInt(object.toString());
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Get an integer value by the key.
     *
     * @param key the key.
     * @param def the default value.
     * @return the integer value.
     */
    public int getInteger(@NotNull final String key, final int def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof Integer) {
            return (Integer) object;
        } else if (object instanceof String) {
            return Integer.parseInt(object.toString());
        }

        return def;
    }

    /**
     * Get an integer value by the key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @return the integer value.
     */
    public @NotNull int[] getIntegerArray(@NotNull final String key, @NotNull final String regex) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof int[]) {
            return (int[]) object;
        } else if (object instanceof String) {
            return parseIntegerArray(regex, object);
        }

        throw new IllegalArgumentException("not found " + key);
    }

    private @NotNull int[] parseIntegerArray(@NotNull final String regex, @NotNull final Object object) {

        final String[] strings = object.toString().split(regex);
        final int[] result = new int[strings.length];

        for (int i = 0, length = strings.length; i < length; i++) {
            result[i] = Integer.parseInt(strings[i]);
        }

        return result;
    }

    /**
     * Get an integer value by the key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @param def   the default value.
     * @return the integer value.
     */
    public @NotNull int[] getIntegerArray(@NotNull final String key, @NotNull final String regex,
                                          @NotNull final int... def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof int[]) {
            return (int[]) object;
        } else if (object instanceof String) {
            return parseIntegerArray(regex, object);
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Get a long value by the key.
     *
     * @param key the key.
     * @return the long value.
     */
    public long getLong(@NotNull final String key) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof Long) {
            return (Long) object;
        } else if (object instanceof String) {
            return Long.parseLong(object.toString());
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Get a long value by the key.
     *
     * @param key the key.
     * @param def the default value.
     * @return the long value.
     */
    public long getLong(@NotNull final String key, final long def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof Long) {
            return (Long) object;
        } else if (object instanceof String) {
            return Long.parseLong(object.toString());
        }

        return def;
    }

    /**
     * Get a long array by the key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @return the long array.
     */
    public @NotNull long[] getLongArray(@NotNull final String key, @NotNull final String regex) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof long[]) {
            return (long[]) object;
        } else if (object instanceof String) {
            return parseLongArray(regex, object);
        }

        throw new IllegalArgumentException("not found " + key);
    }

    private @NotNull long[] parseLongArray(@NotNull final String regex, @NotNull final Object object) {

        final String[] strings = object.toString().split(regex);
        final long[] result = new long[strings.length];

        for (int i = 0, length = strings.length; i < length; i++) {
            result[i] = Long.parseLong(strings[i]);
        }

        return result;
    }

    /**
     * Get a long array by the key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @param def   the default array.
     * @return the long array.
     */
    public @NotNull long[] getLongArray(@NotNull final String key, @NotNull final String regex,
                                        @NotNull final long... def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof long[]) {
            return (long[]) object;
        } else if (object instanceof String) {
            return parseLongArray(regex, object);
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Get a rotation by the key.
     *
     * @param key the key.
     * @return the rotation.
     */
    public @NotNull Quaternion4f getRotation(@NotNull final String key) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof Quaternion4f) {
            return (Quaternion4f) object;
        } else if (object instanceof String) {
            return parseRotation((String) object);
        }

        throw new IllegalArgumentException("not found " + key);
    }

    private @NotNull Quaternion4f parseRotation(@NotNull final String object) {

        final String[] values = object.split(",");

        final Quaternion4f rotation = Quaternion4f.newInstance();
        rotation.setXYZW(parseFloat(values[0]), parseFloat(values[1]), parseFloat(values[2]), parseFloat(values[3]));

        return rotation;
    }

    /**
     * Get a rotation by the key.
     *
     * @param key the key.
     * @param def the default value.
     * @return the rotation.
     */
    public @NotNull Quaternion4f getRotation(@NotNull final String key, @NotNull final Quaternion4f def) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof Quaternion4f) {
            return (Quaternion4f) object;
        } else if (object instanceof String) {
            return parseRotation((String) object);
        }

        return def;
    }

    /**
     * Get a short value by the key.
     *
     * @param key the key.
     * @return the short value.
     */
    public short getShort(@NotNull final String key) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof Short) {
            return (Short) object;
        } else if (object instanceof String) {
            return Short.parseShort(object.toString());
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Get a short value by the key.
     *
     * @param key the key.
     * @param def the default value.
     * @return the short value.
     */
    public short getShort(@NotNull final String key, final short def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof Short) {
            return (Short) object;
        } else if (object instanceof String) {
            return Short.parseShort(object.toString());
        }

        return def;
    }

    /**
     * Get a short array by the key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @return the short array.
     */
    public @NotNull short[] getShortArray(@NotNull final String key, @NotNull final String regex) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof short[]) {
            return (short[]) object;
        } else if (object instanceof String) {
            return parseShortArray(regex, object);
        }

        throw new IllegalArgumentException("not found " + key);
    }

    private @NotNull short[] parseShortArray(@NotNull final String regex, @NotNull final Object object) {

        final String[] strings = object.toString().split(regex);
        final short[] result = new short[strings.length];

        for (int i = 0, length = strings.length; i < length; i++) {
            result[i] = Short.parseShort(strings[i]);
        }

        return result;
    }

    /**
     * Get a short array by the key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @param def   the default array.
     * @return the short array.
     */
    public @NotNull short[] getShortArray(@NotNull final String key, @NotNull final String regex,
                                          @NotNull final short... def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof short[]) {
            return (short[]) object;
        } else if (object instanceof String) {
            return parseShortArray(regex, object);
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Get a string by the key.
     *
     * @param key the key.
     * @return the string.
     */
    public @NotNull String getString(@NotNull final String key) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof String) {
            return object.toString();
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Get a string by the key.
     *
     * @param key the key.
     * @param def the default string.
     * @return the string.
     */
    public @NotNull String getString(@NotNull final String key, @NotNull final String def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof String) {
            return object.toString();
        }

        return def;
    }

    /**
     * Get a string by the key.
     *
     * @param key the key.
     * @param def the default string.
     * @return the string.
     */
    public @Nullable String getNullableString(@NotNull final String key, @Nullable final String def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof String) {
            return object.toString();
        }

        return def;
    }

    /**
     * Get a string array by the key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @return the string array.
     */
    public @NotNull String[] getStringArray(@NotNull final String key, @NotNull final String regex) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof String[]) {
            return (String[]) object;
        } else if (object instanceof String) {
            return object.toString().split(regex);
        }

        throw new IllegalArgumentException("not found " + key);
    }

    /**
     * Get a string array by the key.
     *
     * @param key   the key.
     * @param regex the regex to split if a value is string.
     * @param def   the default array.
     * @return the string array.
     */
    public @NotNull String[] getStringArray(@NotNull final String key, @NotNull final String regex,
                                            @NotNull final String... def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof String[]) {
            return (String[]) object;
        } else if (object instanceof String) {
            return ((String) object).split(regex);
        }

        throw new IllegalArgumentException("no found " + key);
    }

    /**
     * Gets values.
     *
     * @return the table with values.
     */
    @NotNull
    public ObjectDictionary<String, Object> getValues() {
        return values;
    }

    /**
     * Get a vector by the key.
     *
     * @param key the key.
     * @return the vector.
     */
    public @NotNull Vector3f getVector(@NotNull final String key) {

        final Object object = values.get(key);

        if (object == null) {
            throw new IllegalArgumentException("not found " + key);
        } else if (object instanceof Vector3f) {
            return (Vector3f) object;
        } else if (object instanceof String) {
            return parseVector((String) object);
        }

        throw new IllegalArgumentException("not found " + key);
    }

    private @NotNull Vector3f parseVector(@NotNull final String object) {

        final String[] values = object.split(",");

        final Vector3f vector = Vector3f.newInstance();
        vector.set(parseFloat(values[0]), parseFloat(values[1]), parseFloat(values[2]));

        return vector;
    }

    /**
     * Get a vector by the key.
     *
     * @param key the key.
     * @param def the default vector.
     * @return the vector.
     */
    public @NotNull Vector3f getVector(@NotNull final String key, @NotNull final Vector3f def) {

        final Object object = values.get(key);

        if (object == null) {
            return def;
        } else if (object instanceof Vector3f) {
            return (Vector3f) object;
        } else if (object instanceof String) {
            return parseVector((String) object);
        }

        return def;
    }

    /**
     * Clear and fill this table by attributes from a xml node.
     *
     * @param node the xml node.
     * @return the var table
     */
    public @NotNull VarTable parse(@Nullable final Node node) {
        values.clear();

        if (node == null) return this;

        final NamedNodeMap attributes = node.getAttributes();
        if (attributes == null) return this;

        for (int i = 0, length = attributes.getLength(); i < length; i++) {
            final Node item = attributes.item(i);
            set(item.getNodeName(), item.getNodeValue());
        }

        return this;
    }

    /**
     * Clear and fill this table using children of a xml node:
     * <pre>
     * 	&#60;element&#62;
     * 		&#60;child name="name" value="value" /&#62;
     * 		&#60;child name="name" value="value" /&#62;
     * 		&#60;child name="name" value="value" /&#62;
     * 		&#60;child name="name" value="value" /&#62;
     * 	&#60;/element&#62;
     *
     * vars.parse(node, "child", "name", "value")
     * </pre>
     *
     * @param node      the xml node.
     * @param childName the name of a child element.
     * @param nameName  the name of name attribute.
     * @param nameValue the name of value attribute.
     * @return the var table
     */
    public @NotNull VarTable parse(@Nullable final Node node, @NotNull final String childName,
                                   @NotNull final String nameName, @NotNull final String nameValue) {
        values.clear();

        if (node == null) {
            return this;
        }

        for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {

            if (child.getNodeType() != Node.ELEMENT_NODE || !childName.equals(child.getNodeName())) {
                continue;
            }

            final NamedNodeMap attributes = child.getAttributes();

            final Node nameNode = attributes.getNamedItem(nameName);
            final Node valueNode = attributes.getNamedItem(nameValue);

            if (nameNode == null || valueNode == null) {
                continue;
            }

            set(nameNode.getNodeValue(), valueNode.getNodeValue());
        }

        return this;
    }

    /**
     * Set a value by the key.
     *
     * @param key   the key.
     * @param value the value.
     */
    public void set(@NotNull final String key, @NotNull final Object value) {
        values.put(key, notNull(value));
    }

    /**
     * Clear a value by the key.
     *
     * @param key the key.
     */
    public void clear(@NotNull final String key) {
        values.remove(key);
    }

    /**
     * Copy all values from a other table.
     *
     * @param vars the other table.
     * @return the var table
     */
    public @NotNull VarTable set(@NotNull final VarTable vars) {
        values.put(vars.getValues());
        return this;
    }

    /**
     * @param key the key.
     * @return true if the value is exists.
     */
    public boolean has(@NotNull final String key) {
        return values.containsKey(key);
    }

    @Override
    public String toString() {
        return "VarTable: " + ("values = " + values);
    }
}
