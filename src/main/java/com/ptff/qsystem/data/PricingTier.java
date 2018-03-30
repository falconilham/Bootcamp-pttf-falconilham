package com.ptff.qsystem.data;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="item_permit_purchase_pricing_tiers")
public class PricingTier {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_permit_purchase_id")
    private ItemPermitPurchase itemPermitPurchase;
    
	@Column(name="lower_limit", nullable=true)
	private Long lowerLimit;
	
	@Column(name="upper_limit", nullable=true)
	private Long upperLimit;
	
	@Column(name="price")
	@NotNull
	private BigDecimal price;
}