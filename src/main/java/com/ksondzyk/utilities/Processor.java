package com.ksondzyk.utilities;

import com.google.common.primitives.UnsignedLong;
import com.ksondzyk.DataBase.Table;
import com.ksondzyk.entities.Message;
import com.ksondzyk.entities.Packet;
import com.ksondzyk.exceptions.PacketDamagedException;
import com.ksondzyk.storage.ProductsStorage;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;


public class Processor {

    private static final ProductsStorage storage = new ProductsStorage();

    public static String processUDP(Packet packet){
        Message answerMessage = packet.getBMsq();
        try {

        int cType = packet.getBMsq().getCType();
        answerMessage=answer(cType,CipherMy.decode(answerMessage.getMessage()));

        } catch (PacketDamagedException e) {
            e.printStackTrace();
        }
        return CipherMy.decode(answerMessage.getMessage());
    }
    private static Message answer(int cType,String message) throws PacketDamagedException {
        Message answerMessage = new Message(0,0,"error",false);
        try {
            String nums = message.replaceAll("[^0-9]+"," ");
        String letters = message.replaceAll("[^A-Za-z]+", " ");
        nums = nums.replaceAll(" +[^0-9]","");
        nums = nums.replaceAll("^ ", "");
        String[] arrNum = nums.split(" ", 2);
        String[] arr = letters.split(" ", 2);
        String tableName = arr[0];
        String title = arr[1];
        int quantity = Integer.parseInt(arrNum[0]);
        int price = Integer.parseInt(arrNum[1]);


        switch (cType) {
            case -1:
                answerMessage = new Message(0, 1, "send again",false);
                break;
            case 1:
                answerMessage = new Message(0, 1,  String.valueOf(Table.selectOneByTitle(tableName,title).getInt("quantity")),false);
                break;
            case 2:
                answerMessage = new Message(0, 1, "some product has been deleted",false);
                break;
            case 3:
                answerMessage = new Message(0, 1, "some product has been added:",false);
                break;
            case 4:
                answerMessage = new Message(0, 1, "a group of product has been added",false);
                Table.create(tableName);
                break;
            case 5:
                answerMessage = new Message(0, 1, title+ " have been added to "+ tableName,false);
                Table.insert(tableName,1,quantity,price,title);
                break;
            case 6:
                answerMessage = new Message(0, 1, "the price has been set",false);
                break;
            default:
                throw new PacketDamagedException("Unknown command");

        }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return answerMessage;
    }
    public static void process(Packet packet, OutputStream os) throws PacketDamagedException {
        synchronized (storage) {

            System.out.println("Message received: "+ CipherMy.decode(packet.getMessage()));
            Message answerMessage;

            UnsignedLong bPktId= packet.getBPktId();

            int cType = packet.getBMsq().getCType();

            answerMessage = answer(cType,CipherMy.decode(packet.getBMsq().getMessage()));
            Packet answerPacket = new Packet((byte) 1,bPktId, answerMessage);
                try {
                    PacketSender sender = new PacketSender();
                    sender.send(answerPacket, os, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}
