package com.ibm.openpages.ext.util;

import org.apache.commons.io.FileUtils;
import com.ibm.openpages.ext.model.ReportRecord;

import java.io.File;

public class CSVWriter {

    private String fileName;

    private File reportFile;

    public CSVWriter(String fileName){
        this.fileName = fileName;
        this.reportFile = new File(fileName);

    }

    public void writeHeader(String header)throws Exception{

        FileUtils.write(reportFile, header+System.lineSeparator());
    }

    public void writeRecord(ReportRecord reportRecord)throws Exception{


        FileUtils.write(reportFile, reportRecord.toReportRecord(), true);

    }

}
