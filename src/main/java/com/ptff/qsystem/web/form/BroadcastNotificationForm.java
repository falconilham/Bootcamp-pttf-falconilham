package com.ptff.qsystem.web.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

@Data
public class BroadcastNotificationForm {
	@NotNull
	String username;
	
	@NotNull
	String userGroupName;
	
	@NotNull
	@NotEmpty
	String message;
}
