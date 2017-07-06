package com.miteshpathak.zipper.core.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import com.miteshpathak.zipper.core.Zipper;
import com.miteshpathak.zipper.util.Constants;
import com.miteshpathak.zipper.util.FileUtil;
import org.pmw.tinylog.Logger;

/**
 * Handles splitting of files with inflation/deflation at File level
 *
 * @author Mitesh Pathak <miteshpathak05@gmail.com>
 */
public class SplitFileHandler {
    private final Zipper logic;
    private final double maxSplitSize;
    
    public SplitFileHandler(Zipper logic, double maxSplitSize) {
        this.logic = logic;
        this.maxSplitSize = maxSplitSize;
    }
    
    /**
     * A files can be divided into multiple archived files.<br>
     * This function returns the first file's name list.
     * And avoids listing all part files in the same directory.
     *
     * @param dir
     * @return
     */
    public Collection<String> getInflatedFileList(File dir) {
        return FileUtil.listRelativeFilePaths(dir)
                .stream()
                .filter(fname ->
                        fname.substring(fname.lastIndexOf(".")).equals(Constants.FIRST_PART_SUFFIX))
                .collect(Collectors.toSet());
    }
    
    public void inflate(String zipperFile0RelPath, File inputDir, File outputDir) {
        int indexOfPart = zipperFile0RelPath.lastIndexOf(Constants.FIRST_PART_SUFFIX);
        String filenameWithoutPart = zipperFile0RelPath.substring(0, indexOfPart);
        
        File deflatedFile0 = new File(inputDir, zipperFile0RelPath);
        File outputFile = new File(outputDir, filenameWithoutPart);
        FileUtil.createParentDir(outputFile);
        
        Collection<File> splitFiles = listAllPartFiles(deflatedFile0);
        inflateAndMergeFiles(splitFiles, outputFile);
    }
    
    public void deflate(File inputFile, File outputFile) {
        deflateAndSplitFile(inputFile, outputFile);
    }
    
    private void deflateAndSplitFile(File inputFile, File outputFile) {
        byte[] inputBuffer = FileUtil.getSmallByteBuffer();
        byte[] outputBuffer = FileUtil.getLargeByteBuffer();
        
        String filePrefix = outputFile.getName();
        String parentDir = outputFile.getParent();
        
        int partCounter = 1;
        double maxSizePerFile = this.maxSplitSize - Constants.FIXED_BUFFER_SZ_IN_MB;
        
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inputFile));) {
            int bytesRead = -1;
            while ((bytesRead = checkOrReadNewBytes(bis, inputBuffer, bytesRead)) > 0) {
                File newFile = getNewSplitFile(parentDir, filePrefix, partCounter++);
                FileUtil.createParentDir(newFile);
                boolean hasWriteSpace = true;
                
                try (FileOutputStream out = new FileOutputStream(newFile);){
                    while (hasWriteSpace
                            && (bytesRead = checkOrReadNewBytes(bis, inputBuffer, bytesRead)) > 0) {
                        
                        int bytesToWrite = logic.deflate(inputBuffer, outputBuffer, bytesRead);
                        String header = String.format(Constants.HEADER_FORMAT, bytesToWrite);
                        
                        out.write(header.getBytes(StandardCharsets.UTF_8));
                        out.write(outputBuffer, 0, bytesToWrite);
                        
                        bytesRead = -1;
                        hasWriteSpace = !FileUtil.checkExceedsMaxSize(newFile, maxSizePerFile);
                    }
                } catch (IOException ex) {
                    Logger.error(ex, "Failed to split file: {}", newFile.getAbsolutePath());
                    throw new RuntimeException("Failed to split files", ex);
                }
            }
        } catch (IOException ex) {
            Logger.error(ex, "Failed to read inputFile: {}", inputFile.getAbsolutePath());
            throw new RuntimeException("Failed to read input file", ex);
        }
    }
    
    private void inflateAndMergeFiles(Collection<File> srcFiles, File dest) {
        try (BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(dest));) {
            srcFiles.forEach(f -> {
                inflateAndAppend(f, os);
            });
        }
        catch (IOException ex) {
            Logger.error(ex, "Failed to inflate file: {}", dest.getAbsoluteFile());
            throw new RuntimeException("Failed to inflate file to dest", ex);
        }
    }
    
    private void inflateAndAppend(File f, BufferedOutputStream os) {
        byte[] inputBuffer = FileUtil.getLargeByteBuffer();
        byte[] outputBuffer = FileUtil.getLargeByteBuffer();
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));) {
            int bytesToRead;
            while ((bytesToRead = extractHeaderAndRead(bis, inputBuffer)) > 0) {
                int bytesToWrite = logic.inflate(inputBuffer, outputBuffer, bytesToRead);
                os.write(outputBuffer, 0, bytesToWrite);
            }
        }
        catch (Exception ex) {
            Logger.error(ex, "Failed during appending file: {}", f);
            throw new RuntimeException("Failed to inflate and merge inputfile", ex);
        }
    }
    
    private static int checkOrReadNewBytes(InputStream is, byte[] buffer, int bytesRead)
            throws IOException {
        if (is == null)
            return -1;
        
        if (bytesRead > 0)
            return bytesRead;
        
        return is.read(buffer);
    }
    
    private static int extractHeaderAndRead(BufferedInputStream is, byte[] buffer) throws IOException {
        int bytesToRead = -1;
        int headerToRead = is.read(buffer, 0, Constants.HEADER_SIZE);
        if (headerToRead > 0) {
            String header = new String(buffer, 0, headerToRead, StandardCharsets.UTF_8);
            bytesToRead = Integer.parseInt(header.substring(Constants.HEADER_PREFIX.length()));
            bytesToRead = is.read(buffer, 0, bytesToRead);
        }
        return bytesToRead;
    }
    
    private static File getNewSplitFile(String parentPath, String prefix, int count) {
        String fileName = String.format(Constants.FILE_PART_FORMAT, prefix, count);
        return new File(parentPath, fileName);
    }
    
    private static Collection<File> listAllPartFiles(File inputFile) {
        String fileNameWithPart = inputFile.getName();
        int indexOfPartSuffix = fileNameWithPart.lastIndexOf(Constants.FIRST_PART_SUFFIX);
        String fileNameWithoutPart = fileNameWithPart.substring(0, indexOfPartSuffix);
        String regexForPartFiles = fileNameWithoutPart + Constants.ZIP_SUFFIX_REGEX;
        File[] fileParts = inputFile.getParentFile()
                .listFiles((dir, name) -> name.matches(regexForPartFiles));
        Arrays.sort(fileParts);
        return Arrays.asList(fileParts);
    }
}
