/**
 * 
 */
package com.shandagames.android.widget;

import java.io.OutputStream;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.PreviewCallback;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.shandagames.android.log.LogUtils;
import com.shandagames.android.util.CameraHelper;
import com.shandagames.android.util.CameraHelper.Size;

/**
 * @file CameraView.java
 * @create 2012-8-28 上午10:47:36
 * @author lilong
 * @description TODO
 */
public class CameraView extends SurfaceView implements Callback,
		PreviewCallback, PictureCallback {

	private static final String TAG = LogUtils.makeLogTag(CameraView.class);
	
	private Context mContext;
	private Camera mCamera;
	private FilterHandler mFilterHandler;
	private SurfaceHolder mSurfaceHolder;
	
	private boolean mPreviewRunning;
	private int mWidth, mHeight;

	public CameraView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		// TODO Auto-generated constructor stub
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		mWidth = display.getWidth();
		mHeight = display.getHeight();
		setKeepScreenOn(true);
		
		mSurfaceHolder = this.getHolder();
		mSurfaceHolder.setFixedSize(mWidth, mHeight);// 设置分辨率
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		/* 下面设置Surface不维护自己的缓冲区，而是等待屏幕的渲染引擎将内容推送到用户面前 */
		mSurfaceHolder.addCallback(this);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		LogUtils.d(TAG, "surfaceCreated");
		if (mCamera == null) {
			// 开启相机,不能放在构造函数中，不然不会显示画面.
			mCamera = Camera.open();
		}
//		try {
//			mCamera.setPreviewDisplay(mSurfaceHolder);
//		} catch (IOException ex) {
//			// TODO Auto-generated catch block
//			stopCamera();
//			ex.printStackTrace();
//		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		LogUtils.d(TAG, "surfaceChanged");
		if (mPreviewRunning) {
			mCamera.stopPreview();
		}
		Camera.Parameters parameters = mCamera.getParameters(); // 获得相机参数
		CameraHelper cameraHelper = new CameraHelper(parameters);
		//List<Size> supportedPictureSizes = cameraHelper.getSupportedPictureSizes();
		List<Size> supportedPreviewSizes = cameraHelper.getSupportedPreviewSizes();
		if(this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
			parameters.set("orientation", "portrait");     
			parameters.set("rotation", 90);
		} else { //android2.2以上使用camera.setDisplayOrientation(90);     
			parameters.set("orientation", "landscape"); 
		}
		if (!supportedPreviewSizes.isEmpty()) {
			Size size = supportedPreviewSizes.get(0);
			parameters.setPreviewSize(size.width, size.height);// 设置预览照片的大小
		}
		parameters.setPreviewFrameRate(3);// 每秒3帧
		parameters.setPreviewFormat(PixelFormat.YCbCr_420_SP);
		parameters.setPictureFormat(PixelFormat.JPEG);// 设置照片的输出格式
		parameters.set("jpeg-quality", 100);// 照片质量
		parameters.setPictureSize(mWidth, mHeight);// 设置照片的大小
		
		mCamera.setParameters(parameters);
		mCamera.setPreviewCallback(this); // 通过SurfaceView显示取景画面
		mCamera.startPreview();// 开始预览
		mCamera.autoFocus(null);
		mPreviewRunning = true;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		LogUtils.d(TAG, "surfaceDestroyed");
		stopCamera();
	}

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		// TODO Auto-generated method stub
		if (data != null) {
			int imageWidth = camera.getParameters().getPreviewSize().width;
			int imageHeight = camera.getParameters().getPreviewSize().height;
			int RGBData[] = new int[imageWidth * imageHeight];
			
			decodeYUV420SP(RGBData, data, imageWidth, imageHeight); // 解码
			Bitmap bitmap = Bitmap.createBitmap(RGBData, imageWidth, imageHeight,
					Config.ARGB_8888);
			if (mFilterHandler != null) {
				bitmap = mFilterHandler.onBitmapFilter(bitmap);// 冰冻效果
			}
			Canvas canvas = null;
			try {
				canvas = mSurfaceHolder.lockCanvas();
				// 判断非null，才能drawBitmap.
				if (bitmap != null && canvas != null) {
					bitmap = Bitmap.createScaledBitmap(bitmap, mWidth, mHeight, false);
					canvas.drawBitmap(bitmap, 0, 0, null);
				}
			} finally {
				if (canvas != null) {
					mSurfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
		}
	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		// TODO Auto-generated method stub
		try {
			Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			if (mFilterHandler != null) {
				bitmap = mFilterHandler.onBitmapFilter(bitmap);
			}
			
			Uri imageUri =mContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
			OutputStream outStream = mContext.getContentResolver().openOutputStream(imageUri);
			bitmap.compress(CompressFormat.JPEG, 100, outStream);
			outStream.flush();
			outStream.close();
			
			camera.stopPreview();
			//camera.startPreview();// 重新开始照相预览
		} catch (Exception ex) {
			LogUtils.e(TAG, ex.toString());
			ex.printStackTrace();
		}
	}

	/**
	 * 解码
	 */
	private void decodeYUV420SP(int[] rgb, byte[] yuv420sp, int width,
			int height) {
		final int frameSize = width * height;
		for (int j = 0, yp = 0; j < height; j++) {
			int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
			for (int i = 0; i < width; i++, yp++) {
				int y = (0xff & ((int) yuv420sp[yp])) - 16;
				if (y < 0)
					y = 0;
				if ((i & 1) == 0) {
					v = (0xff & yuv420sp[uvp++]) - 128;
					u = (0xff & yuv420sp[uvp++]) - 128;
				}
				int y1192 = 1192 * y;
				int r = (y1192 + 1634 * v);
				int g = (y1192 - 833 * v - 400 * u);
				int b = (y1192 + 2066 * u);

				if (r < 0)
					r = 0;
				else if (r > 262143)
					r = 262143;
				if (g < 0)
					g = 0;
				else if (g > 262143)
					g = 262143;
				if (b < 0)
					b = 0;
				else if (b > 262143)
					b = 262143;
				rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000)
						| ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
			}
		}
	}

	
	public void takePicture() {
		if (mCamera != null) {
			mCamera.takePicture(null, null, this);
		}
	}

	public void stopCamera() {
		if (mCamera != null) {
			mPreviewRunning = false;
			mCamera.setPreviewCallback(null);
			mCamera.stopPreview();// 停止预览
			mCamera.release();// 释放相机资源
			mCamera = null;
		}
	}

	public void setFilterHandler(FilterHandler mFilterHandler) {
		this.mFilterHandler = mFilterHandler;
	}
	
	public static interface FilterHandler {
		
		Bitmap onBitmapFilter(Bitmap bitmap);
		
		void onBitmapFinished(Bitmap bitmap);
	}
}
