/*
 * $Id: AcceptorProfileFactory_impl.java 270 2010-04-28 07:10:26Z itoh $
 *
 * Copyright 2009-2010 Fujitsu Limited.
 * FUJITSU CONFIDENTIAL.
 */
package rsnp.acceptor;

import org.robotservices.v02.profile.acceptor.IBasic_profile;
import org.robotservices.v02.profile.acceptor.IContents_profile;
import org.robotservices.v02.profile.acceptor.IMotion_profile;
import org.robotservices.v02.profile.acceptor.IMultimedia_profile;

import com.fujitsu.rsi.server.acceptor.base.AcceptorProfileFactoryBase;

/**
 * AcceptorFactoryã�®å®Ÿè£…ã‚¯ãƒ©ã‚¹<br>
 * AcceptorProfileFactoryBaseã‚’ç¶™æ‰¿ã�—ã�¦ã�„ã‚‹ã�Ÿã‚�ã€�ä¸�è¦�ã�ªãƒ¡ã‚½ãƒƒãƒ‰ã‚’å®Ÿè£…ã�™ã‚‹å¿…è¦�ã�Œã�ªã�„
 */
public class AcceptorProfileFactory_impl extends AcceptorProfileFactoryBase {

	private static ThreadLocal<SharedData> local = new ThreadLocal<SharedData>() {
		@Override
		protected SharedData initialValue() {
			return new SharedData();
		}
	};

	/*
	 * (é�ž Javadoc)
	 *
	 * @seecom.fujitsu.rsi.server.acceptor.base.AcceptorProfileFactoryBase#
	 * getBasic_profile()
	 */
	@Override
	public IBasic_profile getBasic_profile() {
		return new Basic_profile_impl(local.get());
	}

	/*
	 * (é�ž Javadoc)
	 *
	 * @seecom.fujitsu.rsi.server.acceptor.base.AcceptorProfileFactoryBase#
	 * getContents_profile()
	 */
	@Override
	public IContents_profile getContents_profile() {
		return new Contents_profile_impl(local.get());
	}

	/*
	 * (é�ž Javadoc)
	 *
	 * @seecom.fujitsu.rsi.server.acceptor.base.AcceptorProfileFactoryBase#
	 * getMultimedia_profile()
	 */
	@Override
	public IMultimedia_profile getMultimedia_profile() {
		return new Multimedia_profile_impl(local.get());
	}
	
	@Override
	public IMotion_profile getMotion_profile() {
		return new Motion_profile_impl(local.get());
	}
	
}
