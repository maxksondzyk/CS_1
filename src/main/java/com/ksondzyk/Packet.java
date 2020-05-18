package com.ksondzyk;
import com.github.snksoft.crc.CRC;
import com.google.common.primitives.UnsignedLong;
import lombok.Getter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;


public class Packet {

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

    private byte[] fill() {

        byte[] message = this.bMsq.toBytes();
        wLen = message.length;

        int packetFirstPartLength = bMagic.BYTES + bSrc.BYTES + Long.BYTES + wLen.BYTES;
        ByteBuffer packetFirstPart = ByteBuffer.allocate(packetFirstPartLength)
                                        .put(bMagic)
                                        .put(bSrc)
                                        .putLong(bPktId.longValue())
                                        .putInt(wLen);

        wCRC16_1 = (short)CRC.calculateCRC(CRC.Parameters.CRC16,packetFirstPart.array());
        wCRC16_2 = (short)CRC.calculateCRC(CRC.Parameters.CRC16,message);

        ByteBuffer packet = ByteBuffer.allocate(packetFirstPartLength + wCRC16_1.BYTES + message.length+ wCRC16_2.BYTES)
                                        .put(packetFirstPart)
                                        .putShort(wCRC16_1)
                                        .put(message)
                                        .putShort(wCRC16_2);

        return packet.array();
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
