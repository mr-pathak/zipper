package com.miteshpathak.zipper.core.zip;

import java.io.File;
import java.util.Collection;
import java.util.stream.Collectors;

import com.miteshpathak.zipper.core.Executor;
import com.miteshpathak.zipper.core.ZipperSetting;

/**
 * Executes uncompress operation
 *
 * @author Mitesh Pathak <miteshpathak05@gmail.com>
 */
public class Inflator implements Executor {
    private final SplitFileHandler zip;
    private final File inputDir;
    private final File outputDir;
    
    public Inflator(ZipperSetting settings, SplitFileHandler zip) {
        this.zip = zip;
        this.inputDir = new File(settings.getInputDirPath());
        this.outputDir = new File(settings.getOutputDirPath());
    }
    
    @Override
    public void execute() {
        // map each file as a InflateTask
        Collection<ExecutorTask> tasks = zip.getInflatedFileList(inputDir)
                .stream()
                .map(fp -> new InflateTask(fp))
                .collect(Collectors.toList());
        executeTasks(tasks);
    }
    
    private class InflateTask implements ExecutorTask {
        private final String fname;
        private final File inputFile;
        private boolean success = false;
        
        public InflateTask(String fname) {
            this.fname = fname;
            this.inputFile = new File(inputDir, fname);
        }
        
        @Override
        public InflateTask call() {
            zip.inflate(fname, inputDir, outputDir);
            success = true;
            return this;
        }
        
        @Override
        public boolean isSuccess() {
            return success;
        }
        
        @Override
        public String toString() {
            return "[Unzip] Inflate: " + inputFile.getAbsolutePath();
        }
    }
}
