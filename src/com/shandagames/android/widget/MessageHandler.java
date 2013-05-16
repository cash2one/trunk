package com.shandagames.android.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class MessageHandler {

	public final static int PROCESSING_YUV_DATA = 1;
	public final static int PROCESSING_RGB_DATA = 2;
	public final static int DISPLAY_FACE_DATA = 3;

	public MessageHandler(Context context, int width, int height,
			Messenger dontshootme) {

		viewMessenger = dontshootme;

		imgWidth = width;
		imgHeight = height;
		rgb = new int[width * height];

		decodeYUV.start();
		findFace.start();
	}

	// The 2 working threads
	//
	// First Thread:
	// From an array of byte data in the YUV format (preview format from the
	// camera) to an array of int in RGB format. This array is then transform
	// into a bitmap - img.
	private DecodeYUV decodeYUV = new DecodeYUV();
	private Bitmap img;

	// Second thread:
	// Find one face from the above image.
	private FindFace findFace = new FindFace();

	// This is a messenger. We can send messages to the DuckView in order to
	// display the face when found.
	private Messenger viewMessenger;

	// This boolean tells us if we have processed the YUV data. If true, we get
	// a new array from the camera preview application using copyYuvData
	public boolean yuvProcessed = true;

	private int[] rgb;
	private byte[] yuv;

	// RGB image against which we will find the face
	private int imgWidth;
	private int imgHeight;

	// The final face detected
	private FaceDetector.Face face;

	private Handler queue = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case PROCESSING_YUV_DATA:
				decodeYUV.mHandler.sendEmptyMessage(0);
				break;
			case PROCESSING_RGB_DATA:
				findFace.mHandler.sendEmptyMessage(0);
				break;
			case DISPLAY_FACE_DATA:
				Message duckFace = new Message();
				duckFace.setData(bundle);
				try {
					viewMessenger.send(duckFace);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
		}
	};

	// So we can have other thread communicating here.
	public Handler getHandler() {
		return queue;
	}
	
	// This Thread decode the YUV into a RGB int buffer
	private class DecodeYUV extends Thread {
		public Handler mHandler;

		@Override
		public void run() {
			Looper.prepare();
			mHandler = new Handler() {
				public void handleMessage(Message msg) {
					yuvProcessed = false;
					Log.i("Duck", "begin decoding at"
							+ System.currentTimeMillis());
					decodeYUV420SP(rgb, yuv, imgWidth, imgHeight);
					img = Bitmap.createBitmap(rgb, imgWidth, imgHeight,
							Bitmap.Config.RGB_565);
					yuvProcessed = true;
					queue.sendEmptyMessage(PROCESSING_RGB_DATA);
				}
			};
			Looper.loop();
		}
	};

	Bundle bundle = new Bundle();

	/*
	 * This thread starts when we have generated a RGB image from the YUV
	 * preview image.
	 * 
	 * It will find the face from that RGB image if any.
	 * 
	 * Note that both threads run in parallel in order to gain some speed.
	 */
	private class FindFace extends Thread {
		public Handler mHandler;
		private static final int NUM_FACES = 1;
		private FaceDetector arrayFaces;
		private FaceDetector.Face allFaces[] = new FaceDetector.Face[NUM_FACES];

		@Override
		public void run() {
			Looper.prepare();
			mHandler = new Handler() {
				public void handleMessage(Message msg) {
					arrayFaces = new FaceDetector(imgWidth, imgHeight,
							NUM_FACES);
					arrayFaces.findFaces(img, allFaces);
					face = allFaces[0];
					if (face != null) {
						PointF po = new PointF();
						face.getMidPoint(po);
						bundle.clear();
						bundle.putFloat("centerx", po.x);
						bundle.putFloat("centery", po.y);
						bundle.putFloat("radius", face.eyesDistance());
						//msg.setData(bundle);
					}
					Log.i("Duck", "find a face with: " + face + " in "
							+ System.currentTimeMillis());
					queue.sendEmptyMessage(DISPLAY_FACE_DATA);
				}
			};
			Looper.loop();
		}
	};

	// gathering a local copy of the preview frame
	public void copyYuvData(byte[] data) {
		yuv = data.clone();
	}

	// Actual algorithm to decode YUV data taken from:
	// http://code.google.com/p/android/issues/detail?id=823#c4
	static public void decodeYUV420SP(int[] rgb, byte[] yuv420sp, int width,
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
}
