package com.ptff.qsystem.data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
	
	@Column(name="status")
	private ItemStatus status = ItemStatus.REQUESTED;
	
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval = true)
	private Set<LegalNote> legalNotes;
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval = true)
	private Set<ProductFeature> productFeatures;
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name="item_id")
	private Set<ItemPurchase> purchasePrices = new HashSet<ItemPurchase>();
	
	public List<ItemPurchase> getActivePurchasePrices() {
		return purchasePrices.stream()
				.filter(x -> x.getStatus() == ItemPurchaseStatus.ACTIVE)
				.collect(Collectors.toList());
	}
	
	
    public void addPurchasePrice(ItemPurchase purchasePrice) {
    	purchasePrices.add(purchasePrice);
    	purchasePrice.setItem(this);
    }
 
    public void removePurchasePrice(ItemPurchase purchasePrice) {
    	purchasePrices.remove(purchasePrice);
    	purchasePrice.setItem(null);
    }
}
