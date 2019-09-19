import base64
import hashlib
import hmac
import urllib
import urllib.parse
import urllib.request
import requests
import time
from datetime import datetime

##########################################
# 配置你的KEY
ACCESS_KEY = "Your ACCESS_KEY"
SECRET_KEY = "Your SECRET_KEY"
##########################################

API_HOST = 'hkapi.hotcoin.top'
API_RUL = 'https://' + API_HOST  # + API_TICKER_URI


def paramsSign(params, paramsPrefix, accessSecret):
    host = "hkapi.hotcoin.top"
    method = paramsPrefix['method'].upper()
    uri = paramsPrefix['uri']
    tempParams = urllib.parse.urlencode(sorted(params.items(), key=lambda d: d[0], reverse=False))
    payload = '\n'.join([method, host, uri, tempParams]).encode(encoding='UTF-8')
    accessSecret = accessSecret.encode(encoding='UTF-8')
    return base64.b64encode(hmac.new(accessSecret, payload, digestmod=hashlib.sha256).digest())


def http_post_request(url, params, timeout=10):
    response = requests.post(url, params, timeout=timeout)
    if response.status_code == 200:
        return response.json()
    else:
        return


def get_utc_str():
    return datetime.utcnow().strftime('%Y-%m-%dT%H:%M:%S.%f')[:-3] + 'Z'

def api_key_post(params, API_URI, timeout=10):
    method = 'POST'
    params_to_sign = {'AccessKeyId': ACCESS_KEY,
                      'SignatureMethod': 'HmacSHA256',
                      'SignatureVersion': '2',
                      'Timestamp': get_utc_str()}
    host_name = urllib.parse.urlparse(API_RUL).hostname
    host_name = host_name.lower()
    paramsPrefix = {"host": host_name, 'method': method, 'uri': API_URI}
    params_to_sign.update(params)
    params_to_sign['Signature'] = paramsSign(params_to_sign, paramsPrefix, SECRET_KEY).decode(encoding='UTF-8')
    url = API_RUL + API_URI
    return http_post_request(url, params_to_sign, timeout)


# 下单
def order_send(symbol, op_type, price, volume, timeout=10):
    params = {'symbol': symbol,
              'type': op_type,
              'tradePrice': price,
              'tradeAmount': volume}
    API_RUI = '/v1/order/place'
    return api_key_post(params, API_RUI, timeout)


if __name__ == "__main__":
    symbol = 'btc_gavc'
    price = 0.1
    volume = 40000
    print(order_send(symbol, 'buy', price, volume, timeout=10))

     