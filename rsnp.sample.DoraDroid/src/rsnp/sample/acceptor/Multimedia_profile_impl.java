/*
 * $Id: Multimedia_profile_impl.java 213 2010-04-26 07:49:02Z itoh $
 *
 * Copyright 2009-2010 Fujitsu Limited. FUJITSU CONFIDENTIAL.
 */
package rsnp.sample.acceptor;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.robotservices.v02.profile.common.AttachedFile;
import org.robotservices.v02.profile.common.Ret_value;
import com.fujitsu.rsi.client.acceptor.base.MultimediaProfileBase;
import com.fujitsu.rsi.helper.MultimediaProfileHelper;
import com.fujitsu.rsi.util.RESULT;

import rsnp.sample.DoraDroid;
import rsnp.sample.ImageProvidor;
import rsnp.sample.ObjectHolder;

/**
 * IMultimedia_profile
 *
 */
public class Multimedia_profile_impl extends MultimediaProfileBase {

	/** ImageProvidorï¾ƒï½£çª¶å ‹ï½ªï¾ƒï½£ï¾†å�œï¿½ï¾ƒï½£çª¶å ‹ï½¸ï¾ƒï½£çª¶å ‹ï½§ï¾ƒï½£çª¶å ‹ï½¯ï¾ƒï½£ï¾†å¡šï¿½*/
	private ImageProvidor imageProvidor;
	private String str;

	public Multimedia_profile_impl() {
		// ObjectHolderï¾ƒï½£ï¿½ï½½çª¶ï½¹ï¾ƒï½£çª¶å£ºï¿½ï¾ƒï½¥ï¿½ï½½çª¶ç¦¿ï½¥ï¾‚ï½¾çª¶ï¿½
		imageProvidor = ObjectHolder.getInstance().get(
				ImageProvidor.class.getName());
		System.out.println(imageProvidor.getClass()+" imageProvider");

	}

	/*
	 * (Javadoc)
	 *
	 * @see
	 * org.robotservices.v02.profile.acceptor.IMultimedia_profile#get_camera_image
	 * (long, java.lang.String, java.lang.String)
	 */
	@Override
	public Ret_value get_camera_image(long conv_id, String id, String options) {
		System.err.println("Multimedia_profile_impl:get_camera_image enter");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		String time = sdf.format(new Date());
		System.out.println("ï¾ƒï½§çª¶æ•–ï½»ï¾ƒï½¥ï¾†æŠµï½¿ï½½ï¾ƒï½¥ï¿½ï½½çª¶ç¦¿ï½¥ï¾‚ï½¾çª¶æ°¾ï½¦é‚ƒï½¢çª¶å ™ï½©çª¶å‡ªï¿½ï¾ƒï½¯ï¾‚ï½¼ï¾…ï½¡" + time);
		// ï¾ƒï½§çª¶æ•–ï½»ï¾ƒï½¥ï¾†æŠµï½¿ï½½ï¾ƒï½£çª¶å£ºï¿½ï¾ƒï½¥ï¿½ï½½çª¶ç¦¿ï½¥ï¾‚ï½¾çª¶ï¿½
		imageProvidor.takeImage();
		while(imageProvidor.getSemaphore() == 0){
		}
		byte[] bytes = imageProvidor.getImage("PNG");
		imageProvidor.setSemaphore(0);

		AttachedFile af = new AttachedFile();
		af.set_mime_type("image/png");
		af.set_file_name(time + ".png");
		af.set_capture_time(time);
		af.set_byte_array(bytes);

		Ret_value ret = new Ret_value();
		MultimediaProfileHelper helper = new MultimediaProfileHelper(ret);
		helper.setResult(RESULT.SUCCESS.getResult());
		helper.setDetail("");
		helper.setAttachedFile(af);
		System.err.println("Multimedia_profile_impl:get_camera_image exit");
		return ret;
	}
	@Override
	public Ret_value get_camera_info(long conv_id, String Id){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		String time = sdf.format(new Date());
		Ret_value ret = new Ret_value();
		str = "\n"+"Longitude: "+ DoraDroid.lng + " Latitude:" + DoraDroid.lat+"\n";
		byte[] bytes;
		try {
			bytes = str.getBytes("UTF-16LE");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ret;
		}

		AttachedFile af = new AttachedFile();
		af.set_mime_type("text/plain");
		af.set_file_name(time + ".txt");
		af.set_capture_time(time);
		af.set_byte_array(bytes);

		MultimediaProfileHelper helper = new MultimediaProfileHelper(ret);
		helper.setResult(RESULT.SUCCESS.getResult());
		helper.setDetail("");
		helper.setAttachedFile(af);
		return ret;
	}

}
