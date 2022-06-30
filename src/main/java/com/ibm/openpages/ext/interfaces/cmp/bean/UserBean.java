package com.ibm.openpages.ext.interfaces.cmp.bean;

import com.ibm.openpages.api.logging.LoggerFactory;
import com.ibm.openpages.api.security.IGroup;
import com.ibm.openpages.api.security.IRoleAssignment;
import com.ibm.openpages.api.security.IUser;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class UserBean {
	private final Logger logger = LoggerFactory.getLoggerFactory().getSimpleLogger(UserBean.class);

	private String userName;
	private String id;
	private String description;
	private String firstName;
	private String lastName;
	private String middleName;
	private String password;
	private Date passwordCreationDate;
	private int passwordExpiresInDays;
	private boolean canChangePassword;
	private boolean isTemporaryPassword;
	private boolean isPasswordChangeFromAdmin;
	private boolean isLocked;
	private boolean isSecurityAdministrator;
	private boolean isHidden;
	private boolean isDeleted;
	private boolean isEnabled;
	private boolean isEditable;
	private String emailAddress;
	private int adminLevel;
	private String localeISOCode;
	private String displayName;
	private List<String> availableProfileNames;
	private String preferredProfileName;
	private List<GroupBean> groups;
	private List<RoleAssignmentBean> roles;
	private Map<String, String> lead_product_team;
	private Map<String, String> product_function_team;

	public UserBean(IUser theUser) {
		super();

		this.userName = theUser.getName();
		this.id = theUser.getId().toString();
		this.description = theUser.getDescription();
		this.firstName = theUser.getFirstName();
		this.lastName = theUser.getLastName();
		this.middleName = theUser.getMiddleName();
		this.passwordCreationDate = theUser.getPasswordCreationDate();
		this.passwordExpiresInDays = theUser.passwordExpiresInDays();
		this.canChangePassword = theUser.canChangePassword();
		this.isTemporaryPassword = theUser.isTemporaryPassword();
		this.isPasswordChangeFromAdmin = theUser.isPasswordChangeFromAdmin();
		this.isLocked = theUser.isLocked();
		this.isSecurityAdministrator = theUser.isSecurityAdministrator();
		this.isHidden = theUser.isHidden();
		this.isDeleted = theUser.isDeleted();
		this.isEnabled = theUser.isEnabled();
		this.isEditable = theUser.isEditable();
		this.emailAddress = theUser.getEmailAddress();
		this.adminLevel = theUser.getAdminLevel();
		this.displayName = theUser.getDisplayName();

		Iterator<IGroup> iGroupIterator = theUser.getGroups();
		List<GroupBean> groupBeanList = new ArrayList<GroupBean>();
		while (iGroupIterator.hasNext()) {
			IGroup iGroup = iGroupIterator.next();
			GroupBean groupBean = new GroupBean(iGroup);
			groupBeanList.add(groupBean);
			logger.debug(groupBean.toString());
		}
		setGroups(groupBeanList);

		Iterator<IRoleAssignment> iRoleIterator = theUser.getRoleAssignments();
		List<RoleAssignmentBean> roleList = new ArrayList<RoleAssignmentBean>();
		while (iRoleIterator.hasNext()) {
			IRoleAssignment iRole = iRoleIterator.next();
			RoleAssignmentBean roleAssignmentBean = new RoleAssignmentBean(iRole);
			roleList.add(roleAssignmentBean);
			logger.debug(roleAssignmentBean.toString());
		}
		setRoles(roleList);

		lead_product_team = new HashMap<>();
		product_function_team = new HashMap<>();
	}

	public UserBean(String userName, String id, String description, String firstName, String lastName,
					String middleName, String password, Date passwordCreationDate, int passwordExpiresInDays,
					boolean canChangePassword, boolean isTemporaryPassword, boolean isPasswordChangeFromAdmin, boolean isLocked,
					boolean isSecurityAdministrator, boolean isHidden, boolean isDeleted, boolean isEnabled, boolean isEditable,
					String emailAddress, int adminLevel, String localeISOCode, String displayName, List<GroupBean> groups,
					Map<String, String> product_function_team, Map<String, String> lead_product_team) {
		super();
		lead_product_team = new HashMap<>();
		product_function_team = new HashMap<>();
		this.userName = userName;
		this.id = id;
		this.description = description;
		this.firstName = firstName;
		this.lastName = lastName;
		this.middleName = middleName;
		this.password = password;
		this.passwordCreationDate = passwordCreationDate;
		this.passwordExpiresInDays = passwordExpiresInDays;
		this.canChangePassword = canChangePassword;
		this.isTemporaryPassword = isTemporaryPassword;
		this.isPasswordChangeFromAdmin = isPasswordChangeFromAdmin;
		this.isLocked = isLocked;
		this.isSecurityAdministrator = isSecurityAdministrator;
		this.isHidden = isHidden;
		this.isDeleted = isDeleted;
		this.isEnabled = isEnabled;
		this.isEditable = isEditable;
		this.emailAddress = emailAddress;
		this.adminLevel = adminLevel;
		this.localeISOCode = localeISOCode;
		this.displayName = displayName;
		this.groups = groups;
	}

	public UserBean(String geid, String soeid, String firstname, String lastname, String email, String country) {
		super();
		this.id = geid;
		this.userName = soeid;
		this.firstName = firstname;
		this.lastName = lastname;
		this.emailAddress = email;
		this.localeISOCode = country;
	}

	public UserBean() {

	}

	@Override
	public String toString() {
		return "UserBean{" + "userName='" + userName + '\'' + ", id='" + id + '\'' + ", description='" + description
				+ '\'' + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", middleName='"
				+ middleName + '\'' + ", password='" + password + '\'' + ", passwordCreationDate="
				+ passwordCreationDate + ", passwordExpiresInDays=" + passwordExpiresInDays + ", canChangePassword="
				+ canChangePassword + ", isTemporaryPassword=" + isTemporaryPassword + ", isPasswordChangeFromAdmin="
				+ isPasswordChangeFromAdmin + ", isLocked=" + isLocked + ", isSecurityAdministrator="
				+ isSecurityAdministrator + ", isHidden=" + isHidden + ", isDeleted=" + isDeleted + ", isEnabled="
				+ isEnabled + ", isEditable=" + isEditable + ", emailAddress='" + emailAddress + '\'' + ", adminLevel="
				+ adminLevel + ", localeISOCode='" + localeISOCode + '\'' + ", displayName='" + displayName + '\''
				+ ", availableProfileNames=" + availableProfileNames + ", preferredProfileName='" + preferredProfileName
				+ '\'' + ", groups=" + groups + ", auditor=" + product_function_team + ", ia_division=" + lead_product_team + ", roles="
				+ roles + '}';
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public boolean isTemporaryPassword() {
		return isTemporaryPassword;
	}

	public void setTemporaryPassword(boolean isTemporaryPassword) {
		this.isTemporaryPassword = isTemporaryPassword;
	}

	public boolean isPasswordChangeFromAdmin() {
		return isPasswordChangeFromAdmin;
	}

	public void setPasswordChangeFromAdmin(boolean isPasswordChangeFromAdmin) {
		this.isPasswordChangeFromAdmin = isPasswordChangeFromAdmin;
	}

	public boolean isLocked() {
		return isLocked;
	}

	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}

	public boolean isSecurityAdministrator() {
		return isSecurityAdministrator;
	}

	public void setSecurityAdministrator(boolean isSecurityAdministrator) {
		this.isSecurityAdministrator = isSecurityAdministrator;
	}

	public boolean isHidden() {
		return isHidden;
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public boolean isEditable() {
		return isEditable;
	}

	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLocaleISOCode() {
		return localeISOCode;
	}

	public void setLocaleISOCode(String localeISOCode) {
		this.localeISOCode = localeISOCode;
	}

	public void setStringISOCode(String localeISOCode) {
		this.localeISOCode = localeISOCode;
	}

	public List<GroupBean> getGroups() {
		return groups;
	}

	public void setGroups(List<GroupBean> groups) {
		this.groups = groups;
	}

	public List<String> getAvailableProfileNames() {
		return availableProfileNames;
	}

	public void setAvailableProfileNames(List<String> availableProfileNames) {
		this.availableProfileNames = availableProfileNames;
	}

	public String getPreferredProfileName() {
		return preferredProfileName;
	}

	public void setPreferredProfileName(String preferredProfileName) {
		this.preferredProfileName = preferredProfileName;
	}

	public List<RoleAssignmentBean> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleAssignmentBean> roles) {
		this.roles = roles;
	}

	public String getLeadProductTeam(String key) {
		if (lead_product_team.containsKey(key))
			return lead_product_team.get(key);
		else
			return null;
	}

	public Map<String, String> getAllLeadProductTeam() {
		return lead_product_team;
	}

	public void setLeadProductTeam(String key, String value) {
		lead_product_team.put(key, value);
	}

	public String getProductFunctionTeam(String key) {
		if (product_function_team.containsKey(key))
			return product_function_team.get(key);
		else
			return null;
	}

	public Map<String, String> getAllProductFunctionTeam() {
		return product_function_team;
	}
	
	public void setProductFunctionTeam(String key, String value) {
		product_function_team.put(key, value);
	}

	public void setAllLeadProductTeams(Map<String, String> allLeads){
		lead_product_team = allLeads;
	}
	public void setAllProductFunctionTeams(Map<String, String> allProducts){
		product_function_team = allProducts;
	}

}
