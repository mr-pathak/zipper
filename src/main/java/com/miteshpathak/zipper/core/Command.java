package com.miteshpathak.zipper.core;

/**
 * Represents possible command the program can execute
 *
 * @author Mitesh Pathak <miteshpathak05@gmail.com>
 */
public enum Command {
    INFLATE("-d"),
    DEFLATE("-c"),
    HELP("-h"),
    NONE("");
    
    private final String option;
    
    private Command(String option) {
        this.option = option;
    }
    
    public String getOption() {
        return this.option;
    }
    
    public static Command getCommand(String option) {
        if (INFLATE.getOption().equals(option))
            return INFLATE;
        if (DEFLATE.getOption().equals(option))
            return DEFLATE;
        if (HELP.getOption().equals(option))
            return HELP;
        return NONE;
    }
}
