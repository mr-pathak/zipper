package com.miteshpathak.zipper.core;

import com.miteshpathak.zipper.util.ArgsValidator;
import com.miteshpathak.zipper.util.Constants;

/**
 * Setting holding command and required parameters. <br>
 * Use ZipperSetting.Builder to build settings object
 *
 * @author Mitesh Pathak <miteshpathak05@gmail.com>
 */
public class ZipperSetting {
    private int numOfArgs = -1;
    private String command;
    private String inputDirPath;
    private String outputDirPath;
    private double maxFileSize = -1.0;
    
    private ZipperSetting() {
    }
    
    public int getNumOfArgs() {
        return this.numOfArgs;
    }
    
    public String getCommand() {
        return this.command;
    }
    
    public String getInputDirPath() {
        return this.inputDirPath;
    }
    
    public String getOutputDirPath() {
        return this.outputDirPath;
    }
    
    public double getMaxFileSize() {
        return this.maxFileSize;
    }
    /**
     * Builder for ZipperSetting
     */
    public static class Builder {
        ZipperSetting setting = new ZipperSetting();
        
        public Builder command(String command) {
            setting.command = command;
            return this;
        }
        
        public Builder inputDir(String path) {
            setting.inputDirPath = path;
            return this;
        }
        
        public Builder outputDir(String path) {
            setting.outputDirPath = path;
            return this;
        }
        
        public Builder maxSize(String size) {
            setting.maxFileSize = Double.parseDouble(size);
            if (this.setting.maxFileSize > Constants.MAX_SPLIT_SIZE_IN_MB)
                ArgsValidator.throwExeception("File max size is invalid,"
                        + " it can be an integer between 1 - " + Constants.MAX_SPLIT_SIZE_IN_MB);
            return this;
        }
        
        public ZipperSetting build() {
            setting.numOfArgs = 0;

            if (setting.command != null)
                setting.numOfArgs++;

            if (setting.inputDirPath != null)
                setting.numOfArgs++;

            if (this.setting.outputDirPath != null)
                this.setting.numOfArgs++;

            if (this.setting.maxFileSize > 0)
                this.setting.numOfArgs++;

            return this.setting;
        }
        
        public ZipperSetting build(String[] args) {
            ArgsValidator.checkNull(args);
            switch (Command.getCommand(args[0])) {
                case INFLATE:
                    ArgsValidator.checkArgLength(args, 3);
                    break;
                case DEFLATE:
                    ArgsValidator.checkArgLength(args, 4);
                    maxSize(args[3]);
                    break;
                case HELP:
                    ArgsValidator.checkArgLength(args, 1);
                    return command(args[0]).build();
                default:
                    ArgsValidator.throwExeception("Invalid command");
            }
            command(args[0]);
            inputDir(args[1]);
            outputDir(args[2]);

            return build();
        }
    }
    
}