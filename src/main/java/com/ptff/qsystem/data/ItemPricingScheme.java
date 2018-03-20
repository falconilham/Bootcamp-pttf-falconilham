package com.ptff.qsystem.data;

public enum ItemPricingScheme {
	ALL("Per Item"), TIER("Tiered"), TIER_FIX("Fix Tier"); 
	
	String description;
	
	ItemPricingScheme(String description) {
		this.description = description;
	}
}
