package com.ksondzyk;
import lombok.Getter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;


public class Packet {
    public static final int B_MAGIC_OFFSET = 0;
    public static final int B_SRC_OFFSET = 1;
    public static final int B_PKT_ID_OFFSET = 2;
    public static final int W_LEN_OFFSET = 10;
    public static final int W_CRC_16_OFFSET = 14;
    public static final int B_MSQ_OFFSET = 16;

    private static final byte bMagic = 0x13;
    /*private final byte bSrc;
    private static long bPktId;
    private int wLen;
    private final Message bMsq;*/
    private static long bPktId;


    public Packet(byte bSrc, Message bMsq) throws IOException {
        this.bSrc = bSrc;
        this.bMsq = bMsq;
        bPktId++;
        data = fill();
    }

    @Getter
    Byte bSrc;

    @Getter
    Integer wLen;

    @Getter
    Message bMsq;

    @Getter
    private final byte[] data;

    private byte[] fill() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ByteBuffer temp = ByteBuffer.allocate(B_SRC_OFFSET - B_MAGIC_OFFSET);
        temp.put(bMagic);
        bytes.write(temp.array());

        temp = ByteBuffer.allocate(B_PKT_ID_OFFSET - B_SRC_OFFSET);
        temp.put(bSrc);
        bytes.write(temp.array());

        temp = ByteBuffer.allocate(W_LEN_OFFSET - B_PKT_ID_OFFSET);
        temp.putLong(bPktId);
        bytes.write(temp.array());

        byte[] message = this.bMsq.toBytes();
        wLen = message.length;
        temp = ByteBuffer.allocate(W_CRC_16_OFFSET - W_LEN_OFFSET);
        temp.putInt(wLen);
        bytes.write(temp.array());

        temp = ByteBuffer.allocate(B_MSQ_OFFSET - W_CRC_16_OFFSET);
        byte[] bytes013 = Arrays.copyOfRange(bytes.toByteArray(), B_MAGIC_OFFSET, W_CRC_16_OFFSET);
        temp.putShort((short)CRC.calculateCRC(CRC.Parameters.CRC16,bytes013));
        bytes.write(temp.array());

        temp = ByteBuffer.allocate(message.length);
        temp.put(message);
        bytes.write(temp.array());

        temp = ByteBuffer.allocate(B_MSQ_OFFSET - W_CRC_16_OFFSET);
        byte[] bytes13end = Arrays.copyOfRange(bytes.toByteArray(), B_MSQ_OFFSET, B_MSQ_OFFSET + message.length);
        temp.putShort((short)CRC.calculateCRC(CRC.Parameters.CRC16,bytes13end));
        bytes.write(temp.array());
        return bytes.toByteArray();
    }
}
