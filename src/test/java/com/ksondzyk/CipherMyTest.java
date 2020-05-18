package com.ksondzyk;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.crypto.Cipher;

import static org.junit.Assert.*;

public class CipherMyTest {

    String testString;
    @Before
    public void setUp(){
        testString="Successful test";

    }


    @Test
    public void encode() {
        String encoded = CipherMy.encode(testString);
        Assert.assertNotEquals(testString, encoded);
        Assert.assertEquals(testString, CipherMy.decode(encoded));
    }

    @Test
    public void decode() {
        String encoded = CipherMy.encode(testString);
        Assert.assertEquals(testString, CipherMy.decode(encoded));
    }
}