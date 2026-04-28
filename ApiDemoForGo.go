package main

import (
	"crypto/hmac"
	"crypto/sha256"
	"encoding/base64"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"net/http"
	"net/url"
	"strings"
	"time"
)

// 参考 https://www.hotcoin.com/zh_CN/docs/
// Hotcoin API For Golang
// 本示例基于Go 1.19，仅是方便开发者了解Hotcoin API的使用，具体接入由开发者按自己的规范实现

func main() {
	// 获取账户资产 - /v3/balance
	getV3Balance()

	// 下单 - /v1/order/place
	//postV1OrderPlace()

	// 批量下单 - /v2/order/batchOrders
	//postV2OrderBatchOrders()
}

// Hotcoin API的Golang示例 - 获取账户资产 - /v3/balance

func getV3Balance() {
	// API访问Key
	var accessKey = "您的accessKey"
	// 签名秘钥
	var secretKey = "您的secretKey"
	// 签名算法
	var hmacSha256 = "HmacSHA256"
	// 签名版本号
	var version = "2"
	// HTTP请求方式
	var httpMethod = "GET"
	// 签名域名
	var host = "api.hotcoinfin.com"
	// 请求域名
	var domain = "https://api.hotcoinfin.com"
	// 接口URI
	var uri = "/v3/balance"
	// 业务map - 开发者自定义struct
	var businessMap map[string]string
	businessMap = make(map[string]string)

	// 业务参数
	businessMap["AccessKeyId"] = accessKey
	businessMap["SignatureMethod"] = hmacSha256
	businessMap["SignatureVersion"] = version
	// 当前UTC时间
	businessMap["Timestamp"] = utc()
	// 参数集合
	urlValues := url.Values{}

	for k, v := range businessMap {
		urlValues.Set(k, v)
	}
	// 参数编码
	reqBody := urlValues.Encode()

	// 业务参数拼接
	fmt.Println("业务参数拼接：\n" + reqBody)

	// 待签名的原始字符串
	var payload = httpMethod + "\n" + host + "\n" + uri + "\n" + reqBody
	fmt.Println("-----------------------------------------------------")
	fmt.Println("待签名的原始字符串：\n" + payload)

	hash := hmac.New(sha256.New, []byte(secretKey))
	hash.Write([]byte(payload))
	sign := url.QueryEscape(base64.StdEncoding.EncodeToString(hash.Sum(nil)))
	fmt.Println("-----------------------------------------------------")
	fmt.Println("已签名串：\n" + sign)
	// 拼接签名串
	reqBody = reqBody + "&Signature=" + sign
	var fullUrl = domain + uri + "?" + reqBody
	fmt.Println("最终请求URL：\n" + fullUrl)
	HttpGet(fullUrl)
}

// Hotcoin API的Golang示例 - 下单 - /v1/order/place

func postV1OrderPlace() {
	// API访问Key
	var accessKey = "您的accessKey"
	// 签名秘钥
	var secretKey = "您的secretKey"
	// 签名算法
	var hmacSha256 = "HmacSHA256"
	// 签名版本号
	var version = "2"
	// HTTP请求方式
	var httpMethod = "POST"
	// 签名域名
	var host = "api.hotcoinfin.com"
	// 请求域名
	var domain = "https://api.hotcoinfin.com"
	// 接口URI
	var uri = "/v1/order/place"
	// 业务map - 开发者自定义struct
	var businessMap map[string]string
	businessMap = make(map[string]string)

	// 业务参数
	businessMap["AccessKeyId"] = accessKey
	businessMap["SignatureMethod"] = hmacSha256
	businessMap["SignatureVersion"] = version
	// 当前UTC时间
	businessMap["Timestamp"] = utc()
	// 以下为接口的具体业务参数
	businessMap["symbol"] = "eth_usdt"
	businessMap["type"] = "buy"
	businessMap["tradeAmount"] = "1"
	businessMap["tradePrice"] = "2000"
	// 自定义订单ID，唯一
	businessMap["clientOrderId"] = "1b4db7eb-4057-5ddf-91e0-36dec72071f5"
	// 参数集合
	urlValues := url.Values{}

	for k, v := range businessMap {
		urlValues.Set(k, v)
	}
	// 参数编码
	reqBody := urlValues.Encode()

	// 业务参数拼接
	fmt.Println("业务参数拼接：\n" + reqBody)

	// 待签名的原始字符串
	var payload = httpMethod + "\n" + host + "\n" + uri + "\n" + reqBody
	fmt.Println("-----------------------------------------------------")
	fmt.Println("待签名的原始字符串：\n" + payload)

	hash := hmac.New(sha256.New, []byte(secretKey))
	hash.Write([]byte(payload))
	sign := url.QueryEscape(base64.StdEncoding.EncodeToString(hash.Sum(nil)))
	fmt.Println("-----------------------------------------------------")
	fmt.Println("已签名串：\n" + sign)
	// 拼接签名串
	reqBody = reqBody + "&Signature=" + sign
	var fullUrl = domain + uri + "?" + reqBody
	fmt.Println("最终请求URL：\n" + fullUrl)
	HttpPostUrl(fullUrl)
}

// Hotcoin API的Golang示例 - 批量下单 - /v2/order/batchOrders

func postV2OrderBatchOrders() {
	// API访问Key
	var accessKey = "您的accessKey"
	// 签名秘钥
	var secretKey = "您的secretKey"
	// 签名算法
	var hmacSha256 = "HmacSHA256"
	// 签名版本号
	var version = "2"
	// HTTP请求方式
	var httpMethod = "POST"
	// 签名域名
	var host = "api.hotcoinfin.com"
	// 请求域名
	var domain = "https://api.hotcoinfin.com"
	// 接口URI
	var uri = "/v2/order/batchOrders"
	// 业务map - 开发者自定义struct
	var businessMap map[string]string
	businessMap = make(map[string]string)

	// 业务参数
	businessMap["AccessKeyId"] = accessKey
	businessMap["SignatureMethod"] = hmacSha256
	businessMap["SignatureVersion"] = version
	// 当前UTC时间
	businessMap["Timestamp"] = utc()
	// 以下为接口的具体业务参数
	var orderMap1 map[string]string
	orderMap1 = make(map[string]string)
	orderMap1["symbol"] = "btc_usdt"
	orderMap1["type"] = "buy"
	orderMap1["tradeAmount"] = "0.26"
	orderMap1["tradePrice"] = "18888.68"
	// 自定义订单ID，唯一
	orderMap1["ClientOrderId"] = "1b4db7eb-4057-5ddf-91e0-36dec72071f5"
	var orderMap2 map[string]string
	orderMap2 = make(map[string]string)
	orderMap2["symbol"] = "btc_usdt"
	orderMap2["type"] = "buy"
	orderMap2["tradeAmount"] = "0.38"
	orderMap2["tradePrice"] = "18899.86"
	// 自定义订单ID，唯一
	orderMap2["ClientOrderId"] = "1b4db7eb-4057-5ddf-91e0-36dec72071f6"
	// 参数集合
	urlValues := url.Values{}

	for k, v := range businessMap {
		urlValues.Set(k, v)
	}
	// 参数编码
	reqBody := urlValues.Encode()

	// 业务参数拼接
	fmt.Println("业务参数拼接：\n" + reqBody)

	// 待签名的原始字符串
	var payload = httpMethod + "\n" + host + "\n" + uri + "\n" + reqBody
	fmt.Println("-----------------------------------------------------")
	fmt.Println("待签名的原始字符串：\n" + payload)

	hash := hmac.New(sha256.New, []byte(secretKey))
	hash.Write([]byte(payload))
	sign := url.QueryEscape(base64.StdEncoding.EncodeToString(hash.Sum(nil)))
	fmt.Println("-----------------------------------------------------")
	fmt.Println("已签名串：\n" + sign)
	// 拼接签名串
	reqBody = reqBody + "&Signature=" + sign
	var fullUrl = domain + uri + "?" + reqBody
	fmt.Println("最终请求URL：\n" + fullUrl)
	// 请求体JSON字符串
	jsonBody1, err1 := json.Marshal(orderMap1)
	if err1 != nil {
	}
	jsonBody2, err2 := json.Marshal(orderMap2)
	if err2 != nil {
	}
	jsonBodyStr := "[" + string(jsonBody1) + "," + string(jsonBody2) + "]"
	fmt.Println("JSON请求体：\n" + jsonBodyStr)
	HttpPostJson(fullUrl, jsonBodyStr)
}

// Get请求-url

func HttpGet(fullUrl string) (string, error) {
	resp, err := http.Get(fullUrl)
	if err != nil {
		return "", err
	}
	// 延迟关闭
	defer resp.Body.Close()
	result, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		return "", err
	}
	// 请求状态码
	fmt.Println("请求状态码：" + resp.Status)
	// 请求结果
	fmt.Println("请求结果：" + string(result))
	return string(result), err
}

// Post请求-url

func HttpPostUrl(fullUrl string) (string, error) {
	// 通过http.Post请求第三方接口
	resp, err := http.PostForm(fullUrl, url.Values{})
	if err != nil {
		fmt.Println(err)
		return "", err
	}
	result, err := ioutil.ReadAll(resp.Body)
	// 请求状态码
	fmt.Println("请求状态码：" + resp.Status)
	// 请求结果
	fmt.Println("请求结果：" + string(result))
	return string(result), err
}

// Post请求-json

func HttpPostJson(postUrl string, reqBody string) (string, error) {
	// 通过http.Post请求第三方接口
	resp, err := http.Post(postUrl, "application/json", strings.NewReader(reqBody))
	if err != nil {
		fmt.Println(err)
		return "", err
	}
	result, err := ioutil.ReadAll(resp.Body)
	// 请求状态码
	fmt.Println("请求状态码：" + resp.Status)
	// 请求结果
	fmt.Println("请求结果：" + string(result))
	return string(result), err
}

// 获取UTC时间

func utc() string {
	// 获取当前时间
	now := time.Now()

	// 转换为 UTC 时间
	utc := now.UTC()

	// 返回 UTC 时间
	return utc.Format(time.RFC3339)
}
