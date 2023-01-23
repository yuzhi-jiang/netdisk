package com.yefeng.netdisk.front.config;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * @author 夜枫
 */
public class OathConfig {

    private static Properties props = new Properties();

    static {
        try {
            props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("oauth.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setProps(Properties prop) {
        props = prop;
    }

    public static String getValue(String key) {
        return props.getProperty(key);
    }
}
