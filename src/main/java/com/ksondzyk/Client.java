package com.ksondzyk;

import com.ksondzyk.network.TCP.TCPClientThread;
import com.ksondzyk.network.UDPClientThread;
import com.ksondzyk.utilities.Properties;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Client {

        public static void main(String[] args) {
            String mode = Properties.MODE;
            ExecutorService service = Executors.newFixedThreadPool(10);
            for(int i = 0;i<10;i++){
                service.execute((mode.equals("TCP"))?new TCPClientThread():new UDPClientThread());
            }
            service.shutdown();
        }



}