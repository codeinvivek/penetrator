package com.metasploit.penetrator.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.metasploit.penetrator.PenetratorConstant;
import com.metasploit.penetrator.exception.ConfigLoaderException;

//@Path("/loadPropFile")
public class PenetratorUtil {

	/*@GET
	public void loadPropFile() throws ConfigLoaderException {
		Properties prop = getPropertyFile(PenetratorConstant.TEST_PROP_FILE);
		System.out.println("Property Value: " + prop.getProperty("test"));
	}*/

	public static Properties loadConfigFile() throws ConfigLoaderException {
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = PenetratorUtil.class.getClassLoader().getResourceAsStream(PenetratorConstant.CONFIG_FILE);
			if (input == null) {
				throw new ConfigLoaderException("Unable to load property file");
			}
			prop.load(input);
			return prop;
		} catch (IOException ex) {
			throw new ConfigLoaderException("Unable to load property file", ex);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					throw new ConfigLoaderException("Unable to Close");
				}
			}
		}
	}

}