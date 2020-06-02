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
    private UnsignedLong counter;
    private static DatagramSocket serverSocket;
  //  private static BlockingQueue<DatagramPacket> queue;
  //  InetAddress IPAddress;
   // int port;

    public UDPServerThread() {
        counter = UnsignedLong.ZERO;
        try {
            serverSocket = new DatagramSocket(Server.PORT);
        //    queue = new ArrayBlockingQueue<>(20);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    DatagramPacket receivedPacket;
    byte[] receivedData;

    public void run(){

        try {
         //    receivedData = new byte[Packet.packetMaxSize];
                while(true){
                  //  receivedPacket = new DatagramPacket(receivedData, receivedData.length);
                 //   serverSocket.receive(receivedPacket);
                  //  IPAddress = receivedPacket.getAddress();
                   // port = receivedPacket.getPort();

                    Packet packetReceived = PacketReceiver.receiveUDP(serverSocket);
                    //packetReceived.setClientInetAddress(receivedPacket.getAddress());
                    //packetReceived.setClientPort(receivedPacket.getPort());

                    System.out.println(CipherMy.decode(packetReceived.getBMsq().getMessage()));
                    System.out.println(Processor.processUDP(packetReceived));
                    counter = counter.plus(UnsignedLong.ONE);
                  //  queue.put(receivedPacket);


                  //  send();
                }
        } catch (IOException e) {
            System.err.println("Поток завершив роботу");
        }


    }
/*
    private void send() {
        Packet packet = PacketGenerator.allOkPacket();
        DatagramPacket sendPacket = new DatagramPacket(packet.getData(), packet.getData().length, IPAddress, port);
        try{
            serverSocket.send(sendPacket);
        }catch (Exception e1){
            e1.printStackTrace();
        }
    }

    private void receive() throws PacketDamagedException, InterruptedException {
        ByteBuffer byteBuffer = ByteBuffer.wrap(receivedData);

        Integer wLen = byteBuffer.getInt(Packet.packetPartFirstLengthWithoutwLen);

        byte[] fullPacket = new byte[Packet.packetPartFirstLength + Message.BYTES_WITHOUT_MESSAGE + wLen];
        byteBuffer.get(fullPacket);

        System.out.println("Received");
        System.out.println(Arrays.toString(fullPacket) + "\n");

        Packet packetReceived = new Packet(fullPacket);
        packetReceived.setClientInetAddress(receivedPacket.getAddress());
        packetReceived.setClientPort(receivedPacket.getPort());

        System.out.println(CipherMy.decode(packetReceived.getBMsq().getMessage()));
        System.out.println(Processor.processUDP(packetReceived));
        counter = counter.plus(UnsignedLong.ONE);
        queue.put(receivedPacket);
    }
*/
}