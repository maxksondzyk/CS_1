package com.ksondzyk.network;

import com.ksondzyk.entities.Packet;
import com.ksondzyk.utilities.PacketGenerator;
import com.ksondzyk.utilities.PacketReceiver;
import com.ksondzyk.utilities.PacketSender;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientThread implements Runnable {
    private final Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private static int counter = 0;
    private final int clientID = counter++;
    @Getter
    private static int threadcount = 0;


    public ClientThread(Socket socket) {
        System.out.println("Запустимо клієнт з номером " + clientID);
        threadcount++;
        this.socket = socket;
        try {

            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

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




    public void run() {
        synchronized (socket) {
            try {
                // client sends messages and gets replies
                for (int i = 0; i < 4; i++) {
                    PacketGenerator pg = new PacketGenerator();
                    Packet packet = pg.newPacket(i);

                    PacketSender sender = new PacketSender();
                    sender.send(packet, outputStream,i);

                    PacketReceiver pr = new PacketReceiver();
                    Packet packetReceived = pr.receive(inputStream);

                    System.out.println("Received " + Thread.currentThread().getName());
                    System.out.println("Respond: " + packetReceived.getDecodedMessage());
                }

                PacketSender fin = new PacketSender();

                fin.send(PacketGenerator.newPacket(clientID, "END"), outputStream,0);

                System.out.println("END of " + Thread.currentThread().getName());

            } catch (IOException e) {
                System.err.println("IO Exception");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // Завжди закриває:
                try {
                    socket.close();
                } catch (IOException e) {
                    System.err.println("Socket not closed");
                }
                threadcount--; // Завершуємо цей потік
            }
        }
    }


}
