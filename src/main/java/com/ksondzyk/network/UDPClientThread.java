package com.ksondzyk.network;

import com.ksondzyk.Server;
import com.ksondzyk.entities.Message;
import com.ksondzyk.entities.Packet;
import com.ksondzyk.utilities.PacketGenerator;
import com.ksondzyk.utilities.PacketReceiver;
import com.ksondzyk.utilities.PacketSender;
import lombok.Getter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class UDPClientThread implements Runnable{
    private DatagramSocket socket;
    private InetAddress addr;
    private final PacketGenerator packetGenerator;
    private static int counter = 0;
    private final int clientID = counter++;
    @Getter
    private static int threadcount = 0;
    public UDPClientThread() {
        packetGenerator = new PacketGenerator();
        System.out.println("Запустимо клієнт з номером " + clientID);
        threadcount++;
        try {
            addr = InetAddress.getByName("localhost");
            socket = new DatagramSocket();
        }
        catch (IOException e) {
            // Сокет має бути закритий при будь якій помилці
            // крім помилки конструктора сокета
            socket.close();
        }
        // Якщо все відбудеться нормально сокет буде закрито
        // в методі run() потоку.
    }



    public void run() {
        synchronized (socket) {
            try {
                // client sends messages and gets replies
                for (int i = 0; i < 4; i++) {
                    Packet packet = packetGenerator.newPacket(i);
                    byte[] sentData = packet.getData();
                    byte[] receivedData = new byte[Packet.packetMaxSize];
                    DatagramPacket datagramPacketSent = new DatagramPacket(sentData,sentData.length,addr, Server.PORT);

                    while(true) {
                        socket.send(datagramPacketSent);
                        DatagramPacket datagramPacketReceived = new DatagramPacket(receivedData,receivedData.length);
                        socket.receive(datagramPacketReceived);

                        ByteBuffer byteBuffer = ByteBuffer.wrap(receivedData);

                        Integer wLen = byteBuffer.getInt(Packet.packetPartFirstLengthWithoutwLen);

                        byte[] fullPacket = new byte[Packet.packetPartFirstLength + Message.BYTES_WITHOUT_MESSAGE + wLen];
                        byteBuffer.get(fullPacket);

                        System.out.println("Received");
                        System.out.println(Arrays.toString(fullPacket) + "\n");

                        Packet packetReceived = new Packet(fullPacket,"udp");

                        packetReceived.setClientInetAddress(datagramPacketReceived.getAddress());
                        packetReceived.setClientPort(datagramPacketReceived.getPort());

                        if(packetReceived.getBMsq().getCType()!=-1){
                            break;
                        }else {
                            System.out.println("The packet was corrupted, sending it once more...");
                        }

                    }

                }

            } catch (IOException e) {
                System.err.println("Поток завершив роботу");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // Завжди закриває:
                socket.close();
                threadcount--; // Завершуємо цей потік
            }
        }
    }


}
