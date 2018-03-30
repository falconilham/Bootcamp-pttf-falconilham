package com.ptff.qsystem.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemTruckingPurchaseRepository extends JpaRepository<ItemTruckingPurchase, Long> {
	List<ItemTruckingPurchase> findAllByItemAndStatus(ItemTrucking itemTrucking, ItemPriceStatus itemPurchaseStatus);
	List<ItemTruckingPurchase> findAllByItem(ItemTrucking itemTrucking);
	List<ItemTruckingPurchase> findAllByVendorAndItem(Vendor vendor, ItemTrucking itemTrucking);
}