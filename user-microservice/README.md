## oauth2认证

### 获取code

http://localhost:8103/oauth/authorize?client_id=client_id_123&redirect_uri=http://localhost:8103/callback&response_type=code&scope=authentication


### 通过code获取token


http://127.0.0.1:8103/oauth/token?grant_type=authorization_code&code=EFQTld&redirect_uri=http://localhost:8103/callback&scope=authentication&client_secret=123456&client_id=client_id_123
