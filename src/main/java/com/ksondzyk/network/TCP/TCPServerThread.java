package com.ksondzyk.network.TCP;

import com.ksondzyk.Processing.Processor;
import com.ksondzyk.entities.Message;
import com.ksondzyk.entities.Packet;
import com.ksondzyk.utilities.CipherMy;
import com.ksondzyk.utilities.PacketSender;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.Future;

import static com.ksondzyk.Server.serverIsWorking;

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
                    TCPPacketReceiver pr = new TCPPacketReceiver();

                    Packet packet = pr.receive(is);

                    Future<Message> response = Processor.process(packet,os);

                    while (!response.isDone()) {
                        Thread.sleep(10);
                       // System.out.println("Waiting for client");
                        //System.out.println("wait");
                    }
                    Packet answerPacket = new Packet((byte) 1,packet.getBPktId(), response.get());
                    PacketSender sender = new PacketSender();
                    sender.send(answerPacket, os);

                    System.out.println(CipherMy.decode(response.get().getMessage()));

                    System.out.println("Server received packet " + Thread.currentThread().getName());

                    if (CipherMy.decode(packet.getMessage()).equals("END")) {
                        synchronized (serverIsWorking) {
                            if (serverIsWorking) {
                                serverIsWorking = false;
                                socket.close();
                            }
                        }
                        break;
                    }
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
