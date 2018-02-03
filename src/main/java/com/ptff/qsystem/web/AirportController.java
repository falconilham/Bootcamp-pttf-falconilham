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

import com.ptff.qsystem.data.Airport;
import com.ptff.qsystem.data.AirportRepository;
import com.ptff.qsystem.data.Pager;


@Controller
public class AirportController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AirportController.class);
	
	private static final int BUTTONS_TO_SHOW = 5;
	private static final int INITIAL_PAGE = 0;
	private static final int INITIAL_PAGE_SIZE = 20;
	private static final int[] PAGE_SIZES = { 5, 10, 20, 100 };
	
	@Autowired
	private AirportRepository airportRepository;
	
	@RequestMapping("/airports")
	public String listCountries(
			@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page,
			Model model) {
		
		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
		
		Page<Airport> airports = airportRepository.findAll(new PageRequest(evalPage, evalPageSize));
		Pager pager = new Pager(airports.getTotalPages(), airports.getNumber(), BUTTONS_TO_SHOW);
		
		model.addAttribute("airports", airports);
		model.addAttribute("selectedPageSize", evalPageSize);
		model.addAttribute("pageSizes", PAGE_SIZES);
		model.addAttribute("pager", pager);
		
		return "master/airport/index";
	}

	@RequestMapping("/airports/new")
	public String newAirport(Model model) {
		Airport airport = new Airport();
		model.addAttribute("airport", airport);
		
		return "master/airport/new";
	}
	
	@RequestMapping(value="/airports", method=RequestMethod.POST)
	public String saveAirport(@Valid Airport airport, BindingResult bindingResult, Model model) {
		LOGGER.info("Saving Airport " + airport.getName());
		
		if (bindingResult.hasErrors()) {
			return "master/airport/new";
		}
		
		airport = airportRepository.save(airport);
		return "redirect:/airports";
	}
	
	@RequestMapping("/airports/{id}")
	public String editAirport(@PathVariable("id") Long id, Model model) {
		model.addAttribute("airport", airportRepository.findOne(id));

		
		return "master/airport/edit";
	}	
	
	@RequestMapping(value="/airports/{id}", method=RequestMethod.POST)
	public String updateAirport(@PathVariable Long id, @Valid Airport airport, BindingResult bindingResult, Model model) {
		LOGGER.info("Updating airport: " + airport.getName());

		if (bindingResult.hasErrors()) {
			return "master/airport/edit";
		}
		
		airport = airportRepository.save(airport);
		
		return "redirect:/airports";
	}

}
