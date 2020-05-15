package com.ksondzyk;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) {
        try {
            Packet packet = new Packet((byte) 1,100,2456,"hello");
            PacketReceiver packetReceiver = new PacketReceiver(packet.getData());
            System.out.println("Message: "+packetReceiver.getMessage()+"\nPacket is intact: "+packetReceiver.checkCRC());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
