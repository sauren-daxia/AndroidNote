package rxretrofit_base.model;

import java.io.Serializable;
import java.util.List;

/**
 * Title:
 * Description:
 * Copyright:Copyright(c)2016
 * Company: Cree
 * CreateTime:16/11/29  17:32
 *
 * @author cree
 * @version 1.0
 */
public class PushKeyBean {

    /**
     * push_key : 9096a6989f2bf17a9fa6abdf42eb9512
     * imgUploadSrc : http://loc.ebg.com/assets/imgUpload.php
     * fileUploadSrc : http://loc.ebg.com/assets/fileUpload.php
     * versions : {"id":"5","versions":"1.0.3","publish_time":"100","last_upgrade":1,"content":["1、第三方色发送到发送到\r","2、隧道股份大使馆的是非得失\r","3、的是发的鬼地方个发的发的\r","4、该很近换个地方第三方"]}
     */

    private String push_key;
    private String imgUploadSrc;
    private String fileUploadSrc;
    private VersionsBean versions;

    public VersionsBean getVersions() {
        return versions;
    }

    public void setVersions(VersionsBean versions) {
        this.versions = versions;
    }

    /**
     * id : 5
     * versions : 1.0.3
     * publish_time : 100
     * last_upgrade : 1
     * content : ["1、第三方色发送到发送到\r","2、隧道股份大使馆的是非得失\r","3、的是发的鬼地方个发的发的\r","4、该很近换个地方第三方"]
     */


    public String getPush_key() {
        return push_key;
    }

    public void setPush_key(String push_key) {
        this.push_key = push_key;
    }

    public String getImgUploadSrc() {
        return imgUploadSrc;
    }

    public void setImgUploadSrc(String imgUploadSrc) {
        this.imgUploadSrc = imgUploadSrc;
    }

    public String getFileUploadSrc() {
        return fileUploadSrc;
    }

    public void setFileUploadSrc(String fileUploadSrc) {
        this.fileUploadSrc = fileUploadSrc;
    }

    public class VersionsBean implements Serializable{
        private String id;
        private String versions;
        private String last_upgrade;
        private List<String> content;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getVersions() {
            return versions;
        }

        public void setVersions(String versions) {
            this.versions = versions;
        }

        public String getLast_upgrade() {
            return last_upgrade;
        }

        public void setLast_upgrade(String last_upgrade) {
            this.last_upgrade = last_upgrade;
        }

        public List<String> getContent() {
            return content;
        }

        public void setContent(List<String> content) {
            this.content = content;
        }
    }

}
