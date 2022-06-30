package com.ibm.openpages.ext.interfaces.common.bean;

import java.util.Deque;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

/**
 * <p>
 * This bean represents a metadata within interface layout validation engine.
 * </p>
 * @author Luis Castillo <BR>
 * email : lacastil@mx1.ibm.com <BR>
 * company : IBM Consulting Services Mexico
 * @version 1.0
 * @custom.date : 10-07-2021
 */
public class EngineMetadataBean {


	private String name;
	private Deque<FieldBean> fields;

	public EngineMetadataBean() {
		super();
	}

	public EngineMetadataBean(String name, Deque<FieldBean> fieldBeans) {
		super();
		this.name = name;
		this.fields = fieldBeans;
	}

	public Map<String,String> getFieldPropertyMap(){
		return fields.stream()
				.collect(toMap(FieldBean::getMapTo, FieldBean::getName));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public Deque<FieldBean> getFields() {
		return fields;
	}

	public void setFields(Deque<FieldBean> fieldBeans) {
		this.fields = fieldBeans;
	}

	@Override
	public String toString() {
		return "EngineMetadataBean{" +
				"name='" + name + '\'' +
				", fields=" + fields +
				'}';
	}
}
