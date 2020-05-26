package com.ksondzyk.network.TCPNetwork;

import com.ksondzyk.entities.Message;
import com.ksondzyk.entities.Packet;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Client {

    public static void main(String[] args) {
        start();
//        try {
//            Network network = new TCPNetwork_еуые();
//            network.connect();
//
//            network.send(packet);
//            network.receive();
//
//            network.send(secondPacket);
//            network.receive();
//
//            network.send(secondPacket);
//            network.receive();
//
//            network.close();
//        }  catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public static void start(){
        Message testMessage = new Message(1, 1, "time", false);
        Packet packet = new Packet((byte) 1,  testMessage);

        Message secondTestMessage = new Message(1, 1, "notTime", false);
        Packet secondPacket = new Packet((byte) 1, secondTestMessage);

        ExecutorService service = Executors.newFixedThreadPool(1);
        try {
            Socket socket = new Socket("localhost",5023);

            //  ClientThread clientThread = new ClientThread(socket);
            while(true){
                service.execute(new ClientThread(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}