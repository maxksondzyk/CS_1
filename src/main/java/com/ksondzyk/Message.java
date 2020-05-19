package com.ksondzyk;
import lombok.Getter;

import java.nio.ByteBuffer;

public class Message {
    private final int cType;
    private final int bUserId;
    @Getter
    private final String message;

    public static final int BYTES_WITHOUT_MESSAGE = Integer.BYTES + Integer.BYTES;
    public Message(int cType, int bUserId, String message,boolean encoded)
    {
        this.cType = cType;
        this.bUserId = bUserId;
        if(!encoded)
        {
            message = CipherMy.encode(message);
        }
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
