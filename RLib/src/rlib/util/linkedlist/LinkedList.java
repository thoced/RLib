package rlib.util.linkedlist;

import java.io.Serializable;
import java.util.Deque;
import java.util.function.Consumer;
import java.util.function.Function;

import rlib.util.linkedlist.impl.Node;
import rlib.util.pools.Foldable;

/**
 * Интерфей с для реализации связанного списка. Главное преймущество, это
 * переиспользование узлов списка и быстрая итерация с уменьшением нагрузки на
 * GC. Создаются с помощью {@link LinkedListFactory}.
 * 
 * <pre>
 * for(Node&lt;E&gt; node = getFirstNode(); node != null; node = node.getNext()) {
 * 	? item = node.getItem();
 * 	// handle item
 * }
 * </pre>
 * 
 * @author Ronn
 */
public interface LinkedList<E> extends Deque<E>, Cloneable, Serializable, Foldable {

	/**
	 * Применить функцию на все элементы в списке.
	 *
	 * @param consumer применяемая функция.
	 */
	public default void accept(final Consumer<? super E> consumer) {
		for(Node<E> node = getFirstNode(); node != null; node = node.getNext()) {
			consumer.accept(node.getItem());
		}
	}

	/**
	 * Применить функцию замены всех элементов.
	 *
	 * @param function применяемая функция.
	 */
	public default void apply(final Function<? super E, ? extends E> function) {
		for(Node<E> node = getFirstNode(); node != null; node = node.getNext()) {
			node.setItem(function.apply(node.getItem()));
		}
	}

	/**
	 * Получение элемента по номеру в списке.
	 * 
	 * @param index номер в списке.
	 * @return искомый элемент.
	 */
	public E get(int index);

	/**
	 * @return первый узел списка.
	 */
	public Node<E> getFirstNode();

	/**
	 * @return последний узел списка.
	 */
	public Node<E> getLastNode();

	/**
	 * @param object интересуемый объект.
	 * @return номер его в списке.
	 */
	public int indexOf(Object object);

	/**
	 * Блокировка изменения массива на время чтения его.
	 */
	public default void readLock() {
	}

	/**
	 * Разблокировка изменения массива.
	 */
	public default void readUnlock() {
	}

	/**
	 * Получание с удалением первого элемента.
	 * 
	 * @return первый элемент в очереди.
	 */
	public E take();

	/**
	 * Удаление узла в списке.
	 * 
	 * @param node удаляемый узел.
	 * @return удаленный элемент из узла.
	 */
	public E unlink(Node<E> node);

	/**
	 * Блокировка чтений для изменения массива.
	 */
	public default void writeLock() {
	}

	/**
	 * Разблокировка чтения массива.
	 */
	public default void writeUnlock() {
	}
}
