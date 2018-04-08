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
import com.ptff.qsystem.data.ItemSeaFreight;
import com.ptff.qsystem.data.ItemSeaFreightRepository;
import com.ptff.qsystem.data.ItemType;


@Controller
public class ItemSeaFreightController implements DefaultController{
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemSeaFreightController.class);
	
	@Autowired
	private ItemSeaFreightRepository itemSeaFreightRepository;
	
	@RequestMapping(value="/item/1", method=RequestMethod.POST)
	public String save(@ModelAttribute("item") @Valid ItemSeaFreight itemSeaFreight, BindingResult bindingResult, Model model) {
		LOGGER.info("Saving ItemSeaFreight " + itemSeaFreight.getDescription());
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("itemType", ItemType.SEA_FREIGHT);
			return "/item/new";
		}
		
		itemSeaFreightRepository.save(itemSeaFreight);
		return "redirect:/item/1";
	}
	
	@RequestMapping(value="/item/1/{itemId}", method=RequestMethod.POST)
	public String update(
					@PathVariable("itemId") Long itemId,
					@ModelAttribute("item") @Valid ItemSeaFreight itemSeaFreightForm, BindingResult bindingResult, 
					Model model) {
		LOGGER.info("Updating Item Sea Freight {} - {}", itemSeaFreightForm.getId(), itemSeaFreightForm.getDescription());
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("itemType", ItemType.SEA_FREIGHT);
			return "/item/edit";
		}
		
		ItemSeaFreight itemSeaFreight = itemSeaFreightRepository.findOne(itemId);
		itemSeaFreight.setOrigin(itemSeaFreightForm.getOrigin());
		itemSeaFreight.setDestination(itemSeaFreightForm.getDestination());
		itemSeaFreight.setDescription(itemSeaFreightForm.getDescription());
		itemSeaFreight.setPricingUnit(itemSeaFreightForm.getPricingUnit());
		
		itemSeaFreightRepository.save(itemSeaFreight);
		return "redirect:/item/1/{itemId}";
	}
}
