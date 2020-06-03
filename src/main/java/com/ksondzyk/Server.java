package com.ksondzyk;

import com.ksondzyk.network.TCP.TCPServerThread;
import com.ksondzyk.network.UDPServerThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
    public static final int PORT = 5088;
    public static void main(String[] args) throws IOException {
        String mode = "UDP";

        ExecutorService service = Executors.newFixedThreadPool(5);
        ServerSocket s = new ServerSocket(PORT);
        if(mode=="TCP") {
            while (true)
                service.execute(new TCPServerThread(s.accept()));
        }
        else{
            service.execute(new UDPServerThread());
        }
    }

}