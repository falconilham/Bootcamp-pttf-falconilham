package com.ptff.qsystem.service;

public interface CacheService {

    /**
     * Retrieves value by the given key
     *
     * @param key key
     * @return value
     */
    int get(int key);

    void delete(int a);
}