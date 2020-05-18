package com.ksondzyk;
import com.github.snksoft.crc.CRC;
import com.google.common.primitives.UnsignedLong;
import lombok.Getter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;


public class Packet {
    private static final int B_MAGIC_OFFSET = 0;
    private static final int B_SRC_OFFSET = 1;
    private static final int B_PKT_ID_OFFSET = 2;
    private static final int W_LEN_OFFSET = 10;
    private static final int W_CRC_16_OFFSET = 14;
    private static final int B_MSQ_OFFSET = 16;

    private static final Byte bMagic = 0x13;
    private Byte bSrc;
    private UnsignedLong bPktId = UnsignedLong.ZERO;
    private Integer wLen;
    private Message bMsq;
    private Short wCRC16_1;
    private Short wCRC16_2;
    private byte[] packet;

    public Packet(byte bSrc, Message bMsq) throws IOException {
        this.bSrc = bSrc;
        this.bMsq = bMsq;
        bPktId = bPktId.plus(UnsignedLong.ONE);
        data = fill();
    }

    @Getter
    private final byte[] data;

    private byte[] fill() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        byte[] message = this.bMsq.toBytes();
        wLen = message.length;
        int packetLength = bMagic.BYTES+bSrc.BYTES+(W_LEN_OFFSET - B_PKT_ID_OFFSET)+wLen.BYTES+wCRC16_1.BYTES+message.length+wCRC16_2.BYTES;
        ByteBuffer bb = ByteBuffer.allocate(packetLength);
        bb.put(bMagic).put(bSrc).putLong(bPktId.longValue()).putInt(wLen);
        byte[] bytes1 = Arrays.copyOfRange(bb.array(), B_MAGIC_OFFSET, W_CRC_16_OFFSET);
        wCRC16_1 = (short)CRC.calculateCRC(CRC.Parameters.CRC16,bytes1);
        bb.putShort(wCRC16_1).put(message);
        byte[] bytes2 = Arrays.copyOfRange(bb.array(), B_MSQ_OFFSET, B_MSQ_OFFSET + message.length);
        wCRC16_2 = (short)CRC.calculateCRC(CRC.Parameters.CRC16,bytes2);
        bb.putShort(wCRC16_2);
        bytes.write(bb.array());
        return bytes.toByteArray();

    }


    public Packet(byte[] packet) throws Exception {
        ByteBuffer buffer = ByteBuffer.wrap(packet);
        this.packet = packet;
        Byte expectedBMagic = buffer.get();
        if (!expectedBMagic.equals(bMagic))
            throw new Exception("Unexpected bMagic");
        bSrc = buffer.get();
        bPktId = UnsignedLong.fromLongBits(buffer.getLong());
        wLen = buffer.getInt();
        wCRC16_1 = buffer.getShort();
        byte[] messageBody = new byte[wLen-8];
        bMsq = new Message(buffer.getInt(),buffer.getInt(), String.valueOf(buffer.get(messageBody)));
        wCRC16_2 = buffer.getShort();
        data = fill();
    }

    public boolean checkCRC() {
        int wLength = packet.length-Packet.B_MSQ_OFFSET -(Packet.B_MSQ_OFFSET -Packet.W_CRC_16_OFFSET);
        return(((short)CRC.calculateCRC(CRC.Parameters.CRC16,Arrays.copyOfRange(packet,Packet.B_MAGIC_OFFSET,Packet.W_CRC_16_OFFSET))== ByteBuffer.wrap(Arrays.copyOfRange(packet,Packet.W_CRC_16_OFFSET,Packet.B_MSQ_OFFSET)).getShort())&&
                ((short)CRC.calculateCRC(CRC.Parameters.CRC16,Arrays.copyOfRange(packet,Packet.B_MSQ_OFFSET,Packet.B_MSQ_OFFSET +wLength))== ByteBuffer.wrap(Arrays.copyOfRange(packet,Packet.B_MSQ_OFFSET +wLength,Packet.B_MSQ_OFFSET +wLength+(Packet.B_MSQ_OFFSET -Packet.W_CRC_16_OFFSET))).getShort()));
    }
    public String getMessage(){
        byte[] message = Arrays.copyOfRange(packet,Packet.B_MSQ_OFFSET +Message.MESSAGE_OFFSET +12,packet.length-2-(Packet.B_MSQ_OFFSET - Packet.W_CRC_16_OFFSET));
        String msg = new String(message);
        msg = CipherMy.decode(msg);
        return msg;
    }
}
