package com.ksondzyk;

import com.ksondzyk.entities.Message;
import com.ksondzyk.entities.Packet;

import java.util.Random;

public class PacketGenerator {

    public Packet newPacket(){
        Random rand;
        byte[] array;
        array = new byte[7];
        rand = new Random();
        rand.nextBytes(array);
        Packet packet = new Packet((byte)rand.nextInt(255),new Message(rand.nextInt(6),rand.nextInt(),new String(array),false));
        return packet;
    }
}
