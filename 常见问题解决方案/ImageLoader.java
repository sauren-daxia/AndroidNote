package com.bwf.a_092_listview;

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
import android.util.Log;
import android.widget.ImageView;

public class ImageLoader {
	private static final String PIC_SAVE_PATH = Environment.getExternalStorageDirectory().getPath()
			+ "/com.bwf.lru_test/";
	private LruCache<String, Bitmap> mCache;
	/**线程池 避免创建大量线程对象**/
	private ExecutorService mExecutor = Executors.newFixedThreadPool(5);
	public ImageLoader(ImageLoaderListener listener) {
		int maxSize = (int) (Runtime.getRuntime().maxMemory()/8);
		mCache = new LruCache<String, Bitmap>(maxSize){
			@SuppressLint("NewApi")
			@Override
			protected int sizeOf(String key, Bitmap value) {
				if(Build.VERSION.SDK_INT < 12){
					return value.getRowBytes()*value.getHeight();
				}
				return value.getByteCount();
			}
		};
		this.listener = listener;
	}
	private ImageLoaderListener listener;
	/**
	 * 入口
	 * @param imgageView
	 * @param imgUrl
	 */
	public void displayImg(ImageView imgageView,String imgUrl){
		imgageView.setTag(imgUrl);
		Bitmap bitmap = getBitmapFromLru(imgUrl);
		if(bitmap != null){
			callBack(bitmap,imgageView,imgUrl);
			return;
		}
		GetBitmapTask task = new GetBitmapTask(imgageView, imgUrl);
		mExecutor.execute(task);
	}

	/**
	 * 成功获取到Bitmap之后回调。
	 * @param bitmap
	 * @param imgageView
	 * @param imgUrl
	 */
	private void callBack(final Bitmap bitmap, final ImageView imgageView, final String imgUrl) {
		if(!imgUrl.equals(imgageView.getTag())){
			return;
		}
		if(listener != null){
			Activity aty = (Activity) imgageView.getContext();
			aty.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					listener.success(bitmap, imgageView, imgUrl);
				}
			});
		}
	}
	private Bitmap getBitmapFromLru(String imgUrl) {
		return mCache.get(imgUrl);
	}

	public interface ImageLoaderListener{
		void success(Bitmap bitmap,ImageView img,String imgStr);
	}

	private class GetBitmapTask implements Runnable{
		private ImageView imageView;
		private String imgUrl;
		public GetBitmapTask(ImageView imageView, String imgUrl) {
			super();
			this.imageView = imageView;
			this.imgUrl = imgUrl;
		}

		@Override
		public void run() {
			Bitmap bitmap = getBitmapFromLocal(imgUrl);
			if(bitmap != null){
				callBack(bitmap, imageView, imgUrl);
				return;
			}
			bitmap = getBitmapFromNetwork(imgUrl);
			if(bitmap != null){
				callBack(bitmap, imageView, imgUrl);
			}
		}

	}

	private Bitmap getBitmapFromLocal(String imgUrl) {
		File file = new File(PIC_SAVE_PATH, MD5.encrypt(imgUrl));
		if(!file.exists()){
			return null;
		}
		Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
		if(bitmap != null){
			mCache.put(imgUrl, bitmap);
		}else{
			Log.d("MyAdapter", "bitamp--->:"+bitmap+","+imgUrl);
		}
		return bitmap;
	}

	public Bitmap getBitmapFromNetwork(String imgUrl) {
		File file = new File(PIC_SAVE_PATH, MD5.encrypt(imgUrl));
		FileUtil.downloadFile(imgUrl, file);
		return getBitmapFromLocal(imgUrl);
	}
}
