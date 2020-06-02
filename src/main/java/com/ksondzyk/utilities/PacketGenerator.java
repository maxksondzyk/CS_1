package com.ksondzyk.utilities;

import com.google.common.primitives.UnsignedLong;
import com.ksondzyk.entities.Message;
import com.ksondzyk.entities.Packet;

import java.util.Random;

public class PacketGenerator {

    public static Packet errorPacket(){
        return new Packet((byte)1,new Message(-1,-1,"send again",false));
    }
    public static Packet allOkPacket(){
        return new Packet((byte)1,new Message(1,1,"all ok",false));
            }

    private UnsignedLong bPktID = UnsignedLong.ZERO;

    public Packet newPacket(int clientID, String message){
        bPktID = bPktID.plus(UnsignedLong.ONE);
        Random rand = new Random();


        return  new Packet((byte)rand.nextInt(255),bPktID,
                new Message(rand.nextInt(5)+1, clientID, message,false));

    }
    public Packet newPacket(int i){
        bPktID = bPktID.plus(UnsignedLong.ONE);
        Random rand = new Random();
        String message ="random message";

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
