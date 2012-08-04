/*
 * $Id: ImageProvidor.java 185 2010-04-22 05:38:47Z itoh $
 *
 * Copyright 2009-2010 Fujitsu Limited.
 * FUJITSU CONFIDENTIAL.
 */
package rsnp.sample;

/**
 * Ã§â€�Â»Ã¥Æ’ï¿½Ã£â€šâ€™Ã¥ï¿½â€“Ã¥Â¾â€”Ã£ï¿½â„¢Ã£â€šâ€¹Ã£â€šÂ¤Ã£Æ’Â³Ã£â€šÂ¿Ã£Æ’Â¼Ã£Æ’â€¢Ã£â€šÂ§Ã£Æ’Â¼Ã£â€šÂ¹
 *
 */
public interface ImageProvidor {

	/**
	 * Ã¦Å’â€¡Ã¥Â®Å¡Ã£ï¿½â€¢Ã£â€šÅ’Ã£ï¿½Å¸Ã¥Â½Â¢Ã¥Â¼ï¿½Ã£ï¿½Â®Ã§â€�Â»Ã¥Æ’ï¿½Ã£Æ’ï¿½Ã£â€šÂ¤Ã£Æ’Ë†Ã©â€¦ï¿½Ã¥Ë†â€”Ã£â€šâ€™Ã¥ï¿½â€“Ã¥Â¾â€”Ã£ï¿½â„¢Ã£â€šâ€¹
	 *
	 * @param type
	 *            Ã§â€�Â»Ã¥Æ’ï¿½Ã¥Â½Â¢Ã¥Â¼ï¿½
	 * @return Ã§â€�Â»Ã¥Æ’ï¿½Ã£ï¿½Â®Ã£Æ’ï¿½Ã£â€šÂ¤Ã£Æ’Ë†Ã©â€¦ï¿½Ã¥Ë†â€”
	 */
	public byte[] getImage(String type);
	public void takeImage();
	public int getSemaphore();
	public void setSemaphore(int semaphore);
}
