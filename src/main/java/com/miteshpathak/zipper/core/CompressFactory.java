package com.miteshpathak.zipper.core;

import java.util.Arrays;
import java.util.List;

import org.pmw.tinylog.Logger;

import com.miteshpathak.zipper.core.zip.Deflator;
import com.miteshpathak.zipper.core.zip.Inflator;
import com.miteshpathak.zipper.core.zip.JavaZipper;
import com.miteshpathak.zipper.core.zip.SimpleZipper;
import com.miteshpathak.zipper.core.zip.SplitFileHandler;
import com.miteshpathak.zipper.util.Constants;

/**
 * Factory class for getting following implementation<br>
 * a) Executor<br>
 * b) Zipper<br>
 * c) FileHander<br>
 *
 * @author Mitesh Pathak <miteshpathak05@gmail.com>
 */
public class CompressFactory {
    public static Executor getExecutor(ZipperSetting settings) {
        if (Command.getCommand(settings.getCommand()) == Command.DEFLATE)
            return new Deflator(settings, getFileHandler(settings.getMaxFileSize()));

        if (Command.getCommand(settings.getCommand()) == Command.INFLATE)
            return new Inflator(settings, getFileHandler(settings.getMaxFileSize()));

        if (Command.getCommand(settings.getCommand()) == Command.HELP)
            return  () ->  {
                Logger.info(getHelpMessage());
            };

        throw new RuntimeException("Invalid command, use -h for help");
    }
    
    public static List<Zipper> getZipperImplementations() {
        return Arrays.asList(new JavaZipper(), new SimpleZipper());
    }
    
    private static SplitFileHandler getFileHandler(double maxFileSize) {
        return new SplitFileHandler(getZipperImplementations().get(0), maxFileSize);
    }

    public static String getHelpMessage() {
        StringBuilder sb = new StringBuilder();
        String line = Constants.LINE_SEP;
        
        sb.append(line).append(line);
        sb.append("Usage: java -jar zipper-*.jar [command] [args]").append(line).append(line);

        sb.append("where, [command] can be \"-c\" for compress, \"-d\" for uncompress, \"-h\" for help");
        sb.append(line).append(line);

        sb.append("[args] \"-c\" needs 3 arguments (inputDir path, outputDir path, maxOutputFIleSize - in MB)").append(line);
        sb.append("[args] \"-d\" needs 2 arguments (inputDir, outputDir)").append(line);
        sb.append("[args] \"-h\" needs no arguments").append(line);
        sb.append(line);
        sb.append("NOTE: To run in multithread mode, pass parameter -Dzthreads=n where n is the num of threads");
        sb.append(line);
        sb.append("ex: java -Dzthreads=10 -jar zipper-*.jar -c /path/inpDir /path/outputDir 1");
        sb.append(line);
        
        return sb.toString();
    }
}
