package com.sisisan.data.utils;

/**
 * 对数据进行解封包操作类
 * @author Created by Chenzhi on 2017/8/2.
 */

public class PackageUtils {
    private static final String HEART = "www.bat_msg.com";
    private static final int RADIX = 4;
    private PackageUtils() {
    }
    static class UtilsHolder{
        private static final PackageUtils instance = new PackageUtils();
    }
    public static PackageUtils getInstance(){
        return UtilsHolder.instance;
    }
    private int getUnsignedByte(byte b){
        return b & 0xFF;
    }
    /**
     * 对数据进行封包
     * code为定义的类型
     * @param msg 要发送的数据
     * @return 封包后的数据，为byte类型
     */
    public byte[] DataPackaging(String msg,int code){
        byte[] bytes2 = msg.getBytes();
        int intDataLen = bytes2.length;
        byte[] bytes1 = intToBytes2(intDataLen,code);
        String s = new String(bytes1);
        String msgStr = HEART+s+msg;
        byte[] bytes = msgStr.getBytes();
        return bytes;
    }
    public byte[] DataPackaging(byte[] msg,int code){
        int intDataLen = msg.length;
        byte[] bytes1 = intToBytes2(intDataLen,code);
        String s = new String(bytes1);
        byte[] bytes  = new byte[19+intDataLen];
        System.arraycopy(HEART.getBytes(), 0, bytes, 0,15);
        System.arraycopy(bytes1,0, bytes, 15, 4);
        System.arraycopy(msg,0, bytes, 19, intDataLen);
        return bytes;
    }
    /**
     * 对数据进行解包
     * @param bytes 要解包的数据
     * @return 返回的解包结果，成功为接收到的数据，失败返回解析失败
     */
    public void DataParsing(byte[] bytes, CodeListener codeListener) throws Exception {
        byte[] byteHeader = subBytes(bytes,0,15);
        if(byteHeader == null||!HEART.equals(new String(byteHeader))){
            throw new Exception("数据包不正确!");
        }else{
            byte[] code = subBytes(bytes,15,2);
            byte b = code[0];
            byte b1 = code[1];
            byte[] byteLen = subBytes(bytes,17,2);
            int ContentLen = bytesToInt2(byteLen);
            if(ContentLen == (bytes.length-19)){
                byte[] bytes1 = subBytes(bytes, 19, ContentLen);
                String msgStr = new String(bytes1);
                codeListener.Listener(msgStr,b,b1);
            }else {
                throw new Exception("数据长度不正确!");
            }
        }
    }
    public interface CodeListener{
        void Listener(String data,int code1,int code);
    }
    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(高位在前，低位在后)的顺序。  和bytesToInt2（）配套使用
     */
    public static byte[] intToBytes2(int i,int code) {
        byte[] src = new byte[4];
        int i1 = i % 127;
        int i2 = i / 127;
        src[0] = (byte) 0;
        src[1] = (byte) code;
        src[2] = (byte) i2;
        src[3] = (byte) i1;
        return src;
    }

    /**
     * 按项目要求将4个byte转为int
     * 长度不对或符号不对返回-1
     */
    public static int bytesToInt2(byte[] src) {
        int value;
        if (src.length==2){
            byte b = src[0];
            byte b1 = src[1];
            if (b<0 || b1<0){
                return -1;
            }
            value = b*127+b1;
        }else {
            value = -1;
        }
        return value;
    }
    /**
     * 对byte类型数据进行截取
     * @param src 要截取的byte数据
     * @param begin 从第几位开始截取
     * @param count 截取的数据从长度
     * @return 截取后的结果
     */
    public byte[] subBytes(byte[] src,int begin,int count){
        byte[] bs = new byte[count];
        System.arraycopy(src,begin,bs,0,count);
        return bs;
    }

    /**
     * 将数据转化为ASCII码
     * @param value 要转换的数据
     * @return 转换完成的数据
     */
    public String stringToASCII(String value){
        StringBuffer stringBuffer = new StringBuffer();
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if(i != chars.length - 1)
            {
                stringBuffer.append((int)chars[i]).append(",");
            }
            else {
                stringBuffer.append((int)chars[i]);
            }
        }
        return stringBuffer.toString();
    }
}
