### 错误码
    DC00000("DC00000", "成功"),
        
    DC00001("DC00001", "缺少必要参数"),
        
    DC00002("DC00002", "操作数据库失败"),
        
    DC00003("DC00003", "系统错误")
        
    DC00004("DC00004", "用户登陆密码错误"),
        
    DC00005("DC00005", "该手机号已注册")
### 公共返回字段
| 字段名      | 描述     

| rspCode    | 返回码  

| rspMsg     | 返回码描述    


### 接口说明API
### 1.用户注册
##### URL:http://192.168.1.196:9090//outerApi/userRegister

##### 入参：

     字段名          描述          是否可空
     
     user_name      用户姓名          否   
     
     user_phone     用户电话          否   
     
     user_password  用户密码          否

##### 出参
    
    字段名       描述         
     
    rspCode     返回码
        
    rspMsg      返回值
        
    user_id     用户id

### 2.用户登陆
##### URL:http://192.168.1.196:9090//outerApi/login

##### 入参：

     字段名          描述          是否可空
     
     user_phone     用户电话          否   
     
     user_password  用户密码          否

##### 出参
    
    字段名                 描述     
        
    user_name             姓名
        
    balance               余额
        
    user_phone            电话    
        
    accumulate_points     积分
        
    member_level          用户等级
        
    rspCode               返回码
        
    rspMsg                返回值
        
    user_id               用户id
