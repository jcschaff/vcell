/*
 * Copyright (C) 1999-2011 University of Connecticut Health Center
 *
 * Licensed under the MIT License (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *  http://www.opensource.org/licenses/mit-license.php
 */

package cbit.vcell.client.test;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.ArrayUtils;
import org.jdom.Document;
import org.vcell.util.BeanUtils;
import org.vcell.util.PropertyLoader;
import org.vcell.util.document.UserLoginInfo;
import org.vcell.util.document.VCDocument;
import org.vcell.util.document.VCellSoftwareVersion;
import org.vcell.util.logging.ConsoleCapture;
import org.vcell.util.logging.Logging;

import cbit.util.xml.VCLogger;
import cbit.util.xml.XmlUtil;
import cbit.vcell.client.TranslationLogger;
import cbit.vcell.client.VCellClient;
import cbit.vcell.client.pyvcellproxy.VCellProxyServer;
import cbit.vcell.client.server.ClientServerInfo;
import cbit.vcell.mongodb.VCMongoMessage;
import cbit.vcell.mongodb.VCMongoMessage.ServiceName;
import cbit.vcell.resource.ResourceUtil;
import cbit.vcell.xml.XmlHelper;
/**
 * Insert the type's description here.
 * Creation date: (5/3/2004 12:02:01 PM)
 * @author: Ion Moraru
 */
public class VCellClientTest {
	private static VCellClient vcellClient = null;
	
	public static VCellClient getVCellClient() {
		return vcellClient;
	}
	
	
/**
 * Starts the application.
 * @param args an array of command-line arguments
 */
public static void main(java.lang.String[] args) {
	final boolean  IS_DEBUG = ManagementFactory.getRuntimeMXBean().getInputArguments().toString().indexOf("-agentlib:jdwp") > 0;
	if (!IS_DEBUG) {
		String siteName = VCellSoftwareVersion.fromSystemProperty().getSite().name().toLowerCase();
		ConsoleCapture.getInstance().captureStandardOutAndError(new File(ResourceUtil.getLogDir(),"vcellrun_"+siteName+".log"));
	}
	Logging.init();
	BeanUtils.setDebug(IS_DEBUG);
	if(args != null &&  args.length >= 1 && args[0].equals("-console")){//remove install4j parameter
		List<String> newArgs = new ArrayList<String>();
		newArgs.addAll(Arrays.asList(args));
		newArgs.remove(0);
		args = newArgs.toArray(new String[0]);
	}
	StringBuffer stringBuffer = new StringBuffer();
	for (int i = 0; i < args.length; i++){
		stringBuffer.append("arg"+i+"=\""+args[i]+"\" ");
	}
	System.out.println("starting with arguments ["+stringBuffer+"]");
	System.out.println("Running under Java major version: ONE point "+ ResourceUtil.getJavaVersion().toString()+".  Specifically: Java "+(System.getProperty("java.version"))+
			", published by "+(System.getProperty("java.vendor"))+", on the "+ (System.getProperty("os.arch"))+" architecture running version "+(System.getProperty("os.version"))+
			" of the "+(System.getProperty("os.name"))+" operating system");
	
	ClientServerInfo csInfo = null;
	String hoststr = System.getProperty(PropertyLoader.vcellServerHost);
	String[] hosts = null;
	if (hoststr != null) {
		StringTokenizer st = new StringTokenizer(hoststr," ,;");
		if (st.countTokens() >= 1) {
			hosts = new String[st.countTokens()];
			int count = 0;
			while (st.hasMoreTokens()) {
				hosts[count ++] = st.nextToken();
			}
		}
	}
	if (hosts == null) {
		hosts = new String[1];
	}
	String user = null;
	String password = null;
	VCDocument initialDocument = null;
	if (args.length == 3) {
		hosts[0] = args[0];
		user = args[1];
		password = args[2];
	}else if (args.length==0){
		// this is ok
	}else if (args.length==1){
		//Check if arg is drag-n-drop file or a 'hostname'
		try{
			//drag and drop file on install4j VCell launcher will pass filepath as single arg to VCell
			File openThisVCellFile = new File(args[0]);
			if(openThisVCellFile.exists() && openThisVCellFile.isFile()){
				initialDocument = startupWithOpen(args[0]);
			}
		}catch(Exception e){
			e.printStackTrace();
			//continue to hostname check
		}
		//If startup file not exist assume arg is a hostname
		if(initialDocument == null){
			hosts[0] = args[0];
		}
		//If install4j drag-n-drop, hosts[0] stays null and host is assumed to be loaded from a client property
		
	}else if (args.length==2 && args[0].equals("-open")){
//		hosts[0] = "-local";
		initialDocument = startupWithOpen(args[1]);
	}else{
		System.out.println("usage: VCellClientTest ( ((-local|host[:port]) [userid password]) | ([-open] filename) )");
		System.exit(1);
	}
	if (hosts[0]!=null && hosts[0].equalsIgnoreCase("-local")) {
		csInfo = ClientServerInfo.createLocalServerInfo(user, (password==null || password.length()==0?null:new UserLoginInfo.DigestedPassword(password)));
	} else {
		csInfo = ClientServerInfo.createRemoteServerInfo(hosts, user,(password==null || password.length()==0?null:new UserLoginInfo.DigestedPassword(password)));
	}
	try {
		String propertyFile = PropertyLoader.getProperty(PropertyLoader.propertyFileProperty, "");
		if (propertyFile.length()>0){
			PropertyLoader.loadProperties(ArrayUtils.addAll(REQUIRED_CLIENT_PROPERTIES,REQUIRED_LOCAL_PROPERTIES) );
			try {
				VCMongoMessage.enabled = true;
				VCMongoMessage.serviceStartup(ServiceName.client,null,null);
				PropertyLoader.sendErrorsToMongo();
			}catch (Exception e){
				System.out.println("failed to start Mongo logging");
			}
		}else{
			PropertyLoader.loadProperties(REQUIRED_CLIENT_PROPERTIES);
			VCMongoMessage.enabled = false;
		}

		//call in main thread, since it's quick and not necessarily thread safe
		ResourceUtil.setNativeLibraryDirectory();
		vcellClient = VCellClient.startClient(initialDocument, csInfo);

		VCellProxyServer.startVCellVisitDataServerThread(vcellClient);

		
		//starting loading libraries
		new LibraryLoaderThread(true).start( );
	} catch (Throwable exception) {
		BeanUtils.sendRemoteLogMessage(csInfo.getUserLoginInfo(),csInfo.toString()+"\nvcell startup failed\n\n" + exception.getMessage());
		JOptionPane.showMessageDialog(null, exception.getMessage(), "Fatal Error",JOptionPane.OK_OPTION);
		System.err.println("Exception occurred in main() of VCellApplication");
		exception.printStackTrace(System.out);
	}
}


private static VCDocument startupWithOpen(String fileName) {
	VCDocument initialDocument = null;
	try {
		Document xmlDoc = XmlUtil.readXML(new File(fileName));
		String vcmlString = XmlUtil.xmlToString(xmlDoc, false);
		java.awt.Component parent = null;
		VCLogger vcLogger = new TranslationLogger(parent);
		initialDocument = XmlHelper.XMLToDocument(vcLogger,vcmlString);
	}catch (Exception e){
		e.printStackTrace(System.out);
		JOptionPane.showMessageDialog(null,e.getMessage(),"vcell startup error",JOptionPane.ERROR_MESSAGE);
	}
	return initialDocument;
}

/**
 * array of properties required for correct operation
 */
private static final String REQUIRED_CLIENT_PROPERTIES[] = {
	PropertyLoader.installationRoot,
	PropertyLoader.vcellSoftwareVersion,
};

/**
 * array of properties required for correct local operation
 */
private static final String REQUIRED_LOCAL_PROPERTIES[] = {
	PropertyLoader.primarySimDataDirProperty,
	PropertyLoader.secondarySimDataDirProperty,
	PropertyLoader.dbPassword,
	PropertyLoader.dbUserid,
	PropertyLoader.dbDriverName,
	PropertyLoader.dbConnectURL,
	PropertyLoader.vcellServerIDProperty,
	PropertyLoader.mongodbDatabase,
	PropertyLoader.mongodbHost,
	PropertyLoader.mongodbLoggingCollection,
	PropertyLoader.mongodbPort
	
};
}