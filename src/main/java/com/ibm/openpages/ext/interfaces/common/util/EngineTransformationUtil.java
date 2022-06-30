package com.ibm.openpages.ext.interfaces.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.openpages.ext.interfaces.common.bean.EngineMetadataBean;
import com.ibm.openpages.ext.interfaces.common.constant.EngineConstants;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 * The <code>EngineTransformationUtil</code> contains the methods used to transform json data to java beans.
 * </p>
 * @author Marco Castillo <BR>
 * email : marco.castillo@ibm.com <BR>
 * company : IBM Consulting Services Mexico
 * @version 1.0
 * @custom.date : 11-11-2021
 */
public class EngineTransformationUtil {

    /**
     * <p>
     * Converts a json data string to {@link EngineMetadataBean} using an {@link ObjectMapper}. If an error ocurrs during
     * the convertion, it's not been converted and returns a null
     * </p>
     * @param jsonData
     *              {@link String} to be converted.
     * @return an instance of {@link EngineMetadataBean}.
     */
    public static EngineMetadataBean jsonToMetaEngineBean(String jsonData, Logger logger) {
        if (jsonData != null) {
            ObjectMapper mapper = new ObjectMapper();
            EngineMetadataBean engineBean = null;
            try {
                engineBean = mapper.readValue(jsonData, EngineMetadataBean.class);
            } catch (Exception e) {
                logger.error("EngineMetadataBean", e);
            }
            logger.trace("->Class: BeanUtil --> Method: Jsonto  --> Bean: " + engineBean.toString());
            return engineBean;
        } else {
            return null;
        }
    }

    public static String mapOfMapsToXML(Map<String,Object> mapToTransform, boolean isRoot){

        StringBuilder xml = new StringBuilder();
        if(isRoot)
            xml.append(EngineConstants.XML_VERSION_1_0_ENCODING_UTF_8_STANDALONE_YES);
        Iterator<Map.Entry<String, Object>> it = mapToTransform.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry i = it.next();
            if(i.getValue() instanceof String){
                xml.append("<"+i.getKey()+">" + i.getValue() + "</"+i.getKey()+">");
            } else if(i.getValue() instanceof Map){
                xml.append("<"+i.getKey()+">" + mapOfMapsToXML((Map<String, Object>) i.getValue(), false) + "</"+i.getKey()+">");
            } else if(i.getValue() instanceof List){
                for(Object j : (List)i.getValue()){
                    xml.append("<"+i.getKey()+">" + mapOfMapsToXML((Map<String, Object>) j, false) + "</"+i.getKey()+">");
                }
            }
        }

        return xml.toString();
    }

    public static String getStatusFromStatusMap(String key, Map<String, String> statusMap, Logger logger){
        String status;
        logger.debug("Key : " + key);
        if(statusMap.get(key) != null) status = statusMap.get(key);
        else{
            if(key.matches("^.*,.*$")) status = statusMap.get(key.substring(0,key.indexOf(",")+1)+"*");
            else status = "";
        }
        return status;
    }

    public static String getDifferentTimeZoneDate(Date originalDate){
        System.out.println("Date INPUT: " + originalDate.toString());
        String sDate = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(EngineConstants.DEFAULT_EXTERNAL_DATE_PATTERN);
            TimeZone tz = TimeZone.getTimeZone("America/New_York");
            sdf.setTimeZone(tz);
            sDate = sdf.format(originalDate);
            Date date = sdf.parse(sDate);
            System.out.println("String Date Format OUTPUT:  -- > " + sDate);
            System.out.println("Date Format OUTPUT:  -- > " + date.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sDate;
    }

    public static String getDifferentTimeZoneDate(Date date, String timeZone){
        return date.toInstant().atZone(ZoneId.of(timeZone)).format(DateTimeFormatter.ofPattern(EngineConstants.DEFAULT_EXTERNAL_DATE_PATTERN));
    }

    public static String getOtherTimeZoneTime( Date date, String timezone){
        System.out.println("Date INPUT: " + date.toString());
        String otherTime = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(EngineConstants.DEFAULT_EXTERNAL_DATE_PATTERN);
            //Date date = formatter.parse(dateInString);
            SimpleDateFormat sdfAmerica = new SimpleDateFormat(EngineConstants.DEFAULT_EXTERNAL_DATE_PATTERN);
            TimeZone tzInAmerica = TimeZone.getTimeZone(timezone);
            sdfAmerica.setTimeZone(tzInAmerica);
            String sDateInAmerica = sdfAmerica.format(date); // Convert to String first
            Date dateInAmerica = formatter.parse(sDateInAmerica); // Create a new Date object
            System.out.println("Date Format STRING OUTPUT: " + sDateInAmerica);
            System.out.println("Date Format OUTPUT: " + dateInAmerica.toString());
            otherTime = formatter.format(dateInAmerica);
        } catch (ParseException pe){
            pe.printStackTrace();
        }
        return otherTime;
    }
}
