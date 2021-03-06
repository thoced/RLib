package com.ss.rlib.util.array.impl;

import com.ss.rlib.concurrent.lock.LockFactory;
import com.ss.rlib.util.array.ConcurrentArray;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * The concurrent implementation of the array using {@link ReentrantReadWriteLock} for {@link
 * ConcurrentArray#readLock()}* and {@link ConcurrentArray#writeLock()}.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
public class ConcurrentReentrantRWLockArray<E> extends AbstractConcurrentArray<E> {

    private static final long serialVersionUID = -7985171224116955303L;

    /**
     * The read lock.
     */
    private final Lock readLock;

    /**
     * The write lock.
     */
    private final Lock writeLock;

    /**
     * Instantiates a new Concurrent reentrant rw lock array.
     *
     * @param type the type
     */
    public ConcurrentReentrantRWLockArray(@NotNull final Class<E> type) {
        this(type, 10);
    }

    /**
     * Instantiates a new Concurrent reentrant rw lock array.
     *
     * @param type the type
     * @param size the size
     */
    public ConcurrentReentrantRWLockArray(@NotNull final Class<E> type, final int size) {
        super(type, size);

        final ReadWriteLock readWriteLock = LockFactory.newReentrantRWLock();
        this.readLock = readWriteLock.readLock();
        this.writeLock = readWriteLock.writeLock();
    }

    @Override
    public final long readLock() {
        readLock.lock();
        return 0;
    }

    @Override
    public final void readUnlock(final long stamp) {
        readLock.unlock();
    }

    @Override
    public final long writeLock() {
        writeLock.lock();
        return 0;
    }

    @Override
    public final void writeUnlock(final long stamp) {
        writeLock.unlock();
    }
}