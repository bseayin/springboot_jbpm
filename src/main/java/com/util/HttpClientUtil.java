package com.util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
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
import org.jsoup.helper.StringUtil;
import org.kie.api.task.model.Status;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.controller.KpiController;

/**
 * Created by xenia on 2017/6/6.
 */
public class HttpClientUtil {
	
	private static String rootPath="http://localhost:8080/jbpm/kpi/";
	
	private static String userId="JOJO";
	private static String password="123456";
	
	private static String authorization=userId+":"+password;
	
	
    public static JSONObject doGet(String method,Map<String,Object> params) throws ClientProtocolException, IOException {
        //认证信息对象，用于包含访问翻译服务的用户名和密码
    	String result = null;
    	
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
                result = new String(str1.getBytes("gbk"), "utf-8");
                System.out.println("服务器的响应是:" + result);
                br.close();
                input.close();
            } else {
                System.out.println("响应失败!");
            }
        
            if(result==null) {
            	return null;
            }else {
            	return JSONObject.parseObject(result);
            }
    }

	public static JSONObject doPost(String method,Map<String,Object> params,String requestBody) throws ClientProtocolException, IOException {
		String result = null;
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
                result = new String(str1.getBytes("gbk"), "utf-8");
                System.out.println("服务器的响应是:" + result);
                br.close();
                input.close();
            } else {
                System.out.println("响应失败!");
            }
        
            if(result==null) {
            	return null;
            }else {
            	return JSONObject.parseObject(result);
            }
	}
	
	public static void main(String[] args) {
		String taskId;
		
		
		try {
			
			//0.startProcessNew
			JSONObject res=HttpClientUtil.doPost("startProcessNew", null,null);
			
			String code=res.getString("code");
			if(KpiController.CODE_FAILED.equals(code)) {
				return;
			}
			JSONObject result=(JSONObject) res.get("result");
			
			JSONObject firsttask=(JSONObject) (result.getJSONArray("taskList").get(0));
			
			taskId=firsttask.getString("id");
			
			String processInstanceId=result.getString("processInstanceId");
			
			while(!StringUtil.isBlank(taskId)) {
				
				Map<String,Object> params=new HashMap<String,Object>(16);
				
				
				//1.claim
				params.put("taskId", taskId);
				params.put("userId", userId);
				res=HttpClientUtil.doGet("claimTask", params);	
				
				code=res.getString("code");
				if(KpiController.CODE_FAILED.equals(code)) {
					return;
				}
				
				
				
				//2.start
				
				params.put("taskId", taskId);
				params.put("userId", userId);
				res=HttpClientUtil.doGet("startTask", params);	
				
				code=res.getString("code");
				if(KpiController.CODE_FAILED.equals(code)) {
					return;
				}
				
				//3.complete
				params.put("taskId", taskId);
				params.put("userId", userId);
				res=HttpClientUtil.doPost("completeTask", params,null);	
				
				code=res.getString("code");
				if(KpiController.CODE_FAILED.equals(code)) {
					return;
				}
				
				//4.call search api to find newTaskId
				params.put("processInstanceId", processInstanceId);
				//params.put("status", Status.Ready.toString());
				res=HttpClientUtil.doGet("getProcessTaskList", params);	
				
				code=res.getString("code");
				if(KpiController.CODE_FAILED.equals(code)) {
					return;
				}
				//result=(JSONObject) res.get("result");
				
				JSONArray arr=res.getJSONArray("result");
				if(arr.size()>0 && arr.size()==1) {
					JSONObject nextTask=arr.getJSONObject(0);
					String nextTaskId=nextTask.getString("id");
					if(nextTaskId.equals(taskId)) {
						System.out.println("processInstance["+processInstanceId+"] is finished");
						return;
					}else {
						taskId=nextTaskId;
					}
					//delegate
					/*
					params.put("taskId", taskId);
					params.put("userId", userId);
					params.put("targetUserId", userId);
					res=HttpClientUtil.doGet("assignTask", params);	
					
					code=res.getString("code");
					if(KpiController.CODE_FAILED.equals(code)) {
						return;
					}
					*/
				}else {
					System.out.println("processInstance["+processInstanceId+"] is finished, no task find");
					return;
				}
				//taskId=task.getString("id");
			}
			
			
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
