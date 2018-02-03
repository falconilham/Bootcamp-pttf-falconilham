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

import com.ptff.qsystem.data.Seaport;
import com.ptff.qsystem.data.SeaportRepository;
import com.ptff.qsystem.data.Pager;


@Controller
public class SeaportController {
	private static final Logger LOGGER = LoggerFactory.getLogger(SeaportController.class);
	
	private static final int BUTTONS_TO_SHOW = 5;
	private static final int INITIAL_PAGE = 0;
	private static final int INITIAL_PAGE_SIZE = 20;
	private static final int[] PAGE_SIZES = { 5, 10, 20, 100 };
	
	@Autowired
	private SeaportRepository seaportRepository;
	
	@RequestMapping("/seaports")
	public String listCountries(
			@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page,
			Model model) {
		
		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
		
		Page<Seaport> seaports = seaportRepository.findAll(new PageRequest(evalPage, evalPageSize));
		Pager pager = new Pager(seaports.getTotalPages(), seaports.getNumber(), BUTTONS_TO_SHOW);
		
		model.addAttribute("seaports", seaports);
		model.addAttribute("selectedPageSize", evalPageSize);
		model.addAttribute("pageSizes", PAGE_SIZES);
		model.addAttribute("pager", pager);
		
		return "master/seaport/index";
	}

	@RequestMapping("/seaports/new")
	public String newSeaport(Model model) {
		Seaport seaport = new Seaport();
		model.addAttribute("seaport", seaport);
		
		return "master/seaport/new";
	}
	
	@RequestMapping(value="/seaports", method=RequestMethod.POST)
	public String saveSeaport(@Valid Seaport seaport, BindingResult bindingResult, Model model) {
		LOGGER.info("Saving Seaport " + seaport.getName());
		
		if (bindingResult.hasErrors()) {
			return "master/seaport/new";
		}
		
		seaport = seaportRepository.save(seaport);
		return "redirect:/seaports";
	}
	
	@RequestMapping("/seaports/{id}")
	public String editSeaport(@PathVariable("id") Long id, Model model) {
		model.addAttribute("seaport", seaportRepository.findOne(id));

		
		return "master/seaport/edit";
	}	
	
	@RequestMapping(value="/seaports/{id}", method=RequestMethod.POST)
	public String updateSeaport(@PathVariable Long id, @Valid Seaport seaport, BindingResult bindingResult, Model model) {
		LOGGER.info("Updating seaport: " + seaport.getName());

		if (bindingResult.hasErrors()) {
			return "master/seaport/edit";
		}
		
		seaport = seaportRepository.save(seaport);
		
		return "redirect:/seaports";
	}

}
