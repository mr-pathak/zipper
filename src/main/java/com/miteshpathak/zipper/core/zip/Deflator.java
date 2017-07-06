package com.miteshpathak.zipper.core.zip;

import java.io.File;
import java.util.Collection;
import java.util.stream.Collectors;

import com.miteshpathak.zipper.core.Executor;
import com.miteshpathak.zipper.core.ZipperSetting;
import com.miteshpathak.zipper.util.FileUtil;

/**
 * Executes compress tasks
 *
 * @author Mitesh Pathak <miteshpathak05@gmail.com>
 */
public class Deflator implements Executor {
    private final SplitFileHandler zip;
    private final File inputDir;
    private final File outputDir;
    
    public Deflator(ZipperSetting settings, SplitFileHandler zip) {
        this.zip = zip;
        this.inputDir = new File(settings.getInputDirPath());
        this.outputDir = new File(settings.getOutputDirPath());
    }
    
    @Override
    public void execute() {
        // map each file as a DeflateTask
        Collection<ExecutorTask> tasks = FileUtil.listFiles(this.inputDir)
                .stream()
                .map(f -> new DeflateTask(f))
                .collect(Collectors.toList());
        executeTasks(tasks);
    }
    
    private class DeflateTask implements ExecutorTask {
        private final File inputFile;
        private boolean isSuccess = false;
        
        public DeflateTask(File f) {
            this.inputFile = f;
        }
        
        @Override
        public DeflateTask call() {
            String relativePath = inputFile.getAbsolutePath()
                    .substring(inputDir.getAbsolutePath().length());
            // only line thats is important for single threaded application
            zip.deflate(inputFile, new File(outputDir, relativePath));
            isSuccess = true;
            return this;
        }
        
        @Override
        public boolean isSuccess() {
            return isSuccess;
        }

        @Override
        public String toString() {
            return "[Compress] Deflate: " + inputFile.getAbsolutePath();
        }
    }
}
