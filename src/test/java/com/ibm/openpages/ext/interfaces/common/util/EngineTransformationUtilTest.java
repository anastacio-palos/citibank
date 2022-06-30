package com.ibm.openpages.ext.interfaces.common.util;

import com.ibm.openpages.ext.interfaces.common.constant.EngineConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class EngineTransformationUtilTest {
    private static Logger logger = null;

    @BeforeClass
    public static void setLogger() throws MalformedURLException
    {
        logger = LogManager.getLogger();
    }

    @Test
    public void mapOfMapsToXML(){
        logger.debug("Start Testing");
        Map<String, Object> mapToTest =  new HashMap<>();
        mapToTest.put("AIMSDetails",new HashMap<>());
        Map<String, Object> aimsDetailsMap = (Map<String, Object>) mapToTest.get("AIMSDetails");
        aimsDetailsMap.put("RETIssueValidations",new HashMap<>());
        Map<String, Object> retIssueValidationsMap = (Map<String, Object>) aimsDetailsMap.get("RETIssueValidations");
        retIssueValidationsMap.put("IAEntityID","50213");
        retIssueValidationsMap.put("IAEntityName","E00000244");
        aimsDetailsMap.put("CommonValidations",new HashMap<>());
        Map<String, Object> commonValidationsMap = (Map<String, Object>) aimsDetailsMap.get("CommonValidations");
        commonValidationsMap.put("RequestID","1648491231073");
        commonValidationsMap.put("IssueID","673936");
        aimsDetailsMap.put("CapForm",new ArrayList<Map>());
        List<Map<String, Object>> capList = (List<Map<String, Object>>) aimsDetailsMap.get("CapForm");
        Map<String, Object> cap1 = new HashMap<>();
        cap1.put("iCAPSCapID","727014");
        cap1.put("ValidationInIA","Returned to Owner");
        cap1.put("ReasonFailValidation","adsfdf");
        cap1.put("NarrativeComments","asdfdfd");
        capList.add(cap1);
        Map<String, Object> cap2 = new HashMap<>();
        cap1.put("iCAPSCapID","728354");
        cap1.put("ValidationInIA","Pass Validation");
        capList.add(cap1);
        logger.debug("mapToTest:"+mapToTest);
        String xml = EngineTransformationUtil.mapOfMapsToXML(mapToTest, true);
        logger.debug("xml:"+xml);


    }



    @Test
    public void mapTest(){
        String mapjson = "{\n" +
                "\"Risk Mitigated\":\"Pass Validation\",\n" +
                "\"Risk Mitigated,*\":\"Pass Validation\",\n" +
                "\"Open\":\"Returned to Owner\",\n" +
                "\"Open,*\":\"Failed Validation\",\n" +
                "\"Cancelled\":\"Cancelled\",\n" +
                "\"Cancelled,*\":\"Cancelled\"\n" +
                "}";
        ObjectMapper om = new ObjectMapper();
        Map<String, String> statusMap;
        try {
            statusMap = om.readValue(mapjson,Map.class);
            /*logger.debug("Open -> " + EngineTransformationUtil.getStatusFromStatusMap("Open",statusMap));
            logger.debug("Open, -> " + EngineTransformationUtil.getStatusFromStatusMap("Open,",statusMap));
            logger.debug("Open,0 -> " + EngineTransformationUtil.getStatusFromStatusMap("Open,0",statusMap));
            logger.debug("Open,1 -> " + EngineTransformationUtil.getStatusFromStatusMap("Open,1",statusMap));
            logger.debug("Open,2 -> " + EngineTransformationUtil.getStatusFromStatusMap("Open,2",statusMap));
            logger.debug("Open,30 -> " + EngineTransformationUtil.getStatusFromStatusMap("Open,30",statusMap));
            logger.debug("Open1 -> " + EngineTransformationUtil.getStatusFromStatusMap("Open1",statusMap));*/
        } catch (IOException e) {
            logger.debug(e);
        }
    }

    @Test
    public void getStatusFromStatusMap(){
        logger.debug("adjusted date:"+EngineTransformationUtil.getDifferentTimeZoneDate(Calendar.getInstance().getTime()));
        logger.debug("adjusted date 2:"+EngineTransformationUtil.getDifferentTimeZoneDate(Calendar.getInstance().getTime(), "America/New_York"));
    }

    @Test
    public void getOtherTimeZoneTimeTest() throws ParseException {
        String dateInString = "01/08/2022 10:00:00 AM EST";
        SimpleDateFormat sdf = new SimpleDateFormat(EngineConstants.DEFAULT_EXTERNAL_DATE_PATTERN);
        logger.debug("Original Date : " + dateInString + "-> Parsed Date : " + sdf.format(sdf.parse(dateInString)));
        dateInString = "02/08/2022 10:00:00 AM EST";
        logger.debug("Original Date : " + dateInString + "-> Parsed Date : " + sdf.format(sdf.parse(dateInString)));
        dateInString = "03/08/2022 10:00:00 AM EST";
        logger.debug("Original Date : " + dateInString + "-> Parsed Date : " + sdf.format(sdf.parse(dateInString)));
        dateInString = "04/08/2022 10:00:00 AM EST";
        logger.debug("Original Date : " + dateInString + "-> Parsed Date : " + sdf.format(sdf.parse(dateInString)));
        dateInString = "04/08/2022 10:00:00 AM EDT";
        logger.debug("Original Date : " + dateInString + "-> Parsed Date : " + sdf.format(sdf.parse(dateInString)));
        //logger.debug("Date (New York) (Object) : " + EngineTransformationUtil.getOtherTimeZoneTime(dateInString, "America/Los_Angeles"));

        //logger.debug("New Date : " + EngineTransformationUtil.getOtherTimeZoneTime(Calendar.getInstance().getTime(),"America/Los_Angeles"));
        //logger.debug("New Date : " + EngineTransformationUtil.getOtherTimeZoneTime(Calendar.getInstance().getTime(),"America/Mexico_City"));
        //logger.debug("New Date : " + EngineTransformationUtil.getOtherTimeZoneTime(Calendar.getInstance().getTime(),"EST"));
        //logger.debug("New Date : " + EngineTransformationUtil.getOtherTimeZoneTime(Calendar.getInstance().getTime(),"EST5EDT"));
        //Arrays.stream(TimeZone.getAvailableIDs()).iterator().forEachRemaining(i->logger.debug(i));

        //logger.debug(TimeZone.getDefault().useDaylightTime());
        //logger.debug(TimeZone.getDefault().inDaylightTime( new Date() ));
        
    }


    public String getWrongAnswers(int N, String C) {
        // Write your code here
        int t = 0;
        String answer = "";
        for(int i=0; i < C.length(); i++){
            if(C.charAt(i) == 'A' ) t = 1 + N;
            else if(C.charAt(i) == 'B') t = 2 + N;
            logger.debug("t="+t);
            if(t%2==1) answer += 'A';
            else answer += 'B';

        }


        return "";
    }
}
