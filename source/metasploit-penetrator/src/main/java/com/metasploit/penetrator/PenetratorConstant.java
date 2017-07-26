package com.metasploit.penetrator;

public class PenetratorConstant {
		
	public final static String CONFIG_FILE = "metasploitserver-config.properties";
	public final static String TEST_XML_FILE = "/test.xml";
	public final static int PRETTY_PRINT_INDENT_FACTOR = 4;
	public final static String JAVA_EXE = "msfconsole";
	public final static String DIRECTORY_PATH = "directory_path";
	public final static String FILE_PATH = "file_path";
	public final static String R_HOST = "rhost";
	public final static String S_HOST = "shost";
	public final static String SCAN_XML_FILE = "scan_xml_file";
	public final static String TARGETS_XML_FILE = "targets_xml_file";
	
	public final static String TARGET_LIST_COMMAND = "nmap --min-hostgroup 96 -p 1-65535 -n -T4 -A -v --open --stylesheet=nmap.xsl -oA targets-list ";
	
}
