package com.example.b_0025_exam_lrucache_1;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

public class ImageLoader {
	private static final String LOACTION = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/abc/";
	private LruCache<String, Bitmap> cache;
	private ExecutorService es = Executors.newFixedThreadPool(5);

	public ImageLoader() {
		super();
		int maxSize = (int) (Runtime.getRuntime().maxMemory() / 8);
		this.cache = new LruCache<String, Bitmap>(maxSize) {

			@SuppressLint("NewApi")
			@Override
			protected int sizeOf(String key, Bitmap value) {
				// TODO Auto-generated method stub
				if (Build.VERSION.SDK_INT == Build.VERSION_CODES.HONEYCOMB_MR1) {
					return value.getByteCount();
				}
				return value.getRowBytes() * value.getHeight();
			}
		};
	}

	public void setImage(ImageView image, String urlString) {
		
		image.setTag(urlString);

		Bitmap bitmap = cache.get(urlString);
		if (bitmap != null) {
			showImage(image, bitmap, urlString);
		}

		GetBitmapRunnable gbr = new GetBitmapRunnable(image, urlString);
		es.execute(gbr);
	}

	private class GetBitmapRunnable implements Runnable {

		private ImageView image;
		private String urlString;

		public GetBitmapRunnable(ImageView image, String urlString) {
			super();
			this.image = image;
			this.urlString = urlString;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Bitmap bitmap = getBitmapFromLocal(urlString);
			if (bitmap != null) {
				showImage(image, bitmap, urlString);
			}
			bitmap = getBitmapFromNet(urlString);
			if (bitmap != null) {
				showImage(image, bitmap, urlString);
			}
		}
	}

	private Bitmap getBitmapFromNet(String urlString) {
		File file = new File(LOACTION, MD5.encrypt(urlString));
		FileUtil.downloadFile(urlString, file);
		return getBitmapFromLocal(urlString);
	}

	private Bitmap getBitmapFromLocal(String urlString) {
		File file = new File(LOACTION, MD5.encrypt(urlString));
		Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
		if (bitmap != null) {
			cache.put(urlString, bitmap);
		}
		return bitmap;
	}

	private void showImage(final ImageView image, final Bitmap bitmap,
			String urlString) {
		if (!image.getTag().equals(urlString)) {
			return;
		}
		Activity ac = (Activity) image.getContext();
		ac.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				image.setImageBitmap(bitmap);
			}
		});
	}
}
