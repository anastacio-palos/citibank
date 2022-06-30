package com.ibm.openpages.ext.util;

import org.testng.annotations.Test;

import java.io.File;

import static org.testng.Assert.*;

public class ZipUtilTest {

    @Test
    public void testZipFile() throws Exception{

        File result = ZipUtil.zipFile(new File("/Users/ashish.tiwari/Documents/Ashish/01Documents/Citi/Workspace/Citibank/OPWebApps/src/main/resources/dsmtdb.properties"));
        assert result != null;

        assert result.exists() == true;

        result.delete();

    }
}