/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  com.miteshpathak.zipper.util.ArgsValidator
 *  org.junit.Before
 *  org.junit.Rule
 *  org.junit.Test
 *  org.junit.rules.ExpectedException
 *  org.junit.rules.TemporaryFolder
 */
package com.miteshpathak.zipper.util;

import com.miteshpathak.zipper.util.ArgsValidator;
import java.io.File;
import java.io.IOException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

public class ArgsValidatorTest {
    public static String OP_DIR = "output";
    public static String INP_DIR = "input";
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    public String outputDir;
    public String inputDir;

    @Before
    public void setup() throws IOException {
        this.outputDir = this.folder.newFolder(OP_DIR).getAbsolutePath();
        this.inputDir = this.folder.newFolder(INP_DIR).getAbsolutePath();
    }

    @Test
    public void testCheckCompressorArgValid() {
        String[] validArgsCompress = new String[]{"-c", this.inputDir, this.outputDir, "05"};
        String[] validArgsDecompress = new String[]{"-d", this.inputDir, this.outputDir};
        ArgsValidator.checkCompressorArg((String[])validArgsCompress);
        ArgsValidator.checkCompressorArg((String[])validArgsDecompress);
    }

    @Test
    public void testCheckCompressorArgInvalid() {
        this.expectedEx.expect(RuntimeException.class);
        this.expectedEx.expectMessage("Invalid argument (args)");
        String[] invalidNullArgs = null;
        ArgsValidator.checkCompressorArg(invalidNullArgs);
        this.expectedEx.expectMessage("Invalid command");
        String[] invalidArgsCommand = new String[]{"invalidcommand", this.inputDir, this.outputDir};
        ArgsValidator.checkCompressorArg((String[])invalidArgsCommand);
        this.expectedEx.expectMessage("Invalid input directory path");
        String[] invalidInputPath = new String[]{"-d", "nonexistence", this.outputDir};
        ArgsValidator.checkCompressorArg((String[])invalidInputPath);
        this.expectedEx.expectMessage("Invalid input directory path");
        String[] invalidOutputPath = new String[]{"-d", this.inputDir, "nonexistence"};
        ArgsValidator.checkCompressorArg((String[])invalidOutputPath);
        this.expectedEx.expectMessage("File split size is invalid, use -h for help");
        String[] invalidSplitSize = new String[]{"-c", this.inputDir, this.outputDir, "44444a"};
        ArgsValidator.checkCompressorArg((String[])invalidSplitSize);
    }
}
