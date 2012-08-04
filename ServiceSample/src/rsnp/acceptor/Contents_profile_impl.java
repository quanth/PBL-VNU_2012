/*
 * $Id: Contents_profile_impl.java 270 2010-04-28 07:10:26Z itoh $
 *
 * Copyright 2009-2010 Fujitsu Limited.
 * FUJITSU CONFIDENTIAL.
 */
package rsnp.acceptor;

import static com.fujitsu.rsi.util.DetailCodeManager.COP;
import static com.fujitsu.rsi.util.DetailCodeManager.DC;
import static com.fujitsu.rsi.util.DetailCodeManager.DC_INDEFINITE;
import static com.fujitsu.rsi.util.DetailCodeManager.DISTRIBUTE_CONTENTS;
import static com.fujitsu.rsi.util.DetailCodeManager.MSG;
import static com.fujitsu.rsi.util.DetailCodeManager.STOP_DISTRIBUTE_CONTENTS;

import org.robotservices.v02.exception.RSiException;
import org.robotservices.v02.profile.acceptor.IDistributionTool;
import org.robotservices.v02.profile.acceptor.IDistributor;
import org.robotservices.v02.profile.common.Ret_value;

import rsnp.acceptor.ContentsManager.Contents;


import com.fujitsu.rsi.helper.ContentsProfileHelper;
import com.fujitsu.rsi.server.ApplicationContext;
import com.fujitsu.rsi.server.acceptor.base.ContentsProfileBase;
import com.fujitsu.rsi.util.RESULT;

/**
 * Contents_profileのacceptor実装クラス<br>
 * ContentsProfileBaseを継承しているため、不要なメソッドを実装する必要がない
 */
public class Contents_profile_impl extends ContentsProfileBase {

	private SharedData sharedData;
	private ContentsManager contentsman;

	public Contents_profile_impl(SharedData sharedData) {
		this.sharedData = sharedData;

		// コンテンツ管理をアプリケーションコンテキストに登録
		ApplicationContext appCon = ApplicationContext.getCurrentContext();
		contentsman = (ContentsManager) appCon
				.getAttribute(ContentsManager.KEYNAME);
		if (contentsman == null) {
			contentsman = new ContentsManager();
			appCon.setAttribute(ContentsManager.KEYNAME, contentsman);
		}
	}

	/*
	 * (非 Javadoc)
	 *
	 * @see
	 * com.fujitsu.rsi.server.acceptor.base.ContentsProfileBase#distribute_contents
	 * (long, java.lang.String, int, int,
	 * org.robotservices.v02.profile.acceptor.IDistributionTool)
	 */
	@Override
	public Ret_value distribute_contents(long conv_id, String requirement_term,
			int contents_num, int span, IDistributionTool tools) {

		Ret_value ret = new Ret_value();
		ContentsProfileHelper helper = new ContentsProfileHelper(ret);
		// SharedDataで保持している会話IDと比較
		Long id = sharedData.getConv_id();
		if (id == null || id != conv_id) {
			// 認証エラー
			helper.setResult(RESULT.ERROR.getResult());
			helper.setDetailCode(DC(COP, DISTRIBUTE_CONTENTS, 160));
			helper.setDetail(MSG(160));
			return ret;
		}

		// コンテンツ管理からコンテンツを取得
		final Contents contents = contentsman.registRobot(sharedData
				.getRobot_id());

		// 配信クラスを作成して登録
		tools.setDistributor(new IDistributor() {
			@Override
			public Ret_value doProcess() {
				// データがアップロードされるのを待つ
				String contentsdata = "";
				synchronized (contents) {
					try {
						while ((contentsdata = contents.getData()) == null) {
							contents.wait();
						}
						contents.setData(null);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				Ret_value ret = new Ret_value();
				ContentsProfileHelper helper = new ContentsProfileHelper(ret);
				helper.setResult(RESULT.SUCCESS.getResult());
				// 正常終了時は詳細コードの値が不定
				helper.setDetailCode(DC_INDEFINITE);
				// コンテンツデータを設定
				helper.setDetail(contentsdata);
				return ret;
			}

			@Override
			public void terminate() {
			}
		});

		try {
			// 配信処理の開始
			tools.startDistribution();
		} catch (RSiException e) {
			e.printStackTrace();
		}

		// 依頼結果の返却
		helper.setResult(RESULT.SUCCESS.getResult());
		helper.setDistributionId((long) (Math.random() * 10));
		// 正常終了時は詳細コードの値が不定
		helper.setDetailCode(DC_INDEFINITE);
		// 任意の文字列を設定
		helper.setDetail("依頼結果=正常終了");
		return ret;
	}

	/*
	 * (非 Javadoc)
	 *
	 * @seecom.fujitsu.rsi.server.acceptor.base.ContentsProfileBase#
	 * stop_distribute_contents(long, long,
	 * org.robotservices.v02.profile.acceptor.IDistributionTool)
	 */
	@Override
	public Ret_value stop_distribute_contents(long conv_id,
			long distribution_id, IDistributionTool tools) {

		Ret_value ret = new Ret_value();
		ContentsProfileHelper helper = new ContentsProfileHelper(ret);
		// SharedDataで保持している会話IDと比較
		Long id = sharedData.getConv_id();
		if (id == null || id != conv_id) {
			// 認証エラー
			helper.setResult(RESULT.ERROR.getResult());
			helper.setDetailCode(DC(COP, STOP_DISTRIBUTE_CONTENTS, 180));
			helper.setDetail(MSG(180));
			return ret;
		}

		// 配信処理の停止
		tools.stopDistribute();

		// コンテンツマネージャーからロボットIDの登録を削除
		contentsman.unregistRobot(sharedData.getRobot_id());

		// 依頼結果の返却
		helper.setResult(RESULT.SUCCESS.getResult());
		// 正常終了時は詳細コードの値が不定
		helper.setDetailCode(DC_INDEFINITE);
		// 任意の文字列を設定
		helper.setDetail("依頼結果=正常終了");
		return ret;
	}
}
