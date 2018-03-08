package com.ptff.qsystem.service;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {
	public static final String DEFAULT_LOCATION = "."; 
			
	private String location = DEFAULT_LOCATION;
	
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
