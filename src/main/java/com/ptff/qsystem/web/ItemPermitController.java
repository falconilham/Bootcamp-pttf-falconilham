package com.ptff.qsystem.web;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ptff.qsystem.data.ItemAirFreight;
import com.ptff.qsystem.data.ItemPermit;
import com.ptff.qsystem.data.ItemPermitPurchase;
import com.ptff.qsystem.data.ItemPermitPurchaseRepository;
import com.ptff.qsystem.data.ItemPermitRepository;
import com.ptff.qsystem.data.ItemPriceStatus;
import com.ptff.qsystem.data.ItemType;
import com.ptff.qsystem.data.LegalNote;
import com.ptff.qsystem.data.LegalNoteRepository;
import com.ptff.qsystem.data.Pager;
import com.ptff.qsystem.data.PricingTier;
import com.ptff.qsystem.data.Vendor;
import com.ptff.qsystem.data.VendorRepository;


@Controller
public class ItemPermitController implements DefaultController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemPermitController.class);
	
	@Autowired
	private ItemPermitRepository itemPermitRepository;
	
	@Autowired
	private ItemPermitPurchaseRepository itemPermitPurchaseRepository;
	
	@Autowired
	private VendorRepository vendorRepository;
	
	@Autowired
	private LegalNoteRepository legalNoteRepository;
	
	
	@ModelAttribute("vendors")
    public List<Vendor> vendors() {
        return vendorRepository.findAll();
    }
	
	@ModelAttribute("legalNotes")
    public List<LegalNote> legalNotes() {
        return legalNoteRepository.findAll();
    }
	
	@RequestMapping("/item/permits")
	public String list(Model model, 
			@PageableDefault(sort="name", direction=Sort.Direction.ASC, page=INITIAL_PAGE, size=INITIAL_PAGE_SIZE) Pageable pageable) {
		
		Page<ItemPermit> itemPermits = itemPermitRepository.findAll(pageable);
		
		model.addAttribute("items", itemPermits);
		
		return "item/permit/index";
	}

	@RequestMapping("/item/permits/new")
	public String newItem(Model model) {
		ItemPermit itemPermit = new ItemPermit();
		model.addAttribute("itemPermit", itemPermit);
		
		return "item/permit/new";
	}
	
	@RequestMapping("/item/permits/{id}")
	public String show(@PathVariable("id") Long id, Model model) {
		ItemPermit itemPermit = itemPermitRepository.findOne(id);
		List<ItemPermitPurchase> itemPurchases = itemPermitPurchaseRepository.findAllByItemAndStatus(itemPermit, ItemPriceStatus.ACTIVE);
		
		model.addAttribute("itemPermit", itemPermit);
		model.addAttribute("itemPurchases", itemPurchases);
		
		// Calculate Statistics
		/*BigDecimal averageSell = itemPurchases.size()!=0?
				itemPurchases.stream()
				.map(ItemPermitPurchase::getPrice)
			    .reduce(BigDecimal.ZERO, BigDecimal::add)
			    .divide(new BigDecimal(itemPurchases.size()), RoundingMode.DOWN)
		: BigDecimal.ZERO;
		ItemPermitPurchase minItem = null;
		for (ItemPermitPurchase purchase : itemPurchases) {
			if (minItem == null) {
				minItem = purchase;
			} else {
				if (purchase.getPrice().compareTo(minItem.getPrice())<0) {
					minItem = purchase;
				}
			}
		}
		model.addAttribute("averageSell", averageSell);
		model.addAttribute("minItem", minItem);
		*/	    
		return "item/permit/show";
	}
	
	@RequestMapping(value="/item/permits/{id}/legalnotes", method=RequestMethod.POST)
	public String addLegalNote(@PathVariable("id") Long id, @ModelAttribute("legalNote")Long legalNoteId, Model model) {
		ItemPermit itemPermit = itemPermitRepository.findOne(id);
		LegalNote legalNote = legalNoteRepository.findOne(legalNoteId);
		
		itemPermit.getLegalNotes().add(legalNote);
		
		itemPermitRepository.save(itemPermit);
			    
		return "redirect:/item/permits/"+id;
	}
	
	@RequestMapping(value="/item/permits/{id}/legalnotes/{legalNoteId}/remove")
	public String removeLegalNote(@PathVariable("id") Long id, @PathVariable("legalNoteId")Long legalNoteId, Model model) {
		ItemPermit itemPermit = itemPermitRepository.findOne(id);
		LegalNote legalNote = legalNoteRepository.findOne(legalNoteId);
		
		itemPermit.getLegalNotes().remove(legalNote);
		
		itemPermitRepository.save(itemPermit);
			    
		return "redirect:/item/permits/"+id;
	}
	
	@RequestMapping("/item/permits/{id}/edit")
	public String edit(@PathVariable("id") Long id, Model model) {
		model.addAttribute("itemPermit", itemPermitRepository.findOne(id));

		
		return "item/permit/edit";
	}
	
	@RequestMapping(value="/item/permits/{id}", method=RequestMethod.POST)
	public String updateCountry(@PathVariable Long id, @Valid ItemPermit itemPermit, BindingResult bindingResult, Model model) {
		LOGGER.info("Updating ItemPermit: " + itemPermit.getName());

		if (bindingResult.hasErrors()) {
			return "item/permit/edit";
		}
		
		itemPermit = itemPermitRepository.save(itemPermit);
		
		return "redirect:/item/permits/"+id;
	}

	@RequestMapping("/item/permits/{id}/purchase")
	public String purchase(@PathVariable("id") Long id, Model model) {
		ItemPermit itemPermit = itemPermitRepository.findOne(id);
		ItemPermitPurchase itemPermitPurchase = new ItemPermitPurchase();
		itemPermitPurchase.setItem(itemPermit);
		itemPermitPurchase.setReviewDate(LocalDate.now().plusDays(60));
		
		itemPermitPurchase.getPricingTiers().add(new PricingTier());
		
		model.addAttribute("itemPurchase", itemPermitPurchase);
		model.addAttribute("itemPermit", itemPermit);

		
		return "item/permit/purchase";
	}
	
	@RequestMapping(value="/item/permits/{itemId}/purchase", method=RequestMethod.POST)
	@Transactional
	public String savePurchaseQuote(@PathVariable("itemId") Long itemId, @Valid @ModelAttribute("itemPurchase") ItemPermitPurchase itemPurchase, BindingResult bindingResult, Model model) {
		ItemPermit itemPermit = itemPermitRepository.findOne(itemId);
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("itemPermit", itemPermit);
			
			return "item/permit/purchase";
		}
		
		// Pricing tier is set to 
		for (PricingTier pricingTier : itemPurchase.getPricingTiers()) {
			pricingTier.setItemPermitPurchase(itemPurchase);
		}
		
		// Deactivate  all other vendor pricing for this item
		List<ItemPermitPurchase> itemPurchases = itemPermitPurchaseRepository.findAllByVendorAndItem(itemPurchase.getVendor(), itemPermit);
		for (ItemPermitPurchase oldItemPurchase : itemPurchases) {
			oldItemPurchase.setStatus(ItemPriceStatus.INACTIVE);
			itemPermitPurchaseRepository.save(oldItemPurchase);
		}
		
		itemPurchase.setStatus(ItemPriceStatus.ACTIVE);
		//itemPurchase.setQuoteDate(LocalDate.now());
		itemPurchase = itemPermitPurchaseRepository.save(itemPurchase);
		
		return "redirect:/item/permits/"+itemId;
	}
	
	@RequestMapping(value="/item/permits/{id}/purchase", method=RequestMethod.POST, params="addNewTier")
	public String addPurchaseTier(@PathVariable("id") Long id, @Valid @ModelAttribute("itemPurchase") ItemPermitPurchase itemPurchase, BindingResult bindingResult, Model model) {
		ItemPermit itemPermit = itemPermitRepository.findOne(id);
				
		itemPurchase.addPriceTier(new PricingTier());

		model.addAttribute("itemPermit", itemPermit);
		LOGGER.info("Adding a new Pricing Tier - Pricing Tiers are now {}", itemPurchase.getPricingTiers().size());
		
		return "item/permit/purchase";
	}
	
	@RequestMapping(value="/item/permits/{id}/purchase", method=RequestMethod.POST, params="removeTier")
	public String removePurchaseTier(@PathVariable("id") Long id, @RequestParam("removeTier") int index, @Valid @ModelAttribute("itemPurchase") ItemPermitPurchase itemPurchase, BindingResult bindingResult, Model model) {
		ItemPermit itemPermit = itemPermitRepository.findOne(id);
				
		itemPurchase.getPricingTiers().remove(index);

		model.addAttribute("itemPermit", itemPermit);
		LOGGER.info("Removig Pricing Tier - Pricing Tiers are now {}", itemPurchase.getPricingTiers().size());
		
		return "item/permit/purchase";
	}
	
	
	@RequestMapping(value="/item/3", method=RequestMethod.POST)
	public String save(@ModelAttribute("item") @Valid ItemPermit itemPermit, BindingResult bindingResult, Model model) {
		LOGGER.info("Saving Item Permit " + itemPermit.getDescription());
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("itemType", ItemType.AIR_FREIGHT);
			return "/item/new";
		}
		
		itemPermitRepository.save(itemPermit);
		return "redirect:/item/3";
	}
}
