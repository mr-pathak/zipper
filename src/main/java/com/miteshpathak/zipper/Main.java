package com.miteshpathak.zipper;

import org.apache.commons.lang.time.StopWatch;
import org.pmw.tinylog.Logger;

import com.miteshpathak.zipper.core.CompressFactory;
import com.miteshpathak.zipper.core.ZipperSetting;
import com.miteshpathak.zipper.util.ArgsValidator;

/**
 * Entry point to application
 *
 * @author Mitesh Pathak <miteshpathak05@gmail.com>
 */
public class Main {
    public static void main(String[] args) {
        StopWatch timer = new StopWatch();
        timer.start();
        try {
            ArgsValidator.checkCompressorArg(args);
            ZipperSetting setting = new ZipperSetting.Builder().build(args);
            CompressFactory.getExecutor(setting).execute();
        } catch (Throwable ex) {
            Logger.error(ex);
            Logger.info("Failed to complete task. Please refer logs for more information.");
            Logger.info(CompressFactory.getHelpMessage());
        } finally {
            timer.stop();
            Logger.info("Total execution time = {} ms", timer.getTime());
        }
    }
}
