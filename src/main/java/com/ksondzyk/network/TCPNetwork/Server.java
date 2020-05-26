package com.ksondzyk.network.TCPNetwork;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {

    public static final int PORT = 5087;

    public static void main(String[] args) throws IOException {
        ServerSocket s = new ServerSocket(PORT);
        System.out.println("Сервер запущено.");
        ExecutorService service = Executors.newFixedThreadPool(7);
        try {
            for(int i = 0;i<3;i++){//while (true) {
                Socket socket = s.accept();
                service.execute(new ServerThread(socket));
            }
            service.shutdown();
        } finally {
            s.close();
        }
    }

}