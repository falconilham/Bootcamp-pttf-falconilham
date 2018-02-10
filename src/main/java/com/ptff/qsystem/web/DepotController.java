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

import com.ptff.qsystem.data.Depot;
import com.ptff.qsystem.data.DepotRepository;
import com.ptff.qsystem.data.Pager;


@Controller
public class DepotController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DepotController.class);
	
	private static final int BUTTONS_TO_SHOW = 5;
	private static final int INITIAL_PAGE = 0;
	private static final int INITIAL_PAGE_SIZE = 20;
	private static final int[] PAGE_SIZES = { 5, 10, 20, 100 };
	
	@Autowired
	private DepotRepository depotRepository;
	
	@RequestMapping("/depots")
	public String list(
			@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page,
			Model model) {
		
		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
		
		Page<Depot> depots = depotRepository.findAll(new PageRequest(evalPage, evalPageSize));
		Pager pager = new Pager(depots.getTotalPages(), depots.getNumber(), BUTTONS_TO_SHOW);
		
		model.addAttribute("depots", depots);
		model.addAttribute("selectedPageSize", evalPageSize);
		model.addAttribute("pageSizes", PAGE_SIZES);
		model.addAttribute("pager", pager);
		
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
