package com.miteshpathak.zipper.core;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.pmw.tinylog.Logger;

import com.miteshpathak.zipper.util.Constants;
/**
 * Executor which is an interface to the following tasks<br>
 * a) Deflator - to compress files<br>
 * b) Inflator - to compress files<br>
 *
 * @author Mitesh Pathak <miteshpathak05@gmail.com>
 */
public interface Executor {
    public void execute();
    
    default public void executeTasks(Collection<ExecutorTask> tasks) {
        try {
            ExecutorService executorService = Executors.newScheduledThreadPool(Constants.THREAD_POOL);
            executorService.invokeAll(tasks);
            executorService.shutdown();
        } catch (InterruptedException ex) {
            Logger.error("Interrupted during executeTasks operation");
            throw new RuntimeException("Failed to execute the command", ex);
        }
        printTasksStatus(tasks);
    }

    default public void printTasksStatus(Collection<ExecutorTask> tasks) {
        int totalTasks = tasks.size();
        int failedTasks = 0;
        int succeededTasks = 0;
        
        StringBuffer print = new StringBuffer();
        String line = Constants.LINE_SEP;

        print.append("Logging all tasks information").append(line);        

        for (ExecutorTask task : tasks) {
            if (task.isSuccess()) {                
                succeededTasks++;
                print.append("[SUCCESS] ").append(task).append(line);
            } else {                
                failedTasks++;
                print.append("[FAILURE] ").append(task).append(line);
            }
        }        

        print.append(line);
        print.append("[Total files] = ").append(totalTasks).append(line);
        print.append("[Number of files successfully operated] = ").append(succeededTasks).append(line);
        print.append("[Number of files failed] = ").append(failedTasks).append(line);
        
        Logger.info(print);
    }

    public interface ExecutorTask extends Callable<ExecutorTask> {
        boolean isSuccess();
    }
}
