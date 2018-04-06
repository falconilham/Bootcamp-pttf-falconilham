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

import com.ptff.qsystem.data.ItemAirFreight;
import com.ptff.qsystem.data.ItemAirFreightRepository;
import com.ptff.qsystem.data.ItemSeaFreight;
import com.ptff.qsystem.data.ItemType;


@Controller
public class ItemAirFreightController implements DefaultController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemAirFreightController.class);
	
	@Autowired
	private ItemAirFreightRepository itemAirFreightRepository;
	
	
	
	@RequestMapping(value="/item/0", method=RequestMethod.POST)
	public String save(@ModelAttribute("item") @Valid ItemAirFreight itemAirFreight, BindingResult bindingResult, Model model) {
		LOGGER.info("Saving ItemAirFreight " + itemAirFreight.getDescription());
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("itemType", ItemType.AIR_FREIGHT);
			return "/item/new";
		}
		
		itemAirFreightRepository.save(itemAirFreight);
		return "redirect:/item/0";
	}
	
	@RequestMapping(value="/item/0/{itemId}", method=RequestMethod.POST)
	public String update(
					@PathVariable("itemId") Long itemId,
					@ModelAttribute("item") @Valid ItemAirFreight itemAirFreight, BindingResult bindingResult, 
					Model model) {
		LOGGER.info("Updating Item Sea Freight {} - {}", itemAirFreight.getId(), itemAirFreight.getDescription());
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("itemType", ItemType.AIR_FREIGHT);
			return "/item/edit";
		}
		
		itemAirFreightRepository.save(itemAirFreight);
		return "redirect:/item/0/{itemId}";
	}

}
