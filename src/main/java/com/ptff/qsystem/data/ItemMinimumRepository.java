package com.ptff.qsystem.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemMinimumRepository extends JpaRepository<ItemMinimum, Long> {

	List<ItemMinimum> findAllByItemAndStatus( Item item, ItemPriceStatus itemPurchaseStatus);
	List<ItemMinimum> findAllByItem( Item item);
    
}