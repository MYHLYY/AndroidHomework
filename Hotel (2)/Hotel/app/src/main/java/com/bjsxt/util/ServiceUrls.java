package com.bjsxt.util;

public class ServiceUrls {

    private static String rootserviceUrl="http://3995r916i2.qicp.vip/";

    /**
     * 获取服务器url
     * @return
     */
    public static String getRootserviceUrl(){
        return rootserviceUrl;
    }

    /**
     * 根据传递的method，服务器使用与method同名的方法进行处理
     * @param method
     * @return
     */
    public static String getMethodUrl(String method){
        return getRootserviceUrl()+method;
    }

}
