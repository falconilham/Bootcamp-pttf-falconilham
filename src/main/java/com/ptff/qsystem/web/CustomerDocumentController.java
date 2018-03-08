package com.ptff.qsystem.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.ptff.qsystem.data.Pager;
import com.ptff.qsystem.data.Settings;
import com.ptff.qsystem.data.SettingsRepository;
import com.ptff.qsystem.data.Country;
import com.ptff.qsystem.data.Customer;
import com.ptff.qsystem.data.CustomerContactPerson;
import com.ptff.qsystem.data.CustomerContactPersonRepository;
import com.ptff.qsystem.data.CustomerContactType;
import com.ptff.qsystem.data.CustomerDocument;
import com.ptff.qsystem.data.CustomerDocumentRepository;
import com.ptff.qsystem.data.CustomerRepository;
import com.ptff.qsystem.data.CustomerStatus;
import com.ptff.qsystem.service.StorageService;



@Controller
public class CustomerDocumentController implements DefaultController {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerDocumentController.class);
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private SettingsRepository settingsRepository;
	
	@Autowired
	private DocumentRepository documentRepository;
	
	@Autowired
	private CustomerDocumentRepository customerDocumentRepository;
	
	@Autowired
	private DocumentTypeRepository documentTypeRepository;
	
	@ModelAttribute("documenttypes")
    public List<DocumentType> documentTypes() {
        return documentTypeRepository.findAll();
    }
	
	@RequestMapping("/customers/{id}/document")
	public String newCustomerDocument(@PathVariable("id") Long id, Model model) {
		Customer customer = customerRepository.findOne(id);
		Document document = new Document();
		
		CustomerDocument customerDocument = new CustomerDocument();
		customerDocument.setCustomer(customer);
		customerDocument.setDocument(document);
		
		model.addAttribute("customer", customer);
		model.addAttribute("customerdocument", customerDocument);
		
		return "sale/customer/new_document";
	}
	
	@RequestMapping("/customers/{customerId}/document/{documentId}")
	public void newCustomerDocument(@PathVariable("customerId") Long customerId, @PathVariable("documentId") Long documentId, HttpServletResponse response) {
		CustomerDocument customerDocument = customerDocumentRepository.findOne(documentId);
		
		try {
			InputStream is = new FileInputStream(customerDocument.getDocument().getFilePath());
			IOUtils.copy(is, response.getOutputStream());
			response.setContentType(customerDocument.getDocument().getContentType());
			response.flushBuffer();
		} catch (IOException ioe) {
			LOGGER.error("IO Error while writing to output stream", ioe);
		}
	}
	
	@RequestMapping("/customers/{id}/document/save")
	@Transactional
	public String saveCustomerDocument(@PathVariable("id") Long id, 
											@Valid @ModelAttribute("customerdocument") CustomerDocument customerDocument, BindingResult bindingResult, @RequestParam(value="file", required=true) MultipartFile file, Model model) throws IllegalStateException, IOException {
		
		String rootLocation = settingsRepository.findOne("CUSTOMER_FILE_LOCATION").getValueAsString();
		Customer customer = customerRepository.findOne(id);
		//Validate
		if (bindingResult.hasErrors()) {
			model.addAttribute("customer", customer);
			model.addAttribute("file", file);
			
			return "sale/customer/new_document";
		}
		
		
		// Save the File
		Document document = customerDocument.getDocument();
		String fileName = customer.getName()+customerDocument.getDocumentType().getName()+customerDocument.getNumber();
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
		
		customer.setStatus(CustomerStatus.DRAFT);
		customer = customerRepository.save(customer);

		customerDocument.setDocument(document);
		customerDocument = customerDocumentRepository.save(customerDocument);
		
		return "redirect:/customers/"+customer.getId();
	}
	
	
}
