package com.ibm.openpages.ext.interfaces.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.openpages.api.resource.GRCObjectFilter;
import com.ibm.openpages.api.resource.IAssociationNode;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.resource.IncludeAssociations;
import com.ibm.openpages.api.service.IConfigurationService;
import com.ibm.openpages.api.service.IMetaDataService;
import com.ibm.openpages.api.service.IResourceService;
import com.ibm.openpages.api.service.IServiceFactory;
import com.ibm.openpages.ext.interfaces.common.bean.EngineMetadataBean;
import com.ibm.openpages.ext.interfaces.common.bean.FieldBean;
import com.ibm.openpages.ext.interfaces.common.constant.EngineConstants;
import com.ibm.openpages.ext.interfaces.icaps.constant.IssueCommonConstants;
import com.ibm.openpages.ext.interfaces.icaps.constant.IssueConstants;
import com.ibm.openpages.ext.tss.service.IApplicationUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class EngineUtil {
    @Autowired
    IApplicationUtil applicationUtil;
    /**
     * Convierte en un Json a un bean del tipo <code>EngineMetadataBean</code>.
     *
     * @param JSONString Archivo JSON obtenido de Open Pages
     * @return
     */
    public static EngineMetadataBean jsonToMetaEngineBean(String JSONString, Logger logger) throws IOException {
        if (JSONString != null) {
            ObjectMapper mapper = new ObjectMapper();
            EngineMetadataBean engineBean = null;
            engineBean = mapper.readValue(JSONString, EngineMetadataBean.class);
            return engineBean;
        } else {
            return null;
        }
    }

    public static List<IGRCObject> getPrimaryChildrenFromIssue(IGRCObject parentIssueObject, String typeName,
                                                        IServiceFactory apiFactory, Logger logger) {
        List<IGRCObject> childList = new ArrayList<>();
        IConfigurationService configService = apiFactory.createConfigurationService();
        IMetaDataService iMetaDataService = apiFactory.createMetaDataService();
        IResourceService rss = apiFactory.createResourceService();
        GRCObjectFilter filter = new GRCObjectFilter(configService.getCurrentReportingPeriod());
        filter.getAssociationFilter().setIncludeAssociations(IncludeAssociations.CHILD);
        filter.getAssociationFilter().setTypeFilters(
                iMetaDataService.getType(typeName));
        IGRCObject returnObject = rss.getGRCObject(parentIssueObject.getId(), filter);
        List<IAssociationNode> child = returnObject.getChildren();
        returnObject.getChildren().stream().forEach(currentChild -> {
                    IGRCObject o = rss.getGRCObject(currentChild.getId());
                    if(!IssueCommonConstants.YES_VALUE.equals(ServiceUtil.getLabelValueFromField(o.getField(IssueConstants.IS_SUBSCRIBED_CAP))))
                        childList.add(rss.getGRCObject(currentChild.getId()));
                    else
                        logger.debug("A subscribed Object was omitted");
                }
        );
        return childList;
    }

    public static List<IGRCObject> getChildrenFromIssue(IGRCObject parentIssueObject, String typeName,
                                                  IServiceFactory apiFactory, Logger logger) {
        List<IGRCObject> childList = new ArrayList<>();
        IConfigurationService configService = apiFactory.createConfigurationService();
        IMetaDataService iMetaDataService = apiFactory.createMetaDataService();
        IResourceService rss = apiFactory.createResourceService();
        GRCObjectFilter filter = new GRCObjectFilter(configService.getCurrentReportingPeriod());
        filter.getAssociationFilter().setIncludeAssociations(IncludeAssociations.CHILD);
        filter.getAssociationFilter().setTypeFilters(
                iMetaDataService.getType(typeName));
        IGRCObject returnObject = rss.getGRCObject(parentIssueObject.getId(), filter);
        List<IAssociationNode> child = returnObject.getChildren();
        returnObject.getChildren().stream().forEach(currentChild ->
                childList.add(rss.getGRCObject(currentChild.getId()))
        );
        return childList;
    }

    public static List<IGRCObject> getParentFromIssue(IGRCObject parentIssueObject, String typeName,
                                                IServiceFactory apiFactory, Logger logger) {
        List<IGRCObject> parentList = new ArrayList<>();
        IConfigurationService configService = apiFactory.createConfigurationService();
        IMetaDataService iMetaDataService = apiFactory.createMetaDataService();
        IResourceService rss = apiFactory.createResourceService();
        GRCObjectFilter filter = new GRCObjectFilter(configService.getCurrentReportingPeriod());
        filter.getAssociationFilter().setIncludeAssociations(IncludeAssociations.PARENT);
        filter.getAssociationFilter().setTypeFilters(iMetaDataService.getType(typeName));
        IGRCObject returnObject = rss.getGRCObject(parentIssueObject.getId(), filter);
        returnObject.getParents().stream().forEach(currentParent ->
                parentList.add(rss.getGRCObject(currentParent.getId())));
        return parentList;
    }

    public static String getMultiFieldValue(IGRCObject object, String name, String separator, boolean isLabel, boolean isFirstMatch) {
        StringTokenizer st;
        st = new StringTokenizer(name, separator);
        String multiValue = EngineConstants.EMPTY_VALUE;
        while (st.hasMoreTokens()) {
            if(isLabel)
                multiValue += ServiceUtil.getLabelValueFromField(object.getField(st.nextToken())) + separator;
            else
                multiValue += ServiceUtil.getNameValueFromField(object.getField(st.nextToken())) + separator;
            if (isFirstMatch && multiValue != null && !multiValue.isEmpty())
                break;
        }
        while(!multiValue.isEmpty() && multiValue.endsWith(separator)){
            multiValue = multiValue.substring(0, multiValue.length() - 1);
        }
        return multiValue;
    }

    public static String getMultiValueFromChildren(IGRCObject object, FieldBean field, boolean isLabel, boolean isFirstMatch, IServiceFactory apiFactory, Logger logger) {
        String[] params;
        String multiValue = EngineConstants.EMPTY_VALUE;
        boolean filterPassed;
        List<String> filters = null;
        logger.trace("params:" + field.getSource().getName());
        params = field.getSource().getName().split(EngineConstants.NAME_SEPARATOR_REGEX);
        if (field.getSource().getFilter() != null && !field.getSource().getFilter().isEmpty()) {
            logger.trace("Filters:" + field.getSource().getFilter());
            filters = Arrays.asList(field.getSource().getFilter().split(EngineConstants.FILTER_AND_SEPARATOR_REGEX));
        }
        for (IGRCObject o : EngineUtil.getChildrenFromIssue(object, params[1], apiFactory, logger)) {
            filterPassed = true;
            logger.trace("processing children:"+o.getName() + " of type [" + o.getType().getName() + "]");
            if(filters != null){
                for(String i : filters){
                    String [] filter = i.split(EngineConstants.FILTER_EQUALS_SEPARATOR_REGEX);
                    if (filter != null && filter.length == 2) {
                        String oValue = ServiceUtil.getLabelValueFromField(o.getField(filter[0]));
                        logger.trace(filter[0] + " with value: [" + oValue + "] == [" + filter[1] + "]");
                        if (!oValue.equalsIgnoreCase(filter[1])) {
                            filterPassed = false;
                            break;
                        }
                    }

                }
            }
            logger.trace("filterPassed:" + filterPassed);
            if(filterPassed) {
                multiValue += EngineUtil.getMultiFieldValue(o, params[0], field.getSource().getSeparator(), isLabel, false) + EngineConstants.SEMICOLON_SEPARATOR_REGEX;
                if (isFirstMatch && multiValue != null && !multiValue.isEmpty())
                    break;
            }

        }
        if (!multiValue.isEmpty()) {
            multiValue = multiValue.substring(0, multiValue.length() - 1);
        } else {
            multiValue = EngineConstants.EMPTY_VALUE;
        }
        return multiValue;
    }




}
