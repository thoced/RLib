package com.ss.rlib.concurrent.executor.impl;

import static java.util.Objects.requireNonNull;
import com.ss.rlib.concurrent.lock.LockFactory;
import com.ss.rlib.concurrent.util.ConcurrentUtils;
import com.ss.rlib.util.ClassUtils;
import com.ss.rlib.util.array.ArrayFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.ss.rlib.concurrent.executor.TaskExecutor;
import com.ss.rlib.concurrent.lock.Lockable;
import com.ss.rlib.concurrent.task.CallableTask;
import com.ss.rlib.concurrent.task.SimpleTask;
import com.ss.rlib.logging.Logger;
import com.ss.rlib.logging.LoggerManager;
import com.ss.rlib.util.array.Array;

import java.lang.reflect.Constructor;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;

/**
 * The implementation of single thread task executor.
 *
 * @param <L> the type parameter
 * @author JavaSaBr
 */
public class SingleThreadTaskExecutor<L> implements TaskExecutor<L>, Runnable, Lockable {

    /**
     * The constant LOGGER.
     */
    protected static final Logger LOGGER = LoggerManager.getLogger(SingleThreadTaskExecutor.class);

    /**
     * The list of waiting tasks.
     */
    @NotNull
    private final Array<CallableTask<?, L>> waitTasks;

    /**
     * The list of executing task.
     */
    @NotNull
    private final Array<CallableTask<?, L>> executeTasks;

    /**
     * The executor thread.
     */
    @NotNull
    private final Thread thread;

    /**
     * The thread local objects.
     */
    @NotNull
    private final L localObjects;

    /**
     * The waiting flag.
     */
    @NotNull
    private final AtomicBoolean wait;

    /**
     * The synchronizer.
     */
    @NotNull
    private final Lock lock;

    /**
     * Instantiates a new Single thread task executor.
     *
     * @param threadClass the thread class
     * @param priority    the priority
     * @param name        the name
     * @param local       the local
     */
    public SingleThreadTaskExecutor(@NotNull final Class<? extends Thread> threadClass, final int priority,
                                    @NotNull final String name, @Nullable final L local) {
        this.waitTasks = ArrayFactory.newArray(SimpleTask.class);
        this.executeTasks = ArrayFactory.newArray(SimpleTask.class);
        this.wait = new AtomicBoolean();
        this.lock = LockFactory.newAtomicLock();

        final Constructor<Thread> constructor = ClassUtils.getConstructor(threadClass, Runnable.class, String.class);

        this.thread = ClassUtils.newInstance(requireNonNull(constructor), this, name);
        this.thread.setPriority(priority);
        this.thread.setDaemon(true);
        this.localObjects = check(local, thread);
        this.thread.start();
    }

    /**
     * Check l.
     *
     * @param local  the local
     * @param thread the thread
     * @return the l
     */
    @NotNull
    protected L check(@Nullable final L local, @NotNull final Thread thread) {
        return requireNonNull(local);
    }

    @Override
    public void execute(@NotNull final SimpleTask<L> task) {
        lock();
        try {

            waitTasks.add(task);

            if (wait.get()) {
                synchronized (wait) {
                    if (wait.compareAndSet(true, false)) {
                        ConcurrentUtils.notifyAllInSynchronize(wait);
                    }
                }
            }

        } finally {
            unlock();
        }
    }

    /**
     * Gets execute tasks.
     *
     * @return the list of executing task.
     */
    @NotNull
    protected Array<CallableTask<?, L>> getExecuteTasks() {
        return executeTasks;
    }

    /**
     * Gets local objects.
     *
     * @return the thread local objects.
     */
    @NotNull
    protected L getLocalObjects() {
        return localObjects;
    }

    /**
     * Gets wait.
     *
     * @return the waiting flag.
     */
    @NotNull
    public AtomicBoolean getWait() {
        return wait;
    }

    /**
     * Gets wait tasks.
     *
     * @return the list of waiting tasks.
     */
    @NotNull
    protected Array<CallableTask<?, L>> getWaitTasks() {
        return waitTasks;
    }

    @Override
    public void lock() {
        lock.lock();
    }

    @Override
    public void run() {

        final Array<CallableTask<?, L>> waitTasks = getWaitTasks();
        final Array<CallableTask<?, L>> executeTasks = getExecuteTasks();

        final L local = getLocalObjects();

        while (true) {

            executeTasks.clear();

            lock();
            try {

                if (waitTasks.isEmpty()) {
                    wait.getAndSet(true);
                } else {
                    executeTasks.addAll(waitTasks);
                    waitTasks.clear();
                }

            } finally {
                unlock();
            }

            if (wait.get()) {
                synchronized (wait) {
                    if (wait.get()) {
                        ConcurrentUtils.waitInSynchronize(wait);
                    }
                }
            }

            if (executeTasks.isEmpty()) continue;

            try {

                final long currentTime = System.currentTimeMillis();

                for (final CallableTask<?, L> task : executeTasks.array()) {
                    if (task == null) break;
                    task.call(local, currentTime);
                }

            } catch (final Exception e) {
                LOGGER.warning(e);
            }
        }
    }

    @NotNull
    @Override
    public <R> Future<R> submit(@NotNull final CallableTask<R, L> task) {
        throw new RuntimeException("not implemented.");
    }

    @Override
    public void unlock() {
        lock.unlock();
    }
}
