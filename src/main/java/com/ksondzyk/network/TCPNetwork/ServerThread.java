package com.ksondzyk.network.TCPNetwork;

import com.ksondzyk.PacketReceiver;
import com.ksondzyk.Processor;
import com.ksondzyk.entities.Packet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ServerThread extends Thread {

    private InputStream is;
    private OutputStream os;
    private Socket socket;

    ServerThread(Socket socket) throws IOException{

        this.socket= socket;

            is = socket.getInputStream();
            os = socket.getOutputStream();

           System.out.println("Ready to run");
           start();
    }

    public void run(){

        try {
            while (true) {

                PacketReceiver pr = new PacketReceiver();
                Packet packet = pr.receive(is);

                System.out.println("Server received packet " + currentThread().getName());

                if(packet.getMessage().equals("END"))
                    break;

                Processor.process(packet, os);
            }
            System.out.println("Закриваємо сокет на сервері");
        } catch (IOException e) {
            System.err.println("IO Exception");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Сокет не закрито ...");
            }
        }


    }

}
