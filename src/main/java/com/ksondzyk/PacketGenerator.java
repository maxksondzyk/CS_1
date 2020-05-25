package com.ksondzyk;

import java.util.Random;

public class PacketGenerator {

    Packet newPacket(){
        Random rand;
        byte[] array;
        array = new byte[7];
        rand = new Random();
        rand.nextBytes(array);
        Packet packet = new Packet((byte)rand.nextInt(255),new Message(rand.nextInt(),rand.nextInt(),new String(array),false));
        return packet;
    }
}
