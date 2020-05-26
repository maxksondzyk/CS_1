package com.ksondzyk;

import com.ksondzyk.entities.Message;
import com.ksondzyk.entities.Packet;

public class App {
    public static void main(String[] args) {
        try {

            Message testMessage = new Message(100, 2456, "Hello");
            Packet packet = new Packet((byte) 1, testMessage);
            Packet packetReceiver = new Packet(packet.getData());
            System.out.println("Message: "+packetReceiver.getMessage()+"\nPacket is intact: "+packetReceiver.checkCRC());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
