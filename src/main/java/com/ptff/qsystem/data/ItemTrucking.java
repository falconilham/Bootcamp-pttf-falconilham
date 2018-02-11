package com.ptff.qsystem.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

@Data
@Entity
@Table(name="item_trucking")
public class ItemTrucking {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="source_depot")
	private Depot sourceDepot;
	
	@ManyToOne
	@JoinColumn(name="destination_depot")
	private Depot destinationDepot;
	
	@Column(name="description")
	@NotNull
	@NotEmpty
	private String description;
	

}
