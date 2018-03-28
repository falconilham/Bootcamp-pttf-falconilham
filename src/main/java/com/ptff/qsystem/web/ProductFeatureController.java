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

import com.ptff.qsystem.data.ProductFeature;
import com.ptff.qsystem.data.ProductFeatureRepository;


@Controller
public class ProductFeatureController implements DefaultController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductFeatureController.class);
	
	
	@Autowired
	private ProductFeatureRepository productFeatureRepository;
	
	@RequestMapping("/productfeatures")
	public String list(
			@PageableDefault(sort="name", direction=Sort.Direction.ASC, page=INITIAL_PAGE, size=INITIAL_PAGE_SIZE) Pageable pageable,
			Model model) {
		
		Page<ProductFeature> productFeatures = productFeatureRepository.findAll(pageable);
		
		model.addAttribute("features", productFeatures);
		
		return "master/productfeature/index";
	}

	@RequestMapping("/productfeatures/new")
	public String news(Model model) {
		ProductFeature productFeature = new ProductFeature();
		model.addAttribute("feature", productFeature);
		
		return "master/productfeature/new";
	}
	
	@RequestMapping(value="/productfeatures", method=RequestMethod.POST)
	public String save(@Valid @ModelAttribute("feature")ProductFeature productFeature, BindingResult bindingResult, Model model) {
		LOGGER.info("Saving Product Feature " + productFeature.getName());
		
		if (bindingResult.hasErrors()) {
			return "master/productfeature/new";
		}
		
		productFeature = productFeatureRepository.save(productFeature);
		return "redirect:/productfeatures";
	}
	
	@RequestMapping("/productfeatures/{id}")
	public String edit(@PathVariable("id") Long id, Model model) {
		model.addAttribute("feature", productFeatureRepository.findOne(id));

		
		return "master/productfeature/edit";
	}	
	
	@RequestMapping(value="/productfeatures/{id}", method=RequestMethod.POST)
	public String update(@PathVariable Long id, @Valid @ModelAttribute("feature")ProductFeature productFeature, BindingResult bindingResult, Model model) {
		LOGGER.info("Updating Product Feature: " + productFeature.getName());

		if (bindingResult.hasErrors()) {
			return "master/productfeature/edit";
		}
		
		productFeature = productFeatureRepository.save(productFeature);
		
		return "redirect:/productfeature";
	}

}
