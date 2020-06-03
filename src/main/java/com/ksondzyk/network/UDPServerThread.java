package com.ksondzyk.network;

import com.google.common.primitives.UnsignedLong;
import com.ksondzyk.Server;
import com.ksondzyk.entities.Message;
import com.ksondzyk.entities.Packet;
import com.ksondzyk.exceptions.PacketDamagedException;
import com.ksondzyk.utilities.CipherMy;
import com.ksondzyk.utilities.PacketGenerator;
import com.ksondzyk.utilities.PacketReceiver;
import com.ksondzyk.utilities.Processor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

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