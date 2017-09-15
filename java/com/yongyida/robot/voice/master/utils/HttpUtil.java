package com.yongyida.robot.voice.master.utils;

import android.util.Log;
import com.yongyida.robot.voice.master.application.MasterApplication;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class HttpUtil {
    public static String request(String httpUrl, String httpArg) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?" + httpArg;

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            // HTTP header
            connection.setRequestProperty("apikey", DataUtil.apikey);
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
            Log.i("JSON", result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String key;
    private static String path;
    private static int current_type = -1;
    private static String result1;

    public static void setKey(String result) {
        try {
            result = URLEncoder.encode(result, "utf-8");
            key = result;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static int getTypeSelect() {
        return current_type;
    }

    public static void SetUrlType(int type) {
        current_type = type;
        switch (type) {
            case 0:
                path = "http://ltpapi.voicecloud.cn/analysis/?api_key=K1h4f4Q3323567v5Q7A6PKvoDHQaaJahLpBXbh6F&text=" + key + "&pattern=dp&format=json";
                break;
            case 1:
                String userId = MasterApplication.USER_DI;
                if (userId == null || userId.equals("")) {
                    userId = "Y20ARobot";
                }
                path = "http://api.doudoubot.cn/rsvpbot/general/chat?appid=rsvp1U6jCbczwfAf&token=6UmkAVkk3BSCH4X5&userid=" + userId + "&question=" + key;
                break;
            case 2:
                //path = "http://120.24.242.163:81/boyan_chat/query";
                path = "http://" + Domain.getServerHost() + "/boyan_chat/query";
                break;
            case 3:
                path = "http://120.24.242.163:81/dialogue_record/commit";
                break;
            case 4:
                path = "http://" + Domain.getServerHost() + "/robot/get/age";
                break;
            case 5:
                path = "http://120.24.213.239:81/robot/get/age";
                break;
            default:
                break;
        }
    }

    /**
     * 薄言超脑httpget
     */
    public static String HttpGet() {


        //	String path ="http://ltpapi.voicecloud.cn/analysis/?api_key=K1h4f4Q3323567v5Q7A6PKvoDHQaaJahLpBXbh6F&text=�����й��ˡ�&pattern=dp&format=json";
        try {
                    /*result = URLEncoder.encode(result, "utf-8");
		    		String path ="http://ltpapi.voicecloud.cn/analysis/?api_key=K1h4f4Q3323567v5Q7A6PKvoDHQaaJahLpBXbh6F&text="+result+"&pattern=dp&format=json";*/
            //webView.loadUrl("http://m.baidu.com/s?wd="+ key + "&pn=0&rn=50&tn=jsons");
            URL url = new URL(path);
            Log.d("jlog", path);
            HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
            if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader isr = new InputStreamReader(httpconn.getInputStream(), "utf-8");
                int len = 0;
                StringBuffer str = new StringBuffer();
                while ((len = isr.read()) != -1) {
                    str.append((char) len);
                }
                Log.d("voiceUnderstand", "json:" + str.toString());
                httpconn.disconnect();
                return str.toString();
            } else {
                Log.d("voiceUnderstand", "server error");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 向服务器请求薄言解析，httppost
     */
    public static String submitPostData(String name, String userId, String text, String encode, int rc) {
        HashMap<String, String> params = new HashMap<String, String>();
        switch (rc) {
            case 0:
                params.put("dialogue_json", text);
                params.put("rid", userId);
                break;
            case 4:
                //	params.put("rname", name);
                params.put("question", text);
                params.put("rid", userId);
                params.put("rname", name);
                break;
            case 1:
                params.put("robot_id", userId);
                break;
            default:
                break;
        }
        byte[] data = getRequestData(params, encode).toString().getBytes();//获得请求体
        try {

            //String urlPath = "http://192.168.1.9:80/JJKSms/RecSms.php";
            URL url = new URL(path);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(3000);     //设置连接超时时间
            httpURLConnection.setDoInput(true);                  //打开输入流，以便从服务器获取数据
            httpURLConnection.setDoOutput(true);                 //打开输出流，以便向服务器提交数据
            httpURLConnection.setRequestMethod("POST");     //设置以Post方式提交数据
            httpURLConnection.setUseCaches(false);               //使用Post方式不能使用缓存
            //httpURLConnection.
            //设置请求体的类型是文本类型
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求体的长度
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(data);

            int response = httpURLConnection.getResponseCode();            //获得服务器的响应码
            Log.e("response code=", response + "");
            if (response == HttpURLConnection.HTTP_OK) {
                InputStream inptStream = httpURLConnection.getInputStream();
                //   httpURLConnection.disconnect();
                return dealResponseResult(inptStream);                     //处理服务器的响应结果
            }
        } catch (IOException e) {
            //e.printStackTrace();
            //return "err: " + e.getMessage().toString();
            return "";
        }
        return "";
    }

    /*
     * Function  :   封装请求体信息
     * Param     :   params请求体内容，encode编码格式
     */
    public static String getRequestData(Map<String, String> params, String encode) {
        //  StringBuffer stringBuffer = new StringBuffer();        //存储封装好的请求体信息
        String json_str = "";
        try {
            JSONObject object = new JSONObject();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                object.put(entry.getKey(), entry.getValue());
            }
            json_str = object.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json_str;
    }

    /*
     * Function  :   处理服务器的响应结果（将输入流转化成字符串）
     * Param     :   inputStream服务器的响应输入流
     */
    public static String dealResponseResult(InputStream inputStream) {
        String resultData = null;      //存储处理结果
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        try {
            while ((len = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultData = new String(byteArrayOutputStream.toByteArray());
        return resultData;
    }

}