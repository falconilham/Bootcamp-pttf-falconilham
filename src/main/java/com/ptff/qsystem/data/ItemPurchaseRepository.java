package com.ptff.qsystem.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemPurchaseRepository extends JpaRepository<ItemPurchase, Long> {

	List<ItemPurchase> findAllByItemAndStatus(ItemPermit itemPermit, ItemPriceStatus itemPurchaseStatus);
	List<ItemPurchase> findAllByItem(ItemPermit itemPermit);
	List<ItemPurchase> findAllByVendorAndItem(Vendor vendor, ItemPermit itemPermit);
	List<ItemPurchase> findAllByVendor(Vendor vendor);
    
}