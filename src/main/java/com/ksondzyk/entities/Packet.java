package com.ksondzyk.entities;

import com.github.snksoft.crc.CRC;
import com.google.common.primitives.UnsignedLong;
import com.ksondzyk.utilities.CipherMy;
import com.ksondzyk.exceptions.PacketDamagedException;
import lombok.Getter;

import java.nio.ByteBuffer;


public class Packet {


    public static final Byte bMagic = 0x13;
    private final Byte bSrc;
    @Getter
    private UnsignedLong bPktId = UnsignedLong.ZERO;
    private Integer wLen;
    @Getter
    private Message bMsq;
    private Short wCRC16_1;
    private Short wCRC16_2;

    public final static Integer packetPartFirstLengthWithoutwLen = bMagic.BYTES + Byte.BYTES + Long.BYTES;
    public final static Integer packetPartFirstLength = packetPartFirstLengthWithoutwLen + Integer.BYTES;
    public final static Integer packetPartFirstLengthWithCRC16 = packetPartFirstLength + Short.BYTES;
    public final static Integer packetMaxSize = packetPartFirstLengthWithCRC16 + Message.BYTES_MAX_SIZE;

    public Packet(byte bSrc, Message bMsq) {
        this.bSrc = bSrc;
        this.bMsq = bMsq;
        synchronized (this.bMsq) {
            bPktId = bPktId.plus(UnsignedLong.ONE);
            data = fill();
        }
    }


    public Packet(byte bSrc, UnsignedLong bPktId, Message bMsq) {
        this.bSrc = bSrc;
        this.bMsq = bMsq;
        synchronized (this.bMsq) {
            this.bPktId = bPktId;
            data = fill();
        }
    }
    @Getter
    private final byte[] data;

    private byte[] fill() {

        byte[] message = this.bMsq.toBytes();
        wLen = message.length;

        Integer packetFirstPartLength = bMagic.BYTES + bSrc.BYTES + Long.BYTES + wLen.BYTES;
        Integer packetSecondPartLength = message.length;

        byte[] packetFirstPart = ByteBuffer.allocate(packetFirstPartLength)
                                        .put(bMagic)
                                        .put(bSrc)
                                        .putLong(bPktId.longValue())
                                        .putInt(wLen).array();
        wCRC16_1 = (short)CRC.calculateCRC(CRC.Parameters.CRC16,packetFirstPart);

        byte[] packetPartSecond = ByteBuffer.allocate(packetSecondPartLength)
                .put(message).array();
        wCRC16_2 = (short)CRC.calculateCRC(CRC.Parameters.CRC16,packetPartSecond);

        Integer packetLength = packetFirstPartLength + wCRC16_1.BYTES + packetSecondPartLength + wCRC16_2.BYTES;

        return ByteBuffer.allocate(packetLength)
                .put(packetFirstPart)
                .putShort(wCRC16_1)
                .put(packetPartSecond)
                .putShort(wCRC16_2).array();
}

    public Packet(byte[] packet) throws PacketDamagedException {
        data = packet;
        synchronized (this.data) {
            ByteBuffer bb = ByteBuffer.wrap(packet);

            Byte expectedBMagic = bb.get();
            if (!expectedBMagic.equals(bMagic)) {
                throw new PacketDamagedException("Unexpected bMagic");
            }

            bSrc = bb.get();
            bPktId = UnsignedLong.fromLongBits(bb.getLong());
            wLen = bb.getInt();
            wCRC16_1 = bb.getShort();

            byte[] messageBody = new byte[wLen - 8];
            int cType = bb.getInt();
            int bUserId = bb.getInt();
            bb.get(messageBody);
            String message = new String(messageBody);

            bMsq = new Message(cType, bUserId, message,true);
            wCRC16_2 = bb.getShort();
            if (!checkCRC()) {
                throw new PacketDamagedException("CRC not expected ");
            }
        }
    }

    public String getDecodedMessage(){
        return CipherMy.decode(bMsq.getMessage());
    }

    public boolean checkCRC() {
        byte[] message = this.bMsq.toBytes();
        Integer packetFirstPartLength = bMagic.BYTES + bSrc.BYTES + Long.BYTES + wLen.BYTES;
        Integer packetPartSecondLength = message.length;

        byte[] packetFirstPart = ByteBuffer.allocate(packetFirstPartLength)
                .put(bMagic)
                .put(bSrc)
                .putLong(bPktId.longValue())
                .putInt(wLen).array();
        byte[] packetPartSecond = ByteBuffer.allocate(packetPartSecondLength)
                .put(message).array();

        Short wCRC16_1_test = (short)CRC.calculateCRC(CRC.Parameters.CRC16,packetFirstPart);
        Short wCRC16_2_test = (short)CRC.calculateCRC(CRC.Parameters.CRC16,packetPartSecond);

        return (wCRC16_1_test.equals(wCRC16_1))&&(wCRC16_2_test.equals(wCRC16_2));
    }

    public String getMessage(){
        return bMsq.getMessage();
    }

}
