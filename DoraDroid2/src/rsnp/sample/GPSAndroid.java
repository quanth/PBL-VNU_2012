package rsnp.sample;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class GPSAndroid extends Service implements LocationListener{
	private static final float MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1;		// in meters
	private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000;			// in miliseconds
	protected LocationManager locationManager;
	protected Button retrieveLocationButton;
	
	private boolean GPSEnable = false;
	private boolean NetworkEnable = false;
	private Location location;
	private double latitude;
	private double longitude;
	private Context mContext;
	 
	public double getLatitude(){ 
		if(location != null){
			latitude = location.getLatitude();
		}			
		return latitude;
	}
		
		
	public double getLongitude(){
		if(location != null){
			longitude = location.getLongitude();
		}
		return longitude;
	}
	
	public void setLatitude(double latitude){
		this.latitude = latitude;		
	}
	
	public void setLongitude(double longtitude){
		this.longitude = longitude;
	}
	
    public Location getLocation(){    
        locationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
        
        locationManager.requestLocationUpdates(
        	LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES, 
        	MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, this);
        
			
        GPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (GPSEnable) {
			if (locationManager != null) {
				location = locationManager
						.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				if (location != null) {
					latitude = location.getLatitude();
					longitude = location.getLongitude();
				}
			}
		}
		return location;
    }		
	
	public void stopUsingGPS(){
		if(locationManager != null){
			locationManager.removeUpdates(GPSAndroid.this);
		}		
	}

	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}


	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
