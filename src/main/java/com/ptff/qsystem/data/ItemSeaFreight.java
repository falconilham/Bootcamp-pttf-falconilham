package com.ptff.qsystem.data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue(value = "1")
public class ItemSeaFreight extends Item {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="origin_seaport_id")
	@NotNull
	private Seaport origin;
	
	@ManyToOne
	@JoinColumn(name="destination_seaport_id")
	@NotNull
	private Seaport destination;
	
	public String getName() {
		String name = "";
		
		name += (origin!=null)?origin.getName():"";
		name += " - ";
		name += (destination!=null)?destination.getName():"";
		
		return name;
	}
}
