package uy.edu.um.prog2.adt.queue;

import uy.edu.um.prog2.adt.exceptions.EmptyQueueException;

public interface MyDoubleQueue <T> {

    void enqueueLeft(T element);

    T dequeueLeft() throws EmptyQueueException;

    void enqueueRight(T element);

    T dequeueRight() throws EmptyQueueException, EmptyQueueException;

    boolean isEmpty();

    void imprimirQueue();

}
