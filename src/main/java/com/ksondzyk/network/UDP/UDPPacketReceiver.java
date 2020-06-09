package com.ksondzyk.network.UDP;

import com.ksondzyk.entities.Message;
import com.ksondzyk.entities.Packet;
import com.ksondzyk.exceptions.PacketDamagedException;
import com.ksondzyk.utilities.PacketGenerator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class UDPPacketReceiver {
    public static Packet receive(DatagramSocket serverSocket) throws IOException {
        DatagramPacket receivedPacket;
        byte[] receivedData;

        InetAddress IPAddress=null;
        int port=0;

        try {
            receivedData = new byte[Packet.packetMaxSize];
            receivedPacket = new DatagramPacket(receivedData, receivedData.length);

            serverSocket.receive(receivedPacket);

            IPAddress = receivedPacket.getAddress();
            port = receivedPacket.getPort();

            ByteBuffer byteBuffer = ByteBuffer.wrap(receivedData);
            Integer wLen = byteBuffer.getInt(Packet.packetPartFirstLengthWithoutwLen);

            byte[] fullPacket = new byte[Packet.packetPartFirstLength + Message.BYTES_WITHOUT_MESSAGE + wLen];
            byteBuffer.get(fullPacket);

            Packet result = new Packet(fullPacket,"UDP");

            if(result.getBSrc()==(byte)1) {
                Packet packet = PacketGenerator.allOkPacket(result.getBPktId());
                DatagramPacket sendPacket = new DatagramPacket(packet.getData(), packet.getData().length, IPAddress, port);

                serverSocket.send(sendPacket);
            }
            return result;

        } catch (PacketDamagedException e) {
            e.printStackTrace();
            Packet packet = PacketGenerator.errorPacket();
            DatagramPacket sendPacket = new DatagramPacket(packet.getData(), packet.getData().length, IPAddress, port);
            serverSocket.send(sendPacket);

        }
        return PacketGenerator.errorPacket();
    }
}
