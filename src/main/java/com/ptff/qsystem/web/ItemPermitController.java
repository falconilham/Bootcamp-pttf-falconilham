package com.ptff.qsystem.web;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ptff.qsystem.data.ItemPermit;
import com.ptff.qsystem.data.ItemPermitRepository;
import com.ptff.qsystem.data.ItemType;


@Controller
public class ItemPermitController implements DefaultController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemPermitController.class);
	
	@Autowired
	private ItemPermitRepository itemPermitRepository;
	
	
	@RequestMapping(value="/item/3", method=RequestMethod.POST)
	public String save(@ModelAttribute("item") @Valid ItemPermit itemPermit, BindingResult bindingResult, Model model) {
		LOGGER.info("Saving Item Permit " + itemPermit.getDescription());
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("itemType", ItemType.PERMIT);
			return "/item/new";
		}
		
		itemPermitRepository.save(itemPermit);
		return "redirect:/item/3";
	}
	
	@RequestMapping(value="/item/3/{itemId}", method=RequestMethod.POST)
	public String update(
					@PathVariable("itemId") Long itemId,
					@ModelAttribute("item") @Valid ItemPermit itemPermitForm, BindingResult bindingResult, 
					Model model) {
		LOGGER.info("Updating Item Permit {} - {}", itemPermitForm.getId(), itemPermitForm.getDescription());
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("itemType", ItemType.PERMIT);
			return "/item/edit";
		}
		
		ItemPermit itemPermit = itemPermitRepository.findOne(itemId);
		itemPermit.setName(itemPermitForm.getName());
		itemPermit.setDescription(itemPermitForm.getDescription());
		itemPermit.setPricingUnit(itemPermitForm.getPricingUnit());
		
		itemPermitRepository.save(itemPermit);
		return "redirect:/item/3/{itemId}";
	}
}
