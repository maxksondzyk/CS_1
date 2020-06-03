package com.ksondzyk.network;

import com.ksondzyk.Server;
import com.ksondzyk.entities.Packet;
import com.ksondzyk.utilities.CipherMy;
import com.ksondzyk.utilities.PacketReceiver;
import com.ksondzyk.utilities.Processor;

import java.io.IOException;
import java.net.DatagramSocket;

public class UDPServerThread implements Runnable {
    //  private UnsignedLong counter;
    private static DatagramSocket serverSocket;


    public UDPServerThread() {


        try {
            serverSocket = new DatagramSocket(Server.PORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void run() {

        try {
            while (true) {

                Packet packetReceived = PacketReceiver.receiveUDP(serverSocket);


                if(packetReceived!=null) {
                    System.out.println(CipherMy.decode(packetReceived.getBMsq().getMessage()));
                    System.out.println(Processor.processUDP(packetReceived));
                }
            }
        } catch (IOException e) {
            System.err.println("Поток завершив роботу");
        }


    }
}