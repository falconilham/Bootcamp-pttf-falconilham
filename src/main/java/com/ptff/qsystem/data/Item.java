package com.ptff.qsystem.data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ptff.qsystem.data.converter.LocalDateTimePersistenceConverter;

import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name="items")
@Getter
@Setter
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
	    discriminatorType = DiscriminatorType.INTEGER,
	    name = "item_type_id",
	    columnDefinition = "TINYINT(1)"
	)
@EntityListeners(AuditingEntityListener.class)
public class Item {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(name="item_type_id", insertable=false, updatable=false)
	private ItemType itemType;
	
	@Column(name="description")
	private String description;
	
	@ManyToOne
	@JoinColumn(name="pricing_unit_id")
	private PricingUnit pricingUnit;
	
	@Column(name="status")
	private ItemStatus status = ItemStatus.REQUESTED;
	
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
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval = true)
	private Set<LegalNote> legalNotes = new HashSet<LegalNote>();
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval = true)
	private Set<ProductFeature> productFeatures = new HashSet<ProductFeature>();
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name="item_id")
	private Set<ItemPurchase> purchasePrices = new HashSet<ItemPurchase>();
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name="item_id")
	private Set<ItemMinimum> minimumPrices = new HashSet<ItemMinimum>();
	
	public List<ItemPurchase> getActivePurchasePrices() {
		return purchasePrices.stream()
				.filter(x -> x.getStatus() == ItemPriceStatus.ACTIVE)
				.collect(Collectors.toList());
	}
	
	public ItemMinimum getActiveMinimumPrice() {
		return minimumPrices.stream()
				.filter(x -> x.getStatus() == ItemPriceStatus.ACTIVE)
				.findFirst()
				.orElse(null);
	}
	
	
    public void addPurchasePrice(ItemPurchase purchasePrice) {
    	purchasePrices.add(purchasePrice);
    	purchasePrice.setItem(this);
    }
 
    public void removePurchasePrice(ItemPurchase purchasePrice) {
    	purchasePrices.remove(purchasePrice);
    	purchasePrice.setItem(null);
    }
    
    public String getName() {
    	return "<Name need to be Overriden>";
    }
}
