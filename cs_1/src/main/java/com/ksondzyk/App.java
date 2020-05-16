package com.ksondzyk;

import java.io.IOException;

public class App {
    public static void main(String[] args) {
        try {
            Message testMessage = new Message(100, 2456, "Hello");
            Packet packet = new Packet((byte) 1, testMessage);
            PacketReceiver packetReceiver = new PacketReceiver(packet.getData());
            System.out.println("Message: "+packetReceiver.getMessage()+"\nPacket is intact: "+packetReceiver.checkCRC());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
