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
public class RobotPositionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String str;
	/*
	 * (é�ž Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		req.setCharacterEncoding("Windows-31J");
		// ãƒªã‚¯ã‚¨ã‚¹ãƒˆãƒ‘ãƒ©ãƒ¡ã‚¿ã�®å�–å¾—
		String robotid = req.getParameter("robotid");
		robotid = "levin";
		
		// ãƒ­ãƒœãƒƒãƒˆIDã�ŒæŒ‡å®šã�•ã‚Œã�¦ã�„ã�ªã�‘ã‚Œã�°åˆ�æœŸç”»é�¢è¡¨ç¤º
		/*if (robotid == null) {
			// UIã�¸ãƒ•ã‚©ãƒ¯ãƒ¼ãƒ‰
			System.out.print("step1");
			req.getRequestDispatcher("/jsp/robotGPS.jsp").forward(req, res);			
			return;
		}*/
		System.out.print("step2");

		// ãƒ¯ãƒ¼ã‚«ãƒ¼ãƒžãƒ�ãƒ¼ã‚¸ãƒ£ãƒ¼ã�‹ã‚‰ãƒ¯ãƒ¼ã‚«ãƒ¼ãƒ„ãƒ¼ãƒ«ã‚’å�–å¾—
		RobotWorkerManager rwm = RobotWorkerManager.getInstance();
		IWorkerTool workertool = rwm.getWorkerTool(robotid);
		if (workertool != null) {
			// Multimedia_profileã�§ç”»åƒ�å�–å¾—
			System.out.print("step3");
			InvokerProfileFactory factory = workertool.getProfileFactory();
			IMultimedia_profile mp = factory.getMultimedia_profile();
			long conv_id = workertool.getConv_id();
			try {
				Ret_value ret = mp.get_camera_info(conv_id,"");
				MultimediaProfileHelper helper = new MultimediaProfileHelper(
						ret);
				int filecount = helper.getFileCount();
				for (int i = 0; i < filecount; i++) {
					AttachedFile atfile = helper.getAttachedFile(i);
					byte[] data = atfile.get_byte_array();

					//res.setContentType("text/html");
				/*	ServletOutputStream os = res.getOutputStream();
					os.write(data);
					os.close();*/
					String str = new String(data,"UTF-16LE");
					req.setAttribute("dataGPS", str);
					System.out.println("str:"+str+"\n"+data.length);
					getServletContext().getRequestDispatcher("/jsp/robotGPS.jsp").forward(req, res);
				}
			} catch (RSiException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * (é�ž Javadoc)
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
