package cn.edu.cuit.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

class ConfigHelper {
    static String getConfig(String item) throws IOException {
        Properties properties = new Properties();
        //InputStream inputStream = ConfigHelper.class.getResourceAsStream("/GetConfig.properties.properties");
        BufferedReader bufferedReader = new BufferedReader(new FileReader("./tools/config.properties"));
        properties.load(bufferedReader);

        return properties.getProperty(item);
    }
}
