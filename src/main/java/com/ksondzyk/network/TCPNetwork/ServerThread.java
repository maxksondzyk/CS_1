package com.ksondzyk.network.TCPNetwork;

import com.ksondzyk.PacketReceiver;
import com.ksondzyk.Processor;
import com.ksondzyk.entities.Packet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ServerThread extends Thread {

    private final InputStream is;
    private final OutputStream os;
    private final Socket socket;
    private final Data data;

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
                while(true){
                PacketReceiver pr = new PacketReceiver();

                Packet packet = pr.receive(is);

                System.out.println("Server received packet " + currentThread().getName());

                if (packet.getMessage().equals("END"))
                    break;

                Processor.process(packet, os);
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
