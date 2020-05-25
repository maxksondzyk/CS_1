package com.ksondzyk;

import com.ksondzyk.entities.Message;
import com.ksondzyk.entities.Packet;

import com.ksondzyk.network.Network;

import java.io.IOException;
import java.io.OutputStream;


public class Processor {
    public static void process(Packet packet, OutputStream os) {
        String message = packet.getMessage();
//get cType
        //switch (cType)

        Message answerMessage;
        if (message.equals("time")) {
            answerMessage = new Message(1, 1, "now()", false);
        } else {
            answerMessage = new Message(1, 1, "other", false);
        }
        Packet answerPacket = new Packet((byte) 1, answerMessage);

        try {
            PacketSender sender  = new PacketSender();
            sender.send(answerPacket, os);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
