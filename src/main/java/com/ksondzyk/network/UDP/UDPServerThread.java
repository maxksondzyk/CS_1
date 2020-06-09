package com.ksondzyk.network.UDP;

import com.ksondzyk.entities.Message;
import com.ksondzyk.entities.Packet;
import com.ksondzyk.utilities.CipherMy;
import com.ksondzyk.utilities.Properties;
import com.ksondzyk.network.TCP.TCPPacketReceiver;
import com.ksondzyk.Processing.Processor;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class UDPServerThread implements Runnable {
    private static DatagramSocket serverSocket;


    public UDPServerThread() {

        try {
            serverSocket = new DatagramSocket(Properties.PORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {

        try {
            while (true) {

                Packet packetReceived = UDPPacketReceiver.receive(serverSocket);


                if(packetReceived.getBMsq().getCType()!=-1) {
                    System.out.println(CipherMy.decode(packetReceived.getBMsq().getMessage()));

                    Future<Message> response = Processor.process(packetReceived);

                    System.out.println(CipherMy.decode(response.get().getMessage()));
                }
            }
        } catch (IOException e) {
            System.err.println("Сервер завершив роботу");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}