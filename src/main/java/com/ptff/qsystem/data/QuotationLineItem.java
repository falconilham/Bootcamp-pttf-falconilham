package com.ptff.qsystem.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
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
	private Quotation quotation;

	@ManyToOne
	@JoinColumn(name="item_id", referencedColumnName="id")
	private Item item;
	
	@OneToMany(mappedBy="quotationLineItem", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<QuotationPricingTier> pricingTiers = new ArrayList<QuotationPricingTier>();
	
	
	public QuotationPricingTier getPriceTier(Long priceTierId) {
    	for (QuotationPricingTier pricingTier : pricingTiers) {
    		if (priceTierId.equals(pricingTier.getId()))
    			return pricingTier;
    	}
    	
    	return null;
    }
	
    public void addPriceTier(QuotationPricingTier pricingTier) {
	   pricingTiers.add(pricingTier);
	   pricingTier.setQuotationLineItem(this);
    }
 
    public void removePriceTier(QuotationPricingTier pricingTier) {
    	pricingTiers.remove(pricingTier);
    	pricingTier.setQuotationLineItem(null);
    }
    
    public void removePriceTier(int pricingTierOrderIndex) {
    	QuotationPricingTier pricingTier = pricingTiers.remove(pricingTierOrderIndex);
    	pricingTier.setQuotationLineItem(null);
    }
    
    @PreRemove
    private void preRemove() {
    	for (int i=0; i<pricingTiers.size(); i++)
    		removePriceTier(i);
    }
}
