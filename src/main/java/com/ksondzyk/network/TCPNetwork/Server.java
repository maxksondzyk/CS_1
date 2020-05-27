package com.ksondzyk.network.TCPNetwork;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {

    public static final int PORT = 5087;

    public static void main(String[] args) throws IOException {

        System.out.println("Сервер запущено.");
        ExecutorService service = Executors.newFixedThreadPool(7);
        SSocket sSocket = new SSocket();
        try {
            for(int i = 0;i<5;i++){//while (true) {
                Socket socket = sSocket.getS().accept();
                service.execute(new ServerThread(socket,sSocket));
            }
            service.shutdown();
        } finally {
            sSocket.getS().close();
        }
    }

}