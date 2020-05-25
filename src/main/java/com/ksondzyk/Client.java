package com.ksondzyk;

import com.ksondzyk.entities.Message;
import com.ksondzyk.entities.Packet;

import com.ksondzyk.network.Network;
import com.ksondzyk.network.TCPNetwork;


public class Client {
    public static void main(String[] args) {
        Message testMessage = new Message(1, 1, "time", false);
        Packet packet = new Packet((byte) 1,  testMessage);

        Message secondTestMessage = new Message(1, 1, "notTime", false);
        Packet secondPacket = new Packet((byte) 1, secondTestMessage);

        try {
            Network network = new TCPNetwork();
            network.connect();

            network.send(packet);
            network.receive();

            network.send(secondPacket);
            network.receive();

            network.close();
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }
}

