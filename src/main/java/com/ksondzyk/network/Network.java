package com.ksondzyk.network;
import com.ksondzyk.entities.Packet;


import java.io.IOException;

public interface Network {
        void listen() throws IOException;
        Packet receive() throws  Exception;

        void connect() throws IOException;

        void send(Packet packet) throws IOException;

        void close() throws IOException;
    }


