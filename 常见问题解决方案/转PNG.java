package com.weiyin.card_gobook.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Jason Chen on 2017/8/2.
 */

public class BitmapUtils {
    public static void toPNG(String pngFilePath, String jpgFilePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(jpgFilePath);
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(pngFilePath))) {
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 80, bos)) {
                bos.flush();
                bos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static File toPNG(String jpgFilePath){
        File jpgFile = new File(jpgFilePath);
        if(!jpgFile.exists()){
            return null;
        }
        File parentFile = jpgFile.getParentFile();
        if(!parentFile.exists() || !parentFile.isDirectory()){
            return null;
        }
        File pngFile = new File(parentFile.getAbsoluteFile()+"/photohead.png");
        try {
            pngFile.createNewFile();
            toPNG(pngFile.getAbsolutePath(),jpgFile.getAbsolutePath());
            return pngFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
