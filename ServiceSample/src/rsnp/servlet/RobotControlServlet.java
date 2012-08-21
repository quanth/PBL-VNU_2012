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
 * ãƒ­ãƒœãƒƒãƒˆæ“�ä½œåˆ�æœŸè¡¨ç¤ºç”¨ã‚µãƒ¼ãƒ–ãƒ¬ãƒƒãƒˆ
 */
public class RobotControlServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	Logger m_logger = Logger.getLogger(this.getClass().getName());
	// ã‚³ãƒ³ã‚¹ãƒˆãƒ©ã‚¯ã‚¿
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

		// ãƒªã‚¯ã‚¨ã‚¹ãƒˆãƒ‘ãƒ©ãƒ¡ã‚¿ã�®å�–å¾—
		String MyAction = req.getParameter("MySubmit");
		System.out.println("MyAction:_" + MyAction + "_");
		String robotId = req.getParameter("robotid");
		System.out.println("robotID:_" + robotId + "_");
		//robotId ="levin";
		// ãƒ­ãƒœãƒƒãƒˆIDã�ŒæŒ‡å®šã�•ã‚Œã�¦ã�„ã�ªã�‘ã‚Œã�°åˆ�æœŸç”»é�¢è¡¨ç¤º
		if (robotId == null) {
			// UIã�¸ãƒ•ã‚©ãƒ¯ãƒ¼ãƒ‰
			System.out.println("robotIDã�Œã�ªã�„robocon");
			req.getRequestDispatcher("/jsp/robotcontrol.jsp").forward(req, res);
			return;
		}

		RobotWorkerManager rwm = RobotWorkerManager.getInstance();
		IWorkerTool workertool = rwm.getWorkerTool(robotId);
		if (workertool != null) {
			//System.out.println("wt !=null robocon");
			InvokerProfileFactory factory = workertool.getProfileFactory();
			IMotion_profile motionp = factory.getMotion_profile();
			System.out.println("wt1");
			long conv_id = workertool.getConv_id();
			Ret_value ret = null;
			System.out.println("wt2");
			try {
				//ret = motionp.declare_control(conv_id);
				// å‡¦ç�†ã�®å®Ÿè¡Œ
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO è‡ªå‹•ç”Ÿæˆ�ã�•ã‚Œã�Ÿ catch ãƒ–ãƒ­ãƒƒã‚¯
					e.printStackTrace();
				}
				System.out.println("wt3");
				if (MyAction != null) {
					if (MyAction.equals("forward")) {
						System.out.println("RobotControlServlet:forward");
						ret = motionp.forward(conv_id, 30, "");
					}
					if (MyAction.equals("back")) {
						System.out.println("RobotControlServlet:back");
						ret = motionp.backward(conv_id, 30, "");
					}
					if (MyAction.equals("right")) {
						System.out.println("RobotControlServlet:right");
						ret = motionp.spin_right(conv_id, 30, "");
					}
					if (MyAction.equals("left")) {
						System.out.println("RobotControlServlet:left");
						ret = motionp.spin_left(conv_id,30, "");
					}
					if (MyAction.equals("stop")) {
						System.out.println("RobotControlServlet:stop");
						ret=motionp.stop(conv_id,"");
					}
					System.out.println("wt4");
				}

			} catch (RSiException e) {
				e.printStackTrace();
			}
		}
		req.getRequestDispatcher("/jsp/robotcontrol.jsp").forward(req, res);
	}

}