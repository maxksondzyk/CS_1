package com.ksondzyk.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class NetworkProperties {
    static Properties appProps;
    static String appConfigPath = Thread.currentThread().getContextClassLoader().getResource("network.properties").getPath();

    public static String getProperty(String key) {
        if (appProps == null) {
            appProps = new Properties();
            try {
                appProps.load(new FileInputStream(appConfigPath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return appProps.getProperty(key);
    }
}