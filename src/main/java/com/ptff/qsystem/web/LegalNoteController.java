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

import com.ptff.qsystem.data.LegalNote;
import com.ptff.qsystem.data.LegalNoteRepository;


@Controller
public class LegalNoteController implements DefaultController {
	private static final Logger LOGGER = LoggerFactory.getLogger(LegalNoteController.class);
	
	
	@Autowired
	private LegalNoteRepository legalNoteRepository;
	
	@RequestMapping("/legalnotes")
	public String list(
			@PageableDefault(sort="name", direction=Sort.Direction.ASC, page=INITIAL_PAGE, size=INITIAL_PAGE_SIZE) Pageable pageable,
			Model model) {
		
		Page<LegalNote> legalNotes = legalNoteRepository.findAll(pageable);
		
		model.addAttribute("notes", legalNotes);
		
		return "master/legalnote/index";
	}

	@RequestMapping("/legalnotes/new")
	public String news(Model model) {
		LegalNote legalNote = new LegalNote();
		model.addAttribute("note", legalNote);
		
		return "master/legalnote/new";
	}
	
	@RequestMapping(value="/legalnotes", method=RequestMethod.POST)
	public String save(@Valid @ModelAttribute("unit")LegalNote legalNote, BindingResult bindingResult, Model model) {
		LOGGER.info("Saving Legal Note " + legalNote.getName());
		
		if (bindingResult.hasErrors()) {
			return "master/legalnote/new";
		}
		
		legalNote = legalNoteRepository.save(legalNote);
		return "redirect:/legalnotes";
	}
	
	@RequestMapping("/legalnotes/{id}")
	public String edit(@PathVariable("id") Long id, Model model) {
		model.addAttribute("note", legalNoteRepository.findOne(id));

		
		return "master/legalnote/edit";
	}	
	
	@RequestMapping(value="/legalnotes/{id}", method=RequestMethod.POST)
	public String update(@PathVariable Long id, @Valid @ModelAttribute("note")LegalNote legalNote, BindingResult bindingResult, Model model) {
		LOGGER.info("Updating Legal Note: " + legalNote.getName());

		if (bindingResult.hasErrors()) {
			return "master/legalnote/edit";
		}
		
		legalNote = legalNoteRepository.save(legalNote);
		
		return "redirect:/legalnotes";
	}

}
