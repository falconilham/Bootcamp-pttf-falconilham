package com.ptff.qsystem.data;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@DiscriminatorValue(value = "4")
public class ItemAdditional extends Item{
	@Column(name="name")
	@NotEmpty
	private String name;

	
	public String getName() {
		return name;
	}
}
