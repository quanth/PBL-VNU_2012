/*
 * $Id: CameraImage.java 117 2010-04-12 09:27:58Z mitsuki $
 *
 * Copyright 2009-2010 Fujitsu Limited.
 * FUJITSU CONFIDENTIAL.
 */
package rsnp.sample;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

public class CameraImage {

	/** Ã£â€šÂ«Ã£â€šÂ¦Ã£Æ’Â³Ã£â€šÂ¿Ã£Æ’Â¼ */
	private static int counter = -1;

	/** Ã§ï¿½Â¾Ã¥Å“Â¨Ã£ï¿½Â®Ã£Æ’â€¢Ã£â€šÂ¡Ã£â€šÂ¤Ã£Æ’Â«Ã¥ï¿½ï¿½ */
	private String filename;
	/** Ã§ï¿½Â¾Ã¥Å“Â¨Ã£ï¿½Â®Ã§â€�Â»Ã¥Æ’ï¿½ */
	private byte[] image;
	/** Ã§ï¿½Â¾Ã¥Å“Â¨Ã£ï¿½Â®Ã§â€�Â»Ã¥Æ’ï¿½Ã¥ï¿½â€“Ã¥Â¾â€”Ã¦â€”Â¥Ã¤Â»Ëœ */
	private String captureDate;

	/**
	 * Ã§ï¿½Â¾Ã¥Å“Â¨Ã£ï¿½Â®Ã£â€šÂ«Ã£Æ’Â¡Ã£Æ’Â©Ã§â€�Â»Ã¥Æ’ï¿½Ã£â€šâ€™Ã¨Â¿â€�Ã£ï¿½â„¢Ã£â‚¬â€š
	 */
	public byte[] getImage() {
		return image;
	}

	/**
	 * Ã§ï¿½Â¾Ã¥Å“Â¨Ã£ï¿½Â®Ã§â€�Â»Ã¥Æ’ï¿½Ã¥ï¿½â€“Ã¥Â¾â€”Ã¦â€”Â¥Ã¤Â»ËœÃ£â€šâ€™Ã¨Â¿â€�Ã£ï¿½â„¢Ã£â‚¬â€š
	 *
	 * @return
	 */
	public String getCaptureDate() {
		return captureDate;
	}

	/**
	 * Ã§ï¿½Â¾Ã¥Å“Â¨Ã£ï¿½Â®Ã§â€�Â»Ã¥Æ’ï¿½Ã£Æ’â€¢Ã£â€šÂ¡Ã£â€šÂ¤Ã£Æ’Â«Ã¥ï¿½ï¿½Ã£â€šâ€™Ã¨Â¿â€�Ã£ï¿½â„¢Ã£â‚¬â€š
	 *
	 * @return
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * Ã£Æ’â€¢Ã£â€šÂ¡Ã£â€šÂ¤Ã£Æ’Â«Ã¤Â¸â‚¬Ã¨Â¦Â§Ã£ï¿½Â®Ã¦Â¬Â¡Ã£ï¿½Â®Ã£Æ’â€¢Ã£â€šÂ¡Ã£â€šÂ¤Ã£Æ’Â«Ã£â€šâ€™Ã£Æ’Â­Ã£Æ’Â¼Ã£Æ’â€°Ã£ï¿½â„¢Ã£â€šâ€¹Ã£â‚¬â€š
	 */
	public void loadNext() {

		counter++;

		// Ã¥ï¿½â€“Ã¥Â¾â€”Ã¦â€”Â¥Ã¤Â»Ëœ
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ssZ");
		captureDate = formatter.format(date);

		String type = "png";
		// Ã£Æ’â€¢Ã£â€šÂ¡Ã£â€šÂ¤Ã£Æ’Â«Ã¥ï¿½ï¿½
		filename = String.format("img_%08d." + type, counter);
		// Ã£â€šÂ¤Ã£Æ’Â¡Ã£Æ’Â¼Ã£â€šÂ¸Ã¥ï¿½â€“Ã¥Â¾â€”
		image = createImage(type);
	}

	/**
	 * Ã§â€�Â»Ã¥Æ’ï¿½Ã£â€šâ€™Ã¥ï¿½â€“Ã¥Â¾â€”Ã£ï¿½â€”Ã£ï¿½Â¾Ã£ï¿½â„¢Ã£â‚¬â€š
	 *
	 * @param type
	 *            Ã§â€�Â»Ã¥Æ’ï¿½Ã£Æ’â€¢Ã£â€šÂ©Ã£Æ’Â¼Ã£Æ’Å¾Ã£Æ’Æ’Ã£Æ’Ë†
	 * @return Ã§â€�Â»Ã¥Æ’ï¿½Ã£Æ’â€¡Ã£Æ’Â¼Ã£â€šÂ¿
	 */
	private byte[] createImage(String type) {
		// Ã§â€�Â»Ã¥Æ’ï¿½Ã£â€šâ€™Ã¥ï¿½â€“Ã¥Â¾â€”
		// Make a custom image 
/*		BufferedImage im;
		im = new BufferedImage(240, 180, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = im.createGraphics();
		int height = im.getHeight();
		int width = im.getWidth();
		int randrgb = (int) (Math.pow(2, 24) * Math.random());
		g2d.setColor(new Color(randrgb));
		g2d.fillRect(0, 0, width, height);

		g2d.setColor(Color.RED);
		g2d.setFont(new Font("Century", Font.PLAIN, 14));
		g2d.drawString("File Name : " + filename, 5, 20);
		g2d.drawString("Capture time : ", 5, 40);
		g2d.drawString(captureDate, 10, 54);
		g2d.dispose();

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ImageIO.write(im, type, out);
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		byte[] bytes = out.toByteArray();*/
		
		// Ã£â€šÂ«Ã£Æ’Â¡Ã£Æ’Â©Ã§â€�Â»Ã¥Æ’ï¿½Ã£â€šâ€™Ã¥ï¿½â€“Ã¥Â¾â€”Ã£ï¿½â„¢Ã£â€šâ€¹Ã¥Â Â´Ã¥ï¿½Ë†Ã£ï¿½Â®Ã£Æ’Â­Ã£â€šÂ¸Ã£Æ’Æ’Ã£â€šÂ¯
		// using ImgCapture to get Image from camera
		byte[] bytes = ImgCapture.getInstance().getImg();
		
		return bytes;
	}

}
