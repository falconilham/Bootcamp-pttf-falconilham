package com.ptff.qsystem.data;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="setting")
@Data
public class Settings {
	@Id
	@Column(name="name", length=100)
	private String name;
	
	@Column(name="value", length=100)
	private String value;
	
	@Column(name="description", length=100)
	private String description;
	
	public BigDecimal getValueAsBigDecimal() {
		if (value != null) {
			try {
				return new BigDecimal(value);
			} catch (NumberFormatException nfe) {
				return BigDecimal.ZERO;
			}
		}
		
		return BigDecimal.ZERO;
	}

	public Integer getValueAsInteger() {
		if (value != null) {
			try {
				return new Integer(value);
			} catch (NumberFormatException nfe) {
				return 0;
			}
		}
		
		return 0;
	}
	
	public Boolean getValueAsBoolean() {
		if (value != null) {
			try {
				return new Boolean(value);
			} catch (NumberFormatException nfe) {
				return Boolean.FALSE;
			}
		}
		
		return Boolean.FALSE;
	}

	public String getValueAsString() {
		if (value != null) {
			return value;
		}
		
		return "";
	}
	
	public Settings() {
		name = "";
		value = "";
		description = "";
	}
	
	public Settings(String name, String value) {
		this.name = name;
		this.value = value;
		description = "";
	}

	public LocalTime getValueAsLocalTime() {
		return LocalTime.parse(value, DateTimeFormatter.ofPattern("HH:mm"));
	}



}
