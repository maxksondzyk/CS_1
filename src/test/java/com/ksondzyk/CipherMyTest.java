package com.ksondzyk;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.crypto.Cipher;

import static org.junit.Assert.*;

public class CipherMyTest {

    String s;
    @Before
    public void setUp(){
        s="Successful test";

    }


    @Test
    public void encode() {
        String encoded = CipherMy.encode(s);
        Assert.assertNotEquals(s, encoded);
        Assert.assertEquals(s, CipherMy.decode(encoded));
    }

    @Test
    public void decode() {
        Assert.assertEquals(s, CipherMy.decode( CipherMy.encode(s)));
    }
}