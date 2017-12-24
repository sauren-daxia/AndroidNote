package com.chenzj.baselibrary.base.bean;

import java.io.File;

/**
 * Created by Jason Chen on 2017/11/14.
 * 文件上传实体类
 */

public class UploadPhotoBean {
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 缓存压缩文件
     */
    private File cacheFile;
    /**
     * 文件路径
     */
    private String path;
    /**
     * (文件类型)
     */
    private String mediaType;

    public enum MediaType{
        PNG("image/png"),
        JPG("image/jpg"),
        MP4("audio/mp4");
        private String type;

        MediaType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public File getFile() {
        return cacheFile;
    }

    public void setFile(File cacheFile) {
        if (!cacheFile.exists()) {
            new Throwable("文件不存在 path = " + cacheFile.getAbsolutePath());
        }
        this.cacheFile = cacheFile;
    }

    public void setFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            new Throwable("文件不存在 path = " + path);
        }
        this.cacheFile = file;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType type) {
        this.mediaType = type.getType();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String cachePath) {
        this.path = cachePath;
    }
}
