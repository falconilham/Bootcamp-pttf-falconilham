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

import com.ptff.qsystem.data.Country;
import com.ptff.qsystem.data.CountryRepository;
import com.ptff.qsystem.data.Depot;
import com.ptff.qsystem.data.DepotRepository;
import com.ptff.qsystem.data.ItemTrucking;
import com.ptff.qsystem.data.ItemTruckingPurchase;
import com.ptff.qsystem.data.ItemTruckingPurchaseRepository;
import com.ptff.qsystem.data.ItemTruckingRepository;
import com.ptff.qsystem.data.ItemPurchaseStatus;
import com.ptff.qsystem.data.Pager;
import com.ptff.qsystem.data.Vendor;
import com.ptff.qsystem.data.VendorRepository;


@Controller
public class ItemTruckingController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemTruckingController.class);
	
	private static final int BUTTONS_TO_SHOW = 5;
	private static final int INITIAL_PAGE = 0;
	private static final int INITIAL_PAGE_SIZE = 20;
	private static final int[] PAGE_SIZES = { 5, 10, 20, 100 };
	
	@Autowired
	private ItemTruckingRepository itemTruckingRepository;
	
	@Autowired
	private ItemTruckingPurchaseRepository itemTruckingPurchaseRepository;
	
	@Autowired
	private VendorRepository vendorRepository;
	
	@Autowired
	private DepotRepository depotRepository;
	
	@ModelAttribute("vendors")
    public List<Vendor> vendors() {
        return vendorRepository.findAll();
    }
	
	@ModelAttribute("depots")
    public List<Depot> messages() {
        return depotRepository.findAll();
    }
	
	@RequestMapping("/item/truckings")
	public String list(
			@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page,
			Model model) {
		
		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
		
		Page<ItemTrucking> itemTruckings = itemTruckingRepository.findAll(new PageRequest(evalPage, evalPageSize));
		Pager pager = new Pager(itemTruckings.getTotalPages(), itemTruckings.getNumber(), BUTTONS_TO_SHOW);
		
		model.addAttribute("items", itemTruckings);
		model.addAttribute("selectedPageSize", evalPageSize);
		model.addAttribute("pageSizes", PAGE_SIZES);
		model.addAttribute("pager", pager);
		
		return "item/trucking/index";
	}

	@RequestMapping("/item/truckings/new")
	public String newItem(Model model) {
		ItemTrucking itemTrucking = new ItemTrucking();
		model.addAttribute("itemTrucking", itemTrucking);
		
		return "item/trucking/new";
	}
	
	@RequestMapping(value="/item/truckings", method=RequestMethod.POST)
	public String save(@Valid ItemTrucking itemTrucking, BindingResult bindingResult, Model model) {
		LOGGER.info("Saving ItemTrucking " + itemTrucking.getDescription());
		
		if (bindingResult.hasErrors()) {
			return "item/trucking/new";
		}
		
		itemTrucking = itemTruckingRepository.save(itemTrucking);
		return "redirect:/item/truckings";
	}
	
	@RequestMapping("/item/truckings/{id}")
	public String show(@PathVariable("id") Long id, Model model) {
		ItemTrucking itemTrucking = itemTruckingRepository.findOne(id);
		List<ItemTruckingPurchase> itemPurchases = itemTruckingPurchaseRepository.findAllByItemAndStatus(itemTrucking, ItemPurchaseStatus.ACTIVE);
		
		model.addAttribute("itemTrucking", itemTrucking);
		model.addAttribute("itemPurchases", itemPurchases);
		
		// Calculate Statistics
		BigDecimal averageSell = itemPurchases.size()!=0?
				itemPurchases.stream()
				.map(ItemTruckingPurchase::getPrice)
			    .reduce(BigDecimal.ZERO, BigDecimal::add)
			    .divide(new BigDecimal(itemPurchases.size()), RoundingMode.DOWN)
		: BigDecimal.ZERO;
		ItemTruckingPurchase minItem = null;
		for (ItemTruckingPurchase purchase : itemPurchases) {
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
			    
		return "item/trucking/show";
	}
	
	@RequestMapping("/item/truckings/{id}/edit")
	public String edit(@PathVariable("id") Long id, Model model) {
		model.addAttribute("itemTrucking", itemTruckingRepository.findOne(id));

		
		return "item/trucking/edit";
	}
	
	@RequestMapping(value="/item/truckings/{id}", method=RequestMethod.POST)
	public String updateCountry(@PathVariable Long id, @Valid ItemTrucking itemTrucking, BindingResult bindingResult, Model model) {
		LOGGER.info("Updating ItemTrucking: " + itemTrucking.getDescription());

		if (bindingResult.hasErrors()) {
			return "item/trucking/edit";
		}
		
		itemTrucking = itemTruckingRepository.save(itemTrucking);
		
		return "redirect:/item/truckings/"+id;
	}

	@RequestMapping("/item/truckings/{id}/purchase")
	public String purchase(@PathVariable("id") Long id, Model model) {
		ItemTrucking itemTrucking = itemTruckingRepository.findOne(id);
		ItemTruckingPurchase itemTruckingPurchase = new ItemTruckingPurchase();
		itemTruckingPurchase.setItem(itemTrucking);
		itemTruckingPurchase.setReviewDate(LocalDate.now().plusDays(60));
		
		model.addAttribute("itemPurchase", itemTruckingPurchase);
		model.addAttribute("itemTrucking", itemTrucking);

		
		return "item/trucking/purchase";
	}
	
	@RequestMapping(value="/item/truckings/{id}/purchase", method=RequestMethod.POST)
	@Transactional
	public String savePurchaseQuote(@PathVariable("id") Long id, @Valid @ModelAttribute("itemPurchase") ItemTruckingPurchase itemPurchase, BindingResult bindingResult, Model model) {
		ItemTrucking itemTrucking = itemTruckingRepository.findOne(id);
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("itemTrucking", itemTrucking);
			
			return "item/trucking/purchase";
		}
		
		
		// Deactivate  all other vendor pricing for this item
		List<ItemTruckingPurchase> itemPurchases = itemTruckingPurchaseRepository.findAllByVendorAndItem(itemPurchase.getVendor(), itemTrucking);
		for (ItemTruckingPurchase oldItemPurchase : itemPurchases) {
			oldItemPurchase.setStatus(ItemPurchaseStatus.INACTIVE);
			itemTruckingPurchaseRepository.save(oldItemPurchase);
		}
		
		itemPurchase.setStatus(ItemPurchaseStatus.ACTIVE);
		itemPurchase = itemTruckingPurchaseRepository.save(itemPurchase);
		
		return "redirect:/item/truckings/"+id;
	}
}
