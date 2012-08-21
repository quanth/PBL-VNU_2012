package rsnp.sample;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;

class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback, ImageProvidor
{
        private SurfaceHolder holder;
        private Camera camera;
        
        private Bitmap osb;
        private byte[] frame;
        private ReentrantLock lock = new ReentrantLock();
        private int semaphore; // check whether saveImage() done or not
        
        public int getSemaphore(){
                return this.semaphore;
        }
        public void setSemaphore(int semaphore){
        	this.semaphore = semaphore;
        }
        public class HandlePictureStorage implements PictureCallback
        {
    			/*private CameraSurfaceView cameraView; 
    			public HandlePictureStorage(CameraSurfaceView cameraView) {
    				this.cameraView = cameraView;
    			}*/
                @Override
                public void onPictureTaken(byte[] picture, Camera camera) 
                {                    
                        System.out.println("Picture successfully taken: "+picture);
                        
                        //String fileName = "shareme.jpg";
                        //String mime = "image/jpeg";
                        
                        CameraSurfaceView.this.saveImage(picture);
                        camera.startPreview();
                        CameraSurfaceView.this.semaphore = 1;
                }
        }
        
        Camera.PreviewCallback previewCallback = new Camera.PreviewCallback()  
        { 
                public void onPreviewFrame(byte[] data, Camera camera)  
                { 
                        try 
                        { 
	//                		System.err.println("onPreviewFrame");
	               	        lock.lock();
	                		CameraSurfaceView.this.frame = data.clone();
	                		osb = BitmapFactory.decodeByteArray(data, 0, data.length);;
	                		lock.unlock();
	//                		System.err.println("onPreviewFrame: " + osb);
	                		BitmapFactory.Options opts = new BitmapFactory.Options(); 
	                		Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);//,opts); 
                        } 
                        catch(Exception e) 
                        {

                        } 
                } 
        }; 
        
        public CameraSurfaceView(Context context) 
        {
                super(context);
                System.err.println("constructor");
                //Initiate the Surface Holder properly
                this.holder = this.getHolder();
                this.holder.addCallback(this);
                this.holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                
                this.semaphore = 0;
        }
        
        public void saveImage(byte[] picture) {
        	//lock.lock();
        	//frame = picture;
        	System.err.println("saveImage");
			osb = BitmapFactory.decodeByteArray(picture, 0, picture.length);
			//lock.unlock();
		}

	@Override
        public void surfaceCreated(SurfaceHolder holder) 
        {
                try
                {
                        //Open the Camera in preview mode
                        System.err.println("surfaceCreated");
                        this.camera = Camera.open();
                        this.camera.setPreviewDisplay(this.holder);
                        this.camera.setPreviewCallback(previewCallback); 
                }
                catch(IOException ioe)
                {
                        ioe.printStackTrace(System.out);
                }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) 
        {
                osb = null;
                System.err.println("surfacedChanged");
                Camera.Parameters parameters = camera.getParameters();
                List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();

                for (Camera.Size p : previewSizes) {
                	System.err.println(p.width + "," + p.height);
        		}
                // You need to choose the most appropriate previewSize for your app
                Camera.Size previewSize = previewSizes.get(previewSizes.size()-1); // .... select one of previewSizes here
                
                System.err.println(previewSize.width+","+ previewSize.height );
                
                parameters.setPreviewSize(previewSize.width, previewSize.height);
//                parameters.set("orientation", "portrait");
//                parameters.setRotation(0);
//                System.err.println(parameters);
                
                camera.setParameters(parameters);
                camera.setDisplayOrientation(90);
                camera.startPreview();
        }


        @Override
        public void surfaceDestroyed(SurfaceHolder holder) 
        {
                // Surface will be destroyed when replaced with a new screen
                //Always make sure to release the Camera instance
                System.err.println("surfacedDestroy");
                camera.stopPreview();
                camera.release();
                camera = null;
        }
        
        public Camera getCamera()
        {
                System.err.println("getCamera");
                return this.camera;
        }

        @Override
        public void takeImage(){
        	System.err.println("takeImage");
        	Camera camera = this.getCamera();
        	// CameraSurfaceView.HandlePictureStorage c = this.new HandlePictureStorage();
        	camera.takePicture(null, null, this.new HandlePictureStorage());
        }
	@Override
	public byte[] getImage(String type) {
		System.err.println("Camera.getImage: "+type);    
		if (osb==null) {
			System.err.println("Camera.getImage: osb NULL");
			return  null;
		}
		
		CompressFormat format = CompressFormat.JPEG;
		if (type.equals("PNG")) {
			format = CompressFormat.PNG;
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		//lock.lock();
		try {
			//osb = BitmapFactory.decodeByteArray(frame, 0, frame.length);;
			
			System.err.println("Camera.getImage: decoded");
			osb.compress(format, 100, out);
			System.err.println("Camera.getImage: compressed");
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} finally {
			//lock.unlock();
		}

		byte[] bytes = out.toByteArray();
		System.err.println("Camera.getImage: exit");
		return bytes;
	}
}