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

import com.ptff.qsystem.data.DocumentType;
import com.ptff.qsystem.data.DocumentTypeRepository;
import com.ptff.qsystem.data.Pager;


@Controller
public class DocumentTypeController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentTypeController.class);
	
	private static final int BUTTONS_TO_SHOW = 5;
	private static final int INITIAL_PAGE = 0;
	private static final int INITIAL_PAGE_SIZE = 20;
	private static final int[] PAGE_SIZES = { 5, 10, 20, 100 };
	
	@Autowired
	private DocumentTypeRepository documentTypeRepository;
	
	@RequestMapping("/documenttypes")
	public String list(
			@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page,
			Model model) {
		
		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
		
		Page<DocumentType> documentTypes = documentTypeRepository.findAll(new PageRequest(evalPage, evalPageSize));
		Pager pager = new Pager(documentTypes.getTotalPages(), documentTypes.getNumber(), BUTTONS_TO_SHOW);
		
		model.addAttribute("documenttypes", documentTypes);
		model.addAttribute("selectedPageSize", evalPageSize);
		model.addAttribute("pageSizes", PAGE_SIZES);
		model.addAttribute("pager", pager);
		
		return "master/documenttype/index";
	}

	@RequestMapping("/documenttypes/new")
	public String news(Model model) {
		DocumentType documentType = new DocumentType();
		model.addAttribute("type", documentType);
		
		return "master/documenttype/new";
	}
	
	@RequestMapping(value="/documenttypes", method=RequestMethod.POST)
	public String save(@Valid DocumentType documentType, BindingResult bindingResult, Model model) {
		LOGGER.info("Saving DocumentType " + documentType.getName());
		
		if (bindingResult.hasErrors()) {
			return "master/documenttype/new";
		}
		
		documentType = documentTypeRepository.save(documentType);
		return "redirect:/documenttypes";
	}
	
	@RequestMapping("/documenttypes/{id}")
	public String editSeaport(@PathVariable("id") Long id, Model model) {
		model.addAttribute("type", documentTypeRepository.findOne(id));

		
		return "master/documenttype/edit";
	}	
	
	@RequestMapping(value="/documenttypes/{id}", method=RequestMethod.POST)
	public String updateSeaport(@PathVariable Long id, @Valid DocumentType documentType, BindingResult bindingResult, Model model) {
		LOGGER.info("Updating DocumentType: " + documentType.getName());

		if (bindingResult.hasErrors()) {
			return "master/documenttype/edit";
		}
		
		documentType = documentTypeRepository.save(documentType);
		
		return "redirect:/documenttypes";
	}

}
