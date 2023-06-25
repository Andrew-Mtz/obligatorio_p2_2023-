package uy.edu.um.prog2.adt.queue;

import uy.edu.um.prog2.adt.exceptions.EmptyQueueException;

public interface MyPriorityQueue<T> extends MyQueue<T> {

    void enqueueWithPriority (T element, int prioridad);
    int getPriority (T element) throws EmptyQueueException;

    public void imprimirQueue();
    public int getSize();
    public T dequeue() throws EmptyQueueException;
    public T getFirst() throws EmptyQueueException;

    public int getFirstPriority() throws EmptyQueueException;






}
