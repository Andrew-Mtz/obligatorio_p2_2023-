package uy.edu.um.prog2.adt.heap;

public class HeapEntry<V> implements Comparable<HeapEntry<V>> {

    private float key;
    private V value;

    public HeapEntry(float key, V value) {
        this.key = key;
        this.value = value;
    }

    public float getKey() {
        return key;
    }

    public void setKey(float key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public int compareTo(HeapEntry aComparar) {
        int resultado = 0;
        if (this.key>aComparar.getKey()){
            return 1;
        } else if (this.key<aComparar.getKey()){
            return -1;
        }
        return resultado;
    }
}
