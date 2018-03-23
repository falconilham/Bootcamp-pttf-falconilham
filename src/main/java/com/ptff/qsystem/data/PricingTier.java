package com.ptff.qsystem.data;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PricingTier {
	private Long lowerLimit;
	private Long upperLimit;
	private BigDecimal price;
}
