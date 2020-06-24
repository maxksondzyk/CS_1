package com.ksondzyk.network.TCP;

import com.ksondzyk.entities.Packet;
import com.ksondzyk.utilities.Properties;
import com.ksondzyk.utilities.PacketGenerator;
import com.ksondzyk.utilities.PacketSender;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class TCPClientThread implements Runnable {
    private Socket socket;
    private InetAddress addr;
    private InputStream inputStream;
    private OutputStream outputStream;
    private static int counter = 0;
    private final int clientID = counter++;
    private Packet packet;
    private Packet answer;

    public Packet send(){
        run();
        return answer;
    }
    public TCPClientThread(Packet packet) {
        this.packet = packet;
        System.out.println("Запустимо клієнт з номером " + clientID);
        try {
            addr = InetAddress.getByName("localhost");

            connect();

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

    private void connect() {
        for (int j = 0; j < 5; j++){
            try {
                socket = new Socket(addr, Properties.PORT);
                break;
            }
            catch (IOException e) {
                System.err.println("Couldn't connect to the server: " + j);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
                if (j == 5 - 1){
                    return;
                }
            }
        }
    }


    public void run() {
            try {
                //for (int i = 0; i < 4; i++) {

                    PacketSender sender = new PacketSender();
                    sender.send(packet, outputStream);

                    TCPPacketReceiver pr = new TCPPacketReceiver();
                    answer = pr.receive(inputStream);

                    if (answer.getBPktId().equals(packet.getBPktId())) {
                        System.out.println("CORRECT");
                        //break;
                    }
                    else
                        System.out.println("WRONG PACKET RESPONSE");
               // }

            } catch (IOException e) {
                System.err.println("Поток завершив роботу");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // Завжди закриває:
                try {
                    socket.close();
                } catch (IOException e) {
                    System.err.println("Socket not closed");
                }
            }
    }


}
