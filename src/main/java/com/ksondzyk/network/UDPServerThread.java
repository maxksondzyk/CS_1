package com.ksondzyk.network;

import com.google.common.primitives.UnsignedLong;
import com.ksondzyk.Server;
import com.ksondzyk.entities.Message;
import com.ksondzyk.entities.Packet;
import com.ksondzyk.exceptions.PacketDamagedException;

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
    private static BlockingQueue<DatagramPacket> queue;
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
    DatagramPacket receivedPacket;
    byte[] receivedData;

    public void run(){

        try {
             receivedData = new byte[Packet.packetMaxSize];
                while(true){
                    receivedPacket = new DatagramPacket(receivedData, receivedData.length);
                    serverSocket.receive(receivedPacket);
                    IPAddress = receivedPacket.getAddress();
                    port = receivedPacket.getPort();
                    receive();
                    send();
                }
        } catch (IOException e) {
            System.err.println("Поток завершив роботу");
        } catch (PacketDamagedException e) {
            Packet packet = new Packet((byte)1,new Message(-1,-1,"send again",false));
            DatagramPacket sendPacket = new DatagramPacket(packet.getData(), packet.getData().length, IPAddress, port);
            try{
                serverSocket.send(sendPacket);
                run();
            }catch (Exception e1){
                e1.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    private void send() {
        Packet packet = new Packet((byte)1,new Message(1,1,"all ok",false));
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

        Packet packetReceived = new Packet(fullPacket,"udp");
        packetReceived.setClientInetAddress(receivedPacket.getAddress());
        packetReceived.setClientPort(receivedPacket.getPort());

        counter = counter.plus(UnsignedLong.ONE);
        queue.put(receivedPacket);
    }

}