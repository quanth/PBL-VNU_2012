/*
 * $Id: RSNPController.java 271 2010-04-28 07:11:03Z itoh $
 *
 * Copyright 2009-2010 Fujitsu Limited. FUJITSU CONFIDENTIAL.
 */
package rsnp.sample;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

import org.robotservices.v02.IAsyncCallBack;
import org.robotservices.v02.exception.RSiException;
import org.robotservices.v02.profile.common.Ret_value;
import org.robotservices.v02.profile.invoker.IBasic_profile;
import org.robotservices.v02.profile.invoker.IContents_profile;
import org.robotservices.v02.profile.invoker.IMotion_profile;
import org.robotservices.v02.profile.invoker.IMultimedia_profile;
import org.robotservices.v02.profile.invoker.InvokerProfileFactory;
import org.robotservices.v02.util.ConnectionInfo;

import android.os.Handler;
import android.util.Log;

import com.fujitsu.rsi.helper.BasicProfileHelper;
import com.fujitsu.rsi.helper.ContentsProfileHelper;
import com.fujitsu.rsi.util.RESULT;

public class RSNPController {
	//private static String epn = "http://localhost:8080/ServiceSample/services";
	//private static String epn = "http://10.10.16.69:8080/ServiceSample/services";
	private static String epn = "http://192.168.47.50:8080/ServiceSample/services";
	//private static String epn = "http://collam.dip.jp:8080/ServiceSample2/services";
	private static String robot_id = "levin";
	private static String password = "ae86";

	private IAsyncCallBack callback;

	private InvokerProfileFactory factory = null;
	private IBasic_profile bp = null;
	private IContents_profile cp = null;
	private IMultimedia_profile mp = null;
	private IMotion_profile mop = null; //robotSample 
	long conv_id;

	/**
	 * @param callback
	 */
	public void setEpn(String epn){
		this.epn = epn;
	}
	public String getEpn(){
		return this.epn;
	}
	public RSNPController(IAsyncCallBack callback) {
		this.callback = callback;
	}
	
	public void connect() {
		
		ConnectionInfo connectioninfo = new ConnectionInfo();
		connectioninfo.set_endpointname(epn);
		
		try {
			// obtain a factory
			System.out.print("Connecting to " + epn);
			factory = InvokerProfileFactory.newInstance(connectioninfo);
			// connection
			factory.connect();
			System.out.println(" Connected.");
			
		} catch (RSiException e) {
			e.printStackTrace();
			factory = null;
			return;
		}
		
		// RSNP connection
		try {
			IBasic_profile bp = factory.getBasic_profile();
			Ret_value ret = bp.open(robot_id, password);
		
			BasicProfileHelper helper = new BasicProfileHelper(ret);
		
			if (helper.getResult() == RESULT.SUCCESS.getResult()) {
				conv_id = helper.getConv_id(); // conversation id

				System.err.println("Authentication succeeded. conv_id:" + conv_id);

				cp = factory.getContents_profile();
				cp.distribute_contents(conv_id, "", -1, 10000, callback);
				//System.err.println("done cp");
				
				mp = factory.getMultimedia_profile();
				mp.start_profile(conv_id);
				//System.err.println("done mp");
				
				mop = factory.getMotion_profile();
				mop.start_profile(conv_id);
				//System.err.println("done mop");				
			} else 
				System.out.println("Authentication failed." + helper.getDetail());
			
		} catch (RSiException e) {
			e.printStackTrace();
		}
	}

	public void disconnect() {

		try {
			if (cp != null) {
				cp.stop_distribute_contents(conv_id, 0);
				cp = null;
			}
			if (mp != null) {
				mp.end_profile(conv_id);
			}
			if (bp != null) {
				bp.close(conv_id);
				bp = null;
			}
			if (mop != null) {//RobotSample
				mop.end_profile(conv_id);
				mop = null;
			}
		} catch (RSiException e) {
			e.printStackTrace();
		} finally {
			cp = null;
			bp = null;
		}

		try {
			if (factory != null) {
				// Ã¥Ë†â€¡Ã¦â€“Â­
				factory.disconnect();
			}
		} catch (RSiException e) {
			e.printStackTrace();
		} finally {
			factory = null;
		}
	}

	/**
	 * @return true if connected and false if not connected
	 */
	public boolean isConnected() {

		if (factory != null) {
			return factory.isConnected();
		}
		return false;
	}
}