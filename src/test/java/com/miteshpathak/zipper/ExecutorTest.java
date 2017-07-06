package com.miteshpathak.zipper;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.miteshpathak.zipper.core.CompressFactory;
import com.miteshpathak.zipper.core.Executor;
import com.miteshpathak.zipper.core.ZipperSetting;
import com.miteshpathak.zipper.util.ArgsValidatorTest;
import com.miteshpathak.zipper.util.FileUtil;

/**
 *
 * @author Mitesh Pathak <miteshpathak05@gmail.com>
 */
public class ExecutorTest {
    @Rule
    public TemporaryFolder root = new TemporaryFolder();
    private File outputDir;
    private File inputDir;
    
    @Before
    public void setup() throws IOException {
        this.outputDir = this.root.newFolder(ArgsValidatorTest.OP_DIR);
    }
    
    @Test
    public void testExecuteSuccessSingleFile() throws IOException {
        this.inputDir = generateSimpleInputFolder();
        ZipperSetting setting = new ZipperSetting.Builder()
                .command("-c")
                .inputDir(inputDir.getAbsolutePath())
                .outputDir(outputDir.getAbsolutePath())
                .maxSize("200")
                .build();
        Executor inflator = CompressFactory.getExecutor(setting);
        inflator.execute();
        testDeflatedOperation();
    }
    
    @Test
    public void testSplitSingleFile() throws IOException {
        this.inputDir = generateLargeInputFolder();
        double maxSplitSizeInMB = 1.0;
        int numOfFiles = (int)(Math.ceil(5.6) / maxSplitSizeInMB);
        ZipperSetting setting = new ZipperSetting.Builder()
                .command("-c")
                .inputDir(inputDir.getAbsolutePath())
                .outputDir(outputDir.getAbsolutePath())
                .maxSize(String.valueOf(maxSplitSizeInMB))
                .build();
        
        Executor inflator = CompressFactory.getExecutor(setting);
        inflator.execute();
        
        Collection<File> files = FileUtil.listFiles(outputDir);
        Assert.assertEquals(numOfFiles, files.size());
        files.forEach(f -> Assert.assertTrue(isLessThanEqual(FileUtil.fileSizeInMB(f),
                maxSplitSizeInMB)));
        
        testDeflatedOperation();
    }
    
    private boolean isLessThanEqual(double num1, double num2) {
        return Double.compare(num1, num2) <= 0;
    }
    
    private File generateSimpleInputFolder() throws IOException {
        File inp = root.newFolder(ArgsValidatorTest.INP_DIR);
        File simpleFile = new File(inp, "helloworld.txt");
        FileUtil.create(inp);
        FileUtil.writeStringToFile(simpleFile, "Hello World");
        return inp;
    }
    
    private File generateLargeInputFolder() throws IOException {
        File inpDir = root.newFolder(ArgsValidatorTest.INP_DIR);
        File largeImageFile = new File(inpDir, "test5MbFile.jpg");
        File testFile = FileUtil.getFile("testpath/input/test5MbFile.jpg");
        FileUtil.copyFile(testFile, largeImageFile);
        return inpDir;
    }
    
    private void testDeflatedOperation() throws IOException {
        File tempDir = this.root.newFolder("tmp-test");
        ZipperSetting setting = new ZipperSetting.Builder()
                .command("-d")
                .inputDir(this.outputDir.getAbsolutePath())
                .outputDir(tempDir.getAbsolutePath())
                .build();
        Executor deflator = CompressFactory.getExecutor((ZipperSetting)setting);
        deflator.execute();
        Assert.assertTrue(FileUtil.compareDir(inputDir, tempDir));
    }
}
