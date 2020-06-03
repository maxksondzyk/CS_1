package com.ksondzyk;

import com.ksondzyk.network.TCP.TCPServerThread;
import com.ksondzyk.network.UDPServerThread;
import com.ksondzyk.utilities.NetworkProperties;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {


    public static void main(String[] args) throws IOException {
        String mode = NetworkProperties.MODE;

        ExecutorService service = Executors.newFixedThreadPool(1);
        ServerSocket s = new ServerSocket( NetworkProperties.PORT);
        if(mode.equals("TCP")) {
            while (true)
                service.execute(new TCPServerThread(s.accept()));
        }
        else{
            service.execute(new UDPServerThread());
        }
    }

}