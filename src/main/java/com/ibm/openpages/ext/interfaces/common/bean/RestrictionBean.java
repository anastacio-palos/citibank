package com.ibm.openpages.ext.interfaces.common.bean;

import java.util.List;

/**
 * <p>
 * This bean represents a restriction within interface layout validation engine.
 * </p>
 * @author Luis Castillo <BR>
 * email : lacastil@mx1.ibm.com <BR>
 * company : IBM Consulting Services Mexico
 * @version 1.0
 * @custom.date : 12-22-2021
 */
public class RestrictionBean {
	private String pattern;
	private int minLength;
	private int maxLength;
	private List<ConstraintBean> constraints;


	public RestrictionBean() {
		super();
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public int getMinLength() {
		return minLength;
	}

	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public List<ConstraintBean> getConstraints() {
		return constraints;
	}

	public void setConstraints(List<ConstraintBean> constraints) {
		this.constraints = constraints;
	}

	@Override
	public String toString() {
		return "RestrictionBean{" +
				"pattern='" + pattern + '\'' +
				", minLength=" + minLength +
				", maxLength=" + maxLength +
				", constraints=" + constraints +
				'}';
	}
}
