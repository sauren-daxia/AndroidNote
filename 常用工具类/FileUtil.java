package com.example.b_0025_exam_lrucache_1;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class FileUtil {
	/**
	 * 下载文件
	 * @param imgUrl
	 * @param dstFile
	 */
	public  static void downloadFile(String imgUrl,File dstFile){
		//如果父目录不存在，就创建父目录
		if(!dstFile.getParentFile().exists()){
			dstFile.getParentFile().mkdirs();
		}
		try {
			URL url = new URL(imgUrl);
			URLConnection conn = url.openConnection();
			InputStream is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dstFile));
			byte[] buff = new byte[1024*4];
			int len;
			while((len = bis.read(buff)) != -1){
				bos.write(buff, 0, len);
			}
			bos.flush();
			bos.close();
			bis.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
