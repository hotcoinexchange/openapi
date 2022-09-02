import 'dart:collection';
import 'dart:convert';
import 'dart:io';

import 'package:crypto/crypto.dart';

/// 参考 https://hotcoinex.github.io/zh/spot/introduction.html
/// Hotcoin Global API For Dart
/// Hotcoin Global API的Dart示例 - 下单 - /v1/order/place
/// 本示例仅是方便开发者了解Hotcoin Global API的使用，具体接入由开发者按自己的规范实现
void main() async {
  // API访问Key
  String accessKey = "您的accessKey";
  // 签名秘钥
  String secretKey = "您的secretKey";
  // 签名算法
  String hmacSha256 = "HmacSHA256";
  // 签名版本号
  String version = "2";
  // HTTP请求方式
  String httpMethod = "POST";
  // 签名域名
  String host = "api.hotcoinfin.com";
  // 请求域名
  String domain = "https://api.hotcoinfin.com";
  // 接口URI
  String uri = "/v1/order/place";
  // 业务参数 - 为了方便，此处使用SplayTreeMap保证参数按ASCII排序，开发者按照自己的规范使用
  Map<String, Object> businessMap = SplayTreeMap();
  businessMap["AccessKeyId"] = accessKey;
  businessMap["SignatureMethod"] = hmacSha256;
  businessMap["SignatureVersion"] = version;
  // 当前UTC时间
  businessMap["Timestamp"] = "2022-07-22T22:16:06.123Z";
  // 以下为接口的具体业务参数
  businessMap["symbol"] = "btc_usdt";
  businessMap["type"] = "buy";
  businessMap["tradeAmount"] = "0.26";
  businessMap["tradePrice"] = "18888.68";

  List<String> paramPair = [];
  businessMap.forEach((key, value) {
    paramPair.add(key + "=" + Uri.encodeComponent(value.toString()));
  });
  paramPair.sort();
  // 业务参数拼接
  print("业务参数拼接：\n" + paramPair.join("&"));
  // 待签名的原始字符串
  String unSign =
      httpMethod + "\n" + host + "\n" + uri + "\n" + paramPair.join("&");
  print("-----------------------------------------------------");
  print("待签名的原始字符串：\n" + unSign);
  // 签名秘钥编码
  List<int> secretBytes = utf8.encode(secretKey);
  // 待签名的原始字符串编码
  List<int> messageBytes = utf8.encode(unSign);
  // HmacSHA256签名
  Hmac hmac = Hmac(sha256, secretBytes);
  Digest digest = hmac.convert(messageBytes);
  // Base64编码
  String signatureBase64 = base64.encode(digest.bytes);
  print("-----------------------------------------------------");
  print("签名串Base64编码：\n" + signatureBase64);
  // URI编码
  String signature = Uri.encodeComponent(signatureBase64);
  print("-----------------------------------------------------");
  print("签名串URI编码：\n" + signature);
  // 添加到请求参数
  paramPair.add("Signature=" + signature);

  String fullUrl = domain + uri + "?" + paramPair.join("&");
  print("-----------------------------------------------------");
  print("最终请求：\n" + fullUrl);
  // 创建HttpClient
  var httpClient = HttpClient();
  // 构造Uri
  var requset = await httpClient.postUrl(Uri.parse(fullUrl));
  // 关闭请求，等待响应
  var response = await requset.close();
  // 解析响应结果
  var responseBody = await response.transform(utf8.decoder).join();
  print("-----------------------------------------------------");
  print("接口请求结果：\n" + responseBody);
  httpClient.close();
}
