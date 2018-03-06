package com.ptff.qsystem.web;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import org.springframework.web.bind.annotation.RequestParam;

import com.ptff.qsystem.data.Airport;
import com.ptff.qsystem.data.AirportRepository;
import com.ptff.qsystem.data.Country;
import com.ptff.qsystem.data.CountryRepository;
import com.ptff.qsystem.data.Pager;


@Controller
public class AirportController implements DefaultController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AirportController.class);
	
	@Autowired
	private AirportRepository airportRepository;
	
	@Autowired
	private CountryRepository countryRepository;
	
	
	@ModelAttribute("countries")
    public List<Country> countries() {
        return countryRepository.findAll();
    }
	
	@RequestMapping("/airports")
	public String listCountries(
			Model model,
			@PageableDefault(sort="code", direction=Sort.Direction.ASC, page=INITIAL_PAGE, size=INITIAL_PAGE_SIZE) Pageable pageable) {
		
		
		Page<Airport> airports = airportRepository.findAll(pageable);
		
		model.addAttribute("airports", airports);
		
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
