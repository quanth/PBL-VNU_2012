/*
 * $Id: Multimedia_profile_impl.java 213 2010-04-26 07:49:02Z itoh $
 *
 * Copyright 2009-2010 Fujitsu Limited. FUJITSU CONFIDENTIAL.
 */
package rsnp.sample.acceptor;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.robotservices.v02.profile.common.AttachedFile;
import org.robotservices.v02.profile.common.Ret_value;

import rsnp.sample.ImageProvidor;
import rsnp.sample.ObjectHolder;

import android.hardware.Camera;

import com.fujitsu.rsi.client.acceptor.base.MultimediaProfileBase;
import com.fujitsu.rsi.helper.MultimediaProfileHelper;
import com.fujitsu.rsi.util.RESULT;

/**
 * IMultimedia_profileﾃ｣�ｽﾂｮﾃ･ﾂｮﾅｸﾃｨﾂ｣窶ｦﾃ｣窶堋ｯﾃ｣ﾆ陳ｩﾃ｣窶堋ｹ
 *
 */
public class Multimedia_profile_impl extends MultimediaProfileBase {

	/** ImageProvidorﾃ｣窶堋ｪﾃ｣ﾆ停�ﾃ｣窶堋ｸﾃ｣窶堋ｧﾃ｣窶堋ｯﾃ｣ﾆ塚�*/
	private ImageProvidor imageProvidor;

	/**
	 * Multimedia_profile_implﾃ｣窶堋ｪﾃ｣ﾆ停�ﾃ｣窶堋ｸﾃ｣窶堋ｧﾃ｣窶堋ｯﾃ｣ﾆ塚�｣窶壺�ﾃｦﾂｧ窶ｹﾃｧﾂｯ窶ｰﾃ｣�ｽ邃｢ﾃ｣窶壺�
	 */
	public Multimedia_profile_impl() {

		// ObjectHolderﾃ｣�ｽ窶ｹﾃ｣窶壺�ﾃ･�ｽ窶禿･ﾂｾ窶�
		imageProvidor = ObjectHolder.getInstance().get(
				ImageProvidor.class.getName());
		System.out.println(imageProvidor.getClass()+" imageProvider");
	}

	/*
	 * (ﾃｩ�ｽﾅｾ Javadoc)
	 *
	 * @see
	 * org.robotservices.v02.profile.acceptor.IMultimedia_profile#get_camera_image
	 * (long, java.lang.String, java.lang.String)
	 */
	@Override
	public Ret_value get_camera_image(long conv_id, String id, String options) {
		System.err.println("Multimedia_profile_impl:get_camera_image enter");
		SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		String time = sdf.format(new Date());
		System.out.println("ﾃｧ窶敖ｻﾃ･ﾆ抵ｿｽﾃ･�ｽ窶禿･ﾂｾ窶氾ｦ邃｢窶堙ｩ窶凪�ﾃｯﾂｼﾅ｡" + time);
		
		// ﾃｧ窶敖ｻﾃ･ﾆ抵ｿｽﾃ｣窶壺�ﾃ･�ｽ窶禿･ﾂｾ窶�
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
}
