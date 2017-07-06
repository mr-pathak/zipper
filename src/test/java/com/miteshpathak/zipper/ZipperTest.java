package com.miteshpathak.zipper;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import org.pmw.tinylog.Logger;

import com.miteshpathak.zipper.core.CompressFactory;
import com.miteshpathak.zipper.core.Zipper;
import com.miteshpathak.zipper.util.FileUtil;

/**
 * Test Zipper implementations
 *
 * @author Mitesh Pathak <miteshpathak05@gmail.com>
 */
@RunWith(value=Parameterized.class)
public class ZipperTest {
    private final Zipper zip;
    
    public ZipperTest(Zipper zip) {
        this.zip = zip;
    }
    
    @Test
    public void testZipperOperation() {
        String data = FileUtil.readAsString(FileUtil.getFile("testpath/input/textData.txt"));
        
        byte[] inputExpected = data.getBytes();
        byte[] output = new byte[inputExpected.length * 2];
        int bytesWrote = this.zip.deflate(inputExpected, output, inputExpected.length);
        
        byte[] inputObtained = new byte[inputExpected.length];
        int bytesRead = this.zip.inflate(output, inputObtained, bytesWrote);
        String obtainedData = new String(inputObtained, 0, bytesRead);
        
        Assert.assertTrue((bytesWrote <= inputExpected.length));
        Assert.assertEquals(inputExpected.length, bytesRead);
        Assert.assertArrayEquals(inputExpected, inputObtained);
        Assert.assertEquals(data, obtainedData);
        
        Logger.info(zip + " >> bytesWrote = " + bytesWrote + " -vs- initialSize" + inputExpected.length);
    }
    
    @Parameterized.Parameters
    public static Collection<Zipper> implementationToTest() {
        return CompressFactory.getZipperImplementations();
    }
}
