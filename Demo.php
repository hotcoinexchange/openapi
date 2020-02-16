<?php

class Demo
{

    /**
     * @var object  当前调用类实例
     */
    private static $instance = null;

    private $request_data;

    /**
     * @var curl curl实例
     */
    protected $curl;

    /**
     * 版本号
     */
    const VERSION = 2;

    /**
     * 加密方法
     */
    const HmacSHA256 = "HmacSHA256";

    /**
     * 请求地址
     */
    const HOST = "hkapi.hotcoin.top";

    /**
     * 配置你的KEY
     */
    const ACCESS_KEY = "YOUR ACCESS_KEY";
    const SECRET_KEY = "YOUR SECRET_KEY";


    /**
     * HTTP请求类型
     */
    const REQUEST_GET = "GET";
    const REQUEST_POST = "POST";


    /**
     * 初始化curl函数
     */
    public function __construct()
    {
        $this->curl = curl_init();
        if (empty($this->request_data)) {
            $this->request_data = $_REQUEST;
        }
    }

    /**
     * @return object|static 得到一个调用类的实例
     */
    public static function getInstance()
    {
        if (self::$instance == null) {
            self::$instance = new static();
        }
        return self::$instance;
    }

    /*
     * 发送数据给服务商
     */
    public function sendRequest($url, $param, $type = self::REQUEST_GET, $ssl = false)
    {
        //  根据请求方式的不同组合参数
        if ($type === self::REQUEST_POST) {
            curl_setopt($this->curl, CURLOPT_HTTPHEADER, array('Content-Type: application/x-www-form-urlencoded'));
            curl_setopt($this->curl, CURLOPT_POST, true);
            curl_setopt($this->curl, CURLOPT_POSTFIELDS, http_build_query($param));
        } else {
            curl_setopt($this->curl, CURLOPT_POST, false);
            if (!empty($param)) $url = ($url . "?" . http_build_query($param));
        }
        //  是否开启SSL验证
        if ($ssl === true) {
            curl_setopt($this->curl, CURLOPT_SSL_VERIFYSTATUS, false);
            curl_setopt($this->curl, CURLOPT_SSL_VERIFYPEER, false);
        }
        //  设置返回值
        curl_setopt($this->curl, CURLOPT_RETURNTRANSFER, 1);
        //  设置请求URL
        curl_setopt($this->curl, CURLOPT_URL, $url);
        //  执行
        $result = curl_exec($this->curl);
        if ($result === false) {
            return curl_error($this->curl);
        }
        return $result;
    }


    public function main()
    {
        //  下单
        $uri = "/v1/order/place";
        $params = [
            "AccessKeyId" => self::ACCESS_KEY,
            "SignatureVersion" => self::VERSION,
            "SignatureMethod" => self::HmacSHA256,
            "Timestamp" => date("Y-m-d\TH:i:s") . ".000Z",
            "symbol" => "btc_gavc",
            "type" => "buy",
            "tradePrice" => 1000,
            "tradeAmount" => 1
        ];
//        echo json_encode($params);
//        exit();
        //  得到签名
        $sign = $this->getSignature(self::SECRET_KEY, self::HOST, $uri, self::REQUEST_POST, $params);
        $params["Signature"] = $sign;
        return $this->sendRequest("https://" . self::HOST . $uri, $params, self::REQUEST_POST);
    }

    /**
     * 得到签名方法
     * @param $apiSecret
     * @param $host
     * @param $uri
     * @param $httpMethod
     * @param $params
     * @return string
     */
    public function getSignature($apiSecret, $host, $uri, $httpMethod, $params)
    {
        ksort($params);
        $payload = strtoupper($httpMethod) . "\n" . strtolower($host) . "\n" . $uri . "\n";
        $flag = false;
        foreach ($params as $key => $value) {
            $flag = true;
            $payload = $payload . $key . "=" . urlencode($value) . "&";
        }
        if ($flag) $payload = substr($payload, 0, -1);
        return base64_encode(hash_hmac('sha256', $payload, $apiSecret, true));
    }


}

echo (new Demo())->main();
?>
