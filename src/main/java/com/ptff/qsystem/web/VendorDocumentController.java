package com.ptff.qsystem.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ptff.qsystem.data.Document;
import com.ptff.qsystem.data.DocumentRepository;
import com.ptff.qsystem.data.DocumentType;
import com.ptff.qsystem.data.DocumentTypeRepository;
import com.ptff.qsystem.data.SettingsRepository;
import com.ptff.qsystem.data.Vendor;
import com.ptff.qsystem.data.VendorDocument;
import com.ptff.qsystem.data.VendorDocumentRepository;
import com.ptff.qsystem.data.VendorHistory;
import com.ptff.qsystem.data.VendorHistoryRepository;
import com.ptff.qsystem.data.VendorRepository;
import com.ptff.qsystem.data.VendorStatus;



@Controller
public class VendorDocumentController implements DefaultController {
	private static final Logger LOGGER = LoggerFactory.getLogger(VendorDocumentController.class);
	
	@Autowired
	private VendorRepository vendorRepository;
	
	@Autowired
	private SettingsRepository settingsRepository;
	
	@Autowired
	private DocumentRepository documentRepository;
	
	@Autowired
	private VendorDocumentRepository vendorDocumentRepository;
	
	@Autowired
	private VendorHistoryRepository vendorHistoryRepository;
	
	@Autowired
	private DocumentTypeRepository documentTypeRepository;
	
	@ModelAttribute("documenttypes")
    public List<DocumentType> documentTypes() {
        return documentTypeRepository.findAll();
    }
	
	@RequestMapping("/vendors/{id}/document")
	@PreAuthorize("hasAnyRole('ROLE_PURCHASING', 'ROLE_PURCHASINGMANAGER', 'ROLE_SU')")
	public String newVendorDocument(@PathVariable("id") Long id, Model model) {
		Vendor vendor = vendorRepository.findOne(id);
		Document document = new Document();
		
		VendorDocument vendorDocument = new VendorDocument();
		vendorDocument.setVendor(vendor);
		vendorDocument.setDocument(document);
		
		model.addAttribute("vendor", vendor);
		model.addAttribute("vendordocument", vendorDocument);
		
		return "purchasing/vendor/new_document";
	}
	
	@RequestMapping("/vendors/{vendorId}/document/{documentId}")
	@PreAuthorize("hasAnyRole('ROLE_PURCHASING', 'ROLE_PURCHASINGMANAGER', 'ROLE_FINANCE', 'ROLE_DIRECTOR', 'ROLE_SU')")
	public void newVendorDocument(@PathVariable("vendorId") Long vendorId, @PathVariable("documentId") Long documentId, HttpServletResponse response) {
		VendorDocument customerDocument = vendorDocumentRepository.findOne(documentId);
		
		try {
			InputStream is = new FileInputStream(customerDocument.getDocument().getFilePath());
			IOUtils.copy(is, response.getOutputStream());
			response.setContentType(customerDocument.getDocument().getContentType());
			response.flushBuffer();
		} catch (IOException ioe) {
			LOGGER.error("IO Error while writing to output stream", ioe);
		}
	}
	
	@RequestMapping("/vendors/{id}/document/save")
	@Transactional
	@PreAuthorize("hasAnyRole('ROLE_PURCHASING', 'ROLE_PURCHASINGMANAGER', 'ROLE_SU')")
	public String saveVendorDocument(@PathVariable("id") Long id, 
											@Valid @ModelAttribute("vendordocument") VendorDocument vendorDocument, BindingResult bindingResult, @RequestParam(value="file", required=true) MultipartFile file, Model model) throws IllegalStateException, IOException {
		
		String rootLocation = settingsRepository.findOne("VENDOR_FILE_LOCATION").getValueAsString();
		Vendor vendor = vendorRepository.findOne(id);
		//Validate
		if (bindingResult.hasErrors()) {
			model.addAttribute("vendor", vendor);
			model.addAttribute("file", file);
			
			return "purchasing/vendor/new_document";
		}
		
		
		// Save the File
		Document document = vendorDocument.getDocument();
		String fileName = vendor.getName()+vendorDocument.getDocumentType().getName()+vendorDocument.getNumber();
		fileName = fileName.replaceAll("[^A-Za-z0-9].", "_");
		fileName = fileName + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
		
		LOGGER.info("Attempting to save {}  to : {}", fileName, rootLocation);
		
		
		File dir = new File(rootLocation);
		if (!dir.exists())
			dir.mkdirs();
		File convFile = new File(rootLocation+"/"+fileName);
		
	    file.transferTo(convFile);
	    document.setFilePath(convFile.getPath());
	    document.setName(fileName);
	    document.setContentType(file.getContentType());
		document = documentRepository.save(document);
		
		vendor.setStatus(VendorStatus.DRAFT);
		vendor = vendorRepository.save(vendor);

		vendorDocument.setDocument(document);
		vendorDocument = vendorDocumentRepository.save(vendorDocument);
		
		VendorHistory vendorHistory = new VendorHistory();
		vendorHistory.setVendor(vendor);
		vendorHistory.setMessage(String.format("Adding %s document with the number %s", vendorDocument.getDocumentType().getName(), vendorDocument.getNumber()));
		vendorHistoryRepository.save(vendorHistory);
		
		return "redirect:/vendors/"+vendor.getId();
	}
	
	
}
