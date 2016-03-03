package com.shopfactory.common;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class Util {
	private Util() {}
	
	public static Properties readPropertiesFromResource(String resource) {
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(Util.class.getResourceAsStream(resource)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
