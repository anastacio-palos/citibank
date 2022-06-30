package com.ibm.openpages.ext.interfaces.common.bean;

/**
 * <p>
 * This bean represents a source within interface layout validation engine.
 * </p>
 * @author Luis Castillo <BR>
 * email : lacastil@mx1.ibm.com <BR>
 * company : IBM Consulting Services Mexico
 * @version 1.0
 * @custom.date : 02-17-2022
 */
public class SourceBean {
    private String name;
    private String kind;
    private String separator;
    private String mapper;
    private String filter;
    private String delimiters;


    public SourceBean(String name, String kind, String separator, String mapper, String filter, String delimiters) {
        this.name = name;
        this.kind = kind;
        this.separator = separator;
        this.mapper = mapper;
        this.filter = filter;
        this.delimiters = delimiters;
    }


    public SourceBean() {
        this.name = "";
        this.kind = "";
        this.separator = ",";
        this.mapper = "";
        this.filter = "";
        this.delimiters = "(,)";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public String getMapper() {
        return mapper;
    }

    public void setMapper(String mapper) {
        this.mapper = mapper;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getDelimiters() {
        return delimiters;
    }

    public void setDelimiters(String delimiters) {
        this.delimiters = delimiters;
    }

    @Override
    public String toString() {
        return "SourceBean{" +
                "name='" + name + '\'' +
                ", kind='" + kind + '\'' +
                ", separator='" + separator + '\'' +
                ", mapper='" + mapper + '\'' +
                ", filter='" + filter + '\'' +
                ", delimiters='" + delimiters + '\'' +
                '}';
    }
}
