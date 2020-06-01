package com.ksondzyk.network;

import com.google.common.primitives.UnsignedLong;
import com.ksondzyk.Server;
import com.ksondzyk.entities.Message;
import com.ksondzyk.entities.Packet;
import com.ksondzyk.exceptions.PacketDamagedException;
import com.ksondzyk.utilities.PacketReceiver;
import com.ksondzyk.utilities.Processor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class UDPServerThread implements Runnable {
    private UnsignedLong counter;
    public static DatagramSocket serverSocket;
    public static BlockingQueue<DatagramPacket> queue;
    InetAddress IPAddress;
    int port;

    public UDPServerThread() {
        counter = UnsignedLong.ZERO;
        try {
            serverSocket = new DatagramSocket(Server.PORT);
            queue = new ArrayBlockingQueue<>(20);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void run(){

        try {
            byte[] receivedData = new byte[Packet.packetMaxSize];
                while(true){
                    DatagramPacket receivedPacket = new DatagramPacket(receivedData, receivedData.length);
                    serverSocket.receive(receivedPacket);
                    IPAddress = receivedPacket.getAddress();
                    port = receivedPacket.getPort();

                    ByteBuffer byteBuffer = ByteBuffer.wrap(receivedData);

                    Integer wLen = byteBuffer.getInt(Packet.packetPartFirstLengthWithoutwLen);

                    byte[] fullPacket = new byte[Packet.packetPartFirstLength + Message.BYTES_WITHOUT_MESSAGE + wLen];
                    byteBuffer.get(fullPacket);
                    System.out.println("Received");
                    System.out.println(Arrays.toString(fullPacket) + "\n");

                    Packet packetReceived = new Packet(fullPacket,"udp");

                    packetReceived.setClientInetAddress(receivedPacket.getAddress());
                    packetReceived.setClientPort(receivedPacket.getPort());
                    counter = counter.plus(UnsignedLong.ONE);
                    queue.put(receivedPacket);

//                    PacketReceiver pr = new PacketReceiver();
//
//                    Packet packet = pr.receive(is);
//
//                    System.out.println("Server received packet " + Thread.currentThread().getName());
//
//                    if (packet.getMessage().equals("END"))
//                        break;
//
//                    Processor.process(packet, os);
                }
        } catch (IOException e) {
            System.err.println("Поток завершив роботу");
        } catch (PacketDamagedException e) {

            Packet packet = new Packet((byte)1,new Message(-1,-1,"send again",false));
            // String capitalizedSentence = sentence.toUpperCase();
            // sendData = capitalizedSentence.getBytes();
            DatagramPacket sendPacket =
                    new DatagramPacket(packet.getData(), packet.getData().length, IPAddress, port);
            try{
                serverSocket.send(sendPacket);
            }catch (Exception e1){
                e1.printStackTrace();
            }



        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

}