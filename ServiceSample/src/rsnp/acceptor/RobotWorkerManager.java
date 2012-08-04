/*
 * $Id: RobotWorkerManager.java 270 2010-04-28 07:10:26Z itoh $
 *
 * Copyright 2009-2010 Fujitsu Limited.
 * FUJITSU CONFIDENTIAL.
 */
package rsnp.acceptor;

import java.util.Enumeration;
import java.util.Hashtable;

import org.robotservices.v02.profile.acceptor.IWorkerTool;

public class RobotWorkerManager {
	private static RobotWorkerManager own = new RobotWorkerManager();

	private Hashtable<String, IWorkerTool> robotable = new Hashtable<String, IWorkerTool>();

	private RobotWorkerManager() {
	}

	public static RobotWorkerManager getInstance() {
		return own;
	}

	public void registRobot(String id, IWorkerTool workertool) {
		robotable.put(id, workertool);
	}

	public void unregistRobot(String id) {
		robotable.remove(id);
	}

	public IWorkerTool getWorkerTool(String id) {
		return robotable.get(id);
	}

	public Enumeration<String> getRobotIds() {
		return robotable.keys();
	}

}
