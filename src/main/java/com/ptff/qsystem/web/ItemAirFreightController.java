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

import com.ptff.qsystem.data.Airport;
import com.ptff.qsystem.data.AirportRepository;
import com.ptff.qsystem.data.Country;
import com.ptff.qsystem.data.CountryRepository;
import com.ptff.qsystem.data.Depot;
import com.ptff.qsystem.data.DepotRepository;
import com.ptff.qsystem.data.ItemAirFreight;
import com.ptff.qsystem.data.ItemAirFreightPurchase;
import com.ptff.qsystem.data.ItemAirFreightPurchaseRepository;
import com.ptff.qsystem.data.ItemAirFreightRepository;
import com.ptff.qsystem.data.ItemPurchaseStatus;
import com.ptff.qsystem.data.Pager;
import com.ptff.qsystem.data.Vendor;
import com.ptff.qsystem.data.VendorRepository;


@Controller
public class ItemAirFreightController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemAirFreightController.class);
	
	private static final int BUTTONS_TO_SHOW = 5;
	private static final int INITIAL_PAGE = 0;
	private static final int INITIAL_PAGE_SIZE = 20;
	private static final int[] PAGE_SIZES = { 5, 10, 20, 100 };
	
	@Autowired
	private ItemAirFreightRepository itemAirFreightRepository;
	
	@Autowired
	private ItemAirFreightPurchaseRepository itemAirFreightPurchaseRepository;
	
	@Autowired
	private VendorRepository vendorRepository;
	
	@Autowired
	private AirportRepository airportRepository;
	
	@ModelAttribute("vendors")
    public List<Vendor> vendors() {
        return vendorRepository.findAll();
    }
	
	@ModelAttribute("airports")
    public List<Airport> airports() {
        return airportRepository.findAll();
    }
	
	@RequestMapping("/item/airfreights")
	public String list(
			@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page,
			Model model) {
		
		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
		
		Page<ItemAirFreight> itemAirFreights = itemAirFreightRepository.findAll(new PageRequest(evalPage, evalPageSize));
		Pager pager = new Pager(itemAirFreights.getTotalPages(), itemAirFreights.getNumber(), BUTTONS_TO_SHOW);
		
		model.addAttribute("items", itemAirFreights);
		model.addAttribute("selectedPageSize", evalPageSize);
		model.addAttribute("pageSizes", PAGE_SIZES);
		model.addAttribute("pager", pager);
		
		return "item/airfreight/index";
	}

	@RequestMapping("/item/airfreights/new")
	public String newItem(Model model) {
		ItemAirFreight itemAirFreight = new ItemAirFreight();
		model.addAttribute("itemAirFreight", itemAirFreight);
		
		return "item/airfreight/new";
	}
	
	@RequestMapping(value="/item/airfreights", method=RequestMethod.POST)
	public String save(@Valid ItemAirFreight itemAirFreight, BindingResult bindingResult, Model model) {
		LOGGER.info("Saving ItemAirFreight " + itemAirFreight.getDescription());
		
		if (bindingResult.hasErrors()) {
			return "item/airfreight/new";
		}
		
		itemAirFreight = itemAirFreightRepository.save(itemAirFreight);
		return "redirect:/item/airfreights";
	}
	
	@RequestMapping("/item/airfreights/{id}")
	public String show(@PathVariable("id") Long id, Model model) {
		ItemAirFreight itemAirFreight = itemAirFreightRepository.findOne(id);
		List<ItemAirFreightPurchase> itemPurchases = itemAirFreightPurchaseRepository.findAllByItemAndStatus(itemAirFreight, ItemPurchaseStatus.ACTIVE);
		
		model.addAttribute("itemAirFreight", itemAirFreight);
		model.addAttribute("itemPurchases", itemPurchases);
		
		// Calculate Statistics
		BigDecimal averageSell = itemPurchases.size()!=0?
				itemPurchases.stream()
				.map(ItemAirFreightPurchase::getPrice)
			    .reduce(BigDecimal.ZERO, BigDecimal::add)
			    .divide(new BigDecimal(itemPurchases.size()), RoundingMode.DOWN)
		: BigDecimal.ZERO;
		ItemAirFreightPurchase minItem = null;
		for (ItemAirFreightPurchase purchase : itemPurchases) {
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
			    
		return "item/airfreight/show";
	}
	
	@RequestMapping("/item/airfreights/{id}/edit")
	public String edit(@PathVariable("id") Long id, Model model) {
		model.addAttribute("itemAirFreight", itemAirFreightRepository.findOne(id));

		
		return "item/airfreight/edit";
	}
	
	@RequestMapping(value="/item/airfreights/{id}", method=RequestMethod.POST)
	public String updateCountry(@PathVariable Long id, @Valid ItemAirFreight itemAirFreight, BindingResult bindingResult, Model model) {
		LOGGER.info("Updating ItemAirFreight: " + itemAirFreight.getDescription());

		if (bindingResult.hasErrors()) {
			return "item/airfreight/edit";
		}
		
		itemAirFreight = itemAirFreightRepository.save(itemAirFreight);
		
		return "redirect:/item/airfreights/"+id;
	}

	@RequestMapping("/item/airfreights/{id}/purchase")
	public String purchase(@PathVariable("id") Long id, Model model) {
		ItemAirFreight itemAirFreight = itemAirFreightRepository.findOne(id);
		ItemAirFreightPurchase itemAirFreightPurchase = new ItemAirFreightPurchase();
		itemAirFreightPurchase.setItem(itemAirFreight);
		itemAirFreightPurchase.setReviewDate(LocalDate.now().plusDays(60));
		
		model.addAttribute("itemPurchase", itemAirFreightPurchase);
		model.addAttribute("itemAirFreight", itemAirFreight);

		
		return "item/airfreight/purchase";
	}
	
	@RequestMapping(value="/item/airfreights/{id}/purchase", method=RequestMethod.POST)
	@Transactional
	public String savePurchaseQuote(@PathVariable("id") Long id, @Valid @ModelAttribute("itemPurchase") ItemAirFreightPurchase itemPurchase, BindingResult bindingResult, Model model) {
		ItemAirFreight itemAirFreight = itemAirFreightRepository.findOne(id);
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("itemAirFreight", itemAirFreight);
			
			return "item/airfreight/purchase";
		}
		
		
		// Deactivate  all other vendor pricing for this item
		List<ItemAirFreightPurchase> itemPurchases = itemAirFreightPurchaseRepository.findAllByVendorAndItem(itemPurchase.getVendor(), itemAirFreight);
		for (ItemAirFreightPurchase oldItemPurchase : itemPurchases) {
			oldItemPurchase.setStatus(ItemPurchaseStatus.INACTIVE);
			itemAirFreightPurchaseRepository.save(oldItemPurchase);
		}
		
		itemPurchase.setStatus(ItemPurchaseStatus.ACTIVE);
		itemPurchase = itemAirFreightPurchaseRepository.save(itemPurchase);
		
		return "redirect:/item/airfreights/"+id;
	}
}
