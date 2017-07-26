package com.metasploit.penetrator.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import com.metasploit.penetrator.PenetratorConstant;
import com.metasploit.penetrator.exception.ConfigLoaderException;
import com.metasploit.penetrator.exception.TransformationException;
import com.metasploit.penetrator.util.PenetratorUtil;

@Path("/getAllTargets")
public class TargetDiscoveryService {

	private static Properties config;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllTargets()
			throws ConfigLoaderException, TransformationException, IOException, InterruptedException {
		return getTargets();
	}

	private String getTargets() {
		List<String> command = new ArrayList<>();
		Process process = null;
		BufferedReader bufReader = null;
		StringBuilder sb = null;
		String temp;
		command.add(PenetratorConstant.JAVA_EXE.toString());
		try {
			config = PenetratorUtil.loadConfigFile();
		} catch (ConfigLoaderException e) {
			return new JSONObject().put("error", "Unable to initialize configs " + e.getMessage()).toString();
		}

		ProcessBuilder pb = new ProcessBuilder(command);
		pb.directory(new File(config.getProperty(PenetratorConstant.DIRECTORY_PATH)));
		pb.redirectOutput(new File(config.getProperty(PenetratorConstant.FILE_PATH)));

		try {
			process = pb.start();
			PrintStream commandIn = new PrintStream(process.getOutputStream());
			commandIn.println(PenetratorConstant.TARGET_LIST_COMMAND + config.getProperty(PenetratorConstant.R_HOST));
			commandIn.flush();

			// give some time to the sub process to finish writing its output
			Thread.sleep(25500);
		} catch (IOException | InterruptedException e) {
			return new JSONObject().put("error", "Unable to execute command thread " + e.getMessage()).toString();
		} finally {
			process.destroy();
		}
		try {
			bufReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(config.getProperty(PenetratorConstant.DIRECTORY_PATH)
							+ config.getProperty(PenetratorConstant.TARGETS_XML_FILE))));
			sb = new StringBuilder();
			String line = bufReader.readLine();
			while (line != null) {
				sb.append(line).append("\n");
				line = bufReader.readLine();
			}
			temp = sb.toString();
		} catch (IOException e) {
			return new JSONObject().put("error", "Unable to read the details document " + e.getMessage()).toString();
		} finally {
			try {
				bufReader.close();
			} catch (IOException e) {
				// Ignored for now
			}
		}
		return new JSONObject().put("targets", new JSONArray().put(XML.toJSONObject(temp).getJSONObject("nmaprun")
				.getJSONObject("host").getJSONObject("address").get("addr")).toString()).toString();
	}

}
