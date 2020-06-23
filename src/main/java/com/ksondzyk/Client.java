package com.ksondzyk;

import com.ksondzyk.entities.Packet;
import com.ksondzyk.network.TCP.TCPClientThread;
import com.ksondzyk.network.UDP.UDPClientThread;
import com.ksondzyk.utilities.Properties;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Client {

        Client(Packet packet) {
            String mode = Properties.MODE;
            ExecutorService service = Executors.newFixedThreadPool(10);
            for(int i = 0;i<1;i++){
                service.execute((mode.equals("TCP"))?new TCPClientThread(packet):new UDPClientThread());
            }
            service.shutdown();
        }



}