package com.ptff.qsystem.web;

import java.time.LocalDate;

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
import com.ptff.qsystem.data.ItemMinimum;
import com.ptff.qsystem.data.ItemMinimumPricingTier;
import com.ptff.qsystem.data.ItemMinimumRepository;
import com.ptff.qsystem.data.ItemPriceStatus;
import com.ptff.qsystem.data.ItemRepository;
import com.ptff.qsystem.data.ItemType;
import com.ptff.qsystem.data.SettingsRepository;
import com.ptff.qsystem.data.VendorRepository;


@Controller
@RequestMapping("/item/{itemTypeId}")
public class ItemMinimumController implements DefaultController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemMinimumController.class);
	
	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private ItemMinimumRepository itemMinimumRepository;
	
	@Autowired
	private VendorRepository vendorRepository;
	
	@Autowired
	private SettingsRepository settingsRepository;


	@ModelAttribute("itemType")
    public ItemType itemType(@PathVariable("itemTypeId")int itemTypeId) {
        return ItemType.values()[itemTypeId];
    }

	@RequestMapping("/{itemId}/minimum")
	public String purchase(				
			@PathVariable("itemTypeId")int itemTypeId,
			@PathVariable("itemId")Long itemId, Model model) {
		
		Item item = itemRepository.findOne(itemId);
		ItemMinimum itemMinimum = new ItemMinimum();
		itemMinimum.setItem(item);

		itemMinimum.setQuoteDate(LocalDate.now());
		itemMinimum.setReviewDate(LocalDate.now().plusDays(settingsRepository.findOne("MINIMUM_DEFAULT_REVIEW_DATE").getValueAsInteger()));
		itemMinimum.getPricingTiers().add(new ItemMinimumPricingTier());
		
		model.addAttribute("itemMinimum", itemMinimum);
		model.addAttribute("item", item);

		
		return "item/minimum";
	}
	
	@RequestMapping(value="/{itemId}/minimum", method=RequestMethod.POST)
	@Transactional
	public String saveMinimumQuote(
			@PathVariable("itemTypeId")int itemTypeId,
			@PathVariable("itemId")Long itemId, 
			@Valid @ModelAttribute("itemMinimum") ItemMinimum itemMinimum, BindingResult bindingResult, Model model) {
		Item item = itemRepository.findOne(itemId);
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("item", item);
			
			return "item/minimum";
		}
		
		// Set the link for Pricing Tier
		for (int i=0; i<itemMinimum.getPricingTiers().size(); i++) {
			itemMinimum.getPricingTiers().get(i).setItemMinimum(itemMinimum);
		}
		
		// Deactivate  all other vendor pricing for this item
		for (ItemMinimum oldItemMinimum : item.getMinimumPrices()) {
			oldItemMinimum.setStatus(ItemPriceStatus.INACTIVE);
		}
		
		itemMinimum.setStatus(ItemPriceStatus.ACTIVE);
		itemMinimum.setItem(item);

		itemMinimumRepository.save(itemMinimum);
		return "redirect:/item/"+itemTypeId+"/"+itemId;
	}
	
	@RequestMapping(value="/{itemId}/minimum", method=RequestMethod.POST, params="addNewTier")
	public String addMinimumTier(
			@PathVariable("itemTypeId")int itemTypeId,
			@PathVariable("itemId")Long itemId, 
			@Valid @ModelAttribute("itemMinimum") ItemMinimum itemMinimum, BindingResult bindingResult, Model model) {
		Item item = itemRepository.findOne(itemId);
				
		itemMinimum.addPriceTier(new ItemMinimumPricingTier());

		model.addAttribute("item", item);
		LOGGER.info("Adding a new Pricing Tier - Pricing Tiers are now {}", itemMinimum.getPricingTiers().size());
		
		return "item/minimum";
	}
	
	@RequestMapping(value="/{itemId}/minimum", method=RequestMethod.POST, params="removeTier")
	public String removeMinimumTier(
			@PathVariable("itemTypeId")int itemTypeId,
			@PathVariable("itemId")Long itemId, 
			@RequestParam("removeTier") int priceTierIndex, 
			@Valid @ModelAttribute("itemMinimum") ItemMinimum itemMinimum, BindingResult bindingResult, Model model) {
		
		Item item = itemRepository.findOne(itemId);
				
		itemMinimum.removePriceTier(priceTierIndex);

		model.addAttribute("item", item);
		LOGGER.info("Removig Pricing Tier - Pricing Tiers are now {}", itemMinimum.getPricingTiers().size());
		
		return "item/minimum";
	}
}
