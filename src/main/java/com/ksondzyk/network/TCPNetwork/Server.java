package com.ksondzyk.network.TCPNetwork;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
    public static void main(String[] args) {
     start();
    }
    public static void start(){
        try {
            ServerSocket serverSocket = new ServerSocket(5023);
            ExecutorService service = Executors.newFixedThreadPool(1);

            while(true){
                service.execute(new ServerThread(serverSocket.accept()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}