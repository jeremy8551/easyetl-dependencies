package icu.etl.collection;

import java.util.PriorityQueue;
import java.util.Queue;

import org.junit.Test;

public class ArrayDequeTest {

    @Test
    public void test() {
        ArrayDeque<String> queue = new ArrayDeque<String>();
        queue.add("1");
        queue.add("2");
        queue.add("3");

        System.out.println(queue.poll() + " " + queue.size());
        System.out.println(queue.poll() + " " + queue.size());
        System.out.println(queue.poll() + " " + queue.size());
        System.out.println(queue.poll() + " " + queue.size());
        System.out.println(queue.poll() + " " + queue.size());
    }

    @Test
    public void test1() {
        Queue<Integer> priorityQueue = new PriorityQueue<Integer>();
        priorityQueue.add(1);
        priorityQueue.add(4);
        priorityQueue.add(2);
        priorityQueue.add(3);

        System.out.println(priorityQueue.poll());
        System.out.println(priorityQueue.poll());
        System.out.println(priorityQueue.poll());
        System.out.println(priorityQueue.poll());
    }
}
