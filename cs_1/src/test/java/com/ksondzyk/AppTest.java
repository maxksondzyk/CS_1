package com.ksondzyk;

import junit.framework.TestCase;
import org.junit.Assert;

import java.io.IOException;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{

private String INPUT = "test";
    public void testCheckPacket()
    {
        try {
            Packet p = new Packet((byte)10,100,7731265,INPUT);
            PacketReceiver pr = new PacketReceiver(p.getData());
            Assert.assertTrue(pr.checkCRC());
            Assert.assertEquals(INPUT, pr.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
