package com.ptff.qsystem.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ptff.qsystem.service.CacheService;

@Controller
@RequestMapping("/cache")
public class CacheController {

    private final CacheService cacheService;

    public CacheController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @GetMapping(value = "{value}")
    public int getCacheValue(@PathVariable("value") int value) {
        return cacheService.get(value);
    }

    @DeleteMapping(value = "{value}")
    public void removeCacheValue(@PathVariable("value") int value) {
        cacheService.delete(value);
    }
}
