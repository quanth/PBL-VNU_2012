/*
 * $Id: Multimedia_profile_impl.java 270 2010-04-28 07:10:26Z itoh $
 *
 * Copyright 2009-2010 Fujitsu Limited.
 * FUJITSU CONFIDENTIAL.
 */
package rsnp.acceptor;

import static com.fujitsu.rsi.util.DetailCodeManager.DC;
import static com.fujitsu.rsi.util.DetailCodeManager.DC_INDEFINITE;
import static com.fujitsu.rsi.util.DetailCodeManager.END_PROFILE;
import static com.fujitsu.rsi.util.DetailCodeManager.MMP;
import static com.fujitsu.rsi.util.DetailCodeManager.MSG;
import static com.fujitsu.rsi.util.DetailCodeManager.START_PROFILE;

import org.robotservices.v02.profile.acceptor.IWorkerTool;
import org.robotservices.v02.profile.common.Ret_value;

import com.fujitsu.rsi.helper.MultimediaProfileHelper;
import com.fujitsu.rsi.server.acceptor.base.MultimediaProfileBase;
import com.fujitsu.rsi.util.RESULT;

/**
 * Multimedia_profileのacceptor実装クラス<br>
 * MultimediaProfileBaseを継承しているため、不要なメソッドを実装する必要がない
 */
public class Multimedia_profile_impl extends MultimediaProfileBase {

	private SharedData sharedData;

	public Multimedia_profile_impl(SharedData sharedData) {
		this.sharedData = sharedData;
	}

	/*
	 * (非 Javadoc)
	 *
	 * @see
	 * com.fujitsu.rsi.server.acceptor.base.MultimediaProfileBase#start_profile
	 * (long, org.robotservices.v02.profile.acceptor.IWorkerTool)
	 */
	@Override
	public Ret_value start_profile(long conv_id, final IWorkerTool workertool) {

		Ret_value ret = new Ret_value();
		MultimediaProfileHelper helper = new MultimediaProfileHelper(ret);

		// SharedDataで保持している会話IDと比較
		Long id = sharedData.getConv_id();
		if (id == null || id != conv_id) {
			// 認証エラー
			helper.setResult(RESULT.ERROR.getResult());
			helper.setDetailCode(DC(MMP, START_PROFILE, 0));
			helper.setDetail(MSG(0));
			return ret;
		}

		// ワーカーツールを登録
		RobotWorkerManager rwm = RobotWorkerManager.getInstance();
		rwm.registRobot(sharedData.getRobot_id(), workertool);

		// ★参考★
		// 本サンプルでは、Web入力を受付けるサーブレットを、invokerの実行スレッドとして使用する。
		// このようなスレッドがない場合でも、workertoolを使ってスレッドを起動することができる。
		// workertoolを使ってスレッドを起動する場合のサンプルを以下に示す。
		// ＊手順詳細は「ライブラリー利用ガイド」3.2.2節を参照のこと。
		// -----------------------------------------------------------------------------ここから
		//		 IWorker worker = new IWorker() {
		//			private boolean contflag = true;
		//
		//			@Override
		//			public boolean initialize() {
		//				return true;
		//			}
		//
		//			@Override
		//			public boolean execute() {
		//				String line = "";
		//				try {
		//					BufferedReader reader = new BufferedReader(
		//							new InputStreamReader(System.in));
		//					line = reader.readLine();
		//					System.out.println(line);
		//				} catch (IOException e) {
		//					e.printStackTrace();
		//				}
		//
		//				return contflag;
		//			}
		//
		//			@Override
		//			public void exit() {
		//				System.out.println(sharedData.getRobot_id() + "終了しました。");
		//			}
		//
		//			@Override
		//			public void terminate() {
		//				contflag = false;
		//				System.out.println(sharedData.getRobot_id()
		//						+ "終了します。何かキー押してください。");
		//			}
		//		};
		//		workertool.setWoker(worker);
		//		try {
		//			workertool.startWorker();
		//		} catch (RSiException e) {
		//			e.printStackTrace();
		//		}
		// -----------------------------------------------------------------------------ここまで

		// 依頼結果の返却
		helper.setResult(RESULT.SUCCESS.getResult());
		// 正常終了時は詳細コードの値が不定
		helper.setDetailCode(DC_INDEFINITE);
		// 任意の文字列を設定
		helper.setDetail("依頼結果=正常終了");
		return ret;
	}

	/*
	 * (非 Javadoc)
	 *
	 * @see
	 * com.fujitsu.rsi.server.acceptor.base.MultimediaProfileBase#end_profile
	 * (long, org.robotservices.v02.profile.acceptor.IWorkerTool)
	 */
	@Override
	public Ret_value end_profile(long conv_id, IWorkerTool workertool) {

		Ret_value ret = new Ret_value();
		MultimediaProfileHelper helper = new MultimediaProfileHelper(ret);

		// SharedDataで保持している会話IDと比較
		Long id = sharedData.getConv_id();
		if (id == null || id != conv_id) {
			// 認証エラー
			helper.setResult(RESULT.ERROR.getResult());
			helper.setDetailCode(DC(MMP, END_PROFILE, 0));
			helper.setDetail(MSG(0));
			return ret;
		}

		// ワーカーツールの登録を削除
		RobotWorkerManager rwm = RobotWorkerManager.getInstance();
		rwm.unregistRobot(sharedData.getRobot_id());

		// workertool.stopWorker();

		helper.setResult(RESULT.SUCCESS.getResult());
		// 正常終了時は詳細コードの値が不定
		helper.setDetailCode(DC_INDEFINITE);
		// 任意の文字列を設定
		helper.setDetail("実行結果=正常終了");
		return ret;

	}

}
