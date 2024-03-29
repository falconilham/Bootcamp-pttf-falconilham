package com.ptff.qsystem.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.ptff.qsystem.data.Item;
import com.ptff.qsystem.data.ItemAdditional;
import com.ptff.qsystem.data.ItemAdditionalRepository;
import com.ptff.qsystem.data.ItemAirFreight;
import com.ptff.qsystem.data.ItemAirFreightRepository;
import com.ptff.qsystem.data.ItemPermit;
import com.ptff.qsystem.data.ItemPermitRepository;
import com.ptff.qsystem.data.ItemRepository;
import com.ptff.qsystem.data.ItemSeaFreight;
import com.ptff.qsystem.data.ItemSeaFreightRepository;
import com.ptff.qsystem.data.ItemStatus;
import com.ptff.qsystem.data.ItemTrucking;
import com.ptff.qsystem.data.ItemTruckingRepository;
import com.ptff.qsystem.data.ItemType;
import com.ptff.qsystem.data.LegalNote;
import com.ptff.qsystem.data.LegalNoteRepository;
import com.ptff.qsystem.data.ProductFeature;
import com.ptff.qsystem.data.ProductFeatureRepository;
import com.ptff.qsystem.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemServiceImpl.class);

	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private ItemPermitRepository itemPermitRepository;
	
	@Autowired
	private ItemAirFreightRepository itemAirFreightRepository;

	@Autowired
	private ItemSeaFreightRepository itemSeaFreightRepository;
	
	@Autowired
	private ItemAdditionalRepository itemAdditionalRepository;
	
	@Autowired
	private ItemTruckingRepository itemTruckingRepository;
	
	
	@Autowired
	private LegalNoteRepository legalNoteRepository;
	

	@Autowired
	private ProductFeatureRepository productFeatureRepository;
	
	public Page<Item> list(ItemType itemType, Pageable pageable) {
		return getRepository(itemType).findAll(pageable);
	}
	
	@Override
	public Item createItem(ItemType itemType) {
		switch(itemType) {
		case PERMIT:
			return new ItemPermit();
			
		case AIR_FREIGHT:
			return new ItemAirFreight();
		
		case SEA_FREIGHT:
			return new ItemSeaFreight();
			
		case ADDITIONAL:
			return new ItemAdditional();
		
		case TRUCKING:
			return new ItemTrucking();
				
			
		default:
			return null;
		}
	}

	@Override
	public Item getItem(ItemType itemType, Long itemId) {
		return (Item) getRepository(itemType).findOne(itemId);
	}

	private JpaRepository getRepository(ItemType itemType) {
		switch(itemType) {
		case PERMIT:
			return itemPermitRepository;
			
		case AIR_FREIGHT:
			return itemAirFreightRepository;
		
		case SEA_FREIGHT:
			return itemSeaFreightRepository;
				
		case ADDITIONAL:
			return itemAdditionalRepository;
		
		case TRUCKING:
			return itemTruckingRepository;
			
		default:
			return null;
		}
	}

	@Override
	public void addLegalNote(Long itemId, Long legalNoteId) {
		Item item = itemRepository.findOne(itemId);
		LegalNote legalNote = legalNoteRepository.findOne(legalNoteId);
		item.getLegalNotes().add(legalNote);
		itemRepository.save(item);
	}

	@Override
	public void addProductFeature(Long itemId, Long productFeatureId) {
		Item item = itemRepository.findOne(itemId);
		ProductFeature productFeature = productFeatureRepository.findOne(productFeatureId);
		item.getProductFeatures().add(productFeature);
		itemRepository.save(item);
	}
	
	
	@Override
	public void removeLegalNote(Long itemId, Long legalNoteId) {
		Item item = itemRepository.findOne(itemId);
		LegalNote legalNote = legalNoteRepository.findOne(legalNoteId);
		item.getLegalNotes().remove(legalNote);
		itemRepository.save(item);
	}

	@Override
	public void removeProductFeature(Long itemId, Long productFeatureId) {
		Item item = itemRepository.findOne(itemId);
		ProductFeature productFeature = productFeatureRepository.findOne(productFeatureId);
		item.getProductFeatures().remove(productFeature);
		itemRepository.save(item);
	}

	@Override
	public void submitForApproval(Long itemId) {
		Item item = itemRepository.findOne(itemId);
		item.setStatus(ItemStatus.AWAITING_APPROVAL);
		itemRepository.save(item);
	}
	
	@Override
	public void availableForTransaction(Long itemId) {
		Item item = itemRepository.findOne(itemId);
		item.setStatus(ItemStatus.ACTIVE);
		itemRepository.save(item);
	}
}
