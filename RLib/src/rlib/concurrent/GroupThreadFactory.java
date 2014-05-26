package rlib.concurrent;

import java.lang.reflect.Constructor;
import java.util.concurrent.ThreadFactory;

import rlib.concurrent.atomic.AtomicInteger;
import rlib.util.ClassUtils;

/**
 * Реализация группированной фабрики потоков.
 * 
 * @author Ronn
 */
public class GroupThreadFactory implements ThreadFactory {

	/** номер следующего потока */
	private final AtomicInteger ordinal;
	/** имя группы потоков */
	private final String name;
	/** группа потоков */
	private final ThreadGroup group;

	/** конструктор потока */
	private final Constructor<? extends Thread> constructor;

	/** приоритет потоков */
	private final int priority;

	public GroupThreadFactory(final String name, final Class<? extends Thread> cs, final int priority) {
		this.constructor = ClassUtils.getConstructor(cs, ThreadGroup.class, Runnable.class, String.class);
		this.priority = priority;
		this.name = name;
		this.group = new ThreadGroup(name);
		this.ordinal = new AtomicInteger();
	}

	@Override
	public Thread newThread(final Runnable runnable) {
		final Thread thread = ClassUtils.newInstance(constructor, group, runnable, name + "-" + ordinal.incrementAndGet());
		thread.setPriority(priority);
		return thread;
	}
}
