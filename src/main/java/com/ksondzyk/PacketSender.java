package com.ksondzyk;

import com.ksondzyk.entities.Packet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import static java.lang.Thread.currentThread;

public class PacketSender {

    public void send(Packet packet, OutputStream os,int i) throws IOException {

            byte[] packetBytes;
            if(i==2)
                packetBytes = Arrays.copyOfRange(packet.getData(),0,packet.getData().length/2);
            else
                packetBytes = packet.getData();

            os.write(packetBytes);
            os.flush();

            System.out.println("Send " + currentThread().getName());
    }
}
