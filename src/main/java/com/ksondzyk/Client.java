package com.ksondzyk;

//import com.ksondzyk.network.TCPClientThread;
import com.ksondzyk.network.TCP.TCPClientThread;
import com.ksondzyk.network.UDPClientThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Client {

        public static void main(String[] args) {
            String mode = "TCP";
            ExecutorService service = Executors.newFixedThreadPool(4);
            for(int i = 0;i<4;i++){
                service.execute((mode.equals("UDP"))?new UDPClientThread():new TCPClientThread());
            }
            service.shutdown();
        }



}