package com.ksondzyk;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Packet {
    public static final int B_MAGIC = 0;
    public static final int B_SRC = 1;
    public static final int B_PKT_ID = 2;
    public static final int W_LEN = 10;
    public static final int W_CRC_16 = 14;
    public static final int B_MSQ = 16;

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
        ByteBuffer temp = ByteBuffer.allocate(B_SRC-B_MAGIC);
        temp.put(bMagic);
        bytes.write(temp.array());

        temp = ByteBuffer.allocate(B_PKT_ID-B_SRC);
        temp.put(bSrc);
        bytes.write(temp.array());

        temp = ByteBuffer.allocate(W_LEN-B_PKT_ID);
        temp.putLong(bPktId);
        bytes.write(temp.array());

        byte[] message = this.bMsq.toBytes();
        wLen=message.length;
        temp = ByteBuffer.allocate(W_CRC_16-W_LEN);
        temp.putInt(wLen);
        bytes.write(temp.array());

        temp = ByteBuffer.allocate(B_MSQ-W_CRC_16);
        byte[] bytes013 = Arrays.copyOfRange(bytes.toByteArray(),B_MAGIC,W_CRC_16);
        temp.putShort(CRC.calculate_crc(bytes013));
        bytes.write(temp.array());

        temp = ByteBuffer.allocate(message.length);
        temp.put(message);
        bytes.write(temp.array());

        temp = ByteBuffer.allocate(B_MSQ-W_CRC_16);
        byte[] bytes13end = Arrays.copyOfRange(bytes.toByteArray(),B_MSQ,B_MSQ+message.length);
        temp.putShort(CRC.calculate_crc(bytes13end));
        bytes.write(temp.array());
        return bytes.toByteArray();
    }


    public Message getBMsq(){
        return bMsq;
    }

}
