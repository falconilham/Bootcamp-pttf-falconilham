package com.ptff.qsystem.data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import com.ptff.qsystem.data.converter.LocalDatePersistenceConverter;
import com.ptff.qsystem.data.converter.LocalDateTimePersistenceConverter;

import lombok.Data;

@Data
@Entity
@Table(name="item_minimum")
@EntityListeners(AuditingEntityListener.class)
public class ItemMinimum {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="item_id")
	@NotNull
	private Item item;
		
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name="item_minimum_id")
	private List<ItemMinimumPricingTier> pricingTiers = new ArrayList<ItemMinimumPricingTier>();
	
	@Column(name="quote_date")
	@Convert(converter = LocalDatePersistenceConverter.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate quoteDate;
	
	@Column(name="review_date")
	@Convert(converter = LocalDatePersistenceConverter.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate reviewDate;
	
	@Column(name="status")
	private ItemPriceStatus status;
	

	@Column(name="create_date")
	@CreatedDate
	@Convert(converter = LocalDateTimePersistenceConverter.class)
	private LocalDateTime createTime;
	
	@Column(name="create_user")
	@CreatedBy
	private String createUser;
	
	@Column(name="lastupdate_date")
	@LastModifiedDate
    @Convert(converter = LocalDateTimePersistenceConverter.class)
	private LocalDateTime lastUpdateTime;
	
	@Column(name="lastupdate_user")
	@LastModifiedBy
	private String lastUpdateUser;
	
	
    public void addPriceTier(ItemMinimumPricingTier pricingTier) {
	   pricingTiers.add(pricingTier);
	   pricingTier.setItemMinimum(this);
    }
 
    public void removePriceTier(ItemMinimumPricingTier pricingTier) {
    	pricingTiers.remove(pricingTier);
    	pricingTier.setItemMinimum(null);
    }
    
    public void removePriceTier(int pricingTierOrderIndex) {
    	ItemMinimumPricingTier pricingTier = pricingTiers.remove(pricingTierOrderIndex);
    	pricingTier.setItemMinimum(null);
    }
}
