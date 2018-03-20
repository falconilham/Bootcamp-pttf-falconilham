package com.ptff.qsystem.web;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ptff.qsystem.data.PricingUnit;
import com.ptff.qsystem.data.PricingUnitRepository;


@Controller
public class PricingUnitController implements DefaultController {
	private static final Logger LOGGER = LoggerFactory.getLogger(PricingUnitController.class);
	
	
	@Autowired
	private PricingUnitRepository pricingUnitRepository;
	
	@RequestMapping("/pricingunits")
	public String list(
			@PageableDefault(sort="name", direction=Sort.Direction.ASC, page=INITIAL_PAGE, size=INITIAL_PAGE_SIZE) Pageable pageable,
			Model model) {
		
		Page<PricingUnit> pricingUnits = pricingUnitRepository.findAll(pageable);
		
		model.addAttribute("units", pricingUnits);
		
		return "master/pricingunit/index";
	}

	@RequestMapping("/pricingunits/new")
	public String news(Model model) {
		PricingUnit pricingUnit = new PricingUnit();
		model.addAttribute("unit", pricingUnit);
		
		return "master/pricingunit/new";
	}
	
	@RequestMapping(value="/pricingunits", method=RequestMethod.POST)
	public String save(@Valid @ModelAttribute("unit")PricingUnit pricingUnit, BindingResult bindingResult, Model model) {
		LOGGER.info("Saving Pricing Unit " + pricingUnit.getName());
		
		if (bindingResult.hasErrors()) {
			return "master/pricingunit/new";
		}
		
		pricingUnit = pricingUnitRepository.save(pricingUnit);
		return "redirect:/pricingunits";
	}
	
	@RequestMapping("/pricingunits/{id}")
	public String edit(@PathVariable("id") Long id, Model model) {
		model.addAttribute("unit", pricingUnitRepository.findOne(id));

		
		return "master/pricingunit/edit";
	}	
	
	@RequestMapping(value="/pricingunits/{id}", method=RequestMethod.POST)
	public String update(@PathVariable Long id, @Valid @ModelAttribute("unit")PricingUnit pricingUnit, BindingResult bindingResult, Model model) {
		LOGGER.info("Updating DocumentType: " + pricingUnit.getName());

		if (bindingResult.hasErrors()) {
			return "master/pricingunit/edit";
		}
		
		pricingUnit = pricingUnitRepository.save(pricingUnit);
		
		return "redirect:/pricingunits";
	}

}
