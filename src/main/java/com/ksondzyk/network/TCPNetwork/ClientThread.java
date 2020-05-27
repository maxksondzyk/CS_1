package com.ksondzyk.network.TCPNetwork;

import com.ksondzyk.CipherMy;
import com.ksondzyk.PacketGenerator;
import com.ksondzyk.PacketReceiver;
import com.ksondzyk.PacketSender;
import com.ksondzyk.entities.Packet;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ClientThread extends Thread {
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private static int counter = 0;
    private int clientID = counter++;
    @Getter
    private static int threadcount = 0;
    int state = 0;
    private Data data;


    public ClientThread(Data data) {
        this.data = data;
        System.out.println("Запустимо клієнт з номером " + clientID);
        threadcount++;

        try {
            socket = new Socket(data.getAddr(), Server.PORT);
        }
        catch (IOException e) {
            System.err.println("Не вдалося з'єднатися з сервером");
        }
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
        synchronized (data) {
            try {
                // client sends messages and gets replies
                for (int i = 0; i < 4; i++) {
                    data.state = 1;
                    PacketGenerator pg = new PacketGenerator();

                    while(data.state!=1){
                        data.wait();
                    }
                        Packet packet = pg.newPacket(i);
                    data.state = 2;
                    data.notifyAll();
                    PacketSender sender = new PacketSender();
                    while(data.state!=2){
                        data.wait();
                    }
                    sender.send(packet, outputStream,i);
                    data.state=3;
                    data.notifyAll();
                    PacketReceiver pr = new PacketReceiver();
                    while (data.state!=3){
                        data.wait();
                    }
                    Packet packetReceived = pr.receive(inputStream);
                    data.state = 1;
                    data.notifyAll();

                    System.out.println("Received " + currentThread().getName());
                    System.out.println("Respond: " + packetReceived.getDecodedMessage());
                }

                PacketSender fin = new PacketSender();

                fin.send(PacketGenerator.newPacket(clientID, "END"), outputStream,0);

                System.out.println("END of " + currentThread().getName());

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
