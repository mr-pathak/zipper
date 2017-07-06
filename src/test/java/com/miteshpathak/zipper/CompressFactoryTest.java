package com.miteshpathak.zipper;

import org.junit.Test;

import com.miteshpathak.zipper.core.CompressFactory;
import com.miteshpathak.zipper.core.Executor;
import com.miteshpathak.zipper.core.ZipperSetting;
import com.miteshpathak.zipper.core.zip.Inflator;
import com.miteshpathak.zipper.core.zip.Deflator;

import static org.junit.Assert.*;

/**
 * @author Mitesh Pathak <miteshpathak05@gmail.com>
 */
public class CompressFactoryTest {

    @Test
    public void testGetExecutorValid() {
        String compressArgs[] = {"-c", "ip", "op", "1"};
        String uncompressArgs[] = {"-d", "inp", "op"};
        String helpArgs[] = {"-h"};
        Executor executorDeflate = CompressFactory.getExecutor(
                new ZipperSetting.Builder().build(compressArgs));
        Executor executorInflate = CompressFactory.getExecutor(
                new ZipperSetting.Builder().build(uncompressArgs));

        Executor executorHelp = CompressFactory.getExecutor(
                new ZipperSetting.Builder().build(helpArgs));
        executorHelp.execute();

        assertEquals(Deflator.class, executorDeflate.getClass());
        assertEquals(Inflator.class, executorInflate.getClass());
    }

    @Test(expected = RuntimeException.class)
    public void testGetExecutorInValid() {
        String invalidArgs[] = {"-invalid", "ip", null, "-1"};
        CompressFactory.getExecutor(new ZipperSetting.Builder().build(invalidArgs));
    }
}
