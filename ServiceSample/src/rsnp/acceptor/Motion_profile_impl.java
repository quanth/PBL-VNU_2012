package rsnp.acceptor;

import static com.fujitsu.rsi.util.DetailCodeManager.DC;
import static com.fujitsu.rsi.util.DetailCodeManager.DC_INDEFINITE;
import static com.fujitsu.rsi.util.DetailCodeManager.END_PROFILE;
import static com.fujitsu.rsi.util.DetailCodeManager.MMP;
import static com.fujitsu.rsi.util.DetailCodeManager.MSG;
import static com.fujitsu.rsi.util.DetailCodeManager.START_PROFILE;

import org.apache.log4j.Logger;
import org.robotservices.v02.profile.acceptor.IWorkerTool;
import org.robotservices.v02.profile.common.Ret_value;
import org.robotservices.v02.profile.invoker.IMotion_profile;
import org.robotservices.v02.profile.invoker.InvokerProfileFactory;

import com.fujitsu.rsi.helper.MotionProfileHelper;
import com.fujitsu.rsi.server.acceptor.base.MotionProfileBase;
import com.fujitsu.rsi.util.RESULT;

/**
 * Motion_profile の acceptor の実装
 */
public class Motion_profile_impl extends MotionProfileBase {

	Logger m_logger = Logger.getLogger(this.getClass().getName());
	private SharedData sharedData = null;

	public Motion_profile_impl(SharedData sharedData) {
		this.sharedData = sharedData;
	}

	@Override
	public Ret_value start_profile(long conv_id, IWorkerTool workertool) {
		m_logger.debug("動作プロファイル開始");
		Ret_value ret = new Ret_value();
		MotionProfileHelper helper = new MotionProfileHelper(ret);

		// SharedData で保持している会話 ID と比較
		Long id = sharedData.getConv_id();
		if (id == null || id != conv_id) {
			// 認証エラー
			helper.setResult(RESULT.ERROR.getResult());
			helper.setDetailCode(DC(MMP, START_PROFILE, 0));
			helper.setDetail(MSG(0));
			return ret;
		}

		InvokerProfileFactory factory = workertool.getProfileFactory();
		IMotion_profile motionp = factory.getMotion_profile();
		/*
		try {
			ret = motionp.declare_control(conv_id);
		} catch (RSiException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}*/
		
		// ワーカースレッドの生成
		RobotWorkerManager rwm = RobotWorkerManager.getInstance();

		// ワーカーツールを登録
		rwm.registRobot(this.sharedData.getRobot_id(), workertool);

		// 依頼結果の返却
		helper.setResult(RESULT.SUCCESS.getResult());

		// 正常終了時は詳細コードの値が不定
		helper.setDetailCode(DC_INDEFINITE);

		// 任意の文字列を設定
		helper.setDetail("依頼結果=正常終了");
		return ret;
	}

	@Override
	public Ret_value end_profile(long conv_id, IWorkerTool workertool) {
		Ret_value ret = new Ret_value();
		MotionProfileHelper helper = new MotionProfileHelper(ret);
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
		workertool.stopWorker();
		helper.setResult(RESULT.SUCCESS.getResult());

		// 正常終了時は詳細コードの値が不定
		helper.setDetailCode(DC_INDEFINITE);

		// 任意の文字列を設定
		helper.setDetail("実行結果=正常終了");
		return ret;
	}
}