package com.ptff.qsystem.service.impl;

import org.springframework.stereotype.Service;

import com.ptff.qsystem.service.CacheService;

import javax.cache.annotation.CacheRemove;
import javax.cache.annotation.CacheResult;

@Service
public class CacheServiceImpl implements CacheService {
    @Override
    @CacheResult(cacheName = "testCache")
    public int get(int key) {
        System.out.println("Returning cache value");
        return 0;
    }

    @Override
    @CacheRemove(cacheName = "testCache")
    public void delete(int a) {
        System.out.println("Removing cache value");
    }
}
