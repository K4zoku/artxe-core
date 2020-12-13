package me.kazoku.artxe.utils;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class Cache<K, V> {

    protected Map<K, V> cacheMap;

    public Cache() {
        cacheMap = new LinkedHashMap<>();
    }

    protected abstract V create(K k);

    protected V get(K k) {
        return cacheMap.computeIfAbsent(k, this::create);
    }
}
