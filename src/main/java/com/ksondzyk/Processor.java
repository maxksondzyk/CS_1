package com.ksondzyk;

import com.google.common.primitives.UnsignedLong;
import com.ksondzyk.entities.Message;
import com.ksondzyk.entities.Packet;
import com.ksondzyk.exceptions.PacketDamagedException;
import com.ksondzyk.storage.ProductsStorage;

import java.io.IOException;
import java.io.OutputStream;


public class Processor {

    private static final ProductsStorage storage = new ProductsStorage();

    public static void process(Packet packet, OutputStream os) throws PacketDamagedException {
        synchronized (storage) {
            Message answerMessage;

            UnsignedLong bPktId= packet.getBPktId();

            int cType = packet.getBMsq().getCType();

            switch (cType) {
                case 1:
                    answerMessage = new Message(0, 1, "the amount of products",false);
                    break;
                case 2:
                    answerMessage = new Message(0, 1, "some product has been deleted",false);
                    break;
                case 3:
                    answerMessage = new Message(0, 1, "some product has been added:",false);
                    break;
                case 4:
                    answerMessage = new Message(0, 1, "a group of product has been added",false);
                    break;
                case 5:
                    answerMessage = new Message(0, 1, "a product's name has been added to a group",false);
                    break;
                case 6:
                    answerMessage = new Message(0, 1, "the price has been set",false);
                    break;
                default:
                    throw new PacketDamagedException("Unknown command");
            }
            Packet answerPacket = new Packet((byte) 1,bPktId, answerMessage);

            try {
                PacketSender sender = new PacketSender();
                sender.send(answerPacket, os,0);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
