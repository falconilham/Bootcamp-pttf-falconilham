package com.ptff.qsystem.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ptff.qsystem.data.Item;
import com.ptff.qsystem.data.ItemType;

public interface ItemService {
	public Page<Item> list(ItemType itemType, Pageable pageable);

	public Item createItem(ItemType itemType);
	public Item getItem(ItemType itemType, Long itemId);

	public void addLegalNote(Long itemId, Long legalNoteId);
	public void addProductFeature(Long itemId, Long productFeatureId);

	public void removeLegalNote(Long itemId, Long legalNoteId);
	public void removeProductFeature(Long itemId, Long productFeatureId);

	public void submitForApproval(Long itemId);
	public void availableForTransaction(Long itemId);
}
