package com.miteshpathak.zipper.core.zip;

import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import com.miteshpathak.zipper.core.Zipper;

/**
 * Implementation using java.zip library
 *
 * @author Mitesh Pathak <miteshpathak05@gmail.com>
 */
public class JavaZipper implements Zipper {
    @Override
    public int inflate(byte[] input, byte[] output, int len) {
        Inflater unzip = new Inflater();
        unzip.setInput(input, 0, len);
        int offset = 0;
        while (!unzip.finished()) {
            try {
                offset = unzip.inflate(output, offset, output.length);
            }
            catch (DataFormatException ex) {
                throw new RuntimeException("Failed to uncompress bytes", ex);
            }
        }
        return offset;
    }

    @Override
    public int deflate(byte[] input, byte[] output, int len) {
        Deflater zipCompressor = new Deflater();
        zipCompressor.setLevel(9);
        zipCompressor.setInput(input, 0, len);
        zipCompressor.finish();
        int offset = 0;
        while (!zipCompressor.finished()) {
            offset = zipCompressor.deflate(output, offset, output.length);
        }
        return offset;
    }
}
