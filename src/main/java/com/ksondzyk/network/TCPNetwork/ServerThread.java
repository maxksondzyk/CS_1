package com.ksondzyk.network.TCPNetwork;

import com.ksondzyk.PacketReceiver;
import com.ksondzyk.Processor;
import com.ksondzyk.entities.Packet;
import com.ksondzyk.exceptions.PacketDamagedException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

public class ServerThread extends Thread {

    private InputStream is;
    private OutputStream os;
    private Socket socket;
    private Data data;

    ServerThread(Socket socket, Data data) throws IOException{
        this.data = data;

        this.socket= socket;

            is = socket.getInputStream();
            os = socket.getOutputStream();

           System.out.println("Ready to run");
    }

    public void run(){

        try {
            synchronized (data){
            //while (true) {
                for (int i = 0;i<4;i++){
                data.setState(1);
                PacketReceiver pr = new PacketReceiver();
                while (data.getState() != 1) {
                    data.wait();
                }
                Packet packet = pr.receive(is);
                data.setState(2);
                data.notifyAll();

                System.out.println("Server received packet " + currentThread().getName());

                if (packet.getMessage().equals("END"))
                    break;
                while (data.getState()!=2){
                    data.wait();
                }
                Processor.process(packet, os);
                data.setState(1);
                data.notifyAll();
            }
            }
        } catch (IOException e) {
            System.err.println("IO Exception");
        } catch (Exception e) {
            e.printStackTrace();
        }
         finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Сокет не закрито ...");
            }
        }


    }

}
