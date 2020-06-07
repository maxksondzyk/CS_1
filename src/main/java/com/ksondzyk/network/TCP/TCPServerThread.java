package com.ksondzyk.network.TCP;

import com.ksondzyk.Processing.ProcessingQueue;
import com.ksondzyk.utilities.CipherMy;
import com.ksondzyk.utilities.PacketReceiver;
import com.ksondzyk.utilities.Processor;
import com.ksondzyk.entities.Packet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TCPServerThread implements Runnable {

    private final InputStream is;
    private final OutputStream os;
    private final Socket socket;

    public TCPServerThread(Socket socket) throws IOException{
        this.socket= socket;

            is = socket.getInputStream();
            os = socket.getOutputStream();

           System.out.println("Ready to run");
    }

    public void run(){

        try {
            synchronized (socket) {
                while (true) {
                    PacketReceiver pr = new PacketReceiver();

                    Packet packet = pr.receive(is);
                    int id = packet.getBMsq().getBUserId();
                    // while(true){
                    //   ProcessingQueue.Task processingQueueTask = ProcessingQueue.process(id);

//                    while (!processingQueueTask.isDone()) {
//                        Thread.sleep(10);
//                       // System.out.println("Waiting for client #"+id);
//                       // System.out.println("wait");
//                    }


                    System.out.println("Server received packet " + Thread.currentThread().getName());

                    if (CipherMy.decode(packet.getMessage()).equals("END")) {
                        break;
                    }
                    Processor.process(packet, os);
//            }
                }
            }
        } catch (IOException e) {
            System.err.println("Поток завершив роботу");
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
