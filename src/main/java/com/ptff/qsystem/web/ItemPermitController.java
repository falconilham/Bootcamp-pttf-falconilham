package com.ptff.qsystem.web;

import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ptff.qsystem.data.Country;
import com.ptff.qsystem.data.CountryRepository;
import com.ptff.qsystem.data.ItemPermit;
import com.ptff.qsystem.data.ItemPermitRepository;
import com.ptff.qsystem.data.Pager;


@Controller
public class ItemPermitController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemPermitController.class);
	
	private static final int BUTTONS_TO_SHOW = 5;
	private static final int INITIAL_PAGE = 0;
	private static final int INITIAL_PAGE_SIZE = 20;
	private static final int[] PAGE_SIZES = { 5, 10, 20, 100 };
	
	@Autowired
	private ItemPermitRepository itemPermitRepository;
	
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
		ItemPermit item = new ItemPermit();
		model.addAttribute("item", item);
		
		return "item/permit/new";
	}
	
	@RequestMapping(value="/item/permits", method=RequestMethod.POST)
	public String save(@Valid ItemPermit item, BindingResult bindingResult, Model model) {
		LOGGER.info("Saving ItemPermit " + item.getName());
		
		if (bindingResult.hasErrors()) {
			return "item/permit/new";
		}
		
		item = itemPermitRepository.save(item);
		return "redirect:/item/permits";
	}
	
	@RequestMapping("/item/permits/{id}")
	public String edit(@PathVariable("id") Long id, Model model) {
		model.addAttribute("item", itemPermitRepository.findOne(id));

		
		return "item/permit/edit";
	}	
	
	@RequestMapping(value="/item/permits/{id}", method=RequestMethod.POST)
	public String updateCountry(@PathVariable Long id, @Valid ItemPermit item, BindingResult bindingResult, Model model) {
		LOGGER.info("Updating ItemPermit: " + item.getName());

		if (bindingResult.hasErrors()) {
			return "item/permit/edit";
		}
		
		item = itemPermitRepository.save(item);
		
		return "redirect:/item/permits";
	}

}
