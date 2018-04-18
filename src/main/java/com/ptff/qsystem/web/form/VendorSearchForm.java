package com.ptff.qsystem.web.form;

import lombok.Data;

@Data
public class VendorSearchForm {
	public enum VendorSearchType
    {
        BY_STATUS, BY_ALL
    }
	
	private String name;
	
	private String status;
	
	private VendorSearchType type;
	
	public VendorSearchForm getSearchFilter() {
		VendorSearchForm searchForm = new VendorSearchForm();
		searchForm.name = this.name;
		searchForm.status = this.status;
		
		if (name.length() == 0)
			searchForm.name = "";
		
		if (status.equals("ALL")) {
			searchForm.type = VendorSearchType.BY_ALL;
		} else {
			searchForm.type = VendorSearchType.BY_STATUS;
		}
			
		return searchForm;
	}
	
}
