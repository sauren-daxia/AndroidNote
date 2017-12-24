package com.rxretrofit_build.net;

import java.util.HashMap;

import okhttp3.Request;

/**
 * Title:
 * Description:
 * Copyright:Copyright(c)2016
 * Company: Cree
 * CreateTime:16/11/29  14:42
 *
 * @author cree
 * @version 1.0
 */
public interface BuilderReuqest {
    public HashMap<String,Object> getHeadMap( Request request);
}
