package rxretrofit_base.model;

/**
 * Title:
 * Description:
 * Copyright:Copyright(c)2016
 * Company: Cree
 * CreateTime:16/11/30  12:39
 *
 * @author cree
 * @version 1.0
 */
public enum KeyBean {
    TOKEN("TOKEN"), PUSH_KEY("PUSH_KEY"),ISLOGIN_KEY("userIsLogin"),FILE_NAME("TokenAndPushKey")
    ,USER_FILE_NAME("User_File")
    ;
    String str;

    KeyBean(String str) {
        this.str = str;
    }

    public String valueOf() {
        return str;
    }

}
