package com.ksondzyk.network.TCPNetwork;

import com.ksondzyk.PacketReceiver;
import com.ksondzyk.Processor;
import com.ksondzyk.entities.Packet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ServerThread extends Thread {

    private InputStream is;
    private OutputStream os;
    private Socket socket;
    private SSocket sSocket;

    ServerThread(Socket socket, SSocket sSocket) throws IOException{
        this.sSocket = sSocket;

        this.socket= socket;

            is = socket.getInputStream();
            os = socket.getOutputStream();

           System.out.println("Ready to run");
    }

    public void run(){

        try {
            synchronized (sSocket){
            //while (true) {
                for (int i = 0;i<4;i++){
                sSocket.setState(1);
                PacketReceiver pr = new PacketReceiver();
                while (sSocket.getState() != 1) {
                    sSocket.wait();
                }
                Packet packet = pr.receive(is);
                sSocket.setState(2);
                sSocket.notifyAll();

                System.out.println("Server received packet " + currentThread().getName());

                if (packet.getMessage().equals("END"))
                    break;
                while (sSocket.getState()!=2){
                    sSocket.wait();
                }
                Processor.process(packet, os);
                sSocket.setState(1);
                sSocket.notifyAll();
            }
            }
        } catch (IOException e) {
            System.err.println("IO Exception");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Сокет не закрито ...");
            }
        }


    }

}
