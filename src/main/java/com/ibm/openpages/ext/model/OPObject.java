package com.ibm.openpages.ext.model;

import com.ibm.openpages.ext.constant.AuditScopingHelperConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OPObject {

    private String key;

    private String title;

    private String description;

    private String type;

    private String source;

    private boolean processed = false;

    private String srcRef;

    private String ae;
    private String aeID;

    private OPObject auditReference;

    private String aeObjRefID;

    public OPObject getAuditReference() {

        return auditReference;
    }

    public void setAuditReference(final OPObject auditReference) {

        this.auditReference = auditReference;
    }

    private String parent;

    private boolean parentProcessed;

    private String scope;

    public String getAeID() {

        return aeID;
    }

    public void setAeID(final String aeID) {

        this.aeID = aeID;
    }

    public String getAe() {

        return ae;
    }

    public void setAe(final String ae) {

        this.ae = ae;
    }

    public String getScope() {

        return Optional.ofNullable(scope).orElse(AuditScopingHelperConstant.PCRW_SCOPE_YES);
    }

    public void setScope(final String scope) {

        this.scope = scope;
    }

    private List<String> hierarchy = new ArrayList<>();

    public OPObject() {

    }

    public OPObject(String key) {

        this.key = key;
    }

    public String getKey() {

        return key;
    }

    public void setKey(final String key) {

        this.key = key;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(final String title) {

        this.title = title;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(final String description) {

        this.description = description;
    }

    public String getType() {

        return type;
    }

    public void setType(final String type) {

        this.type = type;
    }

    public boolean isProcessed() {

        return processed;
    }

    public void setProcessed(final boolean processed) {

        this.processed = processed;
    }

    public String getAeObjRefID() {

        return aeObjRefID;
    }

    public void setAeObjRefID(final String aeObjRefID) {

        this.aeObjRefID = aeObjRefID;
    }

    public List<String> getHierarchy() {

        if (hierarchy == null || hierarchy.isEmpty()) {
            hierarchy.add(title);
        }

        return hierarchy;
    }

    public void setHierarchy(final List<String> hierarchy) {

        Optional.ofNullable(hierarchy).filter(h -> !h.isEmpty()).ifPresent(v -> {
            this.hierarchy = hierarchy;
            hierarchy.add(this.title);
        });
    }

    public String getSource() {

        return source;
    }

    public void setSource(final String source) {

        this.source = source;
    }

    public String getSrcRef() {

        return srcRef;
    }

    public void setSrcRef(final String srcRef) {

        this.srcRef = srcRef;
    }

    public String getParent() {

        return parent;
    }

    public void setParent(final String parent) {

        this.parent = parent;
    }

    public boolean isParentProcessed() {

        return parentProcessed;
    }

    public void setParentProcessed(final boolean parentProcessed) {

        this.parentProcessed = parentProcessed;
    }

    @Override
    public String toString() {

        return "OPObject{" + "key='" + key + '\'' + ", title='" + title + '\'' + ", description='" + description + '\'' + ", type='" + type + '\'' + ", source='" + source + '\'' + ", processed=" + processed + ", srcRef='" + srcRef + '\'' + ", ae='" + ae + '\'' + ", aeID='" + aeID + '\'' + ", auditReference=" + auditReference + ", aeObjRefID='" + aeObjRefID + '\'' + ", parent='" + parent + '\'' + ", parentProcessed=" + parentProcessed + ", scope='" + scope + '\'' + ", hierarchy=" + hierarchy + '}';
    }
}
