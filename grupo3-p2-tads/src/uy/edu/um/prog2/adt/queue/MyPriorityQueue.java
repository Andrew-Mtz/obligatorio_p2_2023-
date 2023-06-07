package uy.edu.um.prog2.adt.queue;

public interface MyPriorityQueue<T> extends MyQueue<T> {

    void enqueueWithPriority (T element, int prioridad);

}
