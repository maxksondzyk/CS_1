package com.ksondzyk.entities;

import com.github.snksoft.crc.CRC;
import com.google.common.primitives.UnsignedLong;
import com.ksondzyk.CipherMy;
import com.ksondzyk.entities.Message;
import lombok.Getter;

import java.nio.ByteBuffer;


public class Packet {

    public static final Byte bMagic = 0x13;
    private final Byte bSrc;
    private UnsignedLong bPktId = UnsignedLong.ZERO;
    private Integer wLen;
    private final Message bMsq;
    private Short wCRC16_1;
    private Short wCRC16_2;
    private final String decodedMessage;

    public Packet(byte bSrc, Message bMsq) {
        this.bSrc = bSrc;
        this.bMsq = bMsq;
        decodedMessage = "forbidden";
        bPktId = bPktId.plus(UnsignedLong.ONE);
        data = fill();
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

    public Packet(byte[] packet) throws Exception {
        data = packet;

        ByteBuffer bb = ByteBuffer.wrap(packet);

        Byte expectedBMagic = bb.get();
        if (!expectedBMagic.equals(bMagic)) {
            throw new Exception("Unexpected bMagic");
        }

        bSrc = bb.get();
        bPktId = UnsignedLong.fromLongBits(bb.getLong());
        wLen = bb.getInt();
        wCRC16_1 = bb.getShort();

        byte[] messageBody = new byte[wLen-8];
        int cType = bb.getInt();
        int bUserId = bb.getInt();
        bb.get(messageBody);
        String message = new String(messageBody);

        decodedMessage = CipherMy.decode(message);
        bMsq = new Message(cType, bUserId, message,true);
        wCRC16_2 = bb.getShort();

        if(!checkCRC()){
            throw new Exception("The packet has been damaged");
        }
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
        return decodedMessage;
    }

}
