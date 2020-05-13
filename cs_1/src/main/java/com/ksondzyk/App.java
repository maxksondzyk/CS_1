package com.ksondzyk;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;

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
        Packet packet = null;
        try {
            packet = new Packet((byte) 1,0100,2456,"hello");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(packet.getBMsq().getMessage());
        PacketReceiver packetReceiver = new PacketReceiver(packet.getData());
    }
    public static String sha256hex(String input) {
        return DigestUtils.sha256Hex(input);
    }
}
