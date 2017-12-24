package com.ddth.eclient;

public class EConfig {

	public static final String HOST 					= "192.168.0.240:9000";
	public static final String KEY_HOST_URL 			= "http://192.168.0.240:9000/";
	public static final String KEY_HOST_IMG_URL 		= "http://192.168.0.240:9000/assets";
	
//	public static final String HOST						= "www.ebangon-express.com";
//	public static final String KEY_HOST_URL 			= "http://www.ebangon-express.com/";
//	public static final String KEY_HOST_IMG_URL			= "http://www.ebangon-express.com/assets";

	public static final String KEY_HOST_WEB_BISUNESS 	= "/api/";

	public static final String MSG_SEPARATE 			= "ζ";

	public static final String KEY_USER_KEY 			= "userkey";

	public static final String USER_NAME 				= "user_name";
	
	public static final String USER_ID 					= "user_id";

	public static final String USER_PASSWORD 			= "user_password";

	public static final String USER_EMAIL 				= "user_email";

	public static final String JSESSIONID 				= "jsessionid";

	public static final String UUID 					= "uuid";

	public static final String MERCHANT_ID 				= "merchant_id";

	public static final String VECODE 					= "vecode";

	public static final String VERSIONS 				= "versions";

	public static final String USER_ICON 				= "user_icon";
	public static final String USER_CODE 				= "user_code";
	public static final String RE_CODE 					= "re_code";

	public static final String APISID 					= "api_sid";
	public static final String service_time 			= "service_time";
	public static final String imgDomain 				= "img_domain";
	public static final String imgUploadSrc 			= "img_upload_src";
	public static final String fileUploadSrc 			= "file_upload_src";

	public static final String STAT 					= "stat";

	public static final String Logined 					= "Logined";
	public static final String IS_CREATED_ORDER			= "is_created_order";

	/**
	 * 正则表达式：验证密码
	 */
	public static final String REGEX_PASSWORD 			= "^[a-zA-Z]\\w{5,17}$";

	/**
	 * 正则表达式：验证手机号
	 */
	public static final String REGEX_MOBILE 			= "^(13\\d|18\\d|14[57]|15[012356789]|17[0678])\\d{8}$";
	public static final String CARD_CODE 				= "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$|^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";
	public static final String ZJ_CODE 					= "^(010\\d{8})|(0[2-9]\\d{9}^";
	public static final String BSTAT 					= "b_stat";

	public static final String QCODE 					= "qcode";

	public static final String MERINFO_STAT 			= "merinfo_stat";

	public static final String PHONE 					= "phone";

}
