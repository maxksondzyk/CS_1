package com.ksondzyk;

import com.ksondzyk.entities.Message;
import com.ksondzyk.entities.Packet;

import java.util.Random;

public class PacketGenerator {

    public static Packet newPacket(){
        Random rand = new Random();

        byte[] array = new byte[7];

        rand.nextBytes(array);

        return new Packet((byte)rand.nextInt(255),new Message(rand.nextInt(6),rand.nextInt(),new String(array),false));
    }
}
