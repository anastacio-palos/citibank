package com.ibm.openpages.ext.util;

import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.api.service.IResourceService;
import org.apache.commons.logging.Log;


public class ParentFinderUtil {
    Log log;

    private IResourceService resourceService;

    public ParentFinderUtil(Log log, IResourceService resourceService){
        this.log = log;
        this.resourceService = resourceService;

    }

    public IGRCObject getParent(IGRCObject childObject, String type){

        log.info(String.format("Finding_parent for child=%s, child_type=%s parent_type=%s", childObject.getName(),childObject.getType().getName(), type));

        if(childObject.getPrimaryParent() == null){
            return null;
        }

        IGRCObject primaryParent = resourceService.getGRCObject(childObject.getPrimaryParent());

        if(primaryParent == null || type.equalsIgnoreCase(primaryParent.getType().getName())){

            log.info(String.format("Returning primaryParent=%s", primaryParent==null?primaryParent:primaryParent.getName()+":"+ primaryParent.getType()));

            return primaryParent;

        }else{

            return getParent(primaryParent, type);
        }

    }
}
