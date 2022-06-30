package com.ibm.openpages.ext.util;

import org.testng.annotations.Test;

import static org.testng.Assert.*;
import com.ibm.openpages.ext.model.DSMTReportRecord;

public class CSVWriterTest {

    @Test
    public void testWriteRecord() throws Exception{

        CSVWriter underTest = new CSVWriter("/Users/ashish.tiwari/Documents/Ashish/01Documents/Citi/Workspace/Citibank/OPWebApps/src/main/resources/test.txt");
        underTest.writeHeader("huha");
        underTest.writeRecord(new DSMTReportRecord());
    }
}