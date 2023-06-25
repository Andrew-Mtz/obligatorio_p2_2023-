package uy.edu.um.prog2.adt.queue;

import uy.edu.um.prog2.adt.exceptions.EmptyQueueException;
import uy.edu.um.prog2.adt.lista.Nodo;

public class MyPriorityQueueImpl<T> implements MyPriorityQueue<T> {

    private Nodo primero;
    private Nodo ultimo;

    public MyPriorityQueueImpl() {
        primero = null;
        ultimo = null;
    }

    public void enqueue(T element) {
        Nodo nuevoNodo = new Nodo(element);
        if (primero == null) {
            primero = nuevoNodo;
            ultimo = nuevoNodo;
            nuevoNodo.siguiente = null;
        } else {
            ultimo.siguiente = nuevoNodo;
            ultimo = nuevoNodo;
            nuevoNodo.siguiente = null;
        }
    }

    public T dequeue() throws EmptyQueueException {
        if (isEmpty()) {
            throw new EmptyQueueException();
        }
        T firstIn = (T) primero.getValor();
        primero = primero.getSiguiente();
        return firstIn;
    }

    public boolean isEmpty() {
        return (primero == null);
    }

    public void enqueueWithPriority(T element, int prioridad) {
        Nodo nuevoNodo = new Nodo(element, prioridad);
        if (primero == null) {
            primero = nuevoNodo;
            ultimo = nuevoNodo;
            nuevoNodo.siguiente = null;
            return;
        }
        if (nuevoNodo.prioridad > primero.prioridad) {
            nuevoNodo.siguiente = primero;
            primero = nuevoNodo;
            return;
        }
        Nodo puntero = primero;
        while (puntero.siguiente != null && puntero.siguiente.prioridad >= nuevoNodo.prioridad) {
            puntero = puntero.siguiente;
        }
        nuevoNodo.siguiente = puntero.siguiente;
        puntero.siguiente = nuevoNodo;
    }

    public int getPriority(T element) throws EmptyQueueException {
        Nodo puntero = primero;
        while (puntero != null) {
            if (puntero.value.equals(element)) {
                return puntero.prioridad;
            }
            puntero = puntero.siguiente;
        }

        // Element not found, throw an exception or return a default value
        throw new EmptyQueueException();
    }
    public void imprimirQueue() {
        Nodo puntero = primero;
        while (puntero != null) {
            System.out.println(puntero.getValor() + ": " + puntero.prioridad);
            puntero = puntero.getSiguiente();
        }
    }

    public int getSize() {
        int count = 0;
        Nodo current = primero;
        while (current != null) {
            count++;
            current = current.siguiente;
        }
        return count;
    }

    public T getFirst() throws EmptyQueueException {
        if (isEmpty()) {
            throw new EmptyQueueException();
        }
        return (T) primero.getValor();
    }

    public int getFirstPriority() throws EmptyQueueException {
        if (isEmpty()) {
            throw new EmptyQueueException();
        }
        return primero.prioridad;
    }


}
