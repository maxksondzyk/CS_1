package com.ksondzyk;

import com.github.snksoft.crc.CRC;
import com.ksondzyk.entities.Packet;

import com.ksondzyk.exceptions.PacketDamagedException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;


public class PacketReceiver {


   public Packet receive(InputStream serverInputStream) throws IOException, PacketDamagedException {
       byte buffer[] = new byte[1024];
       ByteArrayOutputStream packetBytes = new ByteArrayOutputStream();
       try {
           synchronized (PacketReceiver.class) {
               if (serverInputStream.available() <= 16)
                   throw new PacketDamagedException("The packet is not full");
               }
               while (serverInputStream.read() != Packet.bMagic) ;


               ByteBuffer byteBuffer;

               packetBytes.write(Packet.bMagic);

                   serverInputStream.read(buffer, 0, 1 + 8);

               packetBytes.write(buffer, 0, 1 + 8); //wrote first part

               int wLen;
                   serverInputStream.read(buffer, 0, Integer.BYTES);
               byteBuffer = ByteBuffer.allocate(Integer.BYTES);
               byteBuffer.put(buffer, 0, Integer.BYTES);
               byteBuffer.rewind();
               wLen = byteBuffer.getInt();

               packetBytes.write(buffer, 0, Integer.BYTES);

               if (serverInputStream.available() <= 4+wLen)
                   throw new PacketDamagedException("The packet is not full");

               serverInputStream.read(buffer, 0, Short.BYTES);

               byteBuffer = ByteBuffer.allocate(Short.BYTES);
               byteBuffer.put(buffer, 0, Short.BYTES);

               byteBuffer.rewind();
               Short crc16_1_real = byteBuffer.getShort();
               Short crc16_1_test = (short) CRC.calculateCRC(CRC.Parameters.CRC16, packetBytes.toByteArray());

               if (!crc16_1_real.equals(crc16_1_test))
                   throw new PacketDamagedException(crc16_1_real, crc16_1_test);
               packetBytes.write(buffer, 0, Short.BYTES);
                   serverInputStream.read(buffer, 0, wLen);
               packetBytes.write(buffer, 0, wLen);

               byteBuffer = ByteBuffer.allocate(wLen);
               byteBuffer.put(buffer, 0, wLen);

               Short crc16_2_test = (short) CRC.calculateCRC(CRC.Parameters.CRC16, byteBuffer.array());
                   serverInputStream.read(buffer, 0, Short.BYTES);
               byteBuffer = ByteBuffer.allocate(Short.BYTES);
               byteBuffer.put(buffer, 0, Short.BYTES);
               byteBuffer.rewind();

               Short crc16_2_real = byteBuffer.getShort();

               if (!crc16_2_real.equals(crc16_2_test))
                   throw new PacketDamagedException(crc16_2_real, crc16_2_test);
               packetBytes.write(buffer, 0, Short.BYTES);
           }
       catch (PacketDamagedException e){
           System.err.println("The packet has not been fuly sent");
           return (PacketGenerator.newPacket(1, "END"));
       }
           Packet packet = new Packet(packetBytes.toByteArray());

           return packet;

   }

}


