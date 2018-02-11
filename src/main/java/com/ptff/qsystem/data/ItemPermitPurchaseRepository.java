package com.ptff.qsystem.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemPermitPurchaseRepository extends JpaRepository<ItemPermitPurchase, Long> {

	List<ItemPermitPurchase> findAllByItemAndStatus(ItemPermit itemPermit, ItemPurchaseStatus itemPurchaseStatus);
	List<ItemPermitPurchase> findAllByItem(ItemPermit itemPermit);
	List<ItemPermitPurchase> findAllByVendorAndItem(Vendor vendor, ItemPermit itemPermit);
    
}