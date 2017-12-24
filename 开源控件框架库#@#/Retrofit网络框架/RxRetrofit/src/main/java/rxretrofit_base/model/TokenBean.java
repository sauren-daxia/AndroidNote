package rxretrofit_base.model;


import com.rxretrofit_build.model.BaseResponse;

/**
 * Title:
 * Description:
 * Copyright:Copyright(c)2016
 * Company: Cree
 * CreateTime:16/11/29  17:20
 *
 * @author cree
 * @version 1.0
 */
public class TokenBean<T> extends BaseResponse<T> {

    @Override
    public String toString() {
        super.toString();
        return "TokenBean{" +
                "message='" + message + '\'' +
                ", token='" + token + '\'' +
                ", server_time=" + server_time +
                '}';
    }

    /**
     * message :
     * token : 7c440a55ac71e94e727e44125c9bee9f
     * server_time : 1480411160
     */

    private String message;
    private String token;
    private int server_time;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getServer_time() {
        return server_time;
    }

    public void setServer_time(int server_time) {
        this.server_time = server_time;
    }
}
