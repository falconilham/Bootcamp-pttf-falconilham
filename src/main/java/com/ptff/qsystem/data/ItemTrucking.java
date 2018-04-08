package com.ptff.qsystem.data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue(value = "2")
public class ItemTrucking extends Item {

	
	public String getName() {
		return "";
	}
}
