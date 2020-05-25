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

    ServerThread(Socket socket){

        this.socket= socket;

        try {
            is = socket.getInputStream();
            os = socket.getOutputStream();

        } catch (IOException e) {
            e.printStackTrace();
        }
      //  System.out.println("Thread is running");

      //  this.start();
    }

    public void run(){
            try {

                    System.out.println("Thread is running");
                    PacketReceiver pr = new PacketReceiver();
                    Packet packet = null;
                    try {
                        packet = pr.receive(is);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    System.out.println("Received" + currentThread().getName());

                    System.err.println(packet.getMessage());

                    Processor.process(packet, os);
            }catch (Exception e){

            }


    }

}
