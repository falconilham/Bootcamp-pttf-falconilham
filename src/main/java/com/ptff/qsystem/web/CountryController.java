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
import com.ptff.qsystem.data.Pager;


@Controller
public class CountryController {
	private static final Logger LOGGER = LoggerFactory.getLogger(CountryController.class);
	
	private static final int BUTTONS_TO_SHOW = 5;
	private static final int INITIAL_PAGE = 0;
	private static final int INITIAL_PAGE_SIZE = 20;
	private static final int[] PAGE_SIZES = { 5, 10, 20, 100 };
	
	@Autowired
	private CountryRepository countryRepository;
	
	@RequestMapping("/countries")
	public String listCountries(
			@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page,
			Model model) {
		
		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
		
		Page<Country> countries = countryRepository.findAll(new PageRequest(evalPage, evalPageSize));
		Pager pager = new Pager(countries.getTotalPages(), countries.getNumber(), BUTTONS_TO_SHOW);
		
		model.addAttribute("countries", countries);
		model.addAttribute("selectedPageSize", evalPageSize);
		model.addAttribute("pageSizes", PAGE_SIZES);
		model.addAttribute("pager", pager);
		
		return "master/country/index";
	}

	@RequestMapping("/countries/new")
	public String newCountry(Model model) {
		Country country = new Country();
		model.addAttribute("country", country);
		
		return "master/country/new";
	}
	
	@RequestMapping(value="/countries", method=RequestMethod.POST)
	public String saveCountry(@Valid Country country, BindingResult bindingResult, Model model) {
		LOGGER.info("Saving Country " + country.getName());
		
		if (bindingResult.hasErrors()) {
			return "master/country/new";
		}
		
		country = countryRepository.save(country);
		return "redirect:/countries";
	}
	
	@RequestMapping("/countries/{id}")
	public String editCountry(@PathVariable("id") Long id, Model model) {
		model.addAttribute("country", countryRepository.findOne(id));

		
		return "master/country/edit";
	}	
	
	@RequestMapping(value="/countries/{id}", method=RequestMethod.POST)
	public String updateCountry(@PathVariable Long id, @Valid Country country, BindingResult bindingResult, Model model) {
		LOGGER.info("Updating country: " + country.getName());

		if (bindingResult.hasErrors()) {
			return "master/country/edit";
		}
		
		country = countryRepository.save(country);
		
		return "redirect:/countries";
	}

}
