package queue;


import org.junit.jupiter.api.Test;
import uy.edu.um.prog2.adt.exceptions.EmptyQueueException;
import uy.edu.um.prog2.adt.queue.MyQueue;
import uy.edu.um.prog2.adt.queue.MyQueueImpl;

import static org.junit.jupiter.api.Assertions.*;

class MyQueueImplTest {

    @Test
    void flujoPrincipal() {

        MyQueue<Integer> queueTest = new MyQueueImpl<>();

        queueTest.enqueue(3);
        queueTest.enqueue(5);
        queueTest.enqueue(8);
        queueTest.enqueue(11);
        queueTest.enqueue(15);

        try {
            Integer eliminado = queueTest.dequeue();
            assertEquals(3,eliminado);
        } catch (EmptyQueueException e) {
            System.out.println("Queue Empty");
        }

        assertFalse(queueTest.isEmpty());

        queueTest.imprimirQueue();

    }
}