package com.util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * Created by xenia on 2017/6/6.
 */
public class HttpClientUtil {
	
	private static String rootPath="http://localhost:8080/jbpm/kpi/";
	private static String authorization="JOJO:123456";
	
	
    public static void doGet(String method,Map<String,Object> params) throws ClientProtocolException, IOException {
        //认证信息对象，用于包含访问翻译服务的用户名和密码

        String path = rootPath+method+"?";
        if(params!=null) {
        	int i=0;
            for(String key:params.keySet()) {        	
            	path+= (i==0?"":"&")+key+"="+params.get(key).toString();
            	i++;
            }
        }
        System.out.println(path);
        //1.创建客户端访问服务器的httpclient对象   打开浏览器
        //HttpClient httpclient = new DefaultHttpClient();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        //2.以请求的连接地址创建get请求对象     浏览器中输入网址
        HttpGet httpget = new HttpGet(path);

        //username:password--->访问的用户名，密码,并使用base64进行加密，将加密的字节信息转化为string类型，encoding--->token
        String encoding = DatatypeConverter.printBase64Binary(authorization.getBytes("UTF-8"));

        httpget.setHeader("Authorization", "Basic " +encoding);
        //3.向服务器端发送请求 并且获取响应对象  浏览器中输入网址点击回车
        HttpResponse response = httpclient.execute(httpget);
        //4.获取响应对象中的响应码
        StatusLine statusLine = response.getStatusLine();//获取请求对象中的响应行对象
        int responseCode = statusLine.getStatusCode();//从状态行中获取状态码

            System.out.println(responseCode);
            if (responseCode == 200) {
                //5.  可以接收和发送消息
                HttpEntity entity = response.getEntity();
                //6.从消息载体对象中获取操作的读取流对象
                InputStream input = entity.getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(input));
                String str1 = br.readLine();
                String result = new String(str1.getBytes("gbk"), "utf-8");
                System.out.println("服务器的响应是:" + result);
                br.close();
                input.close();
            } else {
                System.out.println("响应失败!");
            }
        
    }

	public static void doPost(String method,Map<String,Object> params,String requestBody) throws ClientProtocolException, IOException {

        //认证信息对象，用于包含访问翻译服务的用户名和密码

        String path = rootPath+method;
        
        if(params!=null) {
	        int i=0;
	        for(String key:params.keySet()) {
	        	
	        	path+= (i==0?"":"&")+key+"="+params.get(key).toString();
	        	i++;
	        }
        }
        System.out.println(path);
        
        if(requestBody==null) {
        	requestBody="{}";
        }
        //1.创建客户端访问服务器的httpclient对象   打开浏览器
        //HttpClient httpclient = new DefaultHttpClient();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        //2.以请求的连接地址创建get请求对象     浏览器中输入网址
        HttpPost httppost = new HttpPost(path);

        //username:password--->访问的用户名，密码,并使用base64进行加密，将加密的字节信息转化为string类型，encoding--->token
        String encoding = DatatypeConverter.printBase64Binary(authorization.getBytes("UTF-8"));
        httppost.setHeader("Authorization", "Basic " +encoding);
        
        httppost.setHeader("Content-Type", "application/json");        
        httppost.setEntity(new StringEntity(requestBody, ContentType.create("application/json", "utf-8")));
        
        System.out.println("request parameters" + EntityUtils.toString(httppost.getEntity()));
        
        //3.向服务器端发送请求 并且获取响应对象  浏览器中输入网址点击回车
        HttpResponse response = httpclient.execute(httppost);
        //4.获取响应对象中的响应码
        StatusLine statusLine = response.getStatusLine();//获取请求对象中的响应行对象
        int responseCode = statusLine.getStatusCode();//从状态行中获取状态码

            System.out.println(responseCode);
            if (responseCode == 200) {
                //5.  可以接收和发送消息
                HttpEntity entity = response.getEntity();
                //6.从消息载体对象中获取操作的读取流对象
                InputStream input = entity.getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(input));
                String str1 = br.readLine();
                String result = new String(str1.getBytes("gbk"), "utf-8");
                System.out.println("服务器的响应是:" + result);
                br.close();
                input.close();
            } else {
                System.out.println("响应失败!");
            }
        
    
	}
	
	public static void main(String[] args) {
		
		
		
		try {
			HttpClientUtil.doPost("startProcess", null,null);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
