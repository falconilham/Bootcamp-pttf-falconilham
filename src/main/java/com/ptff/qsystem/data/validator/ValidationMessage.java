package com.ptff.qsystem.data.validator;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class ValidationMessage {
	private List<String> errors = new ArrayList<String>();
	private List<String> warnings = new ArrayList<String>();
	
	public void addError(String error) {
		errors.add(error);
	}
	
	public int errorCount() {
		return errors.size();
	}
	
	public boolean hasErrors() {
		return errors.size()!=0;
	}
	
	public void addWarning(String warning) {
		warnings.add(warning);
	}
	
	public int warningCount() {
		return warnings.size();
	}
	
	public boolean hasWarnings() {
		return warnings.size()!=0;
	}
}
