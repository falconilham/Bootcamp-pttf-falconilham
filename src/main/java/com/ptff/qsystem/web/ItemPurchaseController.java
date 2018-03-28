package com.ptff.qsystem.web;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ptff.qsystem.data.Item;
import com.ptff.qsystem.data.ItemPurchase;
import com.ptff.qsystem.data.ItemPurchasePricingTier;
import com.ptff.qsystem.data.ItemPurchaseRepository;
import com.ptff.qsystem.data.ItemPurchaseStatus;
import com.ptff.qsystem.data.ItemRepository;
import com.ptff.qsystem.data.ItemType;
import com.ptff.qsystem.data.SettingsRepository;
import com.ptff.qsystem.data.Vendor;
import com.ptff.qsystem.data.VendorRepository;


@Controller
@RequestMapping("/item/{itemTypeId}")
public class ItemPurchaseController implements DefaultController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemPurchaseController.class);
	
	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private ItemPurchaseRepository itemPurchaseRepository;
	
	@Autowired
	private VendorRepository vendorRepository;
	
	@Autowired
	private SettingsRepository settingsRepository;


	@ModelAttribute("vendors")
    public List<Vendor> vendors() {
        return vendorRepository.findAll();
    }
	
	@ModelAttribute("itemType")
    public ItemType itemType(@PathVariable("itemTypeId")int itemTypeId) {
        return ItemType.values()[itemTypeId];
    }

	@RequestMapping("/{itemId}/purchase")
	public String purchase(				
			@PathVariable("itemTypeId")int itemTypeId,
			@PathVariable("itemId")Long itemId, Model model) {
		
		Item item = itemRepository.findOne(itemId);
		ItemPurchase itemPurchase = new ItemPurchase();
		itemPurchase.setItem(item);

		itemPurchase.setQuoteDate(LocalDate.now());
		itemPurchase.setReviewDate(LocalDate.now().plusDays(settingsRepository.findOne("PURCHASE_DEFAULT_REVIEW_DATE").getValueAsInteger()));
		itemPurchase.getPricingTiers().add(new ItemPurchasePricingTier());
		
		model.addAttribute("itemPurchase", itemPurchase);
		model.addAttribute("item", item);

		
		return "item/purchase";
	}
	
	@RequestMapping(value="/{itemId}/purchase", method=RequestMethod.POST)
	@Transactional
	public String savePurchaseQuote(
			@PathVariable("itemTypeId")int itemTypeId,
			@PathVariable("itemId")Long itemId, 
			@Valid @ModelAttribute("itemPurchase") ItemPurchase itemPurchase, BindingResult bindingResult, Model model) {
		Item item = itemRepository.findOne(itemId);
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("item", item);
			
			return "item/purchase";
		}
		
		// Set the link for Pricing Tier
		for (int i=0; i<itemPurchase.getPricingTiers().size(); i++) {
			itemPurchase.getPricingTiers().get(i).setItemPurchase(itemPurchase);
		}
		
		// Deactivate  all other vendor pricing for this item
		for (ItemPurchase oldItemPurchase : item.getPurchasePrices()) {
			if (oldItemPurchase.getVendor().equals(itemPurchase.getVendor()))
				oldItemPurchase.setStatus(ItemPurchaseStatus.INACTIVE);
		}
		
		itemPurchase.setStatus(ItemPurchaseStatus.ACTIVE);
		itemPurchase.setItem(item);

		itemPurchaseRepository.save(itemPurchase);
		return "redirect:/item/"+itemTypeId+"/"+itemId;
	}
	
	@RequestMapping(value="/{itemId}/purchase", method=RequestMethod.POST, params="addNewTier")
	public String addPurchaseTier(
			@PathVariable("itemTypeId")int itemTypeId,
			@PathVariable("itemId")Long itemId, 
			@Valid @ModelAttribute("itemPurchase") ItemPurchase itemPurchase, BindingResult bindingResult, Model model) {
		Item item = itemRepository.findOne(itemId);
				
		itemPurchase.addPriceTier(new ItemPurchasePricingTier());

		model.addAttribute("item", item);
		LOGGER.info("Adding a new Pricing Tier - Pricing Tiers are now {}", itemPurchase.getPricingTiers().size());
		
		return "item/purchase";
	}
	
	@RequestMapping(value="/{itemId}/purchase", method=RequestMethod.POST, params="removeTier")
	public String removePurchaseTier(
			@PathVariable("itemTypeId")int itemTypeId,
			@PathVariable("itemId")Long itemId, 
			@RequestParam("removeTier") int priceTierIndex, 
			@Valid @ModelAttribute("itemPurchase") ItemPurchase itemPurchase, BindingResult bindingResult, Model model) {
		
		Item item = itemRepository.findOne(itemId);
				
		itemPurchase.removePriceTier(priceTierIndex);

		model.addAttribute("item", item);
		LOGGER.info("Removig Pricing Tier - Pricing Tiers are now {}", itemPurchase.getPricingTiers().size());
		
		return "item/purchase";
	}
}
