package com.ksondzyk;

import com.ksondzyk.network.UDPServerThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
    public static final int PORT = 5087;
    public static void main(String[] args) throws IOException {

        System.out.println("Сервер запущено.");
        ExecutorService service = Executors.newFixedThreadPool(1);
        ServerSocket s = new ServerSocket(PORT);
          //  while(true){
                service.execute(new UDPServerThread());
            //}
           // service.shutdown();
    }

}