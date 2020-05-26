package com.ksondzyk.network.TCPNetwork;

import com.ksondzyk.PacketGenerator;
import com.ksondzyk.PacketReceiver;
import com.ksondzyk.PacketSender;
import com.ksondzyk.Processor;
import com.ksondzyk.entities.Packet;
import lombok.Getter;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;

public class ClientThread extends Thread {
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private static int counter = 0;
    private int clientID = counter++;
    @Getter
    private static int threadcount = 0;


    public ClientThread(InetAddress addr) {
        System.out.println("Запустимо клієнт з номером " + clientID);
        threadcount++;

        try {
            socket = new Socket(addr, Server.PORT);
        }
        catch (IOException e) {
            System.err.println("Не вдалося з'єднатися з сервером");
        }
        try {

            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            start();
        }
        catch (IOException e) {
            // Сокет має бути закритий при будь якій помилці
            // крім помилки конструктора сокета
            try {
                socket.close();
            }
            catch (IOException e2) {
                System.err.println("Сокет не закрито!");
            }
        }
        // Якщо все відбудеться нормально сокет буде закрито
        // в методі run() потоку.
    }




    public void run(){
            try {
                // client sends messages and gets replies

                for(int i=0; i<2; i++) {
    PacketGenerator pg = new PacketGenerator();
    Packet packet = pg.newPacket();

    PacketSender sender = new PacketSender();
    sender.send(packet, outputStream);

    PacketReceiver pr = new PacketReceiver();
    Packet packetReceived = pr.receive(inputStream);

    System.out.println("Received " + currentThread().getName());
    System.out.println("Respond: " + packetReceived.getMessage());



}

                System.out.println("END of "+currentThread().getName());

            }
            catch (IOException e) {
                System.err.println("IO Exception");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // Завжди закриває:
                try {
                    socket.close();
                }
                catch (IOException e) {
                    System.err.println("Socket not closed");
                }
                threadcount--; // Завершуємо цей потік
            }
        }


}
