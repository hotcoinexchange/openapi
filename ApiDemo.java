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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
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
    private final static String HOST = "hkapi.hotcoin.top";
    
    /**
     * 配置你的KEY
     */
    private final static String ACCESS_KEY = "Your ACCESS_KEY";
    private final static String SECRET_KEY = "Your SECRET_KEY";
    
    public static void main(String[] args) throws UnsupportedEncodingException {
    	//下单
    	String uri = "/v1/order/place";
    	String httpMethod = "POST";
    	Map<String, Object> params = new HashMap<>();
    	params.put("AccessKeyId", ACCESS_KEY);
    	params.put("SignatureVersion", VERSION);
    	params.put("SignatureMethod", HmacSHA256);
    	params.put("Timestamp",format(getUTCTime(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
    	params.put("symbol", "btc_gavc");
    	params.put("type", "buy");
    	params.put("tradePrice", 40000);
    	params.put("tradeAmount", 0.01);
    	String signature = getSignature(SECRET_KEY, HOST, uri, httpMethod, params);
    	params.put("Signature", signature);
    	doHttpRequest(HOST, uri, httpMethod, params);
	}
    
    public static void doHttpRequest(String host,String uri,String httpMethod,Map<String, Object> params) {
    	try {
    		String url = "https://" + host + uri;
    		if("GET".equals(httpMethod.toUpperCase())) {
    			System.out.println(get(url, params));
    		}else if("POST".equals(httpMethod.toUpperCase())) {
    			System.out.println(post(url, params));
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    
    /**
     * get请求
     * 
     * @param url
     * @param param
     * @return
     * @throws Exception 
     */
    public static String get(String url,Map<String, Object> param) throws Exception {
        StringBuilder params=new StringBuilder();
        for(Entry<String, Object> entry:param.entrySet()){
            params.append(entry.getKey());
            params.append("=");
            String value = entry.getValue().toString();
            params.append(URLEncoder.encode(value,"UTF-8"));
            params.append("&");
        }
        if(params.length()>0){
            params.deleteCharAt(params.lastIndexOf("&"));
        }
        String reqUrl = url+(params.length()>0 ? "?"+params.toString() : "");
        URL restServiceURL = new URL(reqUrl);
        HttpURLConnection con = (HttpURLConnection) restServiceURL.openConnection();
        con.setRequestProperty("Charset", "UTF-8");
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
		if (responseCode < 200 || responseCode >= 300) {
			System.out.println("Request failed:" + responseCode);
			return null;
		}
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		StringWriter writer = new StringWriter();
        char[] chars = new char[1024];
        int count = 0;
        while ((count = in.read(chars)) > 0) {
            writer.write(chars, 0, count);
        }
        return writer.toString();
    }
    /**
     * post    请求
     * 
     * @param url
     * @param param
     * @return
     * @throws Exception 
     */
    public static String post(String url,Map<String, Object> params) throws Exception {
    	URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestProperty("Charset", "UTF-8");
		con.setRequestMethod("POST");
		con.setConnectTimeout(30 * 1000);
		con.setReadTimeout(100 * 1000);
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
			return null;
		}
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		StringWriter writer = new StringWriter();
        char[] chars = new char[1024];
        int count = 0;
        while ((count = in.read(chars)) > 0) {
            writer.write(chars, 0, count);
        }
        return writer.toString();
    }
    
    /**
     * 签名
     * 
     * @param apiSecret
     * @param host
     * @param uri
     * @param httpMethod
     * @param params
     * @return
     * @throws Exception 
     */
    public static String getSignature(String apiSecret,String host,String uri,String httpMethod,Map<String, Object> params) {
    	 StringBuilder sb = new StringBuilder(1024);
         sb.append(httpMethod.toUpperCase()).append('\n')
                 .append(host.toLowerCase()).append('\n')
                 .append(uri).append('\n');
         SortedMap<String, Object> map = new TreeMap<>(params);
         for (Map.Entry<String, Object> entry : map.entrySet()) {
             String key = entry.getKey();
             String value = entry.getValue().toString();
             try {
				sb.append(key).append('=').append(URLEncoder.encode(value, "UTF-8")).append('&');
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
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
         System.out.println(payload);
         byte[] hash = hmacSha256.doFinal(payload.getBytes(StandardCharsets.UTF_8));
         //需要对签名进行base64的编码
         String actualSign = Base64.getEncoder().encodeToString(hash);
         actualSign = actualSign.replace("\n","");
         return actualSign;
    }
    
    /**
     * date转String
     * 
     * @param date
     * @param pattern
     * @return
     */
	static public String format(Date date,String pattern) {
		Instant instant = date.toInstant();
	    ZoneId zone = ZoneId.systemDefault();
	    LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
	    return DateTimeFormatter.ofPattern(pattern).format(localDateTime);
	}
	
	
	/**
     * 获得世界协调时的近似值
     * @return  Date
     */
   public static Date getUTCTime(){
   	Calendar cal = Calendar.getInstance();
   	//获得时区和 GMT-0 的时间差,偏移量
   	int offset = cal.get(Calendar.ZONE_OFFSET);
   	//获得夏令时  时差
   	int dstoff = cal.get(Calendar.DST_OFFSET);
   	cal.add(Calendar.MILLISECOND, - (offset + dstoff));
		return cal.getTime();
   }
}
