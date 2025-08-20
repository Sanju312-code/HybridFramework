package com.Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyUtil {

	private static final String FILE_PATH = "./src/test/resources/config.properties";
	private static Properties prop;

	static {
		try (FileInputStream fis = new FileInputStream(FILE_PATH)) {
			prop = new Properties();
			prop.load(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getProperty(String key) {
		return prop.getProperty(key);
	}

}
