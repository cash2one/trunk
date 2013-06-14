package com.shandagames.android.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.util.Log;

/**
 * @file ImageCache.java
 * @create 2013-6-3 下午01:38:43
 * @author lilong (dreamxsky@gmail.com)
 * @description TODO 封装简单的图片下载处理
 */
public final class ImageCache extends Observable {
	private static final String TAG = "ImageCache";
	private static final String NOMEDIA = ".nomedia";
	private static final String CACHE_DIRECTORY = "cache/images";
	
	private File baseDirectory;
	private ExecutorService mExecutor;
	private HttpClient mHttpClient;
	private LruCache<String, Bitmap> lruCache;
	private static ImageCache _instance;
	
	private ConcurrentHashMap<String, Runnable> mActiveRequestsMap = new ConcurrentHashMap<String, Runnable>();
	
	public static void initInstance(String dirPath) {
		_instance = new ImageCache(dirPath);
	}
	
	private ImageCache(String dirPath) {
		mHttpClient = createHttpClient();
	    mExecutor = Executors.newCachedThreadPool();
	        
    	// use 25% of available heap size
		int maxSize = (int)(Runtime.getRuntime().maxMemory() / 4);
		Log.i(TAG, "load bitmap available memory "+maxSize);
		lruCache = new LruCache<String, Bitmap>(maxSize) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();  
			}
		};
		
		baseDirectory = new File(Environment.getExternalStorageDirectory(), dirPath);
	}

	public static ImageCache getInstance() {
		return _instance;
	}
	
	public Bitmap displayBitmap(String url) {
		if (url == null || url.length() == 0)
			return null;
		
		if (lruCache.get(url) != null) { 
			return lruCache.get(url);
		}
		
		File file = getFile(url);
		if (file!=null && file.isFile() 
				&& file.exists()) {
			Bitmap bitmap = decodeFile(file);
			if (bitmap != null) {
				lruCache.put(url, bitmap);
			}
			return bitmap;
		}

		return null;
	}
	
	public void request(String url) {
		if (url == null || url.length() == 0)
			return;
		
		if (!isExternalStorageMounted())
			return;
		
        synchronized (mActiveRequestsMap) {
        	if (!mActiveRequestsMap.containsKey(url)) {
        		Log.i(TAG, "issuing new request for: " + url);
        		Runnable fetcher = newRequestCall(url);
        		mActiveRequestsMap.put(url, fetcher);
        		mExecutor.submit(fetcher);
        	} else {
        		Log.i(TAG, "Already have a pending request for: " + url);
        	}
        }
    }
	
	public File getFile(String url) {
		if (isExternalStorageMounted()) {
			String filePath = hashKeyForDisk(url);
			return new File(getStorageDirectory(), filePath);
		} 
		return null;
	}
	
	public boolean exists(String url) {
		File file = getFile(url);
		return (file!=null && file.isFile() && file.exists());
	}

	public boolean invalidate(String url) {
		File file = getFile(url);
		return (file!=null && file.delete());
	}
	
	public void clearCache() {
		lruCache.evictAll();
	}
	
	public void clear() {
        // Clear the whole cache. Coolness.
		if (isExternalStorageMounted()) {
			File mStorageDirectory = getStorageDirectory();
			String[] children = mStorageDirectory.list();
	        if (children != null) { // children will be null if hte directyr does not exist.
	            for (int i = 0; i < children.length; i++) {
	                File child = new File(mStorageDirectory, children[i]);
	                if (!child.equals(new File(mStorageDirectory, NOMEDIA))) {
	                    Log.v(TAG, "Deleting: " + child);
	                    child.delete();
	                }
	            }
	        }
	        mStorageDirectory.delete();
		}
    }
	
	public void clearAll() {
		clear();
		clearCache();
	}
	
	private boolean isExternalStorageMounted() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
	private File getStorageDirectory() {
		return new File(baseDirectory, CACHE_DIRECTORY);
	}
	
	private File createDirectory() throws IOException {
		File storageDirectory = getStorageDirectory();
        if (!storageDirectory.exists()) {
        	storageDirectory.mkdirs();
        	new File(storageDirectory, NOMEDIA).createNewFile();
        }
        return storageDirectory;
    }
	
    private String hashKeyForDisk(String key) {
    	// A hashing method that changes a string (like a URL) into a hash suitable for using as a disk filename.
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
	
	private Runnable newRequestCall(final String url) {
		return new Runnable() {
			@Override
			public void run() {
				HttpGet httpGet = null;
                try {
                    Log.v(TAG, "Requesting: " + url);
                    httpGet = new HttpGet(url);
                    httpGet.addHeader("Accept-Encoding", "gzip");
                    HttpResponse response = mHttpClient.execute(httpGet);
                    if (response.getStatusLine().getStatusCode()==200) {
                    	HttpEntity entity = response.getEntity();
	                    store(url, entity.getContent());
	                    entity.consumeContent();
	                    Log.v(TAG, "Request successful: " + url);
                    }
                } catch (MalformedURLException e) {
    				Log.w(TAG, "Malformed image url.", e);
    			} catch (FileNotFoundException e) {
    				Log.w(TAG, "Ignoring url because it could not be found: " + url);
    			} catch (IOException e) {
    				Log.w(TAG, "Could not fetch image, could not load.", e);
    			} catch (OutOfMemoryError e) {
    				Log.e("", "Could not fetch image, ran out of memory.", e);
    			} finally {
                    Log.v(TAG, "Request finished: " + url);
                    if (httpGet!=null) httpGet.abort();
                    mActiveRequestsMap.remove(url);
                    notifyObservers(url);
                }
			}
		};
    }
	
	public void store(String url, InputStream is) {
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		BufferedOutputStream os = null;
		try {
			String filePath = hashKeyForDisk(url);
			File targetFile = new File(createDirectory(), filePath);
			bis = new BufferedInputStream(new FlushedInputStream(is), 8 * 1024);
			fos = new FileOutputStream(targetFile);
			os = new BufferedOutputStream(fos, 8 * 1024);
			byte[] b = new byte[2048];
			int count = 0;
			while ((count = bis.read(b)) > 0) {
				os.write(b, 0, count);
			}
			os.flush();
			fos.flush();
		} catch (IOException e) {
			Log.e(TAG, "store ex=" + e.toString(), e);
		} finally {
			try {
				if (bis != null) {
					bis.close();
				}
				if (os != null) {
					os.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}
	
	/** decodes image and scales it to reduce memory consumption */
	public Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			FileInputStream stream1=new FileInputStream(f);
			BitmapFactory.decodeStream(stream1, null, o);
			stream1.close();

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 70;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			o2.inPurgeable = true;
			FileInputStream stream2=new FileInputStream(f);
			Bitmap bitmap=BitmapFactory.decodeStream(stream2, null, o2);
			stream2.close();
			return bitmap;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			lruCache.evictAll();
			System.gc();
		}
		return null;
	}

	public void copyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}
	
	private final DefaultHttpClient createHttpClient() {
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setStaleCheckingEnabled(params, false);
		HttpConnectionParams.setConnectionTimeout(params, 60 * 1000);
		HttpConnectionParams.setSoTimeout(params, 60 * 1000);
		HttpConnectionParams.setSocketBufferSize(params, 8192);
		final SchemeRegistry supportedSchemes = new SchemeRegistry();
		final SocketFactory sf = PlainSocketFactory.getSocketFactory();
		supportedSchemes.register(new Scheme("http", sf, 80));
		final ClientConnectionManager ccm = new ThreadSafeClientConnManager(
				params, supportedSchemes);
		return new DefaultHttpClient(ccm, params);
	}

	@Override
	public void notifyObservers(Object data) {
		setChanged();
		super.notifyObservers(data);
	}

	class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0L;
            while (totalBytesSkipped < n) {
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0L) {
                    int b = read();
                    if (b < 0) {
                        break;  // we reached EOF
                    } else {
                        bytesSkipped = 1; // we read one byte
                    }
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }
	
	public static abstract class ResourceRequestObserver implements Observer {

        private String mRequestUri;

        abstract public void requestReceived(Observable observable, String uri);

        public ResourceRequestObserver(String requestUri) {
            mRequestUri = requestUri;
        }

        @Override
        public void update(Observable observable, Object data) {
            Log.i(TAG, "Recieved update: " + data);
            String dataUri = data.toString();
            if (dataUri.equals(mRequestUri)) {
                requestReceived(observable, dataUri);
            }
        }
    }
}
