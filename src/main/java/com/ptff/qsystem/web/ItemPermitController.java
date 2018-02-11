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
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ptff.qsystem.data.ItemPermit;
import com.ptff.qsystem.data.ItemPermitPurchase;
import com.ptff.qsystem.data.ItemPermitPurchaseRepository;
import com.ptff.qsystem.data.ItemPermitRepository;
import com.ptff.qsystem.data.ItemPurchaseStatus;
import com.ptff.qsystem.data.Pager;
import com.ptff.qsystem.data.Vendor;
import com.ptff.qsystem.data.VendorRepository;


@Controller
public class ItemPermitController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemPermitController.class);
	
	private static final int BUTTONS_TO_SHOW = 5;
	private static final int INITIAL_PAGE = 0;
	private static final int INITIAL_PAGE_SIZE = 20;
	private static final int[] PAGE_SIZES = { 5, 10, 20, 100 };
	
	@Autowired
	private ItemPermitRepository itemPermitRepository;
	
	@Autowired
	private ItemPermitPurchaseRepository itemPermitPurchaseRepository;
	
	@Autowired
	private VendorRepository vendorRepository;
	
	@ModelAttribute("vendors")
    public List<Vendor> messages() {
        return vendorRepository.findAll();
    }
	
	@RequestMapping("/item/permits")
	public String list(
			@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page,
			Model model) {
		
		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
		
		Page<ItemPermit> itemPermits = itemPermitRepository.findAll(new PageRequest(evalPage, evalPageSize));
		Pager pager = new Pager(itemPermits.getTotalPages(), itemPermits.getNumber(), BUTTONS_TO_SHOW);
		
		model.addAttribute("items", itemPermits);
		model.addAttribute("selectedPageSize", evalPageSize);
		model.addAttribute("pageSizes", PAGE_SIZES);
		model.addAttribute("pager", pager);
		
		return "item/permit/index";
	}

	@RequestMapping("/item/permits/new")
	public String newItem(Model model) {
		ItemPermit itemPermit = new ItemPermit();
		model.addAttribute("itemPermit", itemPermit);
		
		return "item/permit/new";
	}
	
	@RequestMapping(value="/item/permits", method=RequestMethod.POST)
	public String save(@Valid ItemPermit itemPermit, BindingResult bindingResult, Model model) {
		LOGGER.info("Saving ItemPermit " + itemPermit.getName());
		
		if (bindingResult.hasErrors()) {
			return "item/permit/new";
		}
		
		itemPermit = itemPermitRepository.save(itemPermit);
		return "redirect:/item/permits";
	}
	
	@RequestMapping("/item/permits/{id}")
	public String show(@PathVariable("id") Long id, Model model) {
		ItemPermit itemPermit = itemPermitRepository.findOne(id);
		List<ItemPermitPurchase> itemPurchases = itemPermitPurchaseRepository.findAllByItemAndStatus(itemPermit, ItemPurchaseStatus.ACTIVE);
		
		model.addAttribute("itemPermit", itemPermit);
		model.addAttribute("itemPurchases", itemPurchases);
		
		// Calculate Statistics
		BigDecimal averageSell = itemPurchases.size()!=0?
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
			    
		return "item/permit/show";
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
		
		model.addAttribute("itemPurchase", itemPermitPurchase);
		model.addAttribute("itemPermit", itemPermit);

		
		return "item/permit/purchase";
	}
	
	@RequestMapping(value="/item/permits/{id}/purchase", method=RequestMethod.POST)
	@Transactional
	public String savePurchaseQuote(@PathVariable("id") Long id, @Valid @ModelAttribute("itemPurchase") ItemPermitPurchase itemPurchase, BindingResult bindingResult, Model model) {
		ItemPermit itemPermit = itemPermitRepository.findOne(id);
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("itemPermit", itemPermit);
			
			return "item/permit/purchase";
		}
		
		
		// Deactivate  all other vendor pricing for this item
		List<ItemPermitPurchase> itemPurchases = itemPermitPurchaseRepository.findAllByVendorAndItem(itemPurchase.getVendor(), itemPermit);
		for (ItemPermitPurchase oldItemPurchase : itemPurchases) {
			oldItemPurchase.setStatus(ItemPurchaseStatus.INACTIVE);
			itemPermitPurchaseRepository.save(oldItemPurchase);
		}
		
		itemPurchase.setStatus(ItemPurchaseStatus.ACTIVE);
		itemPurchase.setQuoteDate(LocalDate.now());
		itemPurchase = itemPermitPurchaseRepository.save(itemPurchase);
		
		return "redirect:/item/permits/"+id;
	}
}
