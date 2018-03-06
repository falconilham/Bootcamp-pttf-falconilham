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

import com.ptff.qsystem.data.Country;
import com.ptff.qsystem.data.CountryRepository;


@Controller
public class CountryController implements DefaultController {
	private static final Logger LOGGER = LoggerFactory.getLogger(CountryController.class);
	
	@Autowired
	private CountryRepository countryRepository;
	
	@RequestMapping("/countries")
	public String listCountriest(Model model, 
			@PageableDefault(sort="code", direction=Sort.Direction.ASC, page=INITIAL_PAGE, size=INITIAL_PAGE_SIZE) Pageable pageable) {
		
		Page<Country> countries = countryRepository.findAll(pageable);
		
		model.addAttribute("countries", countries);
		
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
