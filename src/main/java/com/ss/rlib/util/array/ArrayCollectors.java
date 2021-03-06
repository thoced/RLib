package com.ss.rlib.util.array;

import static java.util.Collections.unmodifiableSet;
import com.ss.rlib.util.ArrayUtils;
import com.ss.rlib.util.array.impl.FinalConcurrentStampedLockArray;
import com.ss.rlib.util.array.impl.FinalFastArray;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * The array collectors factory.
 *
 * @author JavaSaBr
 */
public class ArrayCollectors {

    @NotNull
    private static Set<Collector.Characteristics> CH_ID =
            unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));

    @NotNull
    private static Set<Collector.Characteristics> CH_ID_CONC =
            unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH, Collector.Characteristics.CONCURRENT));


    private static <T, A extends Array<T>> @NotNull Collector<T, A, A> collector(@NotNull final Class<T> type,
                                                                                 @NotNull final Function<Class<T>, A> arrayFactory) {
        return new Collector<T, A, A>() {

            private final Supplier<A> supplier = () -> arrayFactory.apply(type);

            @Override
            public Supplier<A> supplier() {
                return supplier;
            }

            @Override
            public BiConsumer<A, T> accumulator() {
                return Collection::add;
            }

            @Override
            public BinaryOperator<A> combiner() {
                return (source, toAdd) -> {
                    source.addAll(toAdd);
                    return source;
                };
            }

            @Override
            public Function<A, A> finisher() {
                return array -> array;
            }

            @Override
            public Set<Characteristics> characteristics() {
                return CH_ID;
            }
        };
    }

    private static <T, A extends ConcurrentArray<T>> @NotNull Collector<T, A, A> concurrentCollector(
            @NotNull final Class<T> type, @NotNull final Function<Class<T>, A> arrayFactory) {
        return new Collector<T, A, A>() {

            private final Supplier<A> supplier = () -> arrayFactory.apply(type);

            @Override
            public Supplier<A> supplier() {
                return supplier;
            }

            @Override
            public BiConsumer<A, T> accumulator() {
                return Collection::add;
            }

            @Override
            public BinaryOperator<A> combiner() {
                return (source, toAdd) -> {
                    ArrayUtils.runInWriteLock(source, toAdd, Array::addAll);
                    return source;
                };
            }

            @Override
            public Function<A, A> finisher() {
                return array -> array;
            }

            @Override
            public Set<Characteristics> characteristics() {
                return CH_ID_CONC;
            }
        };
    }

    /**
     * Get a collector to collect elements into an array.
     *
     * @param type the type of elements.
     * @return the collector.
     */
    public static <T> @NotNull Collector<T, Array<T>, Array<T>> simple(@NotNull final Class<T> type) {
        return collector(type, FinalFastArray::new);
    }

    /**
     * Get a collector to collect elements in a thread safe array.
     *
     * @param type the type of elements.
     * @return the collector.
     */
    public static <T> @NotNull Collector<T, ConcurrentArray<T>, ConcurrentArray<T>> concurrent(
            @NotNull final Class<T> type) {
        return concurrentCollector(type, FinalConcurrentStampedLockArray::new);
    }
}
