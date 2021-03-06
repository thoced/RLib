package com.ss.rlib.concurrent.lock.impl;

import com.ss.rlib.concurrent.atomic.AtomicInteger;
import org.jetbrains.annotations.NotNull;

import sun.misc.Contended;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * The implementation of the {@link Lock} based on using {@link AtomicInteger} without supporting
 * reentrant calls.
 *
 * @author JavaSaBr
 */
public class AtomicLock implements Lock {

    private static final int STATUS_LOCKED = 1;
    private static final int STATUS_UNLOCKED = 0;

    /**
     * The status of lock.
     */
    @Contended
    protected final AtomicInteger status;

    /**
     * The field for consuming CPU.
     */
    protected int sink;

    /**
     * Instantiates a new Atomic lock.
     */
    public AtomicLock() {
        this.status = new AtomicInteger();
        this.sink = 1;
    }

    @Override
    public void lock() {
        while (!tryLock()) consumeCPU();
    }

    /**
     * Consume cpu.
     */
    protected void consumeCPU() {

        final int value = sink;
        int newValue = value * value;
        newValue += value >>> 1;
        newValue += value & newValue;
        newValue += value ^ newValue;
        newValue += newValue << value;
        newValue += newValue | value;
        newValue += value & newValue;
        newValue += value ^ newValue;
        newValue += newValue << value;
        newValue += newValue | value;

        sink = newValue;
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean tryLock() {
        return status.compareAndSet(STATUS_UNLOCKED, STATUS_LOCKED);
    }

    @Override
    public boolean tryLock(final long time, @NotNull final TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unlock() {
        status.set(STATUS_UNLOCKED);
    }

    @Override
    public String toString() {
        return "AtomicLock{" +
                "status=" + status +
                '}';
    }
}
