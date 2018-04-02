package com.ptff.qsystem.data;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="item_quote_pricing_tiers")
public class QuotationPricingTier {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
    @ManyToOne
    private QuotationLineItem quotationLineItem;
    
	@Column(name="lower_limit", nullable=true)
	private Long lowerLimit;
	
	@Column(name="upper_limit", nullable=true)
	private Long upperLimit;
	
	@Column(name="price")
	@NotNull
	private BigDecimal price;
	
	@Column(name="is_single_price", columnDefinition="TINYINT(1)")
	private Boolean isSinglePrice;
}
