package com.ksondzyk;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) {

//        if (args.length < 1) {
//            System.err.println("Please provide an input!");
//            System.exit(0);
//        }
        //System.out.println(sha256hex(args[0]));
Packet packet = new Packet((byte) 1,0100,2456,"hello");
        //System.out.println(packet.getBMsq().getMessage());
    }

    public static String sha256hex(String input) {
        return DigestUtils.sha256Hex(input);
    }
}
