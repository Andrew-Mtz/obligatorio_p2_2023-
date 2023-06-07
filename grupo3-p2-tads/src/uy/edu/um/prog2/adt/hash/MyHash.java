package uy.edu.um.prog2.adt.hash;



public interface MyHash<K, V> {

    void put(K k, V v);

    void remove(K key);

    V get(K k);

    int size();

    HashEntry<K,V>[] getValues();

}
