package com.ptff.qsystem.data.validator;

@FunctionalInterface
public interface ValidationCondition {
	void validate(final Object object, final ValidationMessage message);
}
