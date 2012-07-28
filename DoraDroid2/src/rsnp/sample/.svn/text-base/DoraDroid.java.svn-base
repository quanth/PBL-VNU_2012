/**
 * Copyright 2010 Guenther Hoelzl, Shawn Brown
 *
 * This file is part of MINDdroid.
 *
 * MINDdroid is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * MINDdroid is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * MINDdroid. If not, see <http://www.gnu.org/licenses/>.
 **/

package rsnp.sample;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.robotservices.v02.IAsyncCallBack;
import org.robotservices.v02.profile.common.Ret_value;
import com.fujitsu.rsi.helper.ContentsProfileHelper;

//import com.lego.minddroid.Lama;
import com.lego.minddroid.BTCommunicator;
import com.lego.minddroid.BTConnectable;
import com.lego.minddroid.DeviceListActivity;
import com.lego.minddroid.LCPMessage;
import com.lego.minddroid.Options;
import com.lego.minddroid.R;
import com.lego.minddroid.R.drawable;
import com.lego.minddroid.R.id;
import com.lego.minddroid.R.layout;
import com.lego.minddroid.R.string;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DoraDroid extends Activity implements BTConnectable {

	private static final int REQUEST_CONNECT_DEVICE = 1000;
	private static final int REQUEST_ENABLE_BT = 2000;
	private static boolean btOnByUs = false;
	private static boolean pairing;
	private static String NXTAddress;
	private static Toast reusableToast;
	private final int TTS_CHECK_CODE = 9991;
	
	public static Resources res;
	
    public static final String MINDDROID_PREFS = "Mprefs";
    public static final String MINDDROID_ROBOT_TYPE = "MrobotType";
    private static int mRobotType;
    
    private static BTCommunicator myBTCommunicator = null;
    private static boolean connected = false;
    private static ProgressDialog connectingProgressDialog;
    private static Handler btcHandler;
    //private Menu myMenu;
    //private Activity thisActivity;
    private static boolean btErrorPending = false;
    static int motorLeft;
    private static int directionLeft; // +/- 1
    static int motorRight;
    private static boolean stopAlreadySent = false;
    private static int directionRight; // +/- 1
    private static int motorAction;
    private static int directionAction; // +/- 1
    private static List<String> programList;
    private static final int MAX_PROGRAMS = 20;
    private static String programToStart;

	/** to test /robotcamera */
	private SurfaceScribeView scribeView;
	/** RSNP controller */
	private RSNPController rsnp;
	
	/** TTS */
	private static TextToSpeech tts;
	private OnInitListener ttsInitListener = new OnInitListener() {

		/*
		 * (é�ž Javadoc)
		 *
		 * @see android.speech.tts.TextToSpeech.OnInitListener#onInit(int)
		 */
		@Override
		public void onInit(int status) {

			if (status == TextToSpeech.SUCCESS) {

				tts.setLanguage(Locale.ENGLISH);
				displayAndSpeech("I'm ready!");
			} else {
				System.out.println("TextToSpeech initialize error!!");
			}
		}
	};
	/**
	 * ãƒ‘ãƒ©ãƒ¡ã‚¿ã�§æŒ‡å®šã�•ã‚Œã�Ÿæ–‡å­—åˆ—ã‚’ç”»é�¢ã�«è¡¨ç¤ºã�—ã€�èª­ã�¿ä¸Šã�’ã‚‹(è‹±èªž)
	 *
	 * @param message
	 *            è¡¨ç¤ºã�™ã‚‹æ–‡å­—åˆ—
	 */
	public void displayAndSpeech(final String message) {

		// ãƒ“ãƒ¥ãƒ¼ã�«è¨­å®š
		final TextView txtView = (TextView) findViewById(R.id.textView);
		txtView.post(new Runnable() {
			@Override
			public void run() {
				txtView.setText(message);
			}
		});

		// èª­ã�¿ä¸Šã�’ã‚‹
		tts.speak(message, TextToSpeech.QUEUE_FLUSH, null);
		System.out.println(message);
	}

	/** Contents_profileã�®ã‚³ãƒ¼ãƒ«ãƒ�ãƒƒã‚¯ */
	private IAsyncCallBack callback = new IAsyncCallBack() {

		/*
		 * (é�ž Javadoc)
		 *
		 * @see
		 * org.robotservices.v02.IAsyncCallBack#doEvent(org.robotservices.v02
		 * .profile .common.Ret_value, boolean)
		 */
		@Override
		public void doEvent(Ret_value ret, boolean isLast) {
			//ret: message sent from webGUI
			ContentsProfileHelper helper = new ContentsProfileHelper(ret);
			String message = helper.getDetail();

			displayAndSpeech(message);
		}

		/*
		 * (é�ž Javadoc)
		 *
		 * @see
		 * org.robotservices.v02.IAsyncCallBack#doException(java.lang.Exception)
		 */
		@Override
		public void doException(Exception arg0) {
			// ã�ªã�«ã‚‚ã�—ã�ªã�„
		}
	};

/*
    public static void quitApplication() {
        if (isBtOnByUs()) { // || NXJUploader.isBtOnByUs()) {
            BluetoothAdapter.getDefaultAdapter().disable();
            setBtOnByUs(false);
        }
        //splashMenu.finish();

    }
*/
    //private View splashMenuView;

    //private static Activity splashMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        res = getResources();
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mRobotType=lookupRobotType();
        
        setContentView(R.layout.dora_droid);
        
        tts = new TextToSpeech(getApplicationContext(), ttsInitListener);
		rsnp = new RSNPController(callback);
		scribeView = new SurfaceScribeView(this);
		// ObjectHolderã�«ç™»éŒ²
		ObjectHolder.getInstance().add(ImageProvidor.class.getName(), scribeView);

		LinearLayout layout = (LinearLayout) findViewById(R.id.layoutMain);
		layout.addView(scribeView);
       		
		//thisActivity = this;
        mRobotType = this.getIntent().getIntExtra(DoraDroid.MINDDROID_ROBOT_TYPE, R.id.robot_type_1);
        setUpByType();
        
        //buttons to control NXT from this application. 
        //They are to test robot control w/o connecting to a web service.
         Button button;
		 button = (Button) findViewById(R.id.right_button);
		 button.setOnClickListener(new View.OnClickListener() {
		 	public void onClick(View arg0) { updateMotorControl(0,20); }
		 });
		 button = (Button) findViewById(R.id.left_button);
		 button.setOnClickListener(new View.OnClickListener() {
		 	public void onClick(View arg0) { updateMotorControl(20,0); }
		 });
		 button = (Button) findViewById(R.id.stop_button);
		 button.setOnClickListener(new View.OnClickListener() {
		 	public void onClick(View arg0) {updateMotorControl(0,0);}
		 });
		 button = (Button) findViewById(R.id.forward_button);
		 button.setOnClickListener(new View.OnClickListener() {
		 	public void onClick(View arg0) {updateMotorControl(20,20);}
		 });
		 button = (Button) findViewById(R.id.back_button);
		 button.setOnClickListener(new View.OnClickListener() {
		 	public void onClick(View arg0) {updateMotorControl(-20,-20);}
		 });
		
		reusableToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
    }
    
    /**
     * Initialization of the motor commands for the different robot types.
     */
    private static void setUpByType() {
        switch (mRobotType) {
            case R.id.robot_type_2:
System.out.println("type2");            	
                motorLeft = BTCommunicator.MOTOR_B;
                directionLeft = 1;
                motorRight = BTCommunicator.MOTOR_C;
                directionRight = 1;
                motorAction = BTCommunicator.MOTOR_A;
                directionAction = 1;
                break;
            case R.id.robot_type_3:
            	System.out.println("type3");
                motorLeft = BTCommunicator.MOTOR_C;
                directionLeft = -1;
                motorRight = BTCommunicator.MOTOR_B;
                directionRight = -1;
                motorAction = BTCommunicator.MOTOR_A;
                directionAction = 1;
                break;
            default:
                // default
            	System.out.println("type default");
                motorLeft = BTCommunicator.MOTOR_B;
                directionLeft = 1;
                motorRight = BTCommunicator.MOTOR_C;
                directionRight = 1;
                motorAction = BTCommunicator.MOTOR_A;
                directionAction = 1;
                break;
        }
    }
    
    /**
     * Creates a new object for communication to the NXT robot via bluetooth and fetches the corresponding handler.
     */
    private void createBTCommunicator() {
        // interestingly BT adapter needs to be obtained by the UI thread - so we pass it in in the constructor
        myBTCommunicator = new BTCommunicator(this, myHandler, BluetoothAdapter.getDefaultAdapter(), getResources());
        btcHandler = myBTCommunicator.getHandler();
    }

    /**
     * Creates and starts the a thread for communication via bluetooth to the NXT robot.
     * @param mac_address The MAC address of the NXT robot.
     */
    private void startBTCommunicator(String mac_address) {
        connected = false;
        System.err.println("startBTCommunicator: connecting");
        connectingProgressDialog = ProgressDialog.show(this, "", res.getString(R.string.connecting_please_wait), true);

        if (myBTCommunicator != null) {
            try {
                myBTCommunicator.destroyNXTconnection();
            }
            catch (IOException e) { }
        }
        createBTCommunicator();
        myBTCommunicator.setMACAddress(mac_address);
        myBTCommunicator.start();
    }

    /**
     * Sends a message for disconnecting to the communication thread.
     */
    public static void destroyBTCommunicator() {

        if (myBTCommunicator != null) {
            sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.DISCONNECT, 0, 0);
            myBTCommunicator = null;
        }

        connected = false;
    }


    /**
     * Sends the motor control values to the communication thread.
     * @param left The power of the left motor 
     * @param rigth The power of the right motor
     */   
    public static void updateMotorControl(int left, int right) {

        if (myBTCommunicator != null) {
            // don't send motor stop twice
            if ((left == 0) && (right == 0)) {
                if (stopAlreadySent)
                    return;
                else
                    stopAlreadySent = true;
            }
            else
                stopAlreadySent = false;         
                        
            // send messages via the handler
            sendBTCmessage(BTCommunicator.NO_DELAY, motorLeft, left * directionLeft, 0);
            sendBTCmessage(BTCommunicator.NO_DELAY, motorRight, right * directionRight, 0);
        }
    }

    /**
     * Sends the message via the BTCommuncator to the robot.
     * @param delay time to wait before sending the message.
     * @param message the message type (as defined in BTCommucator)
     * @param value1 first parameter
     * @param value2 second parameter
     */   
    static void sendBTCmessage(int delay, int message, int value1, int value2) {
        Bundle myBundle = new Bundle();
        myBundle.putInt("message", message);
        myBundle.putInt("value1", value1);
        myBundle.putInt("value2", value2);
        Message myMessage = myHandler.obtainMessage();
        myMessage.setData(myBundle);

        if (delay == 0)
            btcHandler.sendMessage(myMessage);

        else
            btcHandler.sendMessageDelayed(myMessage, delay);
    }

    /**
     * Sends the message via the BTCommuncator to the robot.
     * @param delay time to wait before sending the message.
     * @param message the message type (as defined in BTCommucator)
     * @param String a String parameter
     */       
    static void sendBTCmessage(int delay, int message, String name) {
        Bundle myBundle = new Bundle();
        myBundle.putInt("message", message);
        myBundle.putString("name", name);
        Message myMessage = myHandler.obtainMessage();
        myMessage.setData(myBundle);

        if (delay == 0)
            btcHandler.sendMessage(myMessage);
        else
            btcHandler.sendMessageDelayed(myMessage, delay);
    }
    /**
     * Starts a program on the NXT robot.
     * @param name The program name to start. Has to end with .rxe on the LEGO firmware and with .nxj on the 
     *             leJOS NXJ firmware.
     */   
    public void startProgram(String name) {
        // for .rxe programs: get program name, eventually stop this and start the new one delayed
        // is handled in startRXEprogram()
    	System.err.println("startProgram");
        if (name.endsWith(".rxe")) {
            programToStart = name;        
            sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.GET_PROGRAM_NAME, 0, 0);
            return;
        }
              
        // for .nxj programs: stop bluetooth communication after starting the program
        if (name.endsWith(".nxj")) {
            sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.START_PROGRAM, name);
            destroyBTCommunicator();
            return;
        }        

        // for all other programs: just start the program
        sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.START_PROGRAM, name);
    }

    /**
     * Depending on the status (whether the program runs already) we stop it, wait and restart it again.
     * @param status The current status, 0x00 means that the program is already running.
     */   
    public static void startRXEprogram(byte status) {
        if (status == 0x00) {
            sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.STOP_PROGRAM, 0, 0);
            sendBTCmessage(1000, BTCommunicator.START_PROGRAM, programToStart);
        }    
        else {
            sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.START_PROGRAM, programToStart);
        }
    }     
    
    @Override
    protected void onDestroy() {
    	// RSNPåˆ‡æ–­
    	rsnp.disconnect();
    	// TTSçµ‚äº†
    	tts.shutdown();
        super.onDestroy();
        destroyBTCommunicator();
    }
    
    @Override
    protected void onStart() {
        super.onStart();

        if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else {
            selectNXT();
        }
        if (!rsnp.isConnected()) {
        	System.out.println("Connecting");
        	rsnp.connect();
        }
    }
    
    void selectNXT() {
    	Intent serverIntent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
    }

    /**
     * @return true, when currently pairing 
     */
    @Override
    public boolean isPairing() {
        return pairing;
    }
    
    public static String getNXTAddress(){
    	return NXTAddress;
    }
    /**
     * Asks if bluetooth was switched on during the runtime of the app. For saving 
     * battery we switch it off when the app is terminated.
     * @return true, when bluetooth was switched on by the app
     */
    /*public static boolean isBtOnByUs() {
        return btOnByUs;
    }*/
    /**
     * Sets a flag when bluetooth was switched on durin runtime
     * @param btOnByUs true, when bluetooth was switched on by the app
     */
    /*public static void setBtOnByUs(boolean btOn) {
        btOnByUs = btOn;
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:

                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address and start a new bt communicator thread
                	String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    pairing = data.getExtras().getBoolean(DeviceListActivity.PAIRING);
                    startBTCommunicator(address);
                }
                
                break;
                
            case REQUEST_ENABLE_BT:

                // When the request to enable Bluetooth returns
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        btOnByUs = true;
                        selectNXT();
                        break;
                    case Activity.RESULT_CANCELED:
                        showToast(R.string.bt_needs_to_be_enabled, Toast.LENGTH_SHORT);
                        finish();
                        break;
                    default:
                        showToast(R.string.problem_at_connecting, Toast.LENGTH_SHORT);
                        finish();
                        break;
                }
                
                break;

            // will not be called now, since the check intent is not generated                
            case TTS_CHECK_CODE:
            	/*
                if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                    // success, create the TTS instance
                    mTts = new TextToSpeech(this, this);
                } 
                else {
                    // missing data, install it
                    Intent installIntent = new Intent();
                    installIntent.setAction(
                        TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                    startActivity(installIntent);
                }
                */
                break;                
        }
    }
    
    /**
     * Displays a message as a toast
     * @param textToShow the message
     * @param length the length of the toast to display
     */
    private static void showToast(String textToShow, int length) {
        reusableToast.setText(textToShow);
        reusableToast.setDuration(length);
        reusableToast.show();
    }

    /**
     * Displays a message as a toast
     * @param resID the resource ID to display
     * @param length the length of the toast to display
     */
    private void showToast(int resID, int length) {
        reusableToast.setText(resID);
        reusableToast.setDuration(length);
        reusableToast.show();
    }

    @Override
    protected void onPause() {
       /* if (isBtOnByUs()) {// || NXJUploader.isBtOnByUs()) {
            BluetoothAdapter.getDefaultAdapter().disable();
            setBtOnByUs(false);
            //NXJUploader.setBtOnByUs(false);
        }*/
        super.onPause();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }
    @Override
    public void onSaveInstanceState(Bundle icicle) {
        super.onSaveInstanceState(icicle);
        //mView.unregisterListener();
    }

    /**
     * Creates the menu items
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    /**
     * Enables/disables the menu items
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	boolean rsnpConnected = rsnp.isConnected();
		if (menu != null) {
			MenuItem menuConnect = (MenuItem) menu.findItem(R.id.rsnp_connect);
			menuConnect.setEnabled(!rsnpConnected);

			MenuItem menuDisconnect = (MenuItem) menu.findItem(R.id.rsnp_disconnect);
			menuDisconnect.setEnabled(rsnpConnected);
		}
		return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Handles item selections
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.rsnp_connect:
    			rsnp.connect();
    			scribeView.setBackColor(Color.WHITE);
    			return true;
            case R.id.rsnp_disconnect:
    			rsnp.disconnect();
    			scribeView.setBackColor(Color.GRAY);
    			return true;
            case R.id.clear:
    			scribeView.clearStrokes();
    			return true;
            case R.id.quit:
            	destroyBTCommunicator();
                if (btOnByUs) {
                    showToast(R.string.bt_off_message, Toast.LENGTH_SHORT);
                    BluetoothAdapter.getDefaultAdapter().disable();
                    btOnByUs=false;
                }
                finish();
                return true;
            default:
    			return super.onContextItemSelected(item);
        }
    }
    
    /**
     * Receive messages from the BTCommunicator
     */
    final static Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message myMessage) {
            switch (myMessage.getData().getInt("message")) {
                case BTCommunicator.DISPLAY_TOAST:
                    showToast(myMessage.getData().getString("toastText"), Toast.LENGTH_SHORT);
                    break;
                case BTCommunicator.STATE_CONNECTED:
                    connected = true;
                    programList = new ArrayList<String>();
                    connectingProgressDialog.dismiss();
                    sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.GET_FIRMWARE_VERSION, 0, 0);
                    break;
                case BTCommunicator.MOTOR_STATE:

                    if (myBTCommunicator != null) {
                        byte[] motorMessage = myBTCommunicator.getReturnMessage();
                        int position = byteToInt(motorMessage[21]) + (byteToInt(motorMessage[22]) << 8) + (byteToInt(motorMessage[23]) << 16)
                                       + (byteToInt(motorMessage[24]) << 24);
                        showToast(res.getString(R.string.current_position) + position, Toast.LENGTH_SHORT);
                    }

                    break;

                case BTCommunicator.STATE_CONNECTERROR_PAIRING:
                    connectingProgressDialog.dismiss();
                    destroyBTCommunicator();
                    break;

                case BTCommunicator.STATE_CONNECTERROR:
                    connectingProgressDialog.dismiss();
                case BTCommunicator.STATE_RECEIVEERROR:
                case BTCommunicator.STATE_SENDERROR:

                    destroyBTCommunicator();
                    showToast(res.getString(R.string.bt_error_dialog_message), Toast.LENGTH_LONG);
                    
                    System.err.println("Doradroid handler received: STATE_CONNECTERROR/STATE_RECEIVEERROR/STATE_SENDERROR");
                    /*
                    if (btErrorPending == false) {
                        btErrorPending = true;
                        // inform the user of the error with an AlertDialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                        builder.setTitle(res.getString(R.string.bt_error_dialog_title))
                        .setMessage(getResources().getString(R.string.bt_error_dialog_message)).setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                btErrorPending = false;
                                dialog.cancel();
                                selectNXT();
                            }
                        });
                        builder.create().show();
                    }
*/
                    break;

                case BTCommunicator.FIRMWARE_VERSION:

                    if (myBTCommunicator != null) {
                        byte[] firmwareMessage = myBTCommunicator.getReturnMessage();
                        // check if we know the firmware
                        boolean isLejosMindDroid = true;
                        for (int pos=0; pos<4; pos++) {
                            if (firmwareMessage[pos + 3] != LCPMessage.FIRMWARE_VERSION_LEJOSMINDDROID[pos]) {
                                isLejosMindDroid = false;
                                break;
                            }
                        }
                        if (isLejosMindDroid) {
                            mRobotType = R.id.robot_type_4;
                            setUpByType();
                        }
                        // afterwards we search for all files on the robot
                        sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.FIND_FILES, 0, 0);
                    }

                    break;

                case BTCommunicator.FIND_FILES:

                    if (myBTCommunicator != null) {
                        byte[] fileMessage = myBTCommunicator.getReturnMessage();
                        String fileName = new String(fileMessage, 4, 20);
                        fileName = fileName.replaceAll("\0","");

                        if (mRobotType == R.id.robot_type_4 || fileName.endsWith(".nxj") || fileName.endsWith(".rxe")) {
                            programList.add(fileName);
                        }

                        // find next entry with appropriate handle, 
                        // limit number of programs (in case of error (endless loop))
                        if (programList.size() <= MAX_PROGRAMS)
                            sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.FIND_FILES,
                                           1, byteToInt(fileMessage[3]));
                    }

                    break;
                    
                case BTCommunicator.PROGRAM_NAME:
                    if (myBTCommunicator != null) {
                        byte[] returnMessage = myBTCommunicator.getReturnMessage();
                        startRXEprogram(returnMessage[2]);
                    }
                    
                    break;
                    
                case BTCommunicator.SAY_TEXT:
                    if (myBTCommunicator != null) {
                        byte[] textMessage = myBTCommunicator.getReturnMessage();
                        // evaluate control byte 
                        byte controlByte = textMessage[2];
                        // BIT7: Language
                        if ((controlByte & 0x80) == 0x00) 
                            tts.setLanguage(Locale.US);
                        else
                            tts.setLanguage(Locale.getDefault());
                        // BIT6: Pitch
                        if ((controlByte & 0x40) == 0x00)
                            tts.setPitch(1.0f);
                        else
                            tts.setPitch(0.75f);
                        // BIT0-3: Speech Rate    
                        switch (controlByte & 0x0f) {
                            case 0x01: 
                                tts.setSpeechRate(1.5f);
                                break;                                 
                            case 0x02: 
                                tts.setSpeechRate(0.75f);
                                break;
                            
                            default: tts.setSpeechRate(1.0f);
                                break;
                        }
                                                                                                        
                        String ttsText = new String(textMessage, 3, 19);
                        ttsText = ttsText.replaceAll("\0","");
                        showToast(ttsText, Toast.LENGTH_SHORT);
                        tts.speak(ttsText, TextToSpeech.QUEUE_FLUSH, null);
                        
                    }
                    
                    break;                    
                    
                case BTCommunicator.VIBRATE_PHONE:
                    break;
            }
        }
    };
    
    private static int byteToInt(byte byteValue) {
        int intValue = (byteValue & (byte) 0x7f);

        if ((byteValue & (byte) 0x80) != 0)
            intValue |= 0x80;

        return intValue;
    }

    public void setRobotType(int mRobotType) {
        SharedPreferences mUserPreferences = getSharedPreferences(MINDDROID_PREFS, Context.MODE_PRIVATE);
        Editor mPrefsEditor = mUserPreferences.edit();
        mPrefsEditor.putInt(MINDDROID_ROBOT_TYPE, mRobotType);
        mPrefsEditor.commit();
        this.mRobotType = mRobotType;
    }

    public int lookupRobotType() {
        SharedPreferences mUserPreferences;
        mUserPreferences =  getSharedPreferences(MINDDROID_PREFS, Context.MODE_PRIVATE);
        return mUserPreferences.getInt(MINDDROID_ROBOT_TYPE, R.id.robot_type_1);
    }

    public int getRobotType() {
        return mRobotType;
    }

}
