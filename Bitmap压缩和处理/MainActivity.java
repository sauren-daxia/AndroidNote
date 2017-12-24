package com.bwf.a_096_bitmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
/**
 * 
 * 
 *
 */
public class MainActivity extends Activity {
	private ImageView img;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		img = (ImageView) findViewById(R.id.img);
	}

	/**
	 * �ü���ť ����Ļص�����
	 * @param v
	 */
	public void click1(View v){
		//�Ƚ���õ�һ��BitmapԴ
		Bitmap srcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tangtang);
		Bitmap bitmap = Bitmap.createBitmap(srcBitmap, srcBitmap.getWidth()/2, srcBitmap.getHeight()/2,
				srcBitmap.getWidth()/2, srcBitmap.getHeight()/2);
		img.setImageBitmap(bitmap);
	}

	public void click2(View v){
		//�Ƚ���õ�һ��BitmapԴ
		Bitmap srcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tangtang);
		Matrix m = new Matrix();
		//		m.setTranslate(srcBitmap.getWidth()/2, srcBitmap.getHeight()/2);
		//����
		m.setRotate(180); //˳ʱ����ת90��
		m.postScale(0.6f, 0.6f,srcBitmap.getWidth()/2,srcBitmap.getHeight()/2);
		m.preRotate(-50);
		//����3��������˳���ǣ�Rotate(-90)��Rotate(180)��Scale(0.2f, 0.2f...)
		m.postSkew(0.3f, 0.3f);
		Bitmap bitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(), srcBitmap.getHeight(), m , true);
		img.setImageBitmap(bitmap);
	}

	public void click3(View v){
		//�Ƚ���õ�һ��BitmapԴ
		Bitmap srcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tangtang);
		//����һ����srcBitmap��Сһ�µĿհ׵�Bitmap
		Bitmap bitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);//����һ������
		//ͨ������ʵ�� ͼƬ���µ���
		Matrix matrix = new Matrix();
		matrix.setScale(1, -1); //
		matrix.postTranslate(0, srcBitmap.getHeight());
		canvas.drawBitmap(srcBitmap, matrix , null);
		Paint paint = new Paint();
		paint.setColor(0xffff0000);
		canvas.drawCircle(100, 100, 50, paint);
		img.setImageBitmap(bitmap);
		
		File file = new File(Environment.getExternalStorageDirectory().getPath()+"/tangtang.jpg");
		try {
			bitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
