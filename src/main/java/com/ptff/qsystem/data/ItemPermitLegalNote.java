package com.ptff.qsystem.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="item_permit_legal_notes")
@Getter
@Setter
public class ItemPermitLegalNote {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="item_permit_id")
	private ItemPermit itemPermit;
	
	
	@ManyToOne
	@JoinColumn(name="legal_notes_id")
	private LegalNote legalNote;
	
}
