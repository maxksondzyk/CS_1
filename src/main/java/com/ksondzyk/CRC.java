package com.ksondzyk;

// Copyright 2016, S&K Software Development Ltd.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

// Package crc implements generic CRC calculations up to 64 bits wide.
// It aims to be fairly complete, allowing users to match pretty much
// any CRC algorithm used in the wild by choosing appropriate Parameters.
// And it's also fairly fast for everyday use.
//
// This package has been largely inspired by Ross Williams' 1993 paper "A Painless Guide to CRC Error Detection Algorithms".
// A good list of parameter sets for various CRC algorithms can be found at http://reveng.sourceforge.net/crc-catalogue/.

/**
 This class provides utility functions for CRC calculation using either canonical straight forward approach
 or using "fast" table-driven implementation. Note, that even though table-driven implementation is much faster
 for processing large amounts of data and is commonly referred as fast algorithm, sometimes it might be quicker to
 calculate CRC using canonical algorithm then to prepare the table for table-driven implementation.

 <p>
 Using src is easy. Here is an example of calculating CCITT crc in one call using canonical approach.
 <pre>
 {@code
 String data = "123456789";
 long ccittCrc = CRC.calculateCRC(CRC.Parameters.CCITT, data.getBytes());
 System.out.printf("CRC is 0x%04X\n", ccittCrc); // prints "CRC is 0x29B1"
 }
 </pre>
 <p>
 For larger data, table driven implementation is faster. Here is how to use it.
 <pre>
 {@code
 String data = "123456789";
 CRC tableDriven = new CRC(CRC.Parameters.XMODEM);
 long xmodemCrc = tableDriven.calculateCRC(data.getBytes());
 System.out.printf("CRC is 0x%04X\n", xmodemCrc); // prints "CRC is 0x31C3"
 }
 </pre>
 <p>
 You can also reuse CRC object instance for another crc calculation.
 <p>
 Given that the only state for a CRC calculation is the "intermediate value"
 and it is stored in your code, you can even use same CRC instance to calculate CRC
 of multiple data sets in parallel.
 And if data is too big, you may feed it in chunks
 <pre>
 {@code
 long curValue = tableDriven.init(); // initialize intermediate value
 curValue = tableDriven.update(curValue, "123456789".getBytes()); // feed first chunk
 curValue = tableDriven.update(curValue, "01234567890".getBytes()); // feed next chunk
 long xmodemCrc2 = tableDriven.finalCRC(curValue); // gets CRC of whole data ("12345678901234567890")
 System.out.printf("CRC is 0x%04X\n", xmodemCrc2); // prints "CRC is 0x2C89"
 }
 </pre>
 * */
public class CRC
{
    /**
     *  Parameters represents set of parameters defining a particular CRC algorithm.
     * */
    public static class Parameters
    {
        private final int width;   // Width of the CRC expressed in bits
        private final long polynomial; // Polynomial used in this CRC calculation
        private final boolean reflectIn;   // Refin indicates whether input bytes should be reflected
        private final boolean reflectOut;   // Refout indicates whether output bytes should be reflected
        private final long init; // Init is initial value for CRC calculation
        private final long finalXor; // Xor is a value for final xor to be applied before returning result

        public Parameters(int width, long polynomial, long init, boolean reflectIn, boolean reflectOut, long finalXor)
        {
            this.width = width;
            this.polynomial = polynomial;
            this.reflectIn = reflectIn;
            this.reflectOut = reflectOut;
            this.init = init;
            this.finalXor = finalXor;
        }

        public static final Parameters CRC16 = new Parameters(16, 0x8005, 0x0000, true, true, 0x0);

    }

    /**
     * Reverses order of last count bits.
     * @param  in value wich bits need to be reversed
     * @param count indicates how many bits be rearranged
     * @return      the value with specified bits order reversed
     */
    private static long reflect(long in, int count)
    {
        long ret = in;
        for (int idx = 0; idx < count; idx++)
        {
            long srcbit = 1L << idx;
            long dstbit = 1L << (count - idx - 1);
            if ((in & srcbit) != 0)
            {
                ret |= dstbit;
            }
            else
            {
                ret = ret & (~dstbit);
            }
        }
        return ret;
    }

    /**
     * This method implements simple straight forward bit by bit calculation.
     * It is relatively slow for large amounts of data, but does not require
     * any preparation steps. As a result, it might be faster in some cases
     * then building a table required for faster calculation.
     *
     * Note: this implementation follows section 8 ("A Straightforward CRC Implementation")
     * of Ross N. Williams paper as even though final/sample implementation of this algorithm
     * provided near the end of that paper (and followed by most other implementations)
     * is a bit faster, it does not work for polynomials shorter then 8 bits.
     *
     * @param  crcParams CRC algorithm parameters
     * @param  data data for the CRC calculation
     * @return      the CRC value of the data provided
     */
    public static long calculateCRC(Parameters crcParams, byte[] data)
    {
        long curValue =crcParams.init;
        long topBit = 1L << (crcParams.width - 1);
        long mask = (topBit << 1) - 1;

        for (int i=0; i< data.length; i++)
        {
            long curByte = ((long)(data[i])) & 0x00FFL;
            if (crcParams.reflectIn)
            {
                curByte = reflect(curByte, 8);
            }

            for (int j = 0x80; j != 0; j >>= 1)
            {
                long bit = curValue & topBit;
                curValue <<= 1;

                if ((curByte & j) != 0)
                {
                    bit ^= topBit;
                }

                if (bit != 0)
                {
                    curValue ^= crcParams.polynomial;
                }
            }

        }

        if (crcParams.reflectOut)
        {
            curValue = reflect(curValue, crcParams.width);
        }

        curValue = curValue ^ crcParams.finalXor;

        return curValue & mask;
    }



}

