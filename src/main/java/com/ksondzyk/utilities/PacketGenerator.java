package com.ksondzyk.utilities;

import com.google.common.primitives.UnsignedLong;
import com.ksondzyk.entities.Message;
import com.ksondzyk.entities.Packet;

import java.util.Random;

public class PacketGenerator {
private UnsignedLong bPktID = UnsignedLong.ZERO;

    public Packet newPacket(int clientID, String message){
        bPktID = bPktID.plus(UnsignedLong.ONE);
        Random rand;
        rand = new Random();


        return  new Packet((byte)rand.nextInt(255),bPktID,
                new Message(rand.nextInt(5)+1, clientID, message,false));

    }
    public Packet newPacket(int i){
        bPktID = bPktID.plus(UnsignedLong.ONE);
        Random rand;
        String message ="random message";
        rand = new Random();
        if (i == 1) {
            return new Packet((byte) rand.nextInt(255),bPktID,
                    new Message(rand.nextInt(5) + 1,
                            19,
                            message, false));
        }
        return new Packet((byte) rand.nextInt(255),bPktID,
                new Message(rand.nextInt(5) + 1,
                        rand.nextInt(),
                        message, false));


    }
}
