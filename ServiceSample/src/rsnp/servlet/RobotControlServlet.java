package rsnp.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rsnp.acceptor.RobotWorkerManager;

import org.apache.log4j.Logger;
import org.robotservices.v02.exception.RSiException;
import org.robotservices.v02.profile.acceptor.IWorkerTool;
import org.robotservices.v02.profile.common.Ret_value;
import org.robotservices.v02.profile.invoker.IMotion_profile;
import org.robotservices.v02.profile.invoker.InvokerProfileFactory;

/**
 * ロボット操作初期表示用サーブレット
 */
public class RobotControlServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	Logger m_logger = Logger.getLogger(this.getClass().getName());
	// コンストラクタ
	public RobotControlServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doPost(req, res);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		req.setCharacterEncoding("Windows-31J");

		// リクエストパラメタの取得
		String MyAction = req.getParameter("MySubmit");

		String robotId = req.getParameter("robotid");

		robotId ="levin";
		// ロボットIDが指定されていなければ初期画面表示
		if (robotId == null) {
			// UIへフォワード
			System.out.println("robotIDがないrobocon");
			req.getRequestDispatcher("/jsp/robotcontrol.jsp").forward(req, res);
			return;
		}

		RobotWorkerManager rwm = RobotWorkerManager.getInstance();
		IWorkerTool workertool = rwm.getWorkerTool(robotId);
		if (workertool != null) {
			System.out.println("wt !=null robocon");
			InvokerProfileFactory factory = workertool.getProfileFactory();
			IMotion_profile motionp = factory.getMotion_profile();

			long conv_id = workertool.getConv_id();
			Ret_value ret = null;
			try {
				//ret = motionp.declare_control(conv_id);
				// 処理の実行
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
				if (MyAction != null) {
					if (MyAction.equals("forward")) {
						System.out.println("前進");
						ret = motionp.forward(conv_id, 30, "");
					}
					if (MyAction.equals("back")) {
						System.out.println("後退");
						ret = motionp.backward(conv_id, 30, "");
					}
					if (MyAction.equals("right")) {
						System.out.println("右");
						ret = motionp.spin_right(conv_id, 30, "");
					}
					if (MyAction.equals("left")) {
						System.out.println("左");
						ret = motionp.spin_left(conv_id,30, "");
					}
					if (MyAction.equals("stop")) {
						System.out.println("停止");
						ret=motionp.stop(conv_id,"");
					}
				}

			} catch (RSiException e) {
				e.printStackTrace();
			}
		}
		req.getRequestDispatcher("/jsp/robotcontrol.jsp").forward(req, res);
	}

}