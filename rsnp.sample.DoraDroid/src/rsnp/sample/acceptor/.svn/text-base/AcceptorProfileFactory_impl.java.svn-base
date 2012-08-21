/*
 * $Id: AcceptorProfileFactory_impl.java 271 2010-04-28 07:11:03Z itoh $
 *
 * Copyright 2009-2010 Fujitsu Limited. FUJITSU CONFIDENTIAL.
 */
package rsnp.sample.acceptor;

import org.robotservices.v02.profile.acceptor.IMultimedia_profile;
import org.robotservices.v02.profile.acceptor.IMotion_profile; 

import rsnp.sample.acceptor.Motion_profile_impl;

import com.fujitsu.rsi.client.acceptor.base.AcceptorProfileFactoryBase;

/**
 * AcceptorProfileFactoryã�®å®Ÿè£…ã‚¯ãƒ©ã‚¹
 *
 */
public class AcceptorProfileFactory_impl extends AcceptorProfileFactoryBase {

	/*
	 * (é�ž Javadoc)
	 *
	 * @seecom.fujitsu.rsi.client.acceptor.base.AcceptorProfileFactoryBase#
	 * getMultimedia_profile()
	 */
	@Override
	public IMultimedia_profile getMultimedia_profile() {

		return new Multimedia_profile_impl();
	}
	@Override
	public IMotion_profile getMotion_profile() {
		return new Motion_profile_impl();
	}
}
