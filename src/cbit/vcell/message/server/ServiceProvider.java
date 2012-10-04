/*
 * Copyright (C) 1999-2011 University of Connecticut Health Center
 *
 * Licensed under the MIT License (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *  http://www.opensource.org/licenses/mit-license.php
 */

package cbit.vcell.message.server;
import static cbit.vcell.messaging.admin.ManageConstants.MESSAGE_TYPE_ASKPERFORMANCESTATUS_VALUE;
import static cbit.vcell.messaging.admin.ManageConstants.MESSAGE_TYPE_IAMALIVE_VALUE;
import static cbit.vcell.messaging.admin.ManageConstants.MESSAGE_TYPE_ISSERVICEALIVE_VALUE;
import static cbit.vcell.messaging.admin.ManageConstants.MESSAGE_TYPE_PROPERTY;
import static cbit.vcell.messaging.admin.ManageConstants.MESSAGE_TYPE_REFRESHSERVERMANAGER_VALUE;
import static cbit.vcell.messaging.admin.ManageConstants.MESSAGE_TYPE_REPLYPERFORMANCESTATUS_VALUE;
import static cbit.vcell.messaging.admin.ManageConstants.MESSAGE_TYPE_STOPSERVICE_VALUE;
import static cbit.vcell.messaging.admin.ManageConstants.SERVICE_ID_PROPERTY;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import javax.jms.JMSException;

import org.vcell.util.SessionLog;

import cbit.vcell.message.VCMessage;
import cbit.vcell.message.VCMessageSelector;
import cbit.vcell.message.VCMessageSession;
import cbit.vcell.message.VCMessagingException;
import cbit.vcell.message.VCMessagingService;
import cbit.vcell.message.VCTopicConsumer;
import cbit.vcell.message.VCTopicConsumer.TopicListener;
import cbit.vcell.message.VCellTopic;
import cbit.vcell.messaging.admin.ServiceInstanceStatus;

/**
 * Insert the type's description here.
 * Creation date: (7/2/2003 3:00:11 PM)
 * @author: Fei Gao
 */
public abstract class ServiceProvider {
	protected ServiceInstanceStatus serviceInstanceStatus = null;
	protected VCMessagingService vcMessagingService = null;
	protected VCTopicConsumer vcTopicConsumer = null;
	protected SessionLog log = null;

/**
 * JmsMessaging constructor comment.
 */
protected ServiceProvider(VCMessagingService vcMessageService, ServiceInstanceStatus serviceInstanceStatus, SessionLog log) {
	this.log = log;
	this.vcMessagingService = vcMessageService;
	this.serviceInstanceStatus = serviceInstanceStatus;
}


/**
 * Insert the method's description here.
 * Creation date: (12/17/2003 10:46:29 AM)
 */
public void closeTopicConsumer() {
	if (vcTopicConsumer != null) {
		vcMessagingService.removeMessageConsumer(vcTopicConsumer);
	}
}


/**
 * Insert the method's description here.
 * Creation date: (12/3/2003 10:28:36 AM)
 * @return java.lang.String
 */
private final String getDaemonControlFilter() {
	return MESSAGE_TYPE_PROPERTY + " NOT IN " 
		+ "('" + MESSAGE_TYPE_REPLYPERFORMANCESTATUS_VALUE + "'"
		+ ",'" + MESSAGE_TYPE_REFRESHSERVERMANAGER_VALUE + "'"
		+ ",'" + MESSAGE_TYPE_IAMALIVE_VALUE + "'"
		+ ")";		
}

/**
 * Insert the method's description here.
 * Creation date: (11/19/2001 5:29:47 PM)
 */
public void initControlTopicListener() {
	TopicListener listener = new TopicListener() {

		public void onTopicMessage(VCMessage message, VCMessageSession session) {
			try {
				String msgType = message.getStringProperty(MESSAGE_TYPE_PROPERTY);
				String serviceID = null;
				
				if (msgType == null) {
					return;
				}
				
				if (msgType.equals(MESSAGE_TYPE_ISSERVICEALIVE_VALUE)) {			
					VCMessage reply = session.createObjectMessage(ServiceProvider.this.serviceInstanceStatus);
					reply.setStringProperty(MESSAGE_TYPE_PROPERTY, MESSAGE_TYPE_IAMALIVE_VALUE);
					reply.setStringProperty(SERVICE_ID_PROPERTY, serviceInstanceStatus.getID());
					log.print("sending reply [" + reply.toString() + "]");
					if (message.getReplyTo() != null) {
						reply.setCorrelationID(message.getMessageID());
						session.sendTopicMessage((VCellTopic)message.getReplyTo(), reply);
					} else {
						session.sendTopicMessage(VCellTopic.DaemonControlTopic, reply);
					}
				} else if (msgType.equals(MESSAGE_TYPE_ASKPERFORMANCESTATUS_VALUE)) {				
					VCMessage reply = session.createObjectMessage(serviceInstanceStatus);
					reply.setStringProperty(MESSAGE_TYPE_PROPERTY, MESSAGE_TYPE_REPLYPERFORMANCESTATUS_VALUE);
					reply.setStringProperty(SERVICE_ID_PROPERTY, serviceInstanceStatus.getID());
					session.sendTopicMessage(VCellTopic.DaemonControlTopic, reply);			
					log.print("sending reply [" + reply.toString() + "]");
					
				} else if (msgType.equals(MESSAGE_TYPE_STOPSERVICE_VALUE)) {
					serviceID = message.getStringProperty(SERVICE_ID_PROPERTY);
					if (serviceID != null && serviceID.equalsIgnoreCase(serviceInstanceStatus.getID()))  {
						stopService();
					}
				}
			} catch (Exception ex) {
				log.exception(ex);
			}	
		}

	};
	VCMessageSelector selector = vcMessagingService.createSelector(getDaemonControlFilter());
	String threadName = "Daemon Control Topic Consumer";
	vcTopicConsumer = new VCTopicConsumer(VCellTopic.DaemonControlTopic, listener, selector, threadName);
	vcMessagingService.addMessageConsumer(vcTopicConsumer);
}


/**
 * Insert the method's description here.
 * Creation date: (12/10/2003 8:42:49 AM)
 */
public void stopService() {
	try {
		Thread t = new Thread() {
			public void run() {
				try {
					vcMessagingService.closeAll();
				} catch (VCMessagingException e) {
					e.printStackTrace();
				}
			}
		};
		t.start();
		t.join(3000);	
	} catch (InterruptedException ex) {
	} finally {
		System.exit(0);
	}
}

protected static void initLog(ServiceInstanceStatus serviceInstanceStatus, String logDirectory) throws FileNotFoundException {
	if (serviceInstanceStatus == null) {
		throw new RuntimeException("initLog: serviceInstanceStatus can't be null");		
	}
	if (logDirectory != null) {
		File logdir = new File(logDirectory);
		if (!logdir.exists()) {
			throw new RuntimeException("Log directory doesn't exist");
		}
			
		// log file name:
		// hostname_A_Data_0.log : alpha first data on hostname
		// hostname_B_Db_0.log : beta first database on hostname
		// hostname_R_Export_0.log : rel first export on hostname
		File logfile = new File(logdir, serviceInstanceStatus.getServerID()+"_"+serviceInstanceStatus.getOrdinal() + ".log");
		java.io.PrintStream ps = new PrintStream(new FileOutputStream(logfile), true); // don't append, auto flush
		System.out.println("log file is " + logfile.getAbsolutePath());
		System.setOut(ps);
		System.setErr(ps);
	}	
}




}
