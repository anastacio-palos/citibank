package com.ibm.openpages.ext.interfaces.common.constant;

/**
 * <p>
 * This class contains the constants used by the validation engine.
 * </p>
 * @author Luis Castillo <BR>
 * email : lacastil@mx1.ibm.com <BR>
 * company : IBM Consulting Services Mexico
 * @version 1.0
 * @custom.date : 10-07-2021
 */
public class EngineConstants {
    public static final String STRING_TYPE = "string";
    public static final String DATE_TYPE = "date";
    public static final String LIST_TYPE = "List<?>";
    public static final String MAP_TYPE = "Map<?,?>";
    public static final String QUERY_MAP_TYPE = "Query-Map<?,?>";
    public static final String FIELD_LIST_TYPE = "Field-List<?>";

    public static final String DEFAULT_EXTERNAL_DATE_PATTERN = "MM/dd/yyyy HH:mm:ss a z";
    public static final String OPENPAGES_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String GRCOBJECT_PARENT_FIELD_NAME = "grcobject.parent.field.name";
    public static final String GRCOBJECT_PARENT_FIELD = "grcobject.parent.field";
    public static final String GRCOBJECT_CHILDREN_MULTIFIELD_FIRSTMATCH = "grcobject.children.multifield.firstmatch";
    public static final String GRCOBJECT_CHILDREN_MULTIFIELD_FIRSTMATCH_NAME = "grcobject.children.multifield.firstmatch.name";
    public static final String GRCOBJECT_CHILDREN_MULTIFIELD = "grcobject.children.multifield";
    public static final String GRCOBJECT_CHILDREN_MULTIFIELD_NAME = "grcobject.children.multifield.name";
    public static final String GRCOBJECT_CHILDREN_MULTIFIELD_FURTHEST_FUTURE = "grcobject.children.multifield.furthest.future";
    public static final String GRCOBJECT_CHILDREN_FURTHEST_FUTURE = "grcobject.children.furthest.future";
    public static final String GRCOBJECT_CHILDREN_FURTHEST_PAST = "grcobject.children.furthest.past";
    public static final String GRCOBJECT_CHILDREN_FURTHEST_ABSOLUTE = "grcobject.children.furthest.absolute";
    public static final String GRCOBJECT_CHILDREN = "grcobject.children";
    public static final String GRCOBJECT_MULTIFIELD_FIRSTMATCH = "grcobject.multifield.firstmatch";
    public static final String GRCOBJECT_MULTIFIELD_FIRSTMATCH_NAME = "grcobject.multifield.firstmatch.name";
    public static final String GRCOBJECT_MULTIFIELD = "grcobject.multifield";
    public static final String GRCOBJECT_MULTIFIELD_NAME = "grcobject.multifield.name";
    public static final String GRCOBJECT_MULTIFIELD_STATUS = "grcobject.multifield.status";
    public static final String GRCOBJECT_FIELD_TRANSFORM = "grcobject.field.transform";
    public static final String GRCOBJECT_FIELD_NAME_TRANSFORM = "grcobject.field.name.transform";
    public static final String GRCOBJECT_FIELD_NAME = "grcobject.field.name";
    public static final String GRCOBJECT_FIELD = "grcobject.field";
    public static final String GRCOBJECT_FIELD_EXTRACT = "grcobject.field.extract";
    public static final String GRCOBJECT_DESCRIPTION = "grcobject.description";
    public static final String GRCOBJECT_NAME = "grcobject.name";
    public static final String GRCOBJECT_ID = "grcobject.id";
    public static final String GRCOBJECT = "grcobject";
    public static final String STATIC = "static";

    public static final String DEFAULT_SEPARATOR_REGEX = ",";
    public static final String NAME_SEPARATOR_REGEX = "@";
    public static final String FILTER_AND_SEPARATOR_REGEX = "&&";
    public static final String FILTER_EQUALS_SEPARATOR_REGEX = "==";
    public static final String SEMICOLON_SEPARATOR_REGEX = ";";

    public static final String EMPTY_VALUE = "";
    public static final String UNDEFINED_VALUE = "undefined";
    public static final String UNBOUNDED_VALUE = "unbounded";

    public static final String OPENPAGES_SHORT_DATE_PATTERN = "yy-MMM-dd";

    public static final String XML_VERSION_1_0_ENCODING_UTF_8_STANDALONE_YES = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
}
