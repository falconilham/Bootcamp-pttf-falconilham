package com.ptff.qsystem.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ptff.qsystem.data.Airport;
import com.ptff.qsystem.data.AirportRepository;
import com.ptff.qsystem.data.Item;
import com.ptff.qsystem.data.ItemPermit;
import com.ptff.qsystem.data.ItemType;
import com.ptff.qsystem.data.LegalNote;
import com.ptff.qsystem.data.LegalNoteRepository;
import com.ptff.qsystem.data.ProductFeature;
import com.ptff.qsystem.data.ProductFeatureRepository;
import com.ptff.qsystem.data.Vendor;
import com.ptff.qsystem.data.VendorRepository;
import com.ptff.qsystem.service.ItemService;

@Controller
@RequestMapping("/item/{itemTypeId}")
public class ItemController implements DefaultController{
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private VendorRepository vendorRepository;
	
	@Autowired
	private LegalNoteRepository legalNoteRepository;
	
	@Autowired
	private ProductFeatureRepository productFeatureRepository;
	
	@Autowired
	private AirportRepository airportRepository;
	
	@ModelAttribute("airports")
    public List<Airport> airports() {
        return airportRepository.findAll();
    }
	
	@ModelAttribute("vendors")
    public List<Vendor> vendors() {
        return vendorRepository.findAll();
    }
	
	@ModelAttribute("legalNotes")
    public List<LegalNote> legalNotes() {
        return legalNoteRepository.findAll();
    }
	
	@ModelAttribute("productFeatures")
    public List<ProductFeature> productFeatures() {
        return productFeatureRepository.findAll();
    }
	
	@RequestMapping("")
	public String list(
			@PathVariable("itemTypeId")int itemTypeId,
			Model model, 
			@PageableDefault(sort="id", direction=Sort.Direction.ASC, page=INITIAL_PAGE, size=INITIAL_PAGE_SIZE) Pageable pageable) {
		
		Page<Item> items = itemService.list(ItemType.values()[itemTypeId], pageable);
		
		model.addAttribute("itemType", ItemType.values()[itemTypeId]);
		model.addAttribute("items", items);
		
		return "item/index";
	}
	
	@RequestMapping("/new")
	public String form(
			@PathVariable("itemTypeId")int itemTypeId,
			Model model) {
				
		model.addAttribute("itemType", ItemType.values()[itemTypeId]);
		model.addAttribute("item", itemService.createItem(ItemType.values()[itemTypeId]));
		
		return "item/new";
	}
	
	@RequestMapping("/{itemId}")
	public String form(
			@PathVariable("itemTypeId")int itemTypeId,
			@PathVariable("itemId")Long itemId,
			Model model) {
				
		model.addAttribute("itemType", ItemType.values()[itemTypeId]);
		model.addAttribute("item", itemService.getItem( ItemType.values()[itemTypeId], itemId));
		
		return "item/show";
	}

	@RequestMapping(value="/{itemId}/legalnotes", method=RequestMethod.POST)
	public String addLegalNote(			
			@PathVariable("itemTypeId")int itemTypeId,
			@PathVariable("itemId")Long itemId,
			@ModelAttribute("legalNote")Long legalNoteId, 
			Model model) {
		
		itemService.addLegalNote(itemId, legalNoteId);
			    
		return "redirect:/item/"+itemTypeId+"/"+itemId;
	}
	
	@RequestMapping(value="/{itemId}/legalnotes/{legalNoteId}/remove")
	public String removeLegalNote(
				@PathVariable("itemTypeId")int itemTypeId,
				@PathVariable("itemId")Long itemId,
				@PathVariable("legalNoteId")Long legalNoteId,
				Model model) {
		itemService.removeLegalNote(itemId, legalNoteId);
			    
		return  "redirect:/item/"+itemTypeId+"/"+itemId;
	}
	
	@RequestMapping(value="/{itemId}/productfeatures", method=RequestMethod.POST)
	public String addProductFeature(			
			@PathVariable("itemTypeId")int itemTypeId,
			@PathVariable("itemId")Long itemId,
			@ModelAttribute("productFeature")Long productFeatureId, 
			Model model) {
		
		itemService.addProductFeature(itemId, productFeatureId);
			    
		return "redirect:/item/"+itemTypeId+"/"+itemId;
	}
	
	@RequestMapping(value="/{itemId}/productfeatures/{legalNoteId}/remove")
	public String removeProductFeature(
				@PathVariable("itemTypeId")int itemTypeId,
				@PathVariable("itemId")Long itemId,
				@PathVariable("productFeatureId")Long productFeatureId,
				Model model) {
		itemService.removeProductFeature(itemId, productFeatureId);
			    
		return  "redirect:/item/"+itemTypeId+"/"+itemId;
	}
}
