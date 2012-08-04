/*
 * $Id: RobotCameraServlet.java 270 2010-04-28 07:10:26Z itoh $
 *
 * Copyright 2009-2010 Fujitsu Limited.
 * FUJITSU CONFIDENTIAL.
 */
package rsnp.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.robotservices.v02.exception.RSiException;
import org.robotservices.v02.profile.acceptor.IWorkerTool;
import org.robotservices.v02.profile.common.AttachedFile;
import org.robotservices.v02.profile.common.Ret_value;
import org.robotservices.v02.profile.invoker.IMultimedia_profile;
import org.robotservices.v02.profile.invoker.InvokerProfileFactory;

import rsnp.acceptor.RobotWorkerManager;


import com.fujitsu.rsi.helper.MultimediaProfileHelper;

/**
 * Servlet implementation class MessagePostServlet
 */
public class RobotCameraServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/*
	 * (非 Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		req.setCharacterEncoding("Windows-31J");
		// リクエストパラメタの取得
		String robotid = req.getParameter("robotid");

		// ロボットIDが指定されていなければ初期画面表示
		if (robotid == null) {
			// UIへフォワード
			req.getRequestDispatcher("/jsp/robotcamera.jsp").forward(req, res);
			return;
		}

		// ワーカーマネージャーからワーカーツールを取得
		RobotWorkerManager rwm = RobotWorkerManager.getInstance();
		IWorkerTool workertool = rwm.getWorkerTool(robotid);
		if (workertool != null) {
			// Multimedia_profileで画像取得
			InvokerProfileFactory factory = workertool.getProfileFactory();
			IMultimedia_profile mp = factory.getMultimedia_profile();
			long conv_id = workertool.getConv_id();
			try {
				Ret_value ret = mp.get_camera_image(conv_id, "", "");
				MultimediaProfileHelper helper = new MultimediaProfileHelper(
						ret);
				int filecount = helper.getFileCount();
				for (int i = 0; i < filecount; i++) {
					AttachedFile atfile = helper.getAttachedFile(i);
					byte[] data = atfile.get_byte_array();

					res.setContentType(atfile.get_mime_type());
					ServletOutputStream os = res.getOutputStream();
					os.write(data);
					os.close();
				}
			} catch (RSiException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doPost(req, res);
	}

}
