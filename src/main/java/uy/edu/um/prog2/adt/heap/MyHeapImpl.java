package uy.edu.um.prog2.adt.heap;

import java.util.NoSuchElementException;

public class MyHeapImpl<T extends Comparable<T>> implements MyHeap<T> {

    private static final int CAPACITY = 2;
    private T[] values;
    private int lastValuePosition;
    private boolean heapMax;

    public MyHeapImpl(T[] values, boolean heapMax) {
        this.values = values;
        this.lastValuePosition = values.length;
        this.heapMax = heapMax;
    }

    public MyHeapImpl(boolean heapMax) {
        this(CAPACITY, heapMax);
    }

    public MyHeapImpl(int capacity, boolean heapMax) {
        this.values = (T[]) new Comparable[capacity];
        this.lastValuePosition = 0;
        this.heapMax = heapMax;
    }

    @Override
    public void insert(T value) {
        if (lastValuePosition == values.length)
            resize();

        values[lastValuePosition] = value;
        int valuePosition = lastValuePosition;
        lastValuePosition++;

        if (heapMax) {
            while (valuePosition != 0 && value.compareTo(getFather(valuePosition)) > 0) {
                swap(valuePosition, getFatherPosition(valuePosition));
                valuePosition = getFatherPosition(valuePosition);
            }
        } else {
            while (valuePosition != 0 && value.compareTo(getFather(valuePosition)) < 0) {
                swap(valuePosition, getFatherPosition(valuePosition));
                valuePosition = getFatherPosition(valuePosition);
            }
        }
    }

    private void swap(int i, int j) {
        T temp = values[i];
        values[i] = values[j];
        values[j] = temp;
    }

    private void resize() {
        int newCapacity = values.length * 2;
        T[] newValues = (T[]) new Comparable[newCapacity];
        System.arraycopy(values, 0, newValues, 0, values.length);
        values = newValues;
    }

    private T getFather(int childPosition) {
        return values[(childPosition - 1) / 2];
    }

    private int getFatherPosition(int childPosition) {
        return (childPosition - 1) / 2;
    }

    @Override
    public T deleteAndReturn() {
        if (lastValuePosition == 0)
            throw new NoSuchElementException("Heap is empty");

        T root = values[0];
        values[0] = values[lastValuePosition - 1];
        values[lastValuePosition - 1] = null;
        lastValuePosition--;

        int currentPosition = 0;
        while (true) {
            int leftChildPosition = getLeftChildPosition(currentPosition);
            int rightChildPosition = getRightChildPosition(currentPosition);

            boolean hasLeftChild = leftChildPosition < lastValuePosition;
            boolean hasRightChild = rightChildPosition < lastValuePosition;

            if (!hasLeftChild)
                break; // Reached the end

            int maxChildPosition;
            if (hasRightChild) {
                // Both children exist
                if (heapMax) {
                    maxChildPosition = values[leftChildPosition].compareTo(values[rightChildPosition]) > 0 ?
                            leftChildPosition : rightChildPosition;
                } else {
                    maxChildPosition = values[leftChildPosition].compareTo(values[rightChildPosition]) < 0 ?
                            leftChildPosition : rightChildPosition;
                }
            } else {
                // Only left child exists
                maxChildPosition = leftChildPosition;
            }

            if (heapMax) {
                if (values[currentPosition].compareTo(values[maxChildPosition]) < 0) {
                    swap(currentPosition, maxChildPosition);
                    currentPosition = maxChildPosition;
                } else {
                    break;
                }
            } else {
                if (values[currentPosition].compareTo(values[maxChildPosition]) > 0) {
                    swap(currentPosition, maxChildPosition);
                    currentPosition = maxChildPosition;
                } else {
                    break;
                }
            }
        }

        return root;
    }

    private int getLeftChildPosition(int fatherPosition) {
        return (2 * fatherPosition) + 1;
    }

    private int getRightChildPosition(int fatherPosition) {
        return (2 * fatherPosition) + 2;
    }

    @Override
    public int size() {
        return lastValuePosition;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < lastValuePosition; i++) {
            output.append(values[i]).append(" ");
            double power = Math.log10(i + 2) / Math.log10(2);
            if (power % 1 == 0) {
                output.append("\n");
            }
        }
        return output.toString();
    }
}
