package com.ksondzyk;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Packet {
    private static final byte bMagic = 0x13;
    private byte bSrc;
    private static long bPktId;
    private int wLen;
    private Message bMsq;
    private byte[] data;
    public Packet(byte bSrc, int cType,int bUserId,String message) throws IOException {
        this.bSrc = bSrc;
        this.bMsq = new Message(cType,bUserId,message);
        bPktId++;
        data = fill();
    }
    public byte[] getData(){
        return data;
    }
    private byte[] fill() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ByteBuffer temp = ByteBuffer.allocate(2);
        temp.put(bMagic);
        temp.put(bSrc);
        bytes.write(temp.array());

        temp = ByteBuffer.allocate(8);
        temp.putLong(bPktId);
        bytes.write(temp.array());

        byte[] message = this.bMsq.toBytes();
        wLen = bytes.size();
        wLen+=message.length;
        wLen+=8;
        temp = ByteBuffer.allocate(4);
        temp.putInt(wLen);
        bytes.write(temp.array());

        temp = ByteBuffer.allocate(4);
        byte[] bytes013 = Arrays.copyOfRange(bytes.toByteArray(),0,14);
        temp.putInt(CRC.calculate_crc(bytes013));
        bytes.write(temp.array());

        temp = ByteBuffer.allocate(message.length);
        temp.put(message);
        bytes.write(temp.array());

        temp = ByteBuffer.allocate(4);
        byte[] bytes13end = Arrays.copyOfRange(bytes.toByteArray(),18,18+message.length);
        temp.putInt(CRC.calculate_crc(bytes13end));
        bytes.write(temp.array());
        return bytes.toByteArray();
    }


    public Message getBMsq(){
        return bMsq;
    }

}
