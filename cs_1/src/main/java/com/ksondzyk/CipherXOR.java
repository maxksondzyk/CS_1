package com.ksondzyk;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class CipherXOR {

    private final static char KEY ='A';

    private static String deencode(String string) {
        String outputString = "";
        int len = string.length();

        for (int i = 0; i < len; i++)
            outputString = outputString + (char) (string.charAt(i) ^ KEY);

        return outputString;
    }

    public static String encode(final String string) {
        return deencode(string);
    }

    public static String decode(final String string) {
        return deencode(string);
    }
}

