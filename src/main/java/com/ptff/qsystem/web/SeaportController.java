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

import com.ptff.qsystem.data.Seaport;
import com.ptff.qsystem.data.SeaportRepository;
import com.ptff.qsystem.data.Vendor;
import com.ptff.qsystem.data.Country;
import com.ptff.qsystem.data.CountryRepository;
import com.ptff.qsystem.data.Pager;


@Controller
public class SeaportController implements DefaultController {
	private static final Logger LOGGER = LoggerFactory.getLogger(SeaportController.class);
	
	@Autowired
	private SeaportRepository seaportRepository;
	
	@Autowired
	private CountryRepository countryRepository;
	
	
	@ModelAttribute("countries")
    public List<Country> countries() {
        return countryRepository.findAll();
    }
	
	@RequestMapping("/seaports")
	public String list(
			Model model,
			@PageableDefault(sort="code", direction=Sort.Direction.ASC, page=INITIAL_PAGE, size=INITIAL_PAGE_SIZE) Pageable pageable) {
		
		Page<Seaport> seaports = seaportRepository.findAll(pageable);
		
		model.addAttribute("seaports", seaports);
		
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
