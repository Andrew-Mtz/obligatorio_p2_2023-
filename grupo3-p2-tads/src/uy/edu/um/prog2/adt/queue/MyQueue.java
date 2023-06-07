package uy.edu.um.prog2.adt.queue;


import uy.edu.um.prog2.adt.exceptions.EmptyQueueException;

public interface MyQueue <T> {

    void enqueue(T element);

    T dequeue() throws EmptyQueueException;

    boolean isEmpty();

    void imprimirQueue();

}
