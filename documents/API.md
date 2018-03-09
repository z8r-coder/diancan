### 环境信息
redis:192.168.1.199:6379    
redis-cli -h 192.168.1.199 -p 6379

前台 http server:192.168.1.196:9090  
curl -d "" "192.168.1.196:9090/test/test"

mysql server:192.168.1.160:3306

后台 http server:192.168.1.168:8080   
curl -d "" "192.168.1.168:8080/test/test"
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

##### 出参 JSON
    
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

##### 出参 JSON
    
    字段名                 描述     
        
    user_name             姓名
        
    balance               余额
        
    user_phone            电话    
        
    accumulate_points     积分
        
    member_level          用户等级
        
    rspCode               返回码
        
    rspMsg                返回值
        
    user_id               用户id
    
### 3.获取未被订的桌位号
##### URL:http://192.168.1.196:9090//outerApi/getAvailableBoard

##### 入参：
     字段名                      描述          是否可空
     
     order_board_date           预定时间          否   
     
     order_board_time_interval  预定时段          否
                               （0.上午 1.中午.2）    
                                
     board_type                 餐桌类型          否
                                (1.中 2.西)
                                
     order_people_number        餐桌人数          否
                                (2,4,10)    
     
##### 出参:
    字段名                 描述
        
    board_id_set          空余餐桌集合（数组）