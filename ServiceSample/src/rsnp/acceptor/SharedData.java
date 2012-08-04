/*
 * $Id: SharedData.java 270 2010-04-28 07:10:26Z itoh $
 *
 * Copyright 2009-2010 Fujitsu Limited.
 * FUJITSU CONFIDENTIAL.
 */
package rsnp.acceptor;

public class SharedData {

	/** ロボットID */
	private String robot_id;

	/** 会話ID */
	private Long conv_id;

	/**
	 * ロボットIDを取得します。
	 * 
	 * @return robot_id ロボットID
	 */
	public String getRobot_id() {
		return robot_id;
	}

	/**
	 * ロボットIDを設定します。
	 * 
	 * @param robot_id
	 *            ロボットID
	 */
	public void setRobot_id(String robot_id) {
		this.robot_id = robot_id;
	}

	/**
	 * 会話IDを取得します。
	 * 
	 * @return conv_id 会話ID
	 */
	public Long getConv_id() {
		return conv_id;
	}

	/**
	 * 会話IDを設定します。
	 * 
	 * @param conv_id
	 *            会話ID
	 */
	public void setConv_id(Long conv_id) {
		this.conv_id = conv_id;
	}
}
