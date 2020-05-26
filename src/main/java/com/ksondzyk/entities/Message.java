package com.ksondzyk.entities;

import com.ksondzyk.CipherMy;
import lombok.Getter;

import java.nio.ByteBuffer;

public class Message {
    @Getter
    private final int cType;
    @Getter
    private final int bUserId;
    @Getter
    private final String message;

    public static final int BYTES_WITHOUT_MESSAGE = Integer.BYTES + Integer.BYTES;

    public Message(int cType, int bUserId, String message)
    {
        this.cType = cType;
        this.bUserId = bUserId;

        this.message = message;
    }

    public Integer getMessageBytes()
    {
        return message.length();
    }
    public int getMessageBytesLength()
    {
        return BYTES_WITHOUT_MESSAGE + getMessageBytes();
    }

    public byte[] toBytes()
    {
        ByteBuffer temp = ByteBuffer.allocate(getMessageBytesLength())
        .putInt(cType)
        .putInt(bUserId)
        .put(message.getBytes());

        return temp.array();
    }
}
