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
import com.fujitsu.rsi.client.acceptor.base.MultimediaProfileBase;
import com.fujitsu.rsi.helper.MultimediaProfileHelper;
import com.fujitsu.rsi.util.RESULT;
import rsnp.sample.CameraImage;

/**
 * IMultimedia_profile
 *
 */
public class Multimedia_profile_impl extends MultimediaProfileBase {

	private CameraImage ci;
	public Multimedia_profile_impl() {

		ci = new CameraImage();
		System.out.println(ci.getClass()+" Camera image");
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
		SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		String time = sdf.format(new Date());
		System.out.println("" + time);
		
		ci.loadNext();		
				
		AttachedFile attachedFile = new AttachedFile();
		attachedFile.set_mime_type("image/jpg");
		attachedFile.set_file_name(ci.getFilename());
		attachedFile.set_capture_time(ci.getCaptureDate());
		attachedFile.set_byte_array(ci.getImage());

		Ret_value ret = new Ret_value();
		MultimediaProfileHelper helper = new MultimediaProfileHelper(ret);
		helper.setResult(RESULT.SUCCESS.getResult());
		helper.setDetail("distribute_camera_image");
		helper.setAttachedFile(attachedFile);
		
		System.err.println("Multimedia_profile_impl:get_camera_image exit");
		return ret;
	}
}
