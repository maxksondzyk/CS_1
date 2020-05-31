package com.ksondzyk.utilities;

import com.ksondzyk.entities.Message;
import com.ksondzyk.entities.Packet;

import java.util.Random;

public class PacketGenerator {


    public static Packet newPacket(int clientID, String message){
        Random rand;
        rand = new Random();


        return  new Packet((byte)rand.nextInt(255),
                new Message(rand.nextInt(5)+1, clientID, message,false));

    }
    public static Packet newPacket(int i){
        Random rand;
        String message ="random message";
        rand = new Random();
        if (i == 1) {
            return new Packet((byte) rand.nextInt(255),
                    new Message(rand.nextInt(5) + 1,
                            19,
                            message, false));
        }
        return new Packet((byte) rand.nextInt(255),
                new Message(rand.nextInt(5) + 1,
                        rand.nextInt(),
                        message, false));


    }
}