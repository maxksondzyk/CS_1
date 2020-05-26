package com.ksondzyk.network.TCPNetwork;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Client {

    public static void main(String[] args) {
        start();
    }

    public static void start(){
        ExecutorService service = Executors.newFixedThreadPool(8);
        try {
            Socket socket = new Socket("localhost",5023);
            while(true){
                service.execute(new ClientThread(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}