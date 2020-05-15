package com.ksondzyk;

public class CipherXOR {

    private final static char KEY ='A';

    public static String encode(String str){


        String encr= "";


        for (int i=0; i<str.length(); i++){
            encr=encr+(char)(str.charAt(i)^KEY);
        }

        return encr;

    }

    public static String decode(String str){

        String encr= "";

        for (int i=0; i<str.length(); i++){

            encr=encr+(char)(str.charAt(i) ^KEY);
        }

        return encr;

    }

}
