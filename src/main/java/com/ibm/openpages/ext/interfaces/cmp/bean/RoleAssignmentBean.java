package com.ibm.openpages.ext.interfaces.cmp.bean;

import com.ibm.openpages.api.metadata.ITypeDefinition;
import com.ibm.openpages.api.resource.IFolder;
import com.ibm.openpages.api.security.IRoleAssignment;

public class RoleAssignmentBean {

    private String roleTemplate;
    private String folderPath;
    private String domain;
    private String objectType;

    public RoleAssignmentBean() {
        super();
        this.roleTemplate = "";
        this.folderPath = "";
        this.objectType = "";
        this.securityPrincipal = "";
    }

    public RoleAssignmentBean(IRoleAssignment iRole){
        super();
        this.roleTemplate = iRole.getRoleTemplateName();
        IFolder iFolder = iRole.getFolder();
        this.folderPath = iFolder.getPath();
        this.domain = iFolder.getName();
        ITypeDefinition itypeDefinition = iRole.getTypeDefinition();
        this.objectType = itypeDefinition.getName();
    }

    public RoleAssignmentBean(String roleName, String folderPath, String folderName, String objectType, String securityPrincipal) {
        this.roleTemplate = roleName;
        this.folderPath = folderPath;
        this.domain = folderName;
        this.objectType = objectType;
        this.securityPrincipal = securityPrincipal;
    }

    public String getRoleTemplate() {
        return roleTemplate;
    }

    public void setRoleTemplate(String roleTemplate) {
        this.roleTemplate = roleTemplate;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getSecurityPrincipal() {
        return securityPrincipal;
    }

    public void setSecurityPrincipal(String securityPrincipal) {
        this.securityPrincipal = securityPrincipal;
    }

    private String securityPrincipal;

    @Override
    public String toString() {
        return "RoleBean{" +
                "roleName='" + roleTemplate + '\'' +
                ", folderPath='" + folderPath + '\'' +
                ", folderName='" + domain + '\'' +
                ", objectType='" + objectType + '\'' +
                ", securityPrincipal='" + securityPrincipal + '\'' +
                '}';
    }



}
