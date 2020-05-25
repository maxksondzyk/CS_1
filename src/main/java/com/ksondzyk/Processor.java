package com.ksondzyk;

import com.ksondzyk.entities.Message;
import com.ksondzyk.entities.Packet;

import com.ksondzyk.network.Network;



public class Processor {
    public static void process(Network network, Packet packet) {
        String message = packet.getMessage();

        Message answerMessage;
        if (message.equals("time")) {
            answerMessage = new Message(1, 1, "now()", false);
        } else {
            answerMessage = new Message(1, 1, "other", false);
        }
        Packet answerPacket = new Packet((byte) 1, answerMessage);
/*
        try {
            network.send(answerPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }
}
