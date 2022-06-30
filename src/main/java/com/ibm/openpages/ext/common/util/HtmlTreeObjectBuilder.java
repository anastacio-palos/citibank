package com.ibm.openpages.ext.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.core.Logger;

import com.ibm.openpages.api.resource.IAssociationNode;
import com.ibm.openpages.api.resource.IGRCObject;

public class HtmlTreeObjectBuilder {
    public static final String ICON_SOX_BUS_ENTITY_GIF = "icon-SOXBusEntity.gif";
    public static final String ICON_SOX_PROCESS_GIF = "icon-SOXProcess.gif";
    public static final String ICON_SOX_SUBPROCESS_GIF = "icon-SOXSubprocess.gif";
    public static final String ICON_PLUS_GIF = "plus.gif";
    public static final String ICON_MINUS_GIF = "minus.gif";

    public static final String PATH_TO_ICON_SOX_BUS_ENTITY_GIF = "../../images/" + ICON_SOX_BUS_ENTITY_GIF;
    public static final String PATH_TO_ICON_SOX_PROCESS_GIF = "../../images/" + ICON_SOX_PROCESS_GIF;
    public static final String PATH_TO_ICON_SOX_SUBPROCESS_GIF = "../../images/" + ICON_SOX_SUBPROCESS_GIF;
    public static final String PATH_TO_ICON_PLUS_GIF = "../../images/rev/icons/" + ICON_PLUS_GIF;
    public static final String PATH_TO_ICON_MINUS_GIF = "../../images/rev/icons/" + ICON_MINUS_GIF;

    private static final String BUSINESS_ENTITY_OBJECT_TYPE = "SOXBusEntity";
    private static final String SUBPROCESS_OBJECT_TYPE = "SOXSubprocess";
    private static final String PROCESS_OBJECT_TYPE = "SOXProcess";

    public static final String YES = "Yes";
    public static final String SLASH = "/";
    public static final String DOT_TXT = ".txt";
    public static final String DD_MM_YYYY_DATE_FORMAT = "dd-MM-yyyy";

    private Logger logger = null;
    private Integer indexOfPathColumnInEntityRecord = null;

    public HtmlTreeObjectBuilder(Logger logger, int indexOfPathColumnInEntityRecord) {
        this.logger = logger;
        this.indexOfPathColumnInEntityRecord = indexOfPathColumnInEntityRecord;
    }

    public String generateHtmlForBusinessEntitiesRecursively(List<String> parentEntities,
            List<String> allBusinessEntities, int treeDepthlevel, String parentId, String jsFunctionToCallOnClick)
            throws Exception {
        return generateHtmlForBusinessEntitiesRecursively(parentEntities, allBusinessEntities, treeDepthlevel, parentId,
                jsFunctionToCallOnClick, null);

    }

    public String generateHtmlForBusinessEntitiesRecursively(List<String> parentEntities,
            List<String> allBusinessEntities, int treeDepthlevel, String parentId, String jsFunctionToCallOnClick,
            List<String> unavailableEntitiesIds) throws Exception {
        treeDepthlevel++;
        String html = "<ul id='childrenOf_" + parentId + "' class='" + (treeDepthlevel == 0 ? "" : "hidden") + "'>";
        if(parentEntities == null) {
            return "";
        }
        for (String parentEntity : parentEntities) {
            String[] values = parentEntity.split(CommonResourceUtils.RECORD_COLUMN_SEPARATOR, -1);
            if (values.length < 3) {
                throw new Exception(
                        "generateHtmlForBusinessEntitiesRecursively: expecting values.length >=3 ; actual value: "
                                + values.length + "; parentEntity=" + parentEntity);
            }
            String id = values[0];
            String name = values[1];
            String description = values[2];

            html += "<li id='" + id + "'>";
            html += "<img id='expColIcon_" + id + "' src='" + PATH_TO_ICON_PLUS_GIF + "'"
                    + " onclick=\"expandOrCollapseChildren('" + id + "', '" + PATH_TO_ICON_MINUS_GIF + "','"
                    + PATH_TO_ICON_PLUS_GIF + "')\"/>&nbsp;";
            html += "<img src=\"" + PATH_TO_ICON_SOX_BUS_ENTITY_GIF + "\"/>&nbsp;";

            if (unavailableEntitiesIds != null && unavailableEntitiesIds.contains(id)) {
                html += "<i>" + name /* +(description.isEmpty() ? "" : " / " + description) */ + "</i>";
            }
            else {
                html += "<a href=\"javascript:" + jsFunctionToCallOnClick + "('" + id + "', '" + name + "', '"
                        + description + "');\">" + name/* + (description.isEmpty() ? "" : " / " + description) */
                        + "</a>";
            }
            List<String> childrenEntities = getEntitiesChildrenOf(parentEntity, allBusinessEntities);
            html += generateHtmlForBusinessEntitiesRecursively(childrenEntities, allBusinessEntities, treeDepthlevel,
                    id, jsFunctionToCallOnClick, unavailableEntitiesIds);
            html += "</li>";
        }

        html += "</ul>";
        return html;
    }

    public String generateHtmlForProcessesSubprocesses(List<IGRCObject> parents, List<IGRCObject> allProcSubProc,
            Integer treeDepthlevel, String parentId) {
        treeDepthlevel++;
        String html = "<ul id=\"childrenOf_" + parentId + "\" class=\"" + (treeDepthlevel == 0 ? "" : "hidden") + "\">";
        for (IGRCObject parent : parents) {
            String iconName = ICON_SOX_PROCESS_GIF;
            String objType = parent.getType().getName().toString();
            if (objType.equalsIgnoreCase(SUBPROCESS_OBJECT_TYPE)) {
                iconName = ICON_SOX_SUBPROCESS_GIF;
            }
            else if (objType.equalsIgnoreCase(BUSINESS_ENTITY_OBJECT_TYPE)) {
                iconName = ICON_SOX_BUS_ENTITY_GIF;
            }
            String id = "" + parent.getId();

            html += "<li id=\"" + id + "\">";
            html += "<img id=\"expColIcon_" + id + "\" src=\"icons/" + ICON_PLUS_GIF + "\""
                    + " onclick=\"expandOrCollapseChildren('" + id + "')\"/>&nbsp;";
            html += "<img src=\"icons/" + iconName + "\"/>&nbsp;";
            if (objType.equalsIgnoreCase(PROCESS_OBJECT_TYPE) || objType.equalsIgnoreCase(SUBPROCESS_OBJECT_TYPE)) {
                html += "<a href=\"javascript:confirmDestinationProcOrSubProcSelection('" + id + "', '"
                        + parent.getName() + "');\">" + parent.getName() + " / " + parent.getDescription() + "</a>";
            }
            else {
                html += parent.getName();
            }

            List<IGRCObject> childrenProcSubProc = getChildrenOf(parent, allProcSubProc);
            html += generateHtmlForProcessesSubprocesses(childrenProcSubProc, allProcSubProc, treeDepthlevel, id);
            html += "</li>";
        }

        html += "</ul>";
        // debug("generated html:\n" + html);
        return html;
    }

    public Integer getSmallestIntegerKeyFromMap(Map<Integer, List<String>> entityPathSlashCountMap) {
        Integer smallestKey = null;
        for (Integer key : entityPathSlashCountMap.keySet()) {
            if (smallestKey == null) {
                smallestKey = key;
            }
            else if (key < smallestKey) {
                smallestKey = key;
            }
        }

        if (smallestKey == null) {
            smallestKey = 0;
        }
        return smallestKey;
    }

    public List<String> getEntitiesChildrenOf(String parent, List<String> allBusinessEntities) {
        List<String> children = new ArrayList<String>();
        for (String businessEntity : allBusinessEntities) {
            if (isFirstEntityChildOfSecondOne(businessEntity, parent)) {
                children.add(businessEntity);
            }
        }
        return children;
    }

    public boolean isFirstEntityChildOfSecondOne(String childToTest, String parentToTest) {
        String childToTestPath = childToTest.split(CommonResourceUtils.RECORD_COLUMN_SEPARATOR, -1)[indexOfPathColumnInEntityRecord];
        String parentToTestPath = parentToTest.split(CommonResourceUtils.RECORD_COLUMN_SEPARATOR, -1)[indexOfPathColumnInEntityRecord];

        if (childToTestPath.startsWith(parentToTestPath + SLASH)
                && childToTestPath.length() > parentToTestPath.length()) {
            String childRelativePath = childToTestPath.replaceFirst(parentToTestPath, "");
            if (getEntityPathSlashCharCount(childRelativePath) == 1) {
                return true;
            }
        }
        return false;
    }

    public Integer getEntityPathSlashCharCount(String entityPath) {
        int slashCharCount = 0;
        while (entityPath.indexOf(SLASH) != -1) {
            entityPath = entityPath.substring(0, entityPath.lastIndexOf(SLASH));
            slashCharCount++;
        }
        return slashCharCount;
    }

    public List<String> getRootEntities(List<String> allBusinessEntities) {
        Map<Integer, List<String>> entityPathSlashCountMap = getEntityPathSlashCountMap(allBusinessEntities);
        int smallestSlashCount = getSmallestIntegerKeyFromMap(entityPathSlashCountMap);
        return entityPathSlashCountMap.get(smallestSlashCount);
    }

    public Map<Integer, List<String>> getEntityPathSlashCountMap(List<String> allBusinessEntities) {
        Map<Integer, List<String>> entityPathSlashCountMap = new HashMap<Integer, List<String>>(0);
        for (String entityIdNameDescInScopeLocation : allBusinessEntities) {
            String[] values = entityIdNameDescInScopeLocation.split(CommonResourceUtils.RECORD_COLUMN_SEPARATOR, -1);
            Integer slashCharCount = getEntityPathSlashCharCount(values[indexOfPathColumnInEntityRecord]);
            if (entityPathSlashCountMap.get(slashCharCount) == null) {
                List<String> list = new ArrayList<String>();
                list.add(entityIdNameDescInScopeLocation);
                entityPathSlashCountMap.put(slashCharCount, list);
            }
            else {
                entityPathSlashCountMap.get(slashCharCount).add(entityIdNameDescInScopeLocation);
            }
        }
        return entityPathSlashCountMap;
    }

    public List<IGRCObject> getChildrenOf(IGRCObject parent, List<IGRCObject> allProcessesSubprocesses) {
        List<IGRCObject> children = new ArrayList<IGRCObject>();
        for (IGRCObject procOrSubproc : allProcessesSubprocesses) {
            for (IAssociationNode node : parent.getChildren()) {
                if (node.getId().equals(procOrSubproc.getId()))
                    children.add(procOrSubproc);
            }
        }

        return children;
    }
}
