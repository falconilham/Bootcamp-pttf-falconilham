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

import com.ptff.qsystem.data.ItemAdditional;
import com.ptff.qsystem.data.ItemAdditionalRepository;
import com.ptff.qsystem.data.ItemPermit;
import com.ptff.qsystem.data.ItemType;


@Controller
public class ItemAdditionalController implements DefaultController{
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemAdditionalController.class);
	
	@Autowired
	private ItemAdditionalRepository itemAdditionalRepository;
	
	@RequestMapping(value="/item/4", method=RequestMethod.POST)
	public String save(@ModelAttribute("item") @Valid ItemAdditional itemAdditional, BindingResult bindingResult, Model model) {
		LOGGER.info("Saving ItemSeaFreight " + itemAdditional.getDescription());
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("itemType", ItemType.ADDITIONAL);
			return "/item/new";
		}
		
		itemAdditionalRepository.save(itemAdditional);
		return "redirect:/item/4";
	}
	
	@RequestMapping(value="/item/4/{itemId}", method=RequestMethod.POST)
	public String update(
					@PathVariable("itemId") Long itemId,
					@ModelAttribute("item") @Valid ItemAdditional itemAdditionalForm, BindingResult bindingResult, 
					Model model) {
		LOGGER.info("Updating Item Sea Freight {} - {}", itemAdditionalForm.getId(), itemAdditionalForm.getDescription());
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("itemType", ItemType.ADDITIONAL);
			return "/item/edit";
		}
		
		ItemAdditional itemAdditional = itemAdditionalRepository.findOne(itemId);
		itemAdditional.setName(itemAdditionalForm.getName());
		itemAdditional.setDescription(itemAdditionalForm.getDescription());
		itemAdditional.setPricingUnit(itemAdditionalForm.getPricingUnit());
		
		itemAdditionalRepository.save(itemAdditional);
		return "redirect:/item/4/{itemId}";
	}
}
