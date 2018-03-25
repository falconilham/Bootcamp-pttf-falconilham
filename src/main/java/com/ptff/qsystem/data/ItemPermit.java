package com.ptff.qsystem.data;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name="item_permit")
@Getter
@Setter
public class ItemPermit {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(name="name")
	@NotNull
	@NotEmpty
	private String name;
	
	@Column(name="description")
	private String description;
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval = true)
	private Set<LegalNote> legalNotes;
}
