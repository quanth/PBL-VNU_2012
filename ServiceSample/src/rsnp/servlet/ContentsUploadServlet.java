/*
 * $Id: ContentsUploadServlet.java 270 2010-04-28 07:10:26Z itoh $
 *
 * Copyright 2009-2010 Fujitsu Limited.
 * FUJITSU CONFIDENTIAL.
 */
package rsnp.servlet;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rsnp.acceptor.ContentsManager;


/**
 * Servlet implementation class ContentsUploadServlet
 */
public class ContentsUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 8667365793814936956L;

	protected void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doPost(req, res);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		req.setCharacterEncoding("Windows-31J");

		// リクエストパラメタの取得
		String data = req.getParameter("contentsdata");

		// Servletコンテキストからコンテンツ配信管理を取得
		ServletContext servcon = getServletContext();
		ContentsManager contentsman = (ContentsManager) servcon
				.getAttribute(ContentsManager.KEYNAME);
		if (contentsman != null) {
			// 接続中のロボットID
			String ids = contentsman.getRobotIds();
			req.setAttribute("CONNECTED_ROBOT_IDS", ids);
			if (data != null && !data.equals("")) {
				// 配信処理呼び出し
				int count = contentsman.distribute(data);
				// 配信結果数を設定
				req.setAttribute("DISTRIBUTE_COUNT", count);
			}
		}

		// UIへフォワード
		req.getRequestDispatcher("/jsp/contentsuploader.jsp").forward(req, res);
	}
}
