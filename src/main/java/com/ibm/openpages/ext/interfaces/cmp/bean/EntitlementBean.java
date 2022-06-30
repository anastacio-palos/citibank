package com.ibm.openpages.ext.interfaces.cmp.bean;

import java.util.List;

public class EntitlementBean {

	private String profile;
	private String appId;
	private String ciiid;
	private String product_name;
	private String vsr_request_ID;
	private String csiIdMapping;
	private String geid;
	private String goc;
	private String ritsid;
	private String soeid;
	private String deptid;
	private String deptname;
	private String jobfamily;
	private String jobtitle;
	private String locationid;
	private String managergeid;
	private String primaryloginid;
	private String firstname;
	private String lastname;
	private String email;
	private String city;
	private String state;
	private String country;
	private String address1;
	private String address2;
	private String zipcode;
	private String crs_Building;
	private String crs_Building_Code;
	private String crs_City;
	private String crs_Country;
	private String crs_Region;
	private String crs_State;
	private String profile_GEID;
	private String profile_RITSID;
	private String profile_SOEID;
	private String profile_Firstname;
	private String profile_Lastname;
	private String profile_Email;
	private String requestor_GEID;
	private String requestor_RITSID;
	private String requestor_SOEID;
	private String requestor_Firstname;
	private String requestor_Lastname;
	private String requestor_Email;
	private String gida_isa;
	private String action;
	private List<String> roles_add;
	private List<String> ia_division;
	private String auditor_group;
	private String datasegregatedcountry;
	private List<String> addcountry;
	private String justification;
	private String debug;
	private String idm_Action;
	private String idm_Resource;
	private String messageType;
	private String process;
	private String requestID;
	private String orderRequestIdString;
	private String support;
	private String support2;
	private String order_REQUEST_ID;
	private String lockUser;
	
	public EntitlementBean() {
		super();
	}
	
	public EntitlementBean(String appId, String ciiid, String product_name, String vsr_request_ID,
						   String csiIdMapping, String geid, String goc, String ritsid, String soeid, String deptid, String deptname,
						   String jobfamily, String jobtitle, String locationid, String managergeid, String primaryloginid,
						   String firstname, String lastname, String email, String city, String state, String country, String address1,
						   String address2, String zipcode, String crs_Building, String crs_Building_Code, String crs_City,
						   String crs_Country, String crs_Region, String crs_State, String profile_GEID, String profile_RITSID,
						   String profile_SOEID, String profile_Firstname, String profile_Lastname, String profile_Email,
						   String requestor_GEID, String requestor_RITSID, String requestor_SOEID, String requestor_Firstname,
						   String requestor_Lastname, String requestor_Email, String gida_isa, String action, List<String> roles_add,
						   List<String> ia_division, String auditor_group, String datasegregatedcountry, List<String> addcountry,
						   String justification, String debug, String idm_Action, String idm_Resource, String messageType,
						   String process, String requestID, String orderRequestIdString, String support, String support2,
						   String order_REQUEST_ID, String lockUser) {
		super();
		this.appId = appId;
		this.ciiid = ciiid;
		this.product_name = product_name;
		this.vsr_request_ID = vsr_request_ID;
		this.csiIdMapping = csiIdMapping;
		this.geid = geid;
		this.goc = goc;
		this.ritsid = ritsid;
		this.soeid = soeid;
		this.deptid = deptid;
		this.deptname = deptname;
		this.jobfamily = jobfamily;
		this.jobtitle = jobtitle;
		this.locationid = locationid;
		this.managergeid = managergeid;
		this.primaryloginid = primaryloginid;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.city = city;
		this.state = state;
		this.country = country;
		this.address1 = address1;
		this.address2 = address2;
		this.zipcode = zipcode;
		this.crs_Building = crs_Building;
		this.crs_Building_Code = crs_Building_Code;
		this.crs_City = crs_City;
		this.crs_Country = crs_Country;
		this.crs_Region = crs_Region;
		this.crs_State = crs_State;
		this.profile_GEID = profile_GEID;
		this.profile_RITSID = profile_RITSID;
		this.profile_SOEID = profile_SOEID;
		this.profile_Firstname = profile_Firstname;
		this.profile_Lastname = profile_Lastname;
		this.profile_Email = profile_Email;
		this.requestor_GEID = requestor_GEID;
		this.requestor_RITSID = requestor_RITSID;
		this.requestor_SOEID = requestor_SOEID;
		this.requestor_Firstname = requestor_Firstname;
		this.requestor_Lastname = requestor_Lastname;
		this.requestor_Email = requestor_Email;
		this.gida_isa = gida_isa;
		this.action = action;
		this.roles_add = roles_add;
		this.ia_division = ia_division;
		this.auditor_group = auditor_group;
		this.datasegregatedcountry = datasegregatedcountry;
		this.addcountry = addcountry;
		this.justification = justification;
		this.debug = debug;
		this.idm_Action = idm_Action;
		this.idm_Resource = idm_Resource;
		this.messageType = messageType;
		this.process = process;
		this.requestID = requestID;
		this.orderRequestIdString = orderRequestIdString;
		this.support = support;
		this.support2 = support2;
		this.order_REQUEST_ID = order_REQUEST_ID;
		this.lockUser = lockUser;
	}


	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getCiiid() {
		return ciiid;
	}
	public void setCiiid(String ciiid) {
		this.ciiid = ciiid;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getVsr_request_ID() {
		return vsr_request_ID;
	}
	public void setVsr_request_ID(String vsr_request_ID) {
		this.vsr_request_ID = vsr_request_ID;
	}
	public String getCsiIdMapping() {
		return csiIdMapping;
	}
	public void setCsiIdMapping(String csiIdMapping) {
		this.csiIdMapping = csiIdMapping;
	}
	public String getGeid() {
		return geid;
	}
	public void setGeid(String geid) {
		this.geid = geid;
	}
	public String getGoc() {
		return goc;
	}
	public void setGoc(String goc) {
		this.goc = goc;
	}
	public String getRitsid() {
		return ritsid;
	}
	public void setRitsid(String ritsid) {
		this.ritsid = ritsid;
	}
	public String getSoeid() {
		return soeid;
	}
	public void setSoeid(String soeid) {
		this.soeid = soeid;
	}
	public String getDeptid() {
		return deptid;
	}
	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}
	public String getDeptname() {
		return deptname;
	}
	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}
	public String getJobfamily() {
		return jobfamily;
	}
	public void setJobfamily(String jobfamily) {
		this.jobfamily = jobfamily;
	}
	public String getJobtitle() {
		return jobtitle;
	}
	public void setJobtitle(String jobtitle) {
		this.jobtitle = jobtitle;
	}
	public String getLocationid() {
		return locationid;
	}
	public void setLocationid(String locationid) {
		this.locationid = locationid;
	}
	public String getManagergeid() {
		return managergeid;
	}
	public void setManagergeid(String managergeid) {
		this.managergeid = managergeid;
	}
	public String getPrimaryloginid() {
		return primaryloginid;
	}
	public void setPrimaryloginid(String primaryloginid) {
		this.primaryloginid = primaryloginid;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getCrs_Building() {
		return crs_Building;
	}
	public void setCrs_Building(String crs_Building) {
		this.crs_Building = crs_Building;
	}
	public String getCrs_Building_Code() {
		return crs_Building_Code;
	}
	public void setCrs_Building_Code(String crs_Building_Code) {
		this.crs_Building_Code = crs_Building_Code;
	}
	public String getCrs_City() {
		return crs_City;
	}
	public void setCrs_City(String crs_City) {
		this.crs_City = crs_City;
	}
	public String getCrs_Country() {
		return crs_Country;
	}
	public void setCrs_Country(String crs_Country) {
		this.crs_Country = crs_Country;
	}
	public String getCrs_Region() {
		return crs_Region;
	}
	public void setCrs_Region(String crs_Region) {
		this.crs_Region = crs_Region;
	}
	public String getCrs_State() {
		return crs_State;
	}
	public void setCrs_State(String crs_State) {
		this.crs_State = crs_State;
	}
	public String getProfile_GEID() {
		return profile_GEID;
	}
	public void setProfile_GEID(String profile_GEID) {
		this.profile_GEID = profile_GEID;
	}
	public String getProfile_RITSID() {
		return profile_RITSID;
	}
	public void setProfile_RITSID(String profile_RITSID) {
		this.profile_RITSID = profile_RITSID;
	}
	public String getProfile_SOEID() {
		return profile_SOEID;
	}
	public void setProfile_SOEID(String profile_SOEID) {
		this.profile_SOEID = profile_SOEID;
	}
	public String getProfile_Firstname() {
		return profile_Firstname;
	}
	public void setProfile_Firstname(String profile_Firstname) {
		this.profile_Firstname = profile_Firstname;
	}
	public String getProfile_Lastname() {
		return profile_Lastname;
	}
	public void setProfile_Lastname(String profile_Lastname) {
		this.profile_Lastname = profile_Lastname;
	}
	public String getProfile_Email() {
		return profile_Email;
	}
	public void setProfile_Email(String profile_Email) {
		this.profile_Email = profile_Email;
	}
	public String getRequestor_GEID() {
		return requestor_GEID;
	}
	public void setRequestor_GEID(String requestor_GEID) {
		this.requestor_GEID = requestor_GEID;
	}
	public String getRequestor_RITSID() {
		return requestor_RITSID;
	}
	public void setRequestor_RITSID(String requestor_RITSID) {
		this.requestor_RITSID = requestor_RITSID;
	}
	public String getRequestor_SOEID() {
		return requestor_SOEID;
	}
	public void setRequestor_SOEID(String requestor_SOEID) {
		this.requestor_SOEID = requestor_SOEID;
	}
	public String getRequestor_Firstname() {
		return requestor_Firstname;
	}
	public void setRequestor_Firstname(String requestor_Firstname) {
		this.requestor_Firstname = requestor_Firstname;
	}
	public String getRequestor_Lastname() {
		return requestor_Lastname;
	}
	public void setRequestor_Lastname(String requestor_Lastname) {
		this.requestor_Lastname = requestor_Lastname;
	}
	public String getRequestor_Email() {
		return requestor_Email;
	}
	public void setRequestor_Email(String requestor_Email) {
		this.requestor_Email = requestor_Email;
	}
	public String getGida_isa() {
		return gida_isa;
	}
	public void setGida_isa(String gida_isa) {
		this.gida_isa = gida_isa;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public List<String> getRoles_add() {
		return roles_add;
	}
	public void setRoles_add(List<String> roles_add) {
		this.roles_add = roles_add;
	}
	public List<String> getIa_division() {
		return ia_division;
	}
	public void setIa_division(List<String> ia_division) {
		this.ia_division = ia_division;
	}
	public String getAuditor_group() {
		return auditor_group;
	}
	public void setAuditor_group(String auditor_group) {
		this.auditor_group = auditor_group;
	}
	public String getDatasegregatedcountry() {
		return datasegregatedcountry;
	}
	public void setDatasegregatedcountry(String datasegregatedcountry) {
		this.datasegregatedcountry = datasegregatedcountry;
	}
	public List<String> getAddcountry() {
		return addcountry;
	}
	public void setAddcountry(List<String> addcountry) {
		this.addcountry = addcountry;
	}
	public String getJustification() {
		return justification;
	}
	public void setJustification(String justification) {
		this.justification = justification;
	}
	public String getDebug() {
		return debug;
	}
	public void setDebug(String debug) {
		this.debug = debug;
	}
	public String getIdm_Action() {
		return idm_Action;
	}
	public void setIdm_Action(String idm_Action) {
		this.idm_Action = idm_Action;
	}
	public String getIdm_Resource() {
		return idm_Resource;
	}
	public void setIdm_Resource(String idm_Resource) {
		this.idm_Resource = idm_Resource;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getProcess() {
		return process;
	}
	public void setProcess(String process) {
		this.process = process;
	}
	public String getRequestID() {
		return requestID;
	}
	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}
	public String getOrderRequestIdString() {
		return orderRequestIdString;
	}
	public void setOrderRequestIdString(String orderRequestIdString) {
		this.orderRequestIdString = orderRequestIdString;
	}
	public String getSupport() {
		return support;
	}
	public void setSupport(String support) {
		this.support = support;
	}
	public String getSupport2() {
		return support2;
	}
	public void setSupport2(String support2) {
		this.support2 = support2;
	}
	public String getOrder_REQUEST_ID() {
		return order_REQUEST_ID;
	}
	public void setOrder_REQUEST_ID(String order_REQUEST_ID) {
		this.order_REQUEST_ID = order_REQUEST_ID;
	}

	public String getLockUser() {
		return lockUser;
	}

	public void setLockUser(String lockUser) {
		this.lockUser = lockUser;
	}
	
}
