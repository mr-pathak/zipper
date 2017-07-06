package com.miteshpathak.zipper.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.pmw.tinylog.Logger;

/**
 * File helper
 *
 * @author Mitesh Pathak <miteshpathak05@gmail.com>
 */
public final class FileUtil {
    public static boolean compareDir(File dir1, File dir2) {
        Collection<String> dir1Files = listRelativeFilePaths(dir1);
        Collection<String> dir2Files = listRelativeFilePaths(dir2);

        if (!CollectionUtils.isEqualCollection(dir1Files, dir2Files))
            return false;

        for (String fileName : dir1Files) {
            File fileInDir1 = new File(dir1, fileName);
            File fileInDir2 = new File(dir2, fileName);
            try {
                if (!FileUtils.contentEquals(fileInDir1, fileInDir2))
                    return false;
            }
            catch (IOException ex) {
                Logger.debug(ex, "Failed while comparing directory");
                return false;
            }
        }
        return true;
    }

    public static Collection<String> listRelativeFilePaths(File dir) {
        String dirPath = dir.getAbsolutePath();
        return listFiles(dir).stream()
                .map(f -> f.getAbsolutePath().substring(dirPath.length()))
                .collect(Collectors.toSet());
    }

    public static Collection<String> listAbsoluteFilePaths(File dir) {
        return listFiles(dir).stream()
                .map(f -> f.getAbsolutePath())
                .collect(Collectors.toSet());
    }

    public static void create(File inp) {
        try {
            FileUtils.touch(inp);
        }
        catch (IOException ex) {
            throw new RuntimeException("Failed to create file", ex);
        }
    }

    public static Collection<File> listFiles(File dir) {
        return FileUtils.listFiles(dir, null, true);
    }

    public static File getFile(String name) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(name);
        return new File(url.getPath());
    }

    public static double fileSizeInMB(File f) {
        return 1.0 * (double)f.length() / Constants.ONE_MB;
    }

    public static byte[] getSmallByteBuffer() {
        return new byte[Constants.FIXED_BUFFER_SZ_IN_BYTES / 2];
    }

    public static byte[] getLargeByteBuffer() {
        return new byte[Constants.FIXED_BUFFER_SZ_IN_BYTES];
    }

    /**
     * 
     * @param f
     * @param maxSizeInMb
     * @return true if file size exceeds maxSize
     */
    public static boolean checkExceedsMaxSize(File f, double maxSizeInMb) {
        if (f == null || !f.isFile())
            return true;
        return Double.compare(FileUtil.fileSizeInMB(f), maxSizeInMb) > 0;
    }

    public static void createParentDir(File file) {
        file.getParentFile().mkdirs();
    }


    public static void writeStringToFile(File file, String data) {
        try {
            FileUtils.writeStringToFile(file, data);
        }
        catch (IOException ex) {
            throw new RuntimeException("Failed to write data in file", ex);
        }
    }

    public static String readAsString(File file) {
        try {
            return FileUtils.readFileToString(file);
        }
        catch (IOException ex) {
            throw new RuntimeException("Failed to read file as string", ex);
        }
    }

    public static void copyFile(File src, File dest) {
        try {
            FileUtils.copyFile(src, dest);
        }
        catch (IOException ex) {
            throw new RuntimeException("Failed to copy files", ex);
        }
    }
}
