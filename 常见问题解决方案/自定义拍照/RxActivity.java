package com.example.hp.myapplication.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.hp.myapplication.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hp on 2017/4/7.
 */
public class RxActivity extends Activity implements SurfaceHolder.Callback, View.OnClickListener {
    private FloatingActionButton actionButton;
    private ImageView view;
    private Camera mCamera;
    private SurfaceHolder holder;
    private SurfaceView surfaceView;
    private Button photographBtn;
    private RelativeLayout interceptRlty;
    public static final int CAMERA_FOR_RESULT = 0x0001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx);
        initViews();
        initSurfaceHolder();
    }

    private void initSurfaceHolder() {
        holder = surfaceView.getHolder();
        holder.setFormat(PixelFormat.TRANSPARENT);//translucent半透明 transparent透明
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(this);
    }

    private void initCanmera() {
        Camera.Parameters parameters = mCamera.getParameters();//获取camera的parameter实例
        List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();//获取所有支持的camera尺寸
        Camera.Size optionSize = getOptimalPreviewSize(sizeList, surfaceView.getWidth(), surfaceView.getHeight());//获取一个最为适配的camera.size
        parameters.setPreviewSize(optionSize.width, optionSize.height);//把camera.size赋值到parameters
        parameters.setPictureFormat(ImageFormat.JPEG);
        mCamera.setParameters(parameters);//把parameters设置给camera
        mCamera.setDisplayOrientation(90);//将预览旋转90度
        mCamera.startPreview();//开始预览
    }

    private void initViews() {
        interceptRlty = (RelativeLayout) findViewById(R.id.capture_crop_view);
        photographBtn = (Button) findViewById(R.id.btn_photograph);
        photographBtn.setOnClickListener(this);
        surfaceView = (SurfaceView) findViewById(R.id.capture_preview);
        view = (ImageView) findViewById(R.id.capture_scan_line);
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation
                .RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                0.9f);
        animation.setDuration(4500);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        view.startAnimation(animation);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private static final String TAG = "RxActivity";

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera = Camera.open();
            mCamera.setPreviewDisplay(holder);
        } catch (IOException e) {
            if (null != mCamera) {
                mCamera.release();
                mCamera = null;
            }
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        initCanmera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (null != mCamera) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    @Override
    public void onClick(View v) {
        if (mCamera != null) {
            // 关闭拍照声音
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            // 下面这句代码可以根据系统音量的状态来开关拍照声音
            int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
            if (currentVolume == 0) {
                audioManager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
            }
            mCamera.takePicture(null, null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(final byte[] data, Camera camera) {
                    runOnUiThread(new TimerTask() {
                        @Override
                        public void run() {
                            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

                        }
                    });

                    Observable.create(new ObservableOnSubscribe<String>() {
                        @Override
                        public void subscribe(ObservableEmitter<String> e) throws Exception {
                            String filePath = saveImage(data);

                            if (TextUtils.isEmpty(filePath)) {
                                Log.d(TAG, "subscribe: 图片保存失败");
                            } else {
//                                intercept(filePath, interceptRlty);
                            }
                            e.onNext(filePath);
                            e.onComplete();
                        }
                    })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String filePath) throws Exception {
                                    Intent intent = new Intent();
                                    intent.putExtra("filePath", filePath);
                                    setResult(CAMERA_FOR_RESULT, intent);
                                    finish();
                                }
                            });
                }
            });
            if (currentVolume == 0) { //0代表静音或者震动
                final Handler soundHandler = new Handler();
                Timer t = new Timer();
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        soundHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                                audioManager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
                            }
                        });

                    }
                }, 1000);
            }
        }
    }

    /**
     * 截取图片
     *
     * @param imgPath
     */
    private void intercept(String imgPath, View view) {
        int[] viewLocation = new int[2];
        view.getLocationInWindow(viewLocation);
        int viewX = viewLocation[0]; // x 坐标
        int viewY = viewLocation[1]; // y 坐标

        Bitmap originalPitmap = BitmapFactory.decodeFile(imgPath);
        Bitmap bitmap = Bitmap.createBitmap(originalPitmap, view.getLeft(), view.getTop(), view.getLeft()+view.getWidth(), view.getTop()+view.getHeight());
        Log.d(TAG, "intercept: view.getleft = "+viewX);
        Log.d(TAG, "intercept: view.getTop = "+viewY);
        Log.d(TAG, "intercept: view.getWidth = "+view.getWidth());
        Log.d(TAG, "intercept: view.getHeight = "+view.getHeight());
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(imgPath));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    /**
     * 保存流
     *
     * @param data
     */
    private String saveImage(byte[] data) {
        String path = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String s = UUID.randomUUID().toString();
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + s + ".jpg";
        }

        Bitmap bm0 = BitmapFactory.decodeByteArray(data, 0, data.length);
        Matrix m = new Matrix();
        m.setRotate(90,(float) bm0.getWidth() / 2, (float) bm0.getHeight() / 2);
         Bitmap bitmap = Bitmap.createBitmap(bm0, 0, 0, bm0.getWidth(), bm0.getHeight(), m, true);


        File file = new File(path);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            return path;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
