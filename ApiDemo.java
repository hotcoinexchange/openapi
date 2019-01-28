package com.hotcoin.top;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * 下单demo
 */
public class ApiDemo  {

    /**
     * 版本号
     */
    private final static int VERSION = 2;
    
    /**
     * 加密方法
     */
    private final static String HmacSHA256 = "HmacSHA256";
    
    /**
     * 请求地址
     */
    private final static String HOST = "api.hotcoin.top";
    
    /**
     * 配置你的KEY
     */
    private final static String ACCESS_KEY = "Your ACCESS_KEY";
    private final static String SECRET_KEY = "Your SECRET_KEY";
    
    
    
    public static void main(String[] args) {
    	//下单
    	String uri = "/v1/order/place";
    	String httpMethod = "POST";
    	Map<String, Object> params = new HashMap<>();
    	params.put("AccessKeyId", ACCESS_KEY);
    	params.put("SignatureVersion", VERSION);
    	params.put("SignatureMethod", HmacSHA256);
    	params.put("Timestamp", new Date().getTime());
    	params.put("symbol", "btc_gavc");
    	params.put("type", "buy");
    	params.put("tradePrice", 40000);
    	params.put("tradeAmount", 0.01);
    	String Signature = getSignature(SECRET_KEY, HOST, uri, httpMethod, params);
    	params.put("Signature", Signature);
    	doHttpRequest(HOST, uri, httpMethod, params);
	}
    
    public static void doHttpRequest(String host,String uri,String httpMethod,Map<String, Object> params) {
    	try {
    		String url = "https://" + host + uri;
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestProperty("Charsert", "UTF-8");
			con.setRequestMethod(httpMethod.toUpperCase());
			con.setConnectTimeout(3 * 1000);
			con.setReadTimeout(10 * 1000);
			con.setDoOutput(true);
			if(params != null) {
				DataOutputStream wr = new DataOutputStream(con.getOutputStream());
				StringBuffer httpParams = new StringBuffer();
				for (Map.Entry<String, Object> entry : params.entrySet()) {
		             String key = entry.getKey();
		             String value = entry.getValue().toString();
		             httpParams.append(key).append("=").append(URLEncoder.encode(value,"UTF-8")).append("&");
		        }
				if(httpParams.length() > 0) {
					httpParams.deleteCharAt(httpParams.length() - 1);
				}
				wr.writeBytes(httpParams.toString());
	 			wr.flush();
	 			wr.close();
			}
			int responseCode = con.getResponseCode();
			if (responseCode < 200 || responseCode >= 300) {
				System.out.println("Request failed:" + responseCode);
				return;
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			StringWriter writer = new StringWriter();
            char[] chars = new char[1024];
            int count = 0;
            while ((count = in.read(chars)) > 0) {
                writer.write(chars, 0, count);
            }
            System.out.println(writer.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public static String getSignature(String apiSecret,String host,String uri,String httpMethod,Map<String, Object> params) {
    	 StringBuilder sb = new StringBuilder(1024);
         sb.append(httpMethod.toUpperCase()).append('\n')
                 .append(host.toLowerCase()).append('\n')
                 .append(uri).append('\n');
         SortedMap<String, Object> map = new TreeMap<>(params);
         for (Map.Entry<String, Object> entry : map.entrySet()) {
             String key = entry.getKey();
             String value = entry.getValue().toString();
             sb.append(key).append('=').append(urlEncode(value)).append('&');
         }
         sb.deleteCharAt(sb.length() - 1);
         Mac hmacSha256;
         try {
             hmacSha256 = Mac.getInstance(HmacSHA256);
             SecretKeySpec secKey =
                     new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), HmacSHA256);
             hmacSha256.init(secKey);
         } catch (Exception e) {
             return null;
         } 
         String payload = sb.toString();
         byte[] hash = hmacSha256.doFinal(payload.getBytes(StandardCharsets.UTF_8));
         //需要对签名进行base64的编码
         String actualSign = Base64.getEncoder().encodeToString(hash);
         actualSign = actualSign.replace("\n","");
         return actualSign;
    }
    

    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("UTF-8 encoding not supported!");
        }
    }
}
