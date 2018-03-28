package com.ptff.qsystem.form;

import com.ptff.qsystem.data.ItemType;

import lombok.Data;

@Data
public class ItemSearchForm {
	private ItemType itemType;
	private String name;
}
