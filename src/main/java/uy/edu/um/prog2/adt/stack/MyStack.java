package uy.edu.um.prog2.adt.stack;

import uy.edu.um.prog2.adt.exceptions.EmptyStackException;



public interface MyStack<T> {

    T pop() throws EmptyStackException;

    T top() throws EmptyStackException;

    void push(T element);

    boolean isEmpty();

    void makeEmpty();

    void imprimirStack();

}
