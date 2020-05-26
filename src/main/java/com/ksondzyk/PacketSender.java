package com.ksondzyk;

import com.ksondzyk.entities.Packet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import static java.lang.Thread.currentThread;

public class PacketSender {

    public void send(Packet packet, OutputStream os) throws IOException {
        byte[] packetBytes = packet.getData();

        os.write(packetBytes);
        os.flush();

        System.out.println("Send " + currentThread().getName());
    }
}
