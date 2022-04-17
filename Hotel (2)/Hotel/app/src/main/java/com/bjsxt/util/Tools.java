package com.bjsxt.util;

import android.util.Log;

import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * 1.添加依赖
 *     //gson
 *     implementation 'com.google.code.gson:gson:2.8.6'
 *     //网络请求
 *     implementation 'com.squareup.okhttp3:okhttp:4.4.0'
 *     implementation 'com.squareup.okhttp3:logging-interceptor:4.4.0'
 *
 * 2.添权限
 * <uses-permission android:name="android.permission.INTERNET" />
 *
 *     <application
 *         android:usesCleartextTraffic="true"
 *
 * 3.实体类的包名要和服务端相同
 */

/**
 * 开发常用工具类
 * @author 彭子中
 *
 */
public class Tools {
	private static final String TAG = "Tools";


	/**
	 *
	 * @param obj	将要传输的东西放到一个实体类中
	 * @param myCallBack
	 *
	 *
	 */
	public static void PostDataWithOkHttp(String method,Object obj,final MyCallBack myCallBack){
		OkHttpClient client = new OkHttpClient.Builder()
				.connectTimeout(1000, TimeUnit.MILLISECONDS)
				.build();
		// 2. 创建一个请求
		Gson gson = new Gson();
		byte[] bytes=null;
		try {
			bytes = Tools.objectToBytes(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String json = gson.toJson(bytes);
		RequestBody body = RequestBody.create(
				json,
				MediaType.parse("application/json; charset=utf-8")
		);
		Request request = new Request.Builder()
				.url(ServiceUrls.getMethodUrl(method))
				.post(body).build();
		// 3. 客户端发起请求实例化Call对象，执行请求任务

		Call call = client.newCall(request);
		call.enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				Log.d(TAG, "onFailure...");
			}

			//response.body().string() 将请求体的内容以字符创的形式输出
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if(response.isSuccessful()){
					byte[] bytes = getResponseBytes(response);
					myCallBack.onSuccess(bytes);
				}
			}
		});
	}

	/**
	 *
	 * @param myCallBack	回调接口，用于将onResponse中的数据传递到主线程
	 * @param method	请求服务器处理的方法，用于告知服务器此次请求需要的数据/动作
	 *
	 *     使用示例
	 *                  1.单独定义回调接口
	 *                  public interface MyCallBack {
	 *     					void onSuccess(byte[] bytes );
	 * 					}
	 *
	 * 				2.调用Tools中的方法,第一个参数对应服务器的请求方法，第二个是回调接口用于将OnResponse中的数据传递到主线程
	 * 			        hello是对应的实体类，可以根据自己需求更改，但是客户端和服务器实体类的UID即序列化号和包名要一样
	 *            Tools.GetDataWithOkHttpByURL("hello",new MyCallBack() {
	 *                     @Override
	 *                     public void onSuccess(byte[] bytes) {
	 *                         hello tex = null;
	 *                         try {
	 *                             tex = (hello) bytesToObject(bytes);
	 *                         } catch (Exception e) {
	 *                             e.printStackTrace();
	 *                         }
	 *                         showResponse(tex.getUser());
	 *                     }
	 *                 });
	 *
	 *           3.更新UI的方法写在run里面
	 *           private void showResponse(final String response){
	 *         	   runOnUiThread(new Runnable() {
	 *             @Override
	 *             public void run() {
	 *                 Toast.makeText(LoginActivity.this,response,Toast.LENGTH_SHORT).show();
	 *             }
	 *         });
	 *     }
	 */
	public static void GetDataWithOkHttpByURL(String method,final MyCallBack myCallBack){

		OkHttpClient client = new OkHttpClient.Builder()
				.connectTimeout(1000, TimeUnit.MILLISECONDS)
				.build();
		// 2. 创建一个请求
		Request request = new Request.Builder()
				.url(ServiceUrls.getMethodUrl(method))
				.get()//默认get请求，可以省略
				.build();
		// 3. 客户端发起请求实例化Call对象，执行请求任务
		Call call = client.newCall(request);
		call.enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				Log.d(TAG, "onFailure...");
			}

			//response.body().string() 将请求体的内容以字符创的形式输出
			@Override
			public void onResponse(Call call, Response response) throws IOException {

				//获取服务器responseBody中的字节数组
				byte[] bytes = getResponseBytes(response);
				myCallBack.onSuccess(bytes);
			}
		});

	}

	/**
	 * 获取服务器端返回的response中的String
	 * @param response
	 * @return
	 */
	public static String getResponseString(Response response){
		ResponseBody responseBody = response.body();
		BufferedSource source = responseBody.source();
		try {
			source.request(Long.MAX_VALUE); // request the entire body.
		} catch (IOException e) {
			e.printStackTrace();
		}
		Buffer buffer = source.getBuffer();
		String responseBodyString = buffer.clone().readString(Charset.forName("UTF-8"));
		return  responseBodyString;
	}

	/**
	 * 获取服务器返回的response中的字节数组(一般是实体类序列化后的值)
	 * @param response
	 * @return
	 */
	public static byte[] getResponseBytes(Response response){
		ResponseBody responseBody = response.body();
		BufferedSource source = responseBody.source();
		try {
			source.request(Long.MAX_VALUE); // request the entire body.
		} catch (IOException e) {
			e.printStackTrace();
		}
		Buffer buffer = source.getBuffer();
		// clone buffer before reading from it
		byte[] bytes = buffer.clone().readByteArray();
		return bytes;
	}

	/**
	 * 对象转Byte数组
	 *
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static byte[] objectToBytes(Object obj) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream sOut = new ObjectOutputStream(out);
		sOut.writeObject(obj);
		sOut.flush();
		byte[] bytes = out.toByteArray();


		return bytes;
	}

	/**
	 * 字节数组转对象
	 * @param bytes
	 * @return
	 * @throws Exception
	 */
	public static Object bytesToObject(byte[] bytes) throws Exception {

//byte转object
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		ObjectInputStream sIn = new ObjectInputStream(in);
		return sIn.readObject();

	}

	/**
	 * 
	 * @param value 字符串
	 * @return 如果字符串不为空或者长度不为零返回true
	 */
	public static boolean isNotNull( String value ) {
		if( value == null || "".equals( value.trim()) || "null".equalsIgnoreCase(value) ) {
			return false;
		}
		return true;
	}
	
	/**
	 * ISO编码转换成UTF8编码
	 * @param s 字符串
	 * @return UTF编码字符串
	 */
	public static String ISOtoUTF8(String s) { 
		try { 
			s = new String(s.getBytes("iso-8859-1"), "utf-8");
		} catch (Exception ignored) {
			
		} 
		return s; 
	}
	
	/**
	 * 是否为num
	 * @param str 字符串
	 * @return boolean
	 */
	public static boolean isNum(String str){	
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");	
	}


	/*  <br>　　　　　2019年1月16日已知
   中国电信号段
       133,149,153,173,174,177,180,181,189,199
   中国联通号段
       130,131,132,145,146,155,156,166,175,176,185,186
   中国移动号段
       134(0-8),135,136,137,138,139,147,148,150,151,152,157,158,159,165,178,182,183,184,187,188,198
   上网卡专属号段（用于上网和收发短信，不能打电话）
       如中国联通的是145
   虚拟运营商
       电信：1700,1701,1702
       移动：1703,1705,1706
       联通：1704,1707,1708,1709,171
   卫星通信： 1349 <br>　　　　　未知号段：141、142、143、144、154
   */

	/**
	 * 判断是不是手机号
	 * @param str 字符串
	 * @return boolean
	 */
	public static boolean isMobile(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		String s2="^[1](([3|5|8][\\d])|([4][4,5,6,7,8,9])|([6][2,5,6,7])|([7][^9])|([9][1,8,9]))[\\d]{8}$";// 验证手机号
		if(isNotNull(str)){
			p = Pattern.compile(s2);
			m = p.matcher(str);
			b = m.matches();
		}
		return b;
	}
}
