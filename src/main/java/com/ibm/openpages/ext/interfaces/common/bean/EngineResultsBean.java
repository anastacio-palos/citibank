package com.ibm.openpages.ext.interfaces.common.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * <p>
 * This bean represents a interface layout validation engine result.
 * </p>
 * @author Luis Castillo <BR>
 * email : lacastil@mx1.ibm.com <BR>
 * company : IBM Consulting Services Mexico
 * @version 1.0
 * @custom.date : 10-07-2021
 */
public class EngineResultsBean {
	private Boolean success;
	private List<String> messages;
	private Map<String, Object> dataValues;

	
	public EngineResultsBean() {
		super();
		this.messages = new ArrayList<>();
		this.success = false;
		this.dataValues = new HashMap<>();
	}

	public EngineResultsBean(Boolean success, List<String> messages, Map<String, Object> dataValues) {
		super();
		this.success = success;
		this.messages = messages;
		this.dataValues = dataValues;
	}

	public Boolean isSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public Map<String, Object> getDataValues() {
		return dataValues;
	}

	public void setDataValues(Map<String, Object> dataValues) {
		this.dataValues = dataValues;
	}

	@Override
	public String toString() {
		return "EngineResultsBean{" +
				"success=" + success +
				", messages=" + messages +
				", dataValues=" + dataValues +
				'}';
	}
}
