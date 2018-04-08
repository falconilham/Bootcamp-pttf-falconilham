package com.ptff.qsystem.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QuotationLineItemRepository extends JpaRepository<QuotationLineItem, Long> {
    List<QuotationLineItem> findAllByItem(Item item);
}