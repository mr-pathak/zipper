package com.miteshpathak.zipper.core;

/**
 * Interface which can be used to implement compress and decompress logic
 *
 * @author Mitesh Pathak <miteshpathak05@gmail.com>
 */
public interface Zipper {
    /**
     * Uncompress bytes from input[0...len-1] into output<br>
     * The uncompressed bytes are written in output[] buffer<br>
     * Return the number of bytes written in output
     * 
     * @param input compressed data
     * @param output uncompressed data
     * @param len number of bytes from input to read; input [0...len];
     * 
     * @return  number of bytes written to output
     */
    public int inflate(byte[] input, byte[] output, int len);
    
    /**
     * Compress bytes from input[0...len-1] into output<br>
     * The compressed bytes are written in output[] buffer<br>
     * Return the number of bytes written in output
     * 
     * The expected length of compressed data (output) 
     * should be less than or at worst equal to input
     * 
     * @param input data to compress
     * @param output compressed data
     * @param len number of bytes from input to read; input [0...len];
     * 
     * @return  number of bytes written to output
     */
    public int deflate(byte[] input, byte[] output, int len);
}
