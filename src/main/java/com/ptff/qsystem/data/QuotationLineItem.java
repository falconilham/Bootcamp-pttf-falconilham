package com.ptff.qsystem.data;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="quotation_line_item")
@Getter
@Setter
public class QuotationLineItem {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="quotation_id", referencedColumnName="id")
	private Quotation quotation;
	
	@Column(name="type")
	private QuotationProductType type;
	
	@ManyToOne
	@JoinColumn(name="itempermit_id", referencedColumnName="id")
	private ItemPermit permit;
	
	@ManyToOne
	@JoinColumn(name="itemairfreight_id", referencedColumnName="id")
	private ItemAirFreight airfreight;
	
	@ManyToOne
	@JoinColumn(name="itemtrucking_id", referencedColumnName="id")
	private ItemTrucking trucking;
	
	@Column(name="quoted_price")
	private BigDecimal quotedPrice;
}
