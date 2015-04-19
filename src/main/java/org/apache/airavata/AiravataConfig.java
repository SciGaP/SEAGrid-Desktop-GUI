package org.apache.airavata;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by dimuthuupeksha on 4/19/15.
 */
public class AiravataConfig {

    public static String getProperty(String key){
        Properties prop = new Properties();
        try {
            prop.load(AiravataConfig.class.getClassLoader().getResourceAsStream("airavata.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return prop.getProperty(key);
    }
}
