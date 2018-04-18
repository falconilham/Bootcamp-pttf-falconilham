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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ptff.qsystem.data.Depot;
import com.ptff.qsystem.data.DepotRepository;


@Controller
public class DepotController implements DefaultController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DepotController.class);
	
	@Autowired
	private DepotRepository depotRepository;
	
	@RequestMapping("/depots")
	public String list(
			Model model,
			@PageableDefault(sort="code", direction=Sort.Direction.ASC, page=INITIAL_PAGE, size=INITIAL_PAGE_SIZE) Pageable pageable) {
		
		Page<Depot> depots = depotRepository.findAll(pageable);
		model.addAttribute("depots", depots);
		
		return "master/depot/index";
	}

	@RequestMapping("/depots/new")
	public String newItem(Model model) {
		Depot depot = new Depot();
		model.addAttribute("depot", depot);
		
		return "master/depot/new";
	}
	
	@RequestMapping(value="/depots", method=RequestMethod.POST)
	public String save(@Valid Depot depot, BindingResult bindingResult, Model model) {
		LOGGER.info("Saving Depot " + depot.getName());
		
		if (bindingResult.hasErrors()) {
			return "master/depot/new";
		}
		
		depot = depotRepository.save(depot);
		return "redirect:/depots";
	}
	
	@RequestMapping("/depots/{id}")
	public String edit(@PathVariable("id") Long id, Model model) {
		model.addAttribute("depot", depotRepository.findOne(id));

		
		return "master/depot/edit";
	}	
	
	@RequestMapping(value="/depots/{id}", method=RequestMethod.POST)
	public String update(@PathVariable Long id, @Valid Depot depot, BindingResult bindingResult, Model model) {
		LOGGER.info("Updating depot: " + depot.getName());

		if (bindingResult.hasErrors()) {
			return "master/depot/edit";
		}
		
		depot = depotRepository.save(depot);
		
		return "redirect:/depots";
	}

}
