package config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
 
/**
 * Class to interact with configuration properties file.
 * @author Tony Jiang (
 * 7-7-2015
 *
 */

public class Config {
 
    private static Properties defaultProperties;
    private final static String DEFAULT_CONFIG_FILE_NAME = "defaultconfig.properties";
    
    
    public Config() {
        defaultLoad();
    }
    
    /**
     * Creates and loads the default properties
     */
    private void defaultLoad()
    {
        // load the properties from the default config file
        defaultProperties = new Properties();
        InputStream in;
        try {
            in = getClass().getResourceAsStream(DEFAULT_CONFIG_FILE_NAME);
            defaultProperties.load(in);
        } catch (FileNotFoundException e) {
            System.err.println("Default config file "
                    + DEFAULT_CONFIG_FILE_NAME + " not found!!!");
        } catch (IOException e)
        {
            System.err
                    .println("IOException reading properties from default config file...");
        }
    }
    
    public static Boolean getPropertyBoolean(String key) {
        if (defaultProperties.containsKey(key)) {
            return Boolean.valueOf(defaultProperties.getProperty(key));
        } else {
            System.err.println("Property \""  + key + "\" not found.");
            return null;
        }
    }
    
    public static int getPropertyInt(String key) {
        if (defaultProperties.containsKey(key)) {
            return Integer.parseInt(defaultProperties.getProperty(key));
        } else {
            System.err.println("Property \""  + key + "\" not found.");
            return 0;
        }
    }
}