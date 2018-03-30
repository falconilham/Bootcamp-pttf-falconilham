package com.ptff.qsystem.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemAirFreightPurchaseRepository extends JpaRepository<ItemAirFreightPurchase, Long> {
	List<ItemAirFreightPurchase> findAllByItemAndStatus(ItemAirFreight itemAirFreight, ItemPriceStatus itemPurchaseStatus);
	List<ItemAirFreightPurchase> findAllByItem(ItemAirFreight itemAirFreight);
	List<ItemAirFreightPurchase> findAllByVendorAndItem(Vendor vendor, ItemAirFreight itemAirFreight);
}