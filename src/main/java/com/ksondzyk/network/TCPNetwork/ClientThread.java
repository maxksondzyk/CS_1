package com.ksondzyk.network.TCPNetwork;

import com.ksondzyk.PacketGenerator;
import com.ksondzyk.PacketReceiver;
import com.ksondzyk.Processor;
import com.ksondzyk.entities.Packet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

public class ClientThread extends Thread {

    private InputStream is;
    private OutputStream os;

    public ClientThread(Socket socket){

        try {
            is = socket.getInputStream();
            os = socket.getOutputStream();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){
            try {
                synchronized (this) {
                    PacketGenerator pg = new PacketGenerator();
                    Packet packet = pg.newPacket();
                    byte[] packetBytes = packet.getData();

                    os.write(packetBytes);
                    os.flush();

                    System.out.println("Send" + currentThread().getName());
                    System.out.println(Arrays.toString(packetBytes) + "\n");

                    PacketReceiver pr = new PacketReceiver();
                    Packet packetReceived = pr.receive(is);
                    System.out.println("Received" + currentThread().getName());
                    System.out.println(Arrays.toString(packetBytes) + "\n");

                    Processor.process(packet, os);
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
       // }
    }

}
