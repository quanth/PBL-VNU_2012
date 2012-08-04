/*
 * $Id: Basic_profile_impl.java 270 2010-04-28 07:10:26Z itoh $
 *
 * Copyright 2009-2010 Fujitsu Limited.
 * FUJITSU CONFIDENTIAL.
 */
package rsnp.acceptor;

import static com.fujitsu.rsi.util.DetailCodeManager.BAP;
import static com.fujitsu.rsi.util.DetailCodeManager.DC;
import static com.fujitsu.rsi.util.DetailCodeManager.DC_INDEFINITE;
import static com.fujitsu.rsi.util.DetailCodeManager.MSG;
import static com.fujitsu.rsi.util.DetailCodeManager.OPEN_2MEN;

import java.util.Date;
import java.util.Hashtable;

import org.robotservices.v02.profile.common.Ret_value;

import com.fujitsu.rsi.helper.BasicProfileHelper;
import com.fujitsu.rsi.server.acceptor.base.BasicProfileBase;
import com.fujitsu.rsi.util.RESULT;

/**
 * Basic_profileのacceptor実装クラス<br>
 * BasicProfileBaseを継承しているため、不要なメソッドを実装する必要がない
 */
public class Basic_profile_impl extends BasicProfileBase {

	private SharedData sharedData;

	// 固定アカウント
	private static Hashtable<String, String> accounts;
	static {
		accounts = new Hashtable<String, String>();
		accounts.put("levin", "ae86");
		accounts.put("skyline", "bcnr32");
		accounts.put("rx7", "fd3s");
	}

	private static long createConvId(String seed) {
		return seed.hashCode();
	}

	public Basic_profile_impl(SharedData sharedData) {
		this.sharedData = sharedData;
	}

	/*
	 * (非 Javadoc)
	 *
	 * @see com.fujitsu.rsi.server.acceptor.base.BasicProfileBase#close(long)
	 */
	@Override
	public Ret_value close(long arg0) {

		Ret_value ret = new Ret_value();
		BasicProfileHelper helper = new BasicProfileHelper(ret);
		helper.setResult(RESULT.SUCCESS.getResult());
		// 正常終了時は詳細コードの値が不定
		helper.setDetailCode(DC_INDEFINITE);
		// 任意の文字列を設定
		helper.setDetail("実行結果=正常終了");

		return ret;
	}

	/*
	 * (非 Javadoc)
	 *
	 * @see
	 * com.fujitsu.rsi.server.acceptor.base.BasicProfileBase#open(java.lang.
	 * String, java.lang.String)
	 */
	@Override
	public Ret_value open(String robotid, String password) {

		Ret_value ret = new Ret_value();
		BasicProfileHelper helper = new BasicProfileHelper(ret);

		// robotidとpasswordの確認
		String robotpass = accounts.get(robotid);
		if (robotpass == null) {
			// 未登録ロボットID
			helper.setResult(RESULT.ERROR.getResult());
			helper.setDetailCode(DC(BAP, OPEN_2MEN, 30));
			helper.setDetail(MSG(30));
			return ret;
		} else {
			if (!robotpass.equals(password)) {
				// パスワードミスマッチ
				helper.setResult(RESULT.ERROR.getResult());
				helper.setDetailCode(DC(BAP, OPEN_2MEN, 20));
				helper.setDetail(MSG(20));
				return ret;
			}
		}

		// 会話IDの発行
		String seed = robotid + password + new Date().toString();
		long conv_id = createConvId(seed);

		// 共有データに設定
		sharedData.setRobot_id(robotid);
		sharedData.setConv_id(conv_id);

		helper.setResult(RESULT.SUCCESS.getResult());
		// 正常終了時は詳細コードの値が不定
		helper.setDetailCode(DC_INDEFINITE);
		helper.setConv_id(conv_id);
		return ret;
	}
}
