/*
 * $Id: ContentsManager.java 270 2010-04-28 07:10:26Z itoh $
 *
 * Copyright 2009-2010 Fujitsu Limited.
 * FUJITSU CONFIDENTIAL.
 */
package rsnp.acceptor;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;

public class ContentsManager {

	public static final String KEYNAME = "CONTENTS_MANAGER";

	private Hashtable<String, Contents> robotable = new Hashtable<String, Contents>();

	public synchronized Contents registRobot(String id) {
		Contents cont = new Contents();
		robotable.put(id, cont);
		return cont;
	}

	public synchronized void unregistRobot(String id) {
		robotable.remove(id);
	}

	public synchronized int distribute(String data) {
		Collection<Contents> coll = robotable.values();
		int ret = coll.size();
		for (Contents cont : coll) {
			synchronized (cont) {
				cont.setData(data);
				cont.notifyAll();
			}
		}
		return ret;
	}

	public synchronized String getRobotIds() {
		StringBuilder buff = new StringBuilder("");
		Enumeration<String> en = robotable.keys();
		while (en.hasMoreElements()) {
			buff.append(en.nextElement());
			buff.append(", ");
		}
		return buff.toString();
	}

	public static class Contents {
		private String data = null;

		public String getData() {
			return data;
		}

		public void setData(String data) {
			this.data = data;
		}
	}
}
