package com.miteshpathak.zipper.util;

import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.pmw.tinylog.Logger;

/**
 * Constants value holder.<br>
 * All constants related to application are defined here.
 *
 * @author Mitesh Pathak <miteshpathak05@gmail.com>
 */
public final class Constants {
    public static final String DEFLATE_OPTION = "-c";
    public static final String INFLATE_OPTION = "-d";
    public static final String EMPTY_STRING = "";
    public static final String LINE_SEP = System.lineSeparator();
    public static final int    THREAD_POOL_DEFAULT = 1;
    public static final int    THREAD_POOL = getThreadPool();
    
    public static final String FILE_PART_FORMAT = "%s.mzp-%05d";
    public static final String FIRST_PART_SUFFIX = ".mzp-00001";
    public static final String ZIP_SUFFIX_REGEX = "[.]mzp-\\d+";
    public static final double MAX_SPLIT_SIZE_IN_MB = 1024.0;
    
    public static final long   ONE_MB = FileUtils.ONE_MB;
    public static final int    FIXED_BUFFER_SZ_IN_BYTES = 16384;
    public static final double FIXED_BUFFER_SZ_IN_MB = 0.015625;
    
    public static final String HEADER_PREFIX = "len=";
    public static final String HEADER_FORMAT = "len=%04d";
    public static final String HEADER_SAMPLE = "len=1024";
    public static final int    HEADER_SIZE = "len=1024".getBytes(StandardCharsets.UTF_8).length;
    
    private static int getThreadPool() {
        int threadPoolSize = THREAD_POOL_DEFAULT;
        try {
            threadPoolSize = Integer.parseInt(System.getProperty("zthreads"));
        } catch (NumberFormatException ex) {
            Logger.warn("Invalid thread pool argument -Dzthreads, defaulting to {}", THREAD_POOL_DEFAULT);
            Logger.debug(ex, "Exception while reading zthreads property");
        }
        Logger.info("[Thread Pool Size] = {}", threadPoolSize);
        return threadPoolSize;
    }
}
