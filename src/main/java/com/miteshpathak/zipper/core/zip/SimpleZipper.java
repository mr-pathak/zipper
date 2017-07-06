package com.miteshpathak.zipper.core.zip;

import com.miteshpathak.zipper.core.Zipper;

/**
 * Copy compressor. Simply copies data from one buffer to another.<br>
 * Sample process for both inflation and deflation.<br>
 * 
 * Note:- Main purpose for testing
 *
 * @author Mitesh Pathak <miteshpathak05@gmail.com>
 */
public class SimpleZipper implements Zipper {
    @Override
    public int inflate(byte[] input, byte[] output, int len) {
        return copyArray(input, output, len);
    }

    @Override
    public int deflate(byte[] input, byte[] output, int len) {
        return copyArray(input, output, len);
    }
    
    private static int copyArray(byte[] input, byte[] output, int len) {
        System.arraycopy(input, 0, output, 0, len);
        return len;

    }
}
