package com.ptff.qsystem.data;

public enum ItemType {
	AIR_FREIGHT("airfreight", "Air Freight"), 
	SEA_FREIGHT("seafreight", "Sea Freight"), 
	TRUCKING("truck", "Trucking"), 
	PERMIT("permit", "Permit"),
	ADDITIONAL("additional", "Additional");
	
	public String path;
	public String description;
	
	ItemType(String path, String description) {
		this.path = path;
		this.description = description;
	}
}
