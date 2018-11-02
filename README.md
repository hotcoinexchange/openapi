# API文档
## 安全认证
目前关于apikey申请和修改，请在“账户 - API管理”页面进行相关操作。其中AccessKey为API 访问密钥，SecretKey为用户对请求进行签名的密钥。
重要提示：这两个密钥与账号安全紧密相关，无论何时都请勿向其它人透露
## 合法请求结构
基于安全考虑，除行情API 外的 API 请求都必须进行签名运算。一个合法的请求由以下几部分组成：
方法请求地址,即访问服务器地址：hkapi.hotcoin.top后面跟上方法名，比如hkapi.hotcoin.top/v1/balance。
API 访问密钥（AccessKeyId） 您申请的 APIKEY 中的AccessKey。
签名方法（SignatureMethod） 用户计算签名的基于哈希的协议，此处使用 HmacSHA256。
签名版本（SignatureVersion） 签名协议的版本，此处使用2。
时间戳（Timestamp） 您发出请求的时间 (UTC 时区) (UTC 时区) (UTC 时区)。在查询请求中包含此值有助于防止第三方截取您的请求。如：2017-05-11 16:22:06。 调用的必需参数和可选参数。可以在每个方法的说明中查看这些参数及其含义。签名计算得出的值，用于确保签名有效和未被篡改。
例：
https://hkapi.hotcoin.top/v1/order/cancel?
AccessKeyId=e2xxxxxx-99xxxxxx-84xxxxxx-7xxxx
&id=1234567890
&SignatureMethod=HmacSHA256
&SignatureVersion=2
&Timestamp=2018-01-01 00:00:00
&Signature=calculated value
## 签名运算
API 请求在通过 Internet 发送的过程中极有可能被篡改。为了确保请求未被更改，我们会要求用户在每个请求中带上签名，来校验参数或参数值在传输途中是否发生了更改。

计算签名所需的步骤：
规范要计算签名的请求
因为使用 HMAC 进行签名计算时，使用不同内容计算得到的结果会完全不同。所以在进行签名计算前，请先对请求进行规范化处理。下面以查询某订单详情请求为例进行说明

https://hkapi.hotcoin.top/v1/order/cancel?
AccessKeyId=e2xxxxxx-99xxxxxx-84xxxxxx-7xxxx
&id=1234567890
&SignatureMethod=HmacSHA256
&SignatureVersion=2
&Timestamp=2017-05-11T15:19:30
请求方法（GET 或 POST），后面添加换行符\n。
GET\n
添加小写的访问地址，后面添加换行符\n。
hkapi.hotcoin.top\n
访问方法的路径，后面添加换行符\n。
/ v1/order/cancel \n
按照ASCII码的顺序对参数名进行排序(使用 UTF-8 编码，且进行了 URI 编码，十六进制字符必须大写，如‘:’会被编码为'%3A'，空格被编码为'%20')。
例如，下面是请求参数的原始顺序，进行过编码后。

AccessKeyId=e2xxxxxx-99xxxxxx-84xxxxxx-7xxxx
SignatureMethod=HmacSHA256
SignatureVersion=2
Timestamp=2017-05-11%2015%3A19%3A30
id=1234567890

这些参数会被排序为：

AccessKeyId=e2xxxxxx-99xxxxxx-84xxxxxx-7xxxx
id=1234567890
SignatureMethod=HmacSHA256
SignatureVersion=2
Timestamp=2017-05-11%2015%3A19%3A30
按照以上顺序，将各参数使用字符’&’连接。

AccessKeyId=e2xxxxxx-99xxxxxx-84xxxxxx-7xxxx&SignatureMethod=HmacSHA256&SignatureVersion=2&Timestamp=2017-05-11T15%3A19%3A30&order-id=1234567890
组成最终的要进行签名计算的字符串如下：

GET\n
hkapi.hotcoin.top\n
/v1/order/cancel\n
AccessKeyId=e2xxxxxx-99xxxxxx-84xxxxxx-7xxxx&id=1234567890&SignatureMethod=HmacSHA256&SignatureVersion=2&Timestamp=2017-05-11%2015%3A19%3A30
计算签名，将以下两个参数传入加密哈希函数：
要进行签名计算的字符串
GET\n
hkapi.hotcoin.top\n
hkapi.hotcoin.top\n
AccessKeyId=e2xxxxxx-99xxxxxx-84xxxxxx-7xxxx&id=1234567890&SignatureMethod=HmacSHA256&SignatureVersion=2&Timestamp=2017-05-11T15%3A19%3A30
进行签名的密钥（SecretKey）
b0xxxxxx-c6xxxxxx-94xxxxxx-dxxxx
得到签名计算结果并进行 Base64编码
4F65x5A2bLyMWVQj3Aqp+B4w+ivaA7n5Oi2SuYtCJ9o=
将上述值作为参数Signature的取值添加到 API 请求中。 将此参数添加到请求时，必须将该值进行 URI 编码。
最终，发送到服务器的 API 请求应该为：

https://hotcoin.top/v1/order/cancel? AccessKeyId=e2xxxxxx-99xxxxxx-84xxxxxx-7xxxx&id=1234567890&SignatureMethod=HmacSHA256&SignatureVersion=2&Timestamp=2017-05-11T15%3A19%3A30&Signature=4F65x5A2bLyMWVQj3Aqp+B4w+ivaA7n5Oi2SuYtCJ9o=

symbol 规则： 基础币种+计价币种。如BTC/USDT，symbol为btc_usdt；ETH/BTC， symbol为eth_btc。以此类推。
## api明细
### 下单：/v1/order/place

参数：

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
AccessKeyId|y|String|访问key||
SignatureVersion|y|String|版本
SignatureMethod|y|String|签名方法||HmacSHA256
Signature|y|String|ApiSecret||
Timestamp|y|String|时间戳||
symbol|y|String|交易对||例：btc_usdt
type|y|String|类型||"buy" ,”sell"
tradeAmount|y|BigDecimal|数量||
tradePrice|y|BigDecimal|价钱||

返回：

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
code|y|Int|状态码||成功：200，失败：300
msg|y|String|消息||
time|y|long|当前毫秒数||
data|y|Object|数据||

data

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
ID|y|Int|订单id||

返回json

```json
{
   "code": 200,
   "msg": "委託成功",
   "time": 1536306331399,
   "data":
        {
            "ID": 18194813
        }
}
```


### 订单取消：/v1/order/cancel

参数：

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
AccessKeyId|y|String|访问key
SignatureVersion|y|String|版本
SignatureMethod|y|String|签名方法||HmacSHA256
Signature|y|String|ApiSecret
Timestamp|y|String|时间戳
id|y|Bigint|委单id

返回：

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
code|y|Int|状态码||成功：200，失败：300
msg|y|String|返回消息
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
### 委单详情：/v1/order/detailById

参数：

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
AccessKeyId|y|String|访问key
SignatureVersion|y|String|版本
SignatureMethod|y|String|签名方法||HmacSHA256
Signature|y|String|ApiSecret
Timestamp|y|String|时间戳
id|y|Bigint|委单id

返回：

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
code|y|Int|状态码
msg|n|String|返回消息
time|y|long|当前毫秒数
data|y|Map|委单详情

data

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
types|y|String|委单类型||買單 、賣單
leftcount|y|BigDecimal|未成交
fees|y|BigDecimal|手续费
last|y|BigDecimal|成交数量
count|y|BigDecimal|数量
successamount|y|BigDecimal|已成交总价
source|y|String|来源||API、WEB、APP
type|y|int|类型代码||0（买单），1（卖单）
price|y|BigDecimal|价钱
buysymbol|n|String|买符号
sellsymbol|n|String|卖符号
time|y|String|创建时间
status|y|String|状态||未成交、部分成交、完全成交、撤單處理中、已撤銷

返回json：

```json
{
   "code": 200,
   "msg": "成功",
   "time": 1536306896294,
   "data":    {
      "types": "買單",
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
      "status": "未成交"
   }
}
```
### 获取委单列表：/v1/order/entrust

参数：

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
AccessKeyId|y|String|访问key
SignatureVersion|y|String|版本
SignatureMethod|y|String|签名方法|HmacSHA256
Signature|y|String|ApiSecret
Timestamp|y|String|时间戳
symbol|y|String|交易对||例：btc_usdt
type|n|int|类型|0|0表示全部 1表示当前 2表示历史
count|y|int|条数|7

返回：

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
code|y|int|状态码
msg|n|String|返回消息
time|y|long|当前毫秒数
data|y|Map|委单详情

data

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
entrutsCur|n|Array(map)|当前委单
entrutsHis|n|Array(map)|历史委单

entrutsCur 及  entrutsHis类型相同

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
id|y|BigInteger|委单id
time|y|String|下单时间
types|y|String|委单类型枚举|| "買單", "賣單"
source|y|String|委单来源||"WEB"，"APP"，"API"
price|y|BigDecimal|下单价格
count|y|BigDecimal|下单数量
leftcount|y|BigDecimal|未成交数量
last|y|BigDecimal|成交价格
successamount|y|BigDecimal|成交总价
fees|y|BigDecimal|手续费
status|y|String|委单状态||"未成交", "部分成交", "完全成交", "撤单处理中", "已撤销"
type|y|int|委单类型||	0( "买单"),1( "卖单")
buysymbol|y|String|币种类型符号
sellsymbol|y|String|币种类型符号

返回json

```json
{
   "code": 200,
   "msg": "获取成功！",
   "time": 1527841588334,
   "data":{
      "entrutsHis": [
  			 {
            "types": "賣單",
            "leftcount": 1.0E-4,
            "fees": 0,
            "last": 0,
            "count": 1.0E-4,
            "successamount": 0,
            "source": "WEB",
            "type": 1,
            "price": 1.0E7,
            "buysymbol": "",
            "id": 947644,
            "time": "2018-06-27 17:45:14",
            "sellsymbol": "",
            "status": "已撤銷"
         },
         {
            "types": "賣單",
            "leftcount": 1.0E-4,
            "fees": 0,
            "last": 0,
            "count": 1.0E-4,
            "successamount": 0,
            "source": "WEB",
            "type": 1,
            "price": 1.0E7,
            "buysymbol": "",
            "id": 947645,
            "time": "2018-06-27 17:45:14",
            "sellsymbol": "",
            "status": "已撤銷"
          }
      ],
      "entrutsCur": [
         {
         "types": "買單",
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
         "status": "未成交"
		},
       {
         "types": "買單",
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
         "status": "未成交"
		}
      ]
   }
}
```
### 获取k线数据：/v1/ticker

参数：

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
AccessKeyId|y|String|访问key
SignatureVersion|y|String|版本
SignatureMethod|y|String|签名方法||HmacSHA256
Signature|y|String|ApiSecret
Timestamp|y|String|时间戳
step|y|int|时间：秒||60,3*60,5*60,15*60,30*60,60*60（1小时）,24*60*60（1天）,7*24*60*60（1周）,30*24*60*60（1月）
symbol|y|String|交易对||例：btc_gset

返回 : 

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
code|y|int|状态码
msg|n|String|返回消息
time|y|long|当前毫秒数
data|y|Array(Array(int))|K线数据

data<br>

[[<br>
   1527820200000,   //int 时间<br>
   54598.5,         //float  开<br>
   54598.5,         //float  高<br>
   54598.5,         //float  低<br>
   54598.5,         //float  收<br>
   0               //float  量<br>
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

### 获取深度数据：/v1/depth

参数：

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
AccessKeyId|y|String|访问key
SignatureVersion|y|String|版本
SignatureMethod|y|String|签名方法||HmacSHA256
Signature|y|String|ApiSecret
Timestamp|y|String|时间戳
step|y|int|时间：秒||60,3*60,5*60,15*60,30*60,60*60（1小时）,24*60*60（1天）,7*24*60*60（1周）,30*24*60*60（1月）
symbol|y|String|交易对||例：btc_gset

返回 :

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
code|y|int|状态码
msg|n|String|返回消息
time|y|long|当前毫秒数
data|y|Map|交易深度数据

data

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
depth|y|map
period|y|map|入参step大于0时才有值

period

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
marketFrom|y|String|入参symbol
coinVol|y|String|入参symbol
type|y|long|入参step,时间
data|y|Array（Array）|最后一个k线数据，格式同上，但只有一个

depth

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
bids|y|Array(Array(long))|买盘,[price(成交价), amount(成交量)]
asks|y|Array(Array(long))|卖盘,[price(成交价), amount(成交量)]
date|y|long|时间戳
lastPrice|y|BigDecimal|上次成交价

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
         "marketFrom": "btc_gset",
         "type": 60,
         "coinVol": "btc_gset"
      },
      "depth":{
         "date": 1527837163,
         "asks": [
                        [
               57373.8,
               0.0387
            ],
                        [
               57751.26,
               0.0128
            ],
             [
               57751.26,
               0.0128
            ]
         ],
         "lastPrice": 54598.5
      }
   }
}
```
### 获取 Trade Detail 数据：/v1/trade

参数：

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
AccessKeyId|y|String|访问key
SignatureVersion|y|String|版本
SignatureMethod|y|String|签名方法||HmacSHA256
Signature|y|String|ApiSecret
Timestamp|y|String|时间戳
count|y|int|Trades条数||0
symbol|y|String|交易对||例：btc_gset

返回 : 

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
code|y|int|状态码
msg|n|String|返回消息
time|y|long|当前毫秒数
data|y|Map|交易深度数据

data

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
trades|y|Array(map)|trades数据
sellSymbol|y|String|sellShortName
buySymbol|y|String|buyShortName

trades

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
price|y|long|成交价钱
amount|y|String|成交数量
id|y|String|成交id
time|y|String|成交时间
en_type|y|String|成交方向||"bid","ask"
type|y|String|成交类型||"买入","卖出"

返回json

```json
{
 "code": 200,
 "msg": "成功",
   "time": 1536315868962,
   "data":    {
     "sellSymbol": "BTC",
     "buySymbol": "GSET",
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

获取用户余额：/v1/balance

参数：

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
AccessKeyId|y|String|访问key
SignatureVersion|y|String|版本
SignatureMethod|y|String|签名方法||HmacSHA256
Signature|y|String|ApiSecret
Timestamp|y|String|时间戳

返回 : 

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
code|y|int|状态码
msg|n|String|返回消息
time|y|long|当前毫秒数
data|y|map|交易深度数据

data

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
netassets|y|BigDecimal|净资产
totalassets|y|BigDecimal|总资产
wallet|y|Array(map)|钱包列表

wallet

参数名称|是否必须|类型|描述|默认值|取值范围
------------- | ------------- |  ------------- | ------------- |  ------------- | -------------
id|y|int|主键ID
uid|y|int|用户ID
coinId|y|int|币种ID
total|y|BigDecimal|可用
frozen|y|BigDecimal|冻结
borrow|y|BigDecimal|理财
ico|y|BigDecimal|ico
gmtCreate|y|long|创建时间
gmtModified|y|long|更新时间

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
            "id": 1,
            "uid": 1,
            "coinId": 1,
            "total": 0,
            "frozen": 0,
            "borrow": 0,
            "ico": 0,
            "gmtCreate": 1507626798000,
            "gmtModified": 1507626798000,
            "loginName": null,
            "nickName": null,
            "realName": null,
            "coinName": "比特币",
            "shortName": "BTC",
            "logo": null
         },
         {
            "id": 1,
            "uid": 1,
            "coinId": 1,
            "total": 0,
            "frozen": 0,
            "borrow": 0,
            "ico": 0,
            "gmtCreate": 1507626798000,
            "gmtModified": 1507626798000,
            "loginName": null,
            "nickName": null,
            "realName": null,
            "coinName": "比特币",
            "shortName": "BTC",
            "logo": null
         }
      ],
      "totalassets": 0
   }
}
```



