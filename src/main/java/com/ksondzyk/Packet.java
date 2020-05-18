package com.ksondzyk;
import com.github.snksoft.crc.CRC;
import com.google.common.primitives.UnsignedLong;
import lombok.Getter;

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

    public Packet(byte bSrc, Message bMsq) throws IOException {
        this.bSrc = bSrc;
        this.bMsq = bMsq;
        bPktId = bPktId.plus(UnsignedLong.ONE);
        data = fill();
    }

    @Getter
    private byte[] data;

    private byte[] fill() {

        byte[] message = this.bMsq.toBytes();

        wLen = message.length;

        Integer packetFirstPartLength = bMagic.BYTES + bSrc.BYTES + Long.BYTES + wLen.BYTES;
        byte[] packetFirstPart = ByteBuffer.allocate(packetFirstPartLength)
                                        .put(bMagic)
                                        .put(bSrc)
                                        .putLong(bPktId.longValue())
                                        .putInt(wLen).array();

        wCRC16_1 = (short)CRC.calculateCRC(CRC.Parameters.CRC16,packetFirstPart);

        Integer packetPartSecondLength = message.length;
        byte[] packetPartSecond = ByteBuffer.allocate(packetPartSecondLength)
                .put(message).array();

        wCRC16_2 = (short)CRC.calculateCRC(CRC.Parameters.CRC16,packetPartSecond);


        Integer packetLength = packetFirstPartLength + wCRC16_1.BYTES + packetPartSecondLength + wCRC16_2.BYTES;

        return ByteBuffer.allocate(packetLength).put(packetFirstPart).putShort(wCRC16_1).put(packetPartSecond).putShort(wCRC16_2).array();
}




    public Packet(byte[] packet) throws Exception {
        ByteBuffer bb = ByteBuffer.wrap(packet);
        Byte expectedBMagic = bb.get();
        if (!expectedBMagic.equals(bMagic))
            throw new Exception("Unexpected bMagic");
        bSrc = bb.get();
        bPktId = UnsignedLong.fromLongBits(bb.getLong());
        wLen = bb.getInt();
        wCRC16_1 = bb.getShort();
        byte[] messageBody = new byte[wLen-8];
        bMsq = new Message(bb.getInt(), bb.getInt(), String.valueOf(bb.get(messageBody)));
        wCRC16_2 = bb.getShort();
        data = fill();
    }

//    public boolean checkCRC() {
//    byte[] firstCRC = Arrays.copyOfRange(data,Packet.B_MAGIC_OFFSET,Packet.W_CRC_16_OFFSET);
//        return(((short)CRC.calculateCRC(CRC.Parameters.CRC16,Arrays.copyOfRange(data,Packet.B_MAGIC_OFFSET,Packet.W_CRC_16_OFFSET))== ByteBuffer.wrap(Arrays.copyOfRange(packet,Packet.W_CRC_16_OFFSET,Packet.B_MSQ_OFFSET)).getShort())&&
//                ((short)CRC.calculateCRC(CRC.Parameters.CRC16,Arrays.copyOfRange(data,Packet.B_MSQ_OFFSET,Packet.B_MSQ_OFFSET +wLength))== ByteBuffer.wrap(Arrays.copyOfRange(packet,Packet.B_MSQ_OFFSET +wLength,Packet.B_MSQ_OFFSET +wLength+(Packet.B_MSQ_OFFSET -Packet.W_CRC_16_OFFSET))).getShort()));
//    }
//    public String getMessage(){
//        byte[] message = Arrays.copyOfRange(packet,Packet.B_MSQ_OFFSET +Message.MESSAGE_OFFSET +12,packet.length-2-(Packet.B_MSQ_OFFSET - Packet.W_CRC_16_OFFSET));
//        String msg = new String(message);
//        msg = CipherMy.decode(msg);
//        return msg;
//    }
}
