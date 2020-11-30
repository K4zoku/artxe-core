package me.kazoku.artxe.utils;

public class Pair<K, V> {
    protected final K key;
    protected final V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}
