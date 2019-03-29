## oauth2认证

### 获取code

http://localhost:8083/oauth/authorize?client_id=clientapp&redirect_uri=http://localhost:9001/callback&response_type=code&scope=read_userinfo


### 通过code获取token


http://127.0.0.1:8083/oauth/token?grant_type=authorization_code&code=EFQTld&redirect_uri=http://localhost:9001/callback&scope=read_userinfo&client_secret=112233&client_id=clientapp
