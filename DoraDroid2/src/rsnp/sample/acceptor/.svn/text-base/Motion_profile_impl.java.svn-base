package rsnp.sample.acceptor;

import org.robotservices.v02.profile.common.Ret_value;
import com.fujitsu.rsi.client.acceptor.base.MotionProfileBase;

import rsnp.sample.DoraDroid;

public class Motion_profile_impl extends MotionProfileBase{
	
	@Override
	public Ret_value declare_control(long conv_id) {
		Ret_value ret = new Ret_value();
		System.out.println("in the motionProfile declare");
		return ret;
	}

	@Override
	public Ret_value release_control(long conv_id) {
		Ret_value ret = new Ret_value();
		System.out.println("in the motionProfile release");
		//bs.closeBT();
		return ret;
	}

	@Override
	public Ret_value forward(long conv_id, double distance, String option) {
		Ret_value ret = new Ret_value();
		System.out.println("in the motionProfile forward");
		//bs.selectFunc(8, distance);
		DoraDroid.updateMotorControl(20,20);
		return ret;
	}

	@Override
	public Ret_value backward(long conv_id, double distance, String option) {
		Ret_value ret = new Ret_value();
		System.out.println("in the motionProfile back");
		DoraDroid.updateMotorControl(-20,-20);
		//bs.selectFunc(2, distance);
		return ret;
	}

	@Override
	public Ret_value right(long conv_id, double radius, double degree,
			String option) {
		System.out.println("just in the motionProfile right");
		Ret_value ret = new Ret_value();
		System.out.println("in the motionProfile right");
		//bs.selectFunc(6, radius);
		DoraDroid.updateMotorControl(20,0);
		return ret;
	}

	@Override
	public Ret_value left(long conv_id, double radius, double degree,
			String option) {
		System.out.println("just in the motionProfile left");
		Ret_value ret = new Ret_value();
		System.out.println("in the motionProfile left");
		//bs.selectFunc(4, radius);
		DoraDroid.updateMotorControl(0,20);
		return ret;
	}

	@Override
	public Ret_value spin_right(long conv_id, double degree, String option) {
		System.out.println("just in the motionProfile spinRight");
		Ret_value ret = new Ret_value();
		System.out.println("in the motionProfile spinRight");
		//bs.selectFunc(6, degree);
		DoraDroid.updateMotorControl(20,0);
		return ret;
	}

	@Override
	public Ret_value spin_left(long conv_id, double degree, String option) {
		Ret_value ret = new Ret_value();
		System.out.println("in the motionProfile spinLeft");
		//bs.selectFunc(4, degree);
		DoraDroid.updateMotorControl(0,20);
		return ret;
	}

	@Override
	public Ret_value stop(long conv_id, String option) {
		System.out.println("just in the motionProfile stop");
		Ret_value ret = new Ret_value();
		System.out.println("in the motionProfile stop");
		//bs.selectFunc(5, 0);
		DoraDroid.updateMotorControl(0,0);
		return ret;
	}
}