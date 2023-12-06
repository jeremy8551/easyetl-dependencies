package icu.etl.collection;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * Deque接口的可调整大小的数组实现。阵列设备没有容量限制；
 * 它们根据需要增长以支持使用。它们不是线程安全的；
 * 在没有外部同步的情况下，它们不支持多线程并发访问。
 * 禁止使用空元素。当用作堆栈时，此类可能比堆栈快，当用作队列时，此类可能比LinkedList快。
 * 大多数ArrayDeque操作在摊销的固定时间内运行。
 * 例外情况包括 remove、removeFirstOccurrence、RemoveAsToccurrence、contains、iterator.remove（）和批量操作，所有这些操作都在线性时间内运行。
 * 这个类的迭代器方法返回的迭代器是快速失效的：如果在迭代器创建之后的任何时候，以迭代器自己的remove方法以外的任何方式修改了deque，迭代器通常会抛出ConcurrentModificationException。
 * 因此，在面对并发修改时，迭代器会快速、干净地失败，而不是在将来的不确定时间冒着任意、不确定行为的风险。
 * 请注意，无法保证迭代器的快速失效行为，因为一般来说，在存在非同步并发修改的情况下，不可能做出任何硬保证。
 * 快速失败迭代器会尽最大努力抛出ConcurrentModificationException。
 * 因此，编写依赖于此异常的正确性的程序是错误的：迭代器的快速失败行为应该只用于检测bug。
 * 此类及其迭代器实现集合和迭代器接口的所有可选方法。
 * 此类是Java集合框架的成员。
 *
 * @param <E>
 * @author jeremy8551@qq.com
 */
@SuppressWarnings("unchecked")
public class ArrayDeque<E> extends AbstractCollection<E> implements Cloneable, Serializable {
    private final static long serialVersionUID = 1L;

    /**
     * 存储队列元素的数组。deque的容量是这个数组的长度，它总是2的幂。
     * 数组永远不允许变满，除非在addX方法中，数组在变满后立即调整大小（请参见doubleCapacity），从而避免头部和尾部缠绕以彼此相等。
     * 我们还保证所有不包含deque元素的数组单元始终为空。
     */
    private transient Object[] elements;

    /**
     * deque头部元素的索引（该元素将由remove() 或 pop() 删除）；或者，如果 deque 为空，则为等于 tail 的任意数字。
     */
    private transient int head;

    /**
     * 将下一个元素添加到deque尾部的索引（通过addLast（E）、add（E）或push（E））。
     */
    private transient int tail;

    /**
     * 我们将用于新创建的deque的最小容量。必须是2的幂。
     */
    public final static int MIN_INITIAL_CAPACITY = 8;

    private static int calculateSize(int numElements) {
        int initialCapacity = MIN_INITIAL_CAPACITY;
        // 找到两个元素的最佳幂来容纳元素。
        // 测试 “<=”，因为数组没有满。
        if (numElements >= initialCapacity) {
            initialCapacity = numElements;
            initialCapacity |= (initialCapacity >>> 1);
            initialCapacity |= (initialCapacity >>> 2);
            initialCapacity |= (initialCapacity >>> 4);
            initialCapacity |= (initialCapacity >>> 8);
            initialCapacity |= (initialCapacity >>> 16);
            initialCapacity++;

            if (initialCapacity < 0) { // 元素太多，必须后退
                initialCapacity >>>= 1;// 分配 2^30 个元素
            }
        }
        return initialCapacity;
    }

    /**
     * 分配空数组以容纳给定数量的元素。
     *
     * @param number 元素数
     */
    private void allocateElements(int number) {
        int size = calculateSize(number);
        this.elements = new Object[size];
    }

    /**
     * 将此设备的容量增加一倍。只有在满的时候才呼叫，也就是说，当头部和尾部缠绕成相等的时候。
     */
    private void doubleCapacity() {
        if (this.head != this.tail) {
            throw new IllegalArgumentException();
        }

        int p = this.head;
        int n = this.elements.length;
        int r = n - p; // number of elements to the right of p
        int newCapacity = n << 1;
        if (newCapacity < 0) {
            throw new IllegalStateException("Sorry, deque too big");
        }

        Object[] a = new Object[newCapacity];
        System.arraycopy(this.elements, p, a, 0, r);
        System.arraycopy(this.elements, 0, a, r, p);
        this.elements = a;
        this.head = 0;
        this.tail = n;
    }

    /**
     * 按顺序（从deque中的第一个元素到最后一个元素）将元素从元素数组复制到指定的数组中。
     * 假设数组足够大，可以容纳deque中的所有元素。
     *
     * @return its 元素数组
     */
    private <T> T[] copyElements(T[] a) {
        if (this.head < this.tail) {
            System.arraycopy(this.elements, this.head, a, 0, size());
        } else if (this.head > this.tail) {
            int headPortionLen = this.elements.length - this.head;
            System.arraycopy(this.elements, this.head, a, 0, headPortionLen);
            System.arraycopy(this.elements, 0, a, headPortionLen, this.tail);
        }
        return a;
    }

    /**
     * 构造一个初始容量足以容纳16个元素的空数组队列。
     */
    public ArrayDeque() {
        this.elements = new Object[16];
    }

    /**
     * 构造一个空数组队列，其初始容量足以容纳指定数量的元素。
     *
     * @param size 队列 初始容量的下界
     */
    public ArrayDeque(int size) {
        this.allocateElements(size);
    }

    /**
     * 按照集合迭代器返回的顺序构造包含指定集合元素的队列。
     * （集合的迭代器返回的第一个元素将成为第一个元素，或队列的前面。）
     *
     * @param c 要将其元素放置到队列中的集合
     * @throws NullPointerException
     */
    public ArrayDeque(Collection<? extends E> c) {
        this.allocateElements(c.size());
        this.addAll(c);
    }

    /**
     * 在此队列的指定位置上插入指定的元素。
     *
     * @param e the element to add
     * @throws NullPointerException if the specified element is null
     */
    public void addFirst(E e) {
        if (e == null) {
            throw new NullPointerException();
        }

        this.elements[this.head = (this.head - 1) & (this.elements.length - 1)] = e;
        if (this.head == this.tail) {
            this.doubleCapacity();
        }
    }

    /**
     * 在此队列的末尾插入指定的元素。
     *
     * <p>
     * 此方法相当于 {@link #add}.
     *
     * @param e the element to add
     * @throws NullPointerException if the specified element is null
     */
    public void addLast(E e) {
        if (e == null) {
            throw new NullPointerException();
        }

        this.elements[this.tail] = e;
        if ((this.tail = (this.tail + 1) & (this.elements.length - 1)) == this.head) {
            this.doubleCapacity();
        }
    }

    /**
     * 将指定的元素插入此三角形的前面。
     *
     * @param e 要添加的元素
     * @return {@code true} (由 {@link Deque#offerFirst} 这个决定)
     * @throws NullPointerException
     */
    public boolean offerFirst(E e) {
        this.addFirst(e);
        return true;
    }

    /**
     * 在此队列的末尾插入指定的元素。
     *
     * @param e 要添加的元素
     * @return {@code true} (由 {@link Deque#offerLast} 这个决定)
     * @throws NullPointerException
     */
    public boolean offerLast(E e) {
        this.addLast(e);
        return true;
    }

    /**
     * 移除队列中第一个元素
     *
     * @throws NoSuchElementException {@inheritDoc}
     */
    public E removeFirst() {
        E x = this.pollFirst();
        if (x == null) {
            throw new NoSuchElementException();
        } else {
            return x;
        }
    }

    /**
     * 移除队列尾部的元素
     *
     * @throws NoSuchElementException {@inheritDoc}
     */
    public E removeLast() {
        E x = this.pollLast();
        if (x == null) {
            throw new NoSuchElementException();
        } else {
            return x;
        }
    }

    public E pollFirst() {
        int h = this.head;

        E result = (E) this.elements[h];
        // 如果 队列 为空，则元素为null
        if (result == null) {
            return null;
        }

        this.elements[h] = null; // Must null out slot
        this.head = (h + 1) & (this.elements.length - 1);
        return result;
    }

    public E pollLast() {
        int t = (this.tail - 1) & (this.elements.length - 1);

        E result = (E) this.elements[t];
        if (result == null) {
            return null;
        }

        this.elements[t] = null;
        this.tail = t;
        return result;
    }

    /**
     * 返回队列中第一个元素
     *
     * @throws NoSuchElementException {@inheritDoc}
     */
    public E getFirst() {
        E result = (E) this.elements[this.head];
        if (result == null) {
            throw new NoSuchElementException();
        } else {
            return result;
        }
    }

    /**
     * 返回队列尾端的元素
     *
     * @throws NoSuchElementException {@inheritDoc}
     */
    public E getLast() {
        E result = (E) this.elements[(this.tail - 1) & (this.elements.length - 1)];
        if (result == null) {
            throw new NoSuchElementException();
        } else {
            return result;
        }
    }

    public E peekFirst() {
        // 如果队列为空，则元素[head]为空
        return (E) this.elements[this.head];
    }

    public E peekLast() {
        return (E) this.elements[(this.tail - 1) & (this.elements.length - 1)];
    }

    /**
     * 删除此队列中指定元素的第一个匹配项（从头到尾遍历队列时）。
     * 如果队列不包含该元素，则该元素将保持不变。
     * 更正式地说，删除第一个元素e，使得o等于（e）（如果存在这样的元素）。
     * 如果此队列包含指定的元素，则返回true（或者如果此队列因调用而更改，则返回等效值）。
     *
     * @param o 从该队列中删除的元素（如果存在）
     * @return {@code true} 如果 队列 包含指定的元素
     */
    public boolean removeFirstOccurrence(Object o) {
        if (o == null) {
            return false;
        }

        int mask = this.elements.length - 1;
        int i = this.head;
        Object x;
        while ((x = this.elements[i]) != null) {
            if (o.equals(x)) {
                this.delete(i);
                return true;
            }
            i = (i + 1) & mask;
        }
        return false;
    }

    /**
     * 删除此 队列 中指定元素的最后一个匹配项（从头到尾遍历 队列 时）。
     * 如果 队列 不包含该元素，则该元素将保持不变。更正式地说，删除最后一个元素e，使o等于（e）（如果存在这样的元素）。
     * 如果此 队列 包含指定的元素，则返回 true（或者如果此 队列 因调用而更改，则返回等效值）。
     *
     * @param o 从中删除的元素（如果存在）
     * @return {@code true} 如果 队列 包含指定的元素
     */
    public boolean removeLastOccurrence(Object o) {
        if (o == null) {
            return false;
        }

        int mask = this.elements.length - 1;
        int i = (this.tail - 1) & mask;
        Object x;
        while ((x = this.elements[i]) != null) {
            if (o.equals(x)) {
                this.delete(i);
                return true;
            }
            i = (i - 1) & mask;
        }
        return false;
    }

    /**
     * 在此队列的指定位置上插入指定的元素。
     *
     * <p>
     * 此方法相当于 {@link #addLast}.
     *
     * @param e 要添加的元素
     * @return 由 {@link Collection#add} 指定
     * @throws NullPointerException
     */
    public boolean add(E e) {
        this.addLast(e);
        return true;
    }

    /**
     * 在此队列的末尾插入指定的元素。
     *
     * <p>
     * 此方法相当于 {@link #offerLast}.
     *
     * @param e 要添加的元素
     * @return {@code true} 由 {@link Queue#offer} 指定
     * @throws NullPointerException
     */
    public boolean offer(E e) {
        return this.offerLast(e);
    }

    /**
     * 检索并删除此队列表示的队列头。此方法与poll的不同之处在于，如果此队列为空，则会引发异常。
     *
     * <p>
     * 此方法相当于 {@link #removeFirst}.
     *
     * @return 此 队列 表示的队列的头
     * @throws NoSuchElementException {@inheritDoc}
     */
    public E remove() {
        return this.removeFirst();
    }

    /**
     * 检索并删除此队列表示的队列头（换句话说，此队列的第一个元素），如果此队列为空，则返回null。
     *
     * <p>
     * 此方法相当于 {@link #pollFirst}.
     *
     * @return 此队列表示的队列头，如果此队列为空，则为null
     */
    public E poll() {
        return this.pollFirst();
    }

    /**
     * 检索但不删除此队列表示的队列头。
     * 此方法与peek的不同之处在于，如果此队列为空，则会引发异常。
     *
     * <p>
     * 此方法相当于 {@link #getFirst}.
     *
     * @return 此队列表示的队列的头
     * @throws NoSuchElementException {@inheritDoc}
     */
    public E element() {
        return this.getFirst();
    }

    /**
     * 检索但不删除此队列表示的队列头，如果此队列为空，则返回null。
     * 此方法相当于 {@link #peekFirst}.
     *
     * @return 此队列表示的队列头，如果此队列为空，则为null
     */
    public E peek() {
        return this.peekFirst();
    }

    /**
     * 在该队列的前面插入元素。
     *
     * <p>
     * 此方法相当于 {@link #addFirst}.
     *
     * @param e 要添加的元素
     * @throws NullPointerException
     */
    public void push(E e) {
        this.addFirst(e);
    }

    /**
     * 删除并返回此队列的第一个元素。
     *
     * <p>
     * 此方法等效于 {@link #removeFirst()}.
     *
     * @return 此队列前面的元素（此队列表示的堆栈顶部）
     * @throws NoSuchElementException {@inheritDoc}
     */
    public E pop() {
        return this.removeFirst();
    }

    private void checkInvariants() {
        assert this.elements[this.tail] == null;
        assert this.head == this.tail ? this.elements[this.head] == null : (this.elements[head] != null && this.elements[(tail - 1) & (this.elements.length - 1)] != null);
        assert this.elements[(this.head - 1) & (this.elements.length - 1)] == null;
    }

    /**
     * 在元素数组中的指定位置删除元素，并根据需要调整头部和尾部。这可能导致元素在数组中前后移动。
     * 此方法称为delete而不是remove，以强调其语义不同于 List.remove(int）的语义。
     *
     * @return
     */
    private boolean delete(int i) {
        this.checkInvariants();
        final Object[] elements = this.elements;
        final int mask = elements.length - 1;
        final int h = this.head;
        final int t = this.tail;
        final int front = (i - h) & mask;
        final int back = (t - i) & mask;

        // Invariant: head <= i < tail mod circularity
        if (front >= ((t - h) & mask))
            throw new ConcurrentModificationException();

        // Optimize for least element motion
        if (front < back) {
            if (h <= i) {
                System.arraycopy(elements, h, elements, h + 1, front);
            } else { // Wrap around
                System.arraycopy(elements, 0, elements, 1, i);
                elements[0] = elements[mask];
                System.arraycopy(elements, h, elements, h + 1, mask - h);
            }
            elements[h] = null;
            this.head = (h + 1) & mask;
            return false;
        } else {
            if (i < t) { // Copy the null tail as well
                System.arraycopy(elements, i + 1, elements, i, back);
                this.tail = t - 1;
            } else { // Wrap around
                System.arraycopy(elements, i + 1, elements, i, mask - i);
                elements[mask] = elements[0];
                System.arraycopy(elements, 1, elements, 0, t);
                this.tail = (t - 1) & mask;
            }
            return true;
        }
    }

    /**
     * 返回此队列中的元素数。
     *
     * @return
     */
    public int size() {
        return (this.tail - this.head) & (this.elements.length - 1);
    }

    /**
     * 如果此数据不包含任何元素，则返回true。
     *
     * @return
     */
    public boolean isEmpty() {
        return this.head == this.tail;
    }

    /**
     * 返回此队列中元素的迭代器。元素将从第一个（头部）到最后一个（尾部）排序。这与元素的出列顺序（通过连续调用移除或弹出）相同。
     *
     * @return 此数据集中元素的迭代器
     */
    public Iterator<E> iterator() {
        return new DeqIterator();
    }

    private class DeqIterator implements Iterator<E> {

        private int cursor = head;

        private int fence = tail;

        private int lastRet = -1;

        public boolean hasNext() {
            return this.cursor != this.fence;
        }

        public E next() {
            if (cursor == fence) {
                throw new NoSuchElementException();
            }

            E result = (E) elements[cursor];
            // 此检查未捕获所有可能的共修改，
            // 但是否捕获了破坏遍历的对象
            if (tail != fence || result == null) {
                throw new ConcurrentModificationException();
            }
            lastRet = cursor;
            cursor = (cursor + 1) & (elements.length - 1);
            return result;
        }

        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
            if (delete(lastRet)) { // if left-shifted, undo increment in next()
                cursor = (cursor - 1) & (elements.length - 1);
                fence = tail;
            }
            lastRet = -1;
        }

    }

    /**
     * 如果此数据包含指定的元素，则返回true。
     * 更正式地说，当且仅当此队列至少包含一个元素e，使得o.equals（e）时，返回true。
     *
     * @param o 要检查此队列中是否包含的对象
     * @return {@code true} 如果此数据包含指定的元素
     */
    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }

        int mask = this.elements.length - 1;
        int i = this.head;
        Object x;
        while ((x = this.elements[i]) != null) {
            if (o.equals(x)) {
                return true;
            }
            i = (i + 1) & mask;
        }
        return false;
    }

    /**
     * 从此数据集中删除指定元素的单个实例。
     * 如果队列不包含该元素，则该元素将保持不变。
     * 更正式地说，删除第一个元素e，使得o等于（e）（如果存在这样的元素）。
     * 如果此队列包含指定的元素，则返回true（或者如果此队列因调用而更改，则返回等效值）。
     *
     * <p>
     * 此方法相当于 {@link #removeFirstOccurrence(Object)}.
     *
     * @param o 从该队列中删除的元素（如果存在）
     * @return {@code true} 如果此数据包含指定的元素
     */
    public boolean remove(Object o) {
        return this.removeFirstOccurrence(o);
    }

    /**
     * 从该数据集中删除所有元素。此调用返回后，队列将为空。
     */
    public void clear() {
        int h = this.head;
        int t = this.tail;
        if (h != t) { // clear all cells
            this.head = this.tail = 0;
            int i = h;
            int mask = this.elements.length - 1;
            do {
                this.elements[i] = null;
                i = (i + 1) & mask;
            } while (i != t);
        }
    }

    /**
     * 返回一个数组，该数组按正确顺序（从第一个元素到最后一个元素）包含此数据集中的所有元素。
     * 返回的数组将是 “安全的”，因为此队列不维护对它的引用。（换句话说，此方法必须分配一个新数组）。
     * 因此，调用者可以自由修改返回的数组。
     * 此方法充当基于阵列和基于集合的API之间的桥梁。
     *
     * @return 当前队列中所有元素的数组
     */
    public Object[] toArray() {
        return this.copyElements(new Object[size()]);
    }

    /**
     * 返回一个数组，该数组按正确顺序（从第一个元素到最后一个元素）包含此数据集中的所有元素；
     * 返回数组的运行时类型是指定数组的运行时类型。如果队列适合指定的数组，则返回该数组。
     * 否则，将使用指定数组的运行时类型和此队列的大小分配一个新数组。
     * 如果此队列适合具有空闲空间的指定数组（即，该数组的元素数多于此队列），则紧跟在队列末尾的数组中的元素将设置为null。
     * 与toArray（）方法一样，此方法充当基于数组和基于集合的API之间的桥梁。此外，此方法允许对输出数组的运行时类型进行精确控制，并且在某些情况下可用于节省分配成本。
     * 假设x是已知只包含字符串的队列。以下代码可用于将队列转储到新分配的字符串数组中：
     *
     * <pre>
     * {
     * 	&#64;code
     * 	String[] y = x.toArray(new String[0]);
     * }
     * </pre>
     * <p>
     * 请注意，{@code toArray(new Object[0])} 在功能上与 {@code toArray()} 相同。
     *
     * @param a 存储数据块元素的数组（如果足够大）；否则将为此目的分配相同运行时类型的新数组
     * @return 包含此数据集中所有元素的数组
     * @throws ArrayStoreException
     * @throws NullPointerException
     */
    public <T> T[] toArray(T[] a) {
        int size = size();
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }

        this.copyElements(a);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    /**
     * 返回此队列的一个副本
     *
     * @return 此队列的一个副本
     */
    public ArrayDeque<E> clone() {
        try {
            ArrayDeque<E> result = (ArrayDeque<E>) super.clone();
            result.elements = Arrays.copyOf(this.elements, this.elements.length);
            return result;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

}
