package com.ksondzyk.network;

import com.ksondzyk.PacketReceiver;
import com.ksondzyk.Processor;
import com.ksondzyk.entities.Packet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class TCPNetwork_еуые implements Network {

    Socket socket;
    ServerSocket serverSocket;

    OutputStream socketOutputStream;
    InputStream serverInputStream;


    @Override
    public void listen() throws IOException {

        serverSocket = new ServerSocket(2305);
       //ExecutorService service = Executors.newFixedThreadPool(8);

        socket = serverSocket.accept();


        socketOutputStream = socket.getOutputStream();
        serverInputStream = socket.getInputStream();
       // serverSocket.close();

    }

    @Override
    public void receive() throws Exception {

         PacketReceiver pr = new PacketReceiver();
         Packet packet = pr.receive(serverInputStream);

         System.out.println("Received");

         System.err.println(packet.getMessage());
         //Processor.process(this, packet);
     }


    @Override
    public void connect() throws IOException {
        socket = new Socket("localhost",5023);
        socketOutputStream = socket.getOutputStream();
        serverInputStream = socket.getInputStream();
    }


    @Override
    public void send(Packet packet) throws IOException {
        byte[] packetBytes = packet.getData();

        socketOutputStream.write(packetBytes);
        socketOutputStream.flush();

        System.out.println("Send");
        System.out.println(Arrays.toString(packetBytes) + "\n");
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }
}
