package com.ksondzyk.Processing;

import com.google.common.primitives.UnsignedLong;
import com.ksondzyk.DataBase.Table;
import com.ksondzyk.Server;
import com.ksondzyk.entities.Message;
import com.ksondzyk.entities.Packet;
import com.ksondzyk.exceptions.PacketDamagedException;
import com.ksondzyk.utilities.CipherMy;
import com.ksondzyk.utilities.PacketSender;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.concurrent.*;


public class Processor implements Callable{
    Packet packet;
    static OutputStream os;

    Processor(Packet packet){
        this.packet = packet;
        run();
    }

    static ExecutorService executorPool = Executors.newFixedThreadPool(Server.processingThreadCount);

    static Message answerMessage;

    private static Message answer(int cType,String message) throws PacketDamagedException {
        answerMessage = new Message(0,0,"error",false);
        try {
            String nums = message.replaceAll("[^0-9]+"," ");
            nums = nums.replaceAll(" +[^0-9]","");
            nums = nums.replaceAll("^ ", "");
            String[] arrNum = nums.split(" ", 2);

            int quantity = Integer.parseInt(arrNum[0]);
            int price = Integer.parseInt(arrNum[1]);


            String letters = message.replaceAll("[^A-Za-z]+", " ");
            String[] arr = letters.split(" ", 2);

            String category = arr[0];
            String title = arr[1];


            int currentValue;
            int newValue;
        switch (cType) {
            case -1:
                answerMessage = new Message(0, 1, "send again",false);
                break;
            case 1:
                answerMessage = new Message(0, 1,  "There are "+String.valueOf(Table.selectOneByTitle(title).getInt("quantity"))+" "+title + "for the price of " + Table.selectOneByTitle(title).getInt("price"),false);
                break;
            case 2:
                answerMessage = new Message(0, 1, quantity+" of "+title+" has been deleted",false);
                currentValue = Table.selectOneByTitle(title).getInt("quantity");
                newValue = currentValue - quantity;
                Table.update(title,newValue,"quantity");
                break;
            case 3:
                answerMessage = new Message(0, 1, quantity+" of "+title+" has been added",false);
                currentValue = Table.selectOneByTitle(title).getInt("quantity");
                newValue = currentValue + quantity;
                Table.update(title,newValue,"quantity");
                break;
            case 4:
                answerMessage = new Message(0, 1, title+ " have been added to "+ category,false);
                Table.insert(category,title , quantity,price);
                break;
            case 5:
                answerMessage = new Message(0, 1, "the price of "+title+" has been set to "+price,false);
                Table.update(title,price,"price");

                break;
            default:
                throw new PacketDamagedException("Unknown command");

        }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return answerMessage;
    }
    public static Future<Message> process(Packet packet, OutputStream ostream) {
        os = ostream;
        Callable<Message> processingAsync = new Processor(packet);
        return executorPool.submit(processingAsync);
    }
    public static Future<Message> process(Packet packet) {
        Callable<Message> processingAsync = new Processor(packet);
        return executorPool.submit(processingAsync);
    }
    public void run(){
        synchronized (packet) {

            System.out.println("Message received: "+ CipherMy.decode(packet.getMessage()));

            int cType = packet.getBMsq().getCType();
            try {
            answerMessage = answer(cType,CipherMy.decode(packet.getBMsq().getMessage()));

            } catch (PacketDamagedException e) {
                e.printStackTrace();
            }
        }
    }
//    private static String process(Packet packet, OutputStream os) throws PacketDamagedException {
//
//        return CipherMy.decode(answerMessage.getMessage());
//    }

    @Override
    public Message call() throws Exception {
        try {
            Thread.sleep(1000 * Server.secondsPerTask);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return answerMessage;
    }
    public static void shutdown() {
        executorPool.shutdown();
        try {
            if (!executorPool.awaitTermination(60, TimeUnit.SECONDS))
                System.err.println("ProcessingAsync threads didn't finish in 60 seconds!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
