# websocket-server

## 简介
本项目是基于spring-boot + netty 实现的websocket聊天服务器，使用mongodb存储聊天记录， 通过token实现用户登录与验证，适用于网站平台客服聊天服务。

## 要求
- jdk版本为1.8或1.8+
- monogdb-4.0+

## 验证方法
Token 使用 AES-256-CBC 方式加解密，密码原文json格式如：
```
{"userId":1,"nickname":"nick","expired":1566276451000}
```
## 附
PHP openssl_encrypt 生成token方法
```
function aes_encrypt($data, $key) {
    $hash_key   = md5($key);
    $iv         = random_code(16);
    $data       = openssl_encrypt($data, 'AES-256-CBC', $hash_key, OPENSSL_RAW_DATA, $iv);
    return base64_url_encode($data) . ':' . base64_url_encode($iv);
}

function base64_url_encode($data) {
    return rtrim(strtr(base64_encode($data), '+/', '-_'), '=');
} 
```