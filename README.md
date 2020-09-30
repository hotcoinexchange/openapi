# Hotcoin API
## 安全认证
AccessKey为API 访问密钥，SecretKey为用户对请求进行签名的密钥。
重要提示：这两个密钥与账号安全紧密相关，无论何时都请勿向其它人透露
## 合法请求结构
基于安全考虑，除行情API 外的 API 请求都必须进行签名运算。一个合法的请求由以下几部分组成：<br>
方法请求地址,即访问服务器地址：hkapi.hotcoin.top后面跟上方法名，比如hkapi.hotcoin.top/v1/order/place。<br>
API 访问密钥（AccessKeyId） 您申请的 APIKEY 中的AccessKey。<br>
签名方法（SignatureMethod） 用户计算签名的基于哈希的协议，此处使用 HmacSHA256。<br>
签名版本（SignatureVersion） 签名协议的版本，此处使用2。<br>
时间戳（Timestamp） 您发出请求的时间 (UTC 时区)。在查询请求中包含此值有助于防止第三方截取您的请求。如：2017-05-11T16:22:06.123Z。再次强调是 (UTC 时区)  <br>
调用的必需参数和可选参数。可以在每个方法的说明中查看这些参数及其含义。签名计算得出的值，用于确保签名有效和未被篡改。 <br>
例：<br>
https://hkapi.hotcoin.top/v1/order/place? <br>
AccessKeyId=AccessKeyHotcoin123456789 <br>
&symbol=btc_gavc <br>
&type=buy <br>
&tradePrice=40000 <br>
&tradeAmount=0.1 <br>
&SignatureMethod=HmacSHA256 <br>
&SignatureVersion=2 <br>
&Timestamp=2017-05-11T16:22:06.123Z<br>
&Signature=calculated value <br>
## 签名运算
API 请求在通过 Internet 发送的过程中极有可能被篡改。为了确保请求未被更改，我们会要求用户在每个请求中带上签名，来校验参数或参数值在传输途中是否发生了更改。<br>

计算签名所需的步骤：<br>
规范要计算签名的请求 <br>
因为使用 HMAC 进行签名计算时，使用不同内容计算得到的结果会完全不同。所以在进行签名计算前，请先对请求进行规范化处理。下面以下单请求为例进行说明 <br>

https://hkapi.hotcoin.top/v1/order/place? <br>
AccessKeyId=AccessKeyHotcoin123456789 <br>
&SignatureMethod=HmacSHA256 <br>
&SignatureVersion=2 <br>
&Timestamp=2017-05-11T16:22:06.123Z <br>
&symbol=btc_gavc <br>
&type=buy <br>
&tradePrice=40000 <br>
&tradeAmount=0.1 <br>

请求方法（GET 或 POST），后面添加换行符\n。 <br>

GET\n <br>

添加小写的访问地址，后面添加换行符\n。 <br>

hkapi.hotcoin.top\n <br>

访问方法的路径，后面添加换行符\n。<br>

/v1/order/place\n <br>

按照ASCII码的顺序对参数名进行排序(使用 UTF-8 编码，且进行了 URI 编码，十六进制字符必须大写，如‘:’会被编码为'%3A'，空格被编码为'%20')。<br>
例如，下面是请求参数的原始顺序，进行过编码后。<br>

AccessKeyId=AccessKeyHotcoin123456789 <br>
&SignatureMethod=HmacSHA256 <br>
&SignatureVersion=2 <br>
&Timestamp=2017-05-11T16:22:06.123Z <br>
&symbol=btc_gavc <br>
&type=buy <br>
&tradePrice=40000 <br>
&tradeAmount=0.1 <br>

这些参数会被排序为：<br>

AccessKeyId=AccessKeyHotcoin123456789 <br>
SignatureMethod=HmacSHA256 <br>
SignatureVersion=2 <br>
Timestamp=2017-05-11T16%3A22%3A06.123Z& <br>
symbol=btc_gavc <br>
tradeAmount=0.01 <br>
tradePrice=40000 <br>
type=buy <br>

按照以上顺序，将各参数使用字符’&’连接。<br>

AccessKeyId=AccessKeyHotcoin123456789&SignatureMethod=HmacSHA256&SignatureVersion=2&Timestamp=2017-05-11T16%3A22%3A06.123Z&symbol=btc_gavc&tradeAmount=0.1&tradePrice=40000&type=buy <br>

组成最终的要进行签名计算的字符串如下：<br>

GET\n <br>
hkapi.hotcoin.top\n <br>
/v1/order/place\n <br>
AccessKeyId=AccessKeyHotcoin123456789&SignatureMethod=HmacSHA256&SignatureVersion=2&Timestamp=2017-05-11T16%3A22%3A06.123Z&symbol=btc_gavc&tradeAmount=0.1&tradePrice=40000&type=buy <br>

计算签名，将以下两个参数传入加密哈希函数： <br>
要进行签名计算的字符串 <br>

GET\n <br>
hkapi.hotcoin.top\n <br>
/v1/order/place\n <br>
AccessKeyId=AccessKeyHotcoin123456789&SignatureMethod=HmacSHA256&SignatureVersion=2&Timestamp=2017-05-11T16%3A22%3A06.123Z&symbol=btc_gavc&tradeAmount=0.1&tradePrice=40000&type=buy <br>

进行签名的密钥（SecretKey）<br>

SecretKeyHotcoin123456789 <br>

得到签名计算结果并进行 Base64编码 <br>

2oEC+yhkHTsNkgPUq4ZB/5mlY7EZAtUDWOQ5EO01D+I= <br>

将上述值作为参数Signature的取值添加到 API 请求中。 将此参数添加到请求时，必须将该值进行 URI 编码。<br>
最终，发送到服务器的 API 请求应该为：<br>

https://hotcoin.top/v1/order/place?AccessKeyId=AccessKeyHotcoin123456789&SignatureMethod=HmacSHA256&SignatureVersion=2&Timestamp=2017-05-11T16%3A22%3A06.123Z&symbol=btc_gavc&tradeAmount=0.1&tradePrice=40000&type=buy&Signature=2oEC%2ByhkHTsNkgPUq4ZB%2F5mlY7EZAtUDWOQ5EO01D%2BI%3D <br>

symbol 规则： 基础币种+计价币种。如BTC/USDT，symbol为btc_usdt；ETH/BTC， symbol为eth_btc。以此类推。<br>

## 接口列表

- [下单 /v1/order/place](#下单)
- [订单取消 /v1/order/cancel](#订单取消)
- [委单详情 /v1/order/detailById](#委单详情)
- [成交详情 /v1/order/counterpartiesById](#成交详情)
- [获取委单列表 /v1/order/entrust](#获取委单列表)
- [获取k线数据 /v1/ticker](#获取k线数据)
- [获取深度数据 /v1/depth](#获取深度数据)
- [获取实时成交数据 /v1/trade](#获取实时成交数据)
- [获取用户余额 /v1/balance](#获取用户余额)
- [当前和历史成交记录 /v1/order/matchresults ](#当前和历史成交记录)
- [批量撤单 /v1/order/batchCancelOrders ](#批量撤单)
- [批量下单 /v1/order/batchOrders ](#批量下单)

## Demo
- [Demo for java](https://github.com/hotcoinex/openapi/blob/master/ApiDemo.java)
- [Demo for Python](https://github.com/hotcoinex/openapi/blob/master/ApiDemo.py)



## api明细
### 下单

POST /v1/order/place

参数：

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
AccessKeyId|y|string|访问key||
SignatureVersion|y|string|版本
SignatureMethod|y|string|签名方法||HmacSHA256
Signature|y|string|ApiSecret||
Timestamp|y|string|时间戳||
symbol|y|string|交易对||例：btc_usdt
type|y|string|类型||"buy" ,”sell"
tradeAmount|y|number|数量||
tradePrice|y|number|价钱||

返回：

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
code|y|int|状态码||成功：200，失败：300
msg|y|string|消息||
time|y|long|当前毫秒数||
data|y|object|数据||

data

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
ID|y|bigint|订单id||

返回json

```json
{
   "code": 200,
   "msg": "委托成功",
   "time": 1536306331399,
   "data":
        {
            "ID": 18194813
        }
}
```


### 订单取消
POST /v1/order/cancel

参数：

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
AccessKeyId|y|string|访问key
SignatureVersion|y|string|版本
SignatureMethod|y|string|签名方法||HmacSHA256
Signature|y|string|ApiSecret
Timestamp|y|string|时间戳
id|y|bigint|委单id

返回：

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
code|y|int|状态码||成功：200，失败：300
msg|y|string|返回消息
time|y|long|当前毫秒数

返回json

```json
{
   "code": 200,
   "msg": "取消成功",
   "time": 1536306495984,
   "data": null
}
```
### 委单详情

GET /v1/order/detailById

参数：

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
AccessKeyId|y|string|访问key
SignatureVersion|y|string|版本
SignatureMethod|y|string|签名方法||HmacSHA256
Signature|y|string|ApiSecret
Timestamp|y|string|时间戳
id|y|bigint|委单id
leverAcctid|n|string|非杠杆下单无需传词字段，杠杆子账户id，对应开户接口的clientId||


返回：

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
code|y|int|状态码
msg|n|string|返回消息
time|y|long|当前毫秒数
data|y|object|委单详情

data

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
types|y|string|委单类型||买单 、卖单
leftcount|y|number|未成交
fees|y|number|手续费
last|y|number|当前委单最新成交价
count|y|number|数量
successamount|y|number|已成交总价
source|y|string|来源||API、WEB、APP
type|y|int|类型代码||0（买单），1（卖单）
price|y|number|价钱
buysymbol|n|string|买符号
sellsymbol|n|string|卖符号
time|y|string|创建时间
statusCode|y|int|状态码||1 未成交 2 部分成交 3 完全成交 4 撤单处理中 5 已撤销
status|y|string|状态||未成交、部分成交、完全成交、撤单处理中、已撤销

返回json：

```json
{
   "code": 200,
   "msg": "成功",
   "time": 1536306896294,
   "data":    {
      "types": "买单",
      "leftcount": 0.01,
      "fees": 0,
      "last": 0,
      "count": 0.01,
      "successamount": 0,
      "source": "API",
      "type": 0,
      "price": 40000,
      "buysymbol": "",
      "id": 18194814,
      "time": "2018-09-07 15:48:44",
      "sellsymbol": "",
      "statusCode":1,
      "status": "未成交"
   }
}
```
### 成交详情

GET /v1/order/counterpartiesById

参数：

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
AccessKeyId|y|string|访问key
SignatureVersion|y|string|版本
SignatureMethod|y|string|签名方法||HmacSHA256
Signature|y|string|ApiSecret
Timestamp|y|string|时间戳
id|y|bigint|委单id||

返回 : 

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
code|y|int|状态码
msg|n|string|返回消息
time|y|long|当前毫秒数
data|y|object|对手单详情

data

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
entrusts|y|array(object)|对手单列表

wallet

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
id|y|bigint|主键ID
isSelfTrade|y|int|是否自成交 0 否 1 是
sysmbol|y|string|交易对
entrustType|y|int|委单类型 0 买单 1 卖单
entrustId|y|bigint|委单ID
matchId|y|bigint|成交ID
amount|y|number|成交总价
prize|y|number|价格
count|y|number|数量
createTime|y|string|创建时间

返回json
```json
{
    "code":200,
    "data":{
        "entrusts":[
            {
                "amount":1.2042000000,
                "count":2.2300000000,
                "createTime":"2019-05-27 18:15:12",
                "entrustId":431879850,
                "entrustType":0,
                "id":101192723,
                "isSelfTrade":1,
                "matchId":431879852,
                "prize":0.5400000000,
                "sysmbol":"btc_gavc"
            }
        ]
    },
    "msg":"成功",
    "time":1568690580787
}

```


### 获取委单列表

GET /v1/order/entrust

参数：

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
AccessKeyId|y|string|访问key
SignatureVersion|y|string|版本
SignatureMethod|y|string|签名方法|HmacSHA256
Signature|y|string|ApiSecret
Timestamp|y|string|时间戳
symbol|y|string|交易对||例：btc_usdt
type|n|int|类型|0|0表示全部 1表示当前 2表示历史
page|n|int|页码|1|
count|y|int|条数|7|[1-100] 最大100条

返回：

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
code|y|int|状态码
msg|n|string|返回消息
time|y|long|当前毫秒数
data|y|object|委单详情

data

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
entrutsCur|n|array(object)|当前委单
entrutsHis|n|array(object)|历史委单

entrutsCur 及  entrutsHis类型相同

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
id|y|bigint|委单id
time|y|string|下单时间
types|y|string|委单类型|| 买单、卖单
source|y|string|委单来源||"WEB"，"APP"，"API"
price|y|number|下单价格
count|y|number|下单数量
leftcount|y|number|未成交数量
last|y|number|成交价格
successamount|y|number|成交总价
fees|y|number|手续费
status|y|string|委单状态||未成交、部分成交、完全成交、撤单处理中、已撤销
type|y|int|委单类型||	0( "买单"),1( "卖单")
buysymbol|y|string|币种类型符号
sellsymbol|y|string|币种类型符号

返回json

```json
{
   "code": 200,
   "msg": "获取成功！",
   "time": 1527841588334,
   "data":{
      "entrutsHis": [
  			 {
            "types": "买单",
            "leftcount": 1.0E-4,
            "fees": 0,
            "last": 0,
            "count": 1.0E-4,
            "successamount": 0,
            "source": "WEB",
            "type": 1,
            "price": 1.0E7,
            "buysymbol": "GAVC",
            "id": 947644,
            "time": "2018-06-27 17:45:14",
            "sellsymbol": "BTC",
            "status": "已撤销"
         },
         {
            "types": "买单",
            "leftcount": 1.0E-4,
            "fees": 0,
            "last": 0,
            "count": 1.0E-4,
            "successamount": 0,
            "source": "WEB",
            "type": 1,
            "price": 1.0E7,
            "buysymbol": "GAVC",
            "id": 947645,
            "time": "2018-06-27 17:45:14",
            "sellsymbol": "BTC",
            "status": "已撤销"
          }
      ],
      "entrutsCur": [
         {
         "types": "买单",
         "leftcount": 0.01,
         "fees": 0,
         "last": 0,
         "count": 0.01,
         "successamount": 0,
         "source": "API",
         "type": 0,
         "price": 40000,
         "buysymbol": "GAVC",
         "id": 18194814,
         "time": "2018-09-07 15:48:44",
         "sellsymbol": "BTC",
         "status": "未成交"
		},
       {
         "types": "卖单",
         "leftcount": 0.01,
         "fees": 0,
         "last": 0,
         "count": 0.01,
         "successamount": 0,
         "source": "API",
         "type": 0,
         "price": 40000,
         "buysymbol": "GAVC",
         "id": 18194814,
         "time": "2018-09-07 15:48:44",
         "sellsymbol": "BTC",
         "status": "未成交"
		}
      ]
   }
}
```
### 获取k线数据
GET /v1/ticker
参数：

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
AccessKeyId|y|string|访问key
SignatureVersion|y|string|版本
SignatureMethod|y|string|签名方法||HmacSHA256
Signature|y|string|ApiSecret
Timestamp|y|string|时间戳
step|y|int|时间：秒||60（1分钟）,300（5分钟）,900（15分钟）,1800（30分钟）,3600（1小时）,86400（1天）,604800（1周）,2592000（1月）
symbol|y|string|交易对||例：btc_gavc

返回 : 

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
code|y|int|状态码
msg|n|string|返回消息
time|y|long|当前毫秒数
data|y|array(array(number))|K线数据

data<br>

[[ <br>
   1527820200000,   //int 时间<br>
   54598.5,         //number  开<br>
   54598.5,         //number  高<br>
   54598.5,         //number  低<br>
   54598.5,         //number  收<br>
   0.0000          //number  量<br>
   ],<br>
   ......<br>
]<br>


返回json

```json
{
   "code": 200,
   "msg": "成功",
   "time": 1527838104874,
   "data": [
	 [
         1527820200000,
         54598.5,
         54598.5,
         54598.5,
         54598.5,
         0               
     ],
     [
         1527820200000,
         54598.5,
         54598.5,
         54598.5,
         54598.5,
         0
     ]
	]
}
```

### 获取深度数据：
GET /v1/depth
参数：

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
symbol|y|string|交易对||例：btc_gavc
step|n|int|加上此参数可查最新一个k线数据，类型为时间，单位秒||60,3*60,5*60,15*60,30*60,60*60（1小时）,24*60*60（1天）,7*24*60*60（1周）,30*24*60*60（1月）


返回 :

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
code|y|int|状态码
msg|n|string|返回消息
time|y|long|当前毫秒数
data|y|object|交易深度数据

data

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
depth|y|object
period|n|object|传step时才有值

depth

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
bids|y|array(array(string))|买盘,[price(成交价), amount(成交量)]
asks|y|array(array(string))|卖盘,[price(成交价), amount(成交量)]
date|y|long|时间戳
lastPrice|y|string|最新成交价

period

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
marketFrom|y|string|入参symbol
coinVol|y|string|入参symbol
type|y|long|入参step,时间
data|y|array（array）|最后一个k线数据，格式同上，但只有一个



返回json

```json
{
   "code": 200,
   "msg": "成功",
   "time": 1527837164605,
   "data":{
      "period":{
         "data": [[
            1527837120000,
            54598.5,
            54598.5,
            54598.5,
            54598.5,
            0
         ]],
         "marketFrom": "btc_gavc",
         "type": 60,
         "coinVol": "btc_gavc"
      },
      "depth":{
         "date": 1527837163,
         "asks": [
                        [
               '57373.8',
               '0.0387'
            ],
                        [
               '57751.26',
               '0.0128'
            ],
             [
               '57751.26',
               '0.0128'
            ]
         ],
	 "bids": [
                        [
               '57373.8',
               '0.0387'
            ],
                        [
               '57751.26',
               '0.0128'
            ],
             [
               '57751.26',
               '0.0128'
            ]
         ],
         "lastPrice": '54598.5'
      }
   }
}
```
### 获取实时成交数据：/v1/trade

参数：

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
AccessKeyId|y|string|访问key
SignatureVersion|y|string|版本
SignatureMethod|y|string|签名方法||HmacSHA256
Signature|y|string|ApiSecret
Timestamp|y|string|时间戳
count|y|int|Trades条数||0
symbol|y|string|交易对||例：btc_gavc

返回 : 

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
code|y|int|状态码
msg|n|string|返回消息
time|y|long|当前毫秒数
data|y|object|实时成交数据

data

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
trades|y|array(object)|trades数据
sellSymbol|y|string|sellSymbol
buySymbol|y|string|buySymbol

trades

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
price|y|long|成交价钱
amount|y|string|成交数量
id|y|string|成交id
time|y|string|成交时间
en_type|y|string|成交方向||"bid"(买入),"ask"(卖出)
type|y|string|成交类型||"买入","卖出"

返回json

```json
{
 "code": 200,
 "msg": "成功",
   "time": 1536315868962,
   "data":    {
     "sellSymbol": "BTC",
     "buySymbol": "GAVC",
     "trades": [
       {
          "price": 0.007,
          "amount": 66491.04,
          "id": 1,
          "time": "02:45:08",
          "en_type": "ask",
          "type": "卖出"
       }, 
	   {
          "price": 0.007,
          "amount": 66491.04,
          "id": 1,
          "time": "02:45:08",
          "en_type": "ask",
          "type": "卖出"
       }

	]
   }
}
```

### 获取用户余额

/v1/balance

参数：

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
AccessKeyId|y|string|访问key
SignatureVersion|y|string|版本
SignatureMethod|y|string|签名方法||HmacSHA256
Signature|y|string|ApiSecret
Timestamp|y|string|时间戳

返回 : 

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
code|y|int|状态码
msg|n|string|返回消息
time|y|long|当前毫秒数
data|y|object|交易深度数据

data

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
netassets|y|number|净资产，单位为gavc
totalassets|y|number|总资产，单位为gavc
wallet|y|array(object)|钱包列表

wallet

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
coinName|y|long|币种名称
uid|y|int|用户ID
coinId|y|int|币种ID
total|y|number|可用
frozen|y|number|冻结
symbol|y|string|币种symbol
shortName|y|string|币种简称


返回json
```json
{
   "code": 200,
   "msg": "成功",
   "time": 1527835756743,
   "data":    {
      "netassets": 0,
      "wallet": [
         {
			"uid":1100011,
			"coinId":1,
			"symbol":"BTC",
			"total":1000.0000000000,
			"frozen":1000.0000000000,
			"coinName":"比特币",
			"shortName":"BTC"
		},
		{
			"uid":1100011,
			"coinId":2,
			"symbol":"LTC",
			"total":1000.0000000000,
			"frozen":1000.0000000000,
			"coinName":"莱特币",
			"shortName":"LTC"
		},
		{
			"uid":1100011,
			"coinId":4,
			"symbol":"ETH",
			"total":1000.0000000000,
			"frozen":0E-10,
			"coinName":"以太坊",
			"shortName":"ETH"
		}
      ],
      "totalassets": 0
   }
}
```
### 杠杆下单（暂未开放）：/v1/order/leverorder

参数：

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
AccessKeyId|y|string|访问key||
SignatureVersion|y|string|版本
SignatureMethod|y|string|签名方法||HmacSHA256
Signature|y|string|ApiSecret||
Timestamp|y|string|时间戳||
symbol|y|string|交易对||例：btc_usdt
type|y|string|类型||"buy" ,”sell"
tradeAmount|y|number|数量||
tradePrice|y|number|价钱||
leverAcctid|y|string|杠杆子账户id，对应开户接口的clientId||

返回：

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
code|y|int|状态码||成功：200，失败：300
msg|y|string|消息||
time|y|long|当前毫秒数||
data|y|object|数据||

data

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
ID|y|bigint|订单id||

返回json

```json
{
   "code": 200,
   "msg": "委托成功",
   "time": 1536306331399,
   "data":
        {
            "ID": 18194813
        }
}
```
### 订单取消（暂未开放）：/v1/order/levercancel

参数：

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
AccessKeyId|y|string|访问key
SignatureVersion|y|string|版本
SignatureMethod|y|string|签名方法||HmacSHA256
Signature|y|string|ApiSecret
Timestamp|y|string|时间戳
id|y|bigint|委单id
leverAcctid|y|string|杠杆子账户id，对应开户接口的clientId||

返回：

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
code|y|int|状态码||成功：200，失败：300
msg|y|string|返回消息
time|y|long|当前毫秒数

返回json

```json
{
   "code": 200,
   "msg": "取消成功",
   "time": 1536306495984,
   "data": null
}
```

### 获取用户余额：/v1/leverbalance

参数：

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
AccessKeyId|y|string|访问key
SignatureVersion|y|string|版本
SignatureMethod|y|string|签名方法||HmacSHA256
Signature|y|string|ApiSecret
Timestamp|y|string|时间戳
leverAcctid|y|string|杠杆子账户id，对应开户接口的clientId||

返回 : 

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
code|y|int|状态码
msg|n|string|返回消息
time|y|long|当前毫秒数
data|y|object|交易深度数据

data

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
netassets|y|number|净资产
totalassets|y|number|总资产
wallet|y|array(object)|钱包列表

wallet

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
coinName|y|long|币种名称
uid|y|int|用户ID
coinId|y|int|币种ID
total|y|number|可用
frozen|y|number|冻结
symbol|y|string|币种symbol
shortName|y|string|币种简称

返回json
```json
{
   "code": 200,
   "msg": "成功",
   "time": 1527835756743,
   "data":    {
      "netassets": 0,
      "wallet": [
         {
			"uid":1100011,
			"coinId":1,
			"symbol":"BTC",
			"total":1000.0000000000,
			"frozen":1000.0000000000,
			"coinName":"比特币",
			"shortName":"BTC"
		},
		{
			"uid":1100011,
			"coinId":2,
			"symbol":"LTC",
			"total":1000.0000000000,
			"frozen":1000.0000000000,
			"coinName":"莱特币",
			"shortName":"LTC"
		},
		{
			"uid":1100011,
			"coinId":4,
			"symbol":"ETH",
			"total":1000.0000000000,
			"frozen":0E-10,
			"coinName":"以太坊",
			"shortName":"ETH"
		}
      ],
      "totalassets": 0
   }
}
```
### 当前和历史成交记录 

GET /v1/order/matchresults

参数：

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
AccessKeyId|y|string|访问key
SignatureVersion|y|string|版本
SignatureMethod|y|string|签名方法||HmacSHA256
Signature|y|string|ApiSecret
Timestamp|y|string|时间戳
symbol|y|string|交易对||例：btc_usdt
types|y|string|查询的订单类型组合，使用','分割||0：买, 1：卖
startDate|n|string|查询开始日期, 日期格式yyyy-mm-dd|-1d 查询结束日期的前1天|取值范围 [((endDate) – 1), (endDate)] ，查询窗口最大为2天，窗口平移范围为最近61天
endDate|n|string|查询结束日期, 日期格式yyyy-mm-dd|today|取值范围 [(today-60), today] ，查询窗口最大为2天，窗口平移范围为最近61天
from|n|string|查询起始 ID|订单成交记录ID（最大值）|
direct|n|string|查询方向|默认 next， 成交记录 ID 由大到小排序|prev 向前，时间（或 ID）正序；next 向后，时间（或 ID）倒序）
size|n|string| 查询记录大小|100|[1，100]

返回 : 

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
code|y|int|状态码
msg|n|string|返回消息
time|y|long|当前毫秒数
data|y|object|实时成交数据

data

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
entrustdetail|n|array(object)|成交记录

entrustdetail

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
createdAt|y|long|成交时间
filledAmount|y|string|成交数量
filledFees|y|string|成交手续费
id|y|long|订单成交记录id
matchId|y|long|撮合id
orderId|y|long|订单id
price|y|string|成交价格
type|y|string|订单类型||0：买, 1：卖
role|y|string|成交角色||taker,maker

### 批量撤单 

POST /v1/order/batchCancelOrders 
`注意：此接口只提交取消请求，实际取消结果需要通过订单状态，撮合状态等接口来确认。`

参数：

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
orderIds|y|String|撤销订单ID列表||单次不超过100个订单id 例如 "2232,1232,2321"

返回 : 

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
code|y|int|状态码
msg|n|string|返回消息
data|y|object|

### 批量撤单(Open Orders)

POST /v1/order/batchCancelOpenOrders
`注意：此接口只提交取消请求，实际取消结果需要通过订单状态，撮合状态等接口来确认。`

参数：

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
symbol|y|String|交易代码列表（最多10 个symbols，多个交易代码间以逗号分隔），btc_usdt, eth_btc...（||
side|n|String|交易方向||buy -买方向 sell -卖方向  为空时，则获取所有方向的委单进行撤销。

返回 : 

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
code|y|int|状态码
msg|n|string|返回消息
data|y|object|

data

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
{successCount|y|int|成功撤销数量
failCount}|y|int|撤销失败数量



### 批量下单

API Key 权限：交易

一个批量最多10张订单
POST /v1/order/batchOrders

参数：
参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
orders|y|object|订单列表||

orders

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
[{symbol|y|string|交易对||例：btc_usdt
type|y|string|类型||"buy" ,”sell"
tradeAmount|y|number|数量||
tradePrice}]|y|number|价钱||

返回 : 

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
code|y|int|状态码
msg|n|string|返回消息
data|y|object|

data

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
[{ID|y|bigint|订单id||
errcode|n|string|返回错误码
errmsg}]|n|string|返回错误描述
