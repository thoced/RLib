package rlib.util.linkedlist;

import rlib.util.linkedlist.impl.FastLinkedList;
import rlib.util.linkedlist.impl.SortedLinkedList;

/**
 * Утильный класс по работе со связанными списками.
 *
 * @author Ronn
 */
public final class LinkedListFactory {

    /**
     * Создание быстрого связанного списка.
     *
     * @param type тип элементов в списке.
     * @return новый связанный список.
     */
    public static <E> LinkedList<E> newLinkedList(final Class<?> type) {
        return new FastLinkedList<>(type);
    }

    /**
     * Создание быстрого отсортированного списка.
     *
     * @param type тип элементов.
     * @return отсортированный связанный список.
     */
    public static <E extends Comparable<E>> LinkedList<E> newSortedLinkedList(final Class<?> type) {
        return new SortedLinkedList<>(type);
    }

    private LinkedListFactory() {
        throw new RuntimeException();
    }
}
