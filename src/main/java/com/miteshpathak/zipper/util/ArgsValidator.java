package com.miteshpathak.zipper.util;

import java.io.File;

import com.miteshpathak.zipper.core.Command;

/**
 * Argument validator helper
 *
 * @author Mitesh Pathak <miteshpathak05@gmail.com>
 */
public class ArgsValidator {
    public static void checkCompressorArg(String[] args) {
        if (args == null)
            ArgsValidator.throwExeception("Invalid argument (args)");
        
        Command enteredCommand = Command.getCommand(args[0]);
        switch (enteredCommand) {
            case INFLATE:
                ArgsValidator.checkArgLength(args, 3);
                ArgsValidator.checkFile(args[1], true, "Invalid input directory path");
                ArgsValidator.checkFile(args[2], false, "Invalid input directory path");
                break;
            case DEFLATE:
                ArgsValidator.checkArgLength(args, 4);
                ArgsValidator.checkFile(args[1], true, "Invalid input directory path");
                ArgsValidator.checkFile(args[2], false, "Invalid input directory path");
                ArgsValidator.checkPositiveInteger(args[3],
                        "File max size is invalid, it can be an integer between 1 - "
                                + Constants.MAX_SPLIT_SIZE_IN_MB);
                break;
            case HELP:
                break;
            case NONE:
                ArgsValidator.throwExeception("Invalid command");
        }
    }
    
    public static void throwExeception(String msg) {
        throw new RuntimeException(msg);
    }
    
    public static void checkArgLength(String[] args, int len) {
        if (args == null || args.length != len)
            ArgsValidator.throwExeception("Invalid command arguments");
    }
    
    private static void checkFile(String filePath, boolean isRead, String msg) {
        if (filePath == null)
            ArgsValidator.throwExeception(msg);
        
        File file = new File(filePath);
        boolean isWrite = !isRead;
        
        if (!file.exists() || !file.isDirectory())
            ArgsValidator.throwExeception(msg);
        
        if (isRead && !file.canRead())
            ArgsValidator.throwExeception(msg);
        
        if (isWrite && !file.canWrite())
            ArgsValidator.throwExeception(msg);
    }
    
    private static void checkPositiveInteger(String arg, String msg) {
        int num = 0;
        try {
            num = Integer.parseInt(arg);
        }
        catch (NumberFormatException ex) {
            // empty catch block
        }
        if (num < 1)
            ArgsValidator.throwExeception(msg);
    }
    
    public static void checkNull(Object args) {
        if (args == null)
            ArgsValidator.throwExeception("Argument is null");
    }    
}
