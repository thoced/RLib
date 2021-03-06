package com.ss.rlib.util.dictionary;

import org.jetbrains.annotations.NotNull;

/**
 * The fast implementation of {@link IntegerDictionary} without threadsafe supporting.
 *
 * @param <V> the type parameter
 * @author JavaSaBr
 */
public class FastIntegerDictionary<V> extends AbstractIntegerDictionary<V> implements UnsafeIntegerDictionary<V> {

    /**
     * The array of entries.
     */
    private IntegerEntry<V>[] content;

    /**
     * The next size value at which to resize (capacity * load factor).
     */
    private int threshold;

    /**
     * The count of values in this {@link Dictionary}.
     */
    private int size;

    /**
     * Instantiates a new Fast integer dictionary.
     */
    protected FastIntegerDictionary() {
        this(DEFAULT_LOAD_FACTOR, DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * Instantiates a new Fast integer dictionary.
     *
     * @param loadFactor the load factor
     */
    protected FastIntegerDictionary(final float loadFactor) {
        this(loadFactor, DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * Instantiates a new Fast integer dictionary.
     *
     * @param initCapacity the init capacity
     */
    protected FastIntegerDictionary(final int initCapacity) {
        this(DEFAULT_LOAD_FACTOR, initCapacity);
    }

    /**
     * Instantiates a new Fast integer dictionary.
     *
     * @param loadFactor   the load factor
     * @param initCapacity the init capacity
     */
    protected FastIntegerDictionary(final float loadFactor, final int initCapacity) {
        super(loadFactor, initCapacity);
    }

    @Override
    public void setSize(final int size) {
        this.size = size;
    }

    @Override
    public void setContent(@NotNull final IntegerEntry<V>[] content) {
        this.content = content;
    }

    @NotNull
    @Override
    public IntegerEntry<V>[] content() {
        return content;
    }

    @Override
    public void setThreshold(final int threshold) {
        this.threshold = threshold;
    }

    @Override
    public int getThreshold() {
        return threshold;
    }

    @Override
    protected int decrementSizeAndGet() {
        return --size;
    }

    @Override
    protected int incrementSizeAndGet() {
        return ++size;
    }

    @Override
    public final int size() {
        return size;
    }
}
