### 环境信息
redis:192.168.1.199:6379    
redis-cli -h 192.168.1.199 -p 6379

前台 http server:192.168.1.196:9090  
curl -d "" "192.168.1.196:9090/test/test"

mysql server:192.168.1.160:3306

后台 http server:192.168.1.168:8080   
curl -d "" "192.168.1.168:8080/test/test"

zk:192.168.1.199:2181   
./zkCli.sh -server 192.168.1.199:2181
### 错误码
    DC00000("DC00000", "成功"),
        
    DC00001("DC00001", "缺少必要参数")
        
    DC00002("DC00002", "操作数据库失败")
        
    DC00003("DC00003", "系统错误")
        
    DC00004("DC00004", "用户登陆密码错误")
        
    DC00005("DC00005", "该手机号已注册")
        
    DC00006("DC00006", "桌位号不存在")
        
    DC00007("DC00007", "该手机号未注册")
        
    DC00008("DC00008", "系统繁忙，请稍后再试")
        
    DC00009("DC00009", "该桌位已被预定，请重新选择桌位")
        
    DC00010("DC00010", "该用户不存在")
        
    DC00011("DC00011", "已是最后一页")
        
    DC00012("DC00012", "亲，菜品最多只能点100道哟!"),
        
    DC00013("DC00013", "该订单已过期，请重试")
        
    DC00014("DC00014", "该用户没有可用的优惠券"),
        
    DC00015("DC00015", "该订单无效"),
        
    DC00016("DC00016", "卡券不存在"),
        
    DC00017("DC00017", "卡券已过期"),
        
    DC00018("DC00018", "金额未达到，不能使用优惠券"),
        
    DC00019("DC00019", "亲，您还没有点菜哟"),
        
    DC00020("DC00020", "支付失败,账户余额补足，请充值"),
        
    DC00021("DC00021", "手机号不正确，请重新试"),
        
    DC00022("DC00022", "该订单已支付成功，请重新下单"),
        
    DC00023("DC00023", "该订单不存在，请重新下单"),
        
    DC00024("DC00024", "您的账户最多只能存放100万")
    
    DC00025("DC00025", "亲，您还有订单未支付，请及时支付")
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
    
    order_id              订单id
    
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
    
### 4.获取全部菜系信息
##### URL：http://192.168.1.196:9090//outerApi/getAllFoodType
##### 入参:

##### 出参:

    字段名                 描述
            
    foodType              菜品信息

### 5.订桌
##### URL: http://192.168.1.196:9090//outerApi/reserveBoard
##### 入参：
     字段名                      描述          是否可空
     
     order_board_date           预定时间          否   
     
     order_board_time_interval  预定时段          否
                               （0.上午 1.中午.2）    
                                
     board_id                   餐桌号            否
     
     user_id                    用户号            否

##### 出参:
     字段名                 描述
      vip                         是否能成为VIP
                                          (0:不能 1:能)
                                
      accumulate_points           此次消费获取的积分
                      
      order_paid                  此次实际支付金额           
     
### 6.根据菜系获取菜品
##### URL: http://192.168.1.196:9090//outerApi/getFoodByType
##### 入参：
     字段名                      描述          是否可空
     
     order_id                   订单号           否
     
     food_type_id               菜品类型          否
        
     food_page                  分页             否

##### 出参:
     字段名                 描述
                 
     food_all              该菜系的所有菜品
        
     food_page_num         菜单页数

     food_num              菜品数量
### 7.根据用户号获取用户信息
##### URL: http://192.168.1.196:9090//outerApi/getUsrInfo
##### 入参：
     字段名                      描述          是否可空
     
     user_id                    用户号       否

##### 出参:
     字段名                 描述   
        
     user_name             姓名
        
     balance               余额
        
     user_phone            电话    
        
     accumulate_points     积分
        
     member_level          用户等级
        
     user_id               用户id
     
     coupon_num           优惠券数量
     
     user_card_no          用户卡号
     
### 8.向购物车添加菜品
##### URL: http://192.168.1.196:9090//outerApi/addFood
##### 入参：
     字段名                      描述          是否可空
     
     order_id                   点单号           否
     
     food_id                    菜品号           否
        
     food_num                   菜品数量         否

##### 出参:
     字段名                 描述


### 9.展示购物车
##### URL: http://192.168.1.196:9090//outerApi/shoppingCart
##### 入参：
     字段名                      描述          是否可空
     
     order_id                   点单号           否
     
     user_id                    用户号           否

##### 出参:
     字段名                 描述   
                
    pay_food_all           购物车中的所有菜品信息
        
    food_sum_money         总计
        
    vip_food_money         vip优惠价
        
    total_food_money       结账
        
    is_vip_user            是否是VIP用户
    
### 10.获取优惠券列表
##### URL: http://192.168.1.196:9090//outerApi/getCouponList
##### 入参：
     字段名                      描述          是否可空
     
     order_id                   订单号           否
        
     user_id                    用户号           否   
##### 出参:
     字段名                 描述   
                
    coupon_List            优惠券列表
    
### 11.使用优惠券
##### URL: http://192.168.1.196:9090//outerApi/useCoupon
##### 入参：
     字段名                      描述          是否可空
     
     order_id                   订单号           否
        
     coupon_id                  优惠券号           否   
##### 出参:
     字段名                 描述   
                
    coupon_Amt            使用优惠券后的金额
    
### 12.在购物车中修改菜品数量
##### URL: http://192.168.1.196:9090//outerApi/shoppingCartAddMinus
##### 入参：
     字段名                      描述          是否可空
     
     food_id                   被修改菜品的ID     否
        
     food_num                  被修改菜品的数量    否
     
     order_id                  订单号            否
     
     coupon_id                 使用的卡券号       是（若为空不用卡券支付）   
##### 出参:
         字段名                        描述   
                    
        mod_food_single_sum         被修改菜品的小计
            
        food_sum_money              总计
        
        vip_food_money              VIP优惠价
        
        total_food_money            结账
        
### 13.结账
##### URL: http://192.168.1.196:9090//outerApi/checkOut
##### 入参：
     字段名                      描述          是否可空
     
     order_id                  订单号            否
     
     coupon_id                 使用的卡券号       是（若为空不用卡券支付）   
##### 出参:
     字段名                        描述   
                 
     
### 14.获取用户的优惠券列表
##### URL: http://192.168.1.196:9090//outerApi/getUserCoupon
##### 入参：
     字段名                      描述          是否可空
     
     user_id                    用户号            否
##### 出参:
     字段名                        描述   
                    
     coupon_num                  用户卡券数量
                          
     coupon_list                用户卡券列表信息
     
### 15.获取用户详细信息
##### URL: http://192.168.1.196:9090//outerApi/getUserDetailInfo
##### 入参：
     字段名                      描述          是否可空
     
     user_id                    用户号            否
##### 出参:
     字段名                        描述   
                    
     user_name                  用户姓名
                          
     user_birth                 用户生日
     
     user_gender                用户性别
     
     phone                      用户联系方式
     
### 16.修改用户详细信息
##### URL: http://192.168.1.196:9090//outerApi/modifiedUserInfo
##### 入参：
     字段名                      描述          是否可空
     
     user_id                    用户号           否
     
     user_name                  用户姓名         否
                              
     user_birth                 用户生日         否
         
     user_gender                用户性别         否
         
     user_phone                 用户联系方式      否
##### 出参:
     字段名                        描述   
     
### 17.充值业务
##### URL: http://192.168.1.196:9090//outerApi/recharge
##### 入参：
     字段名                      描述          是否可空
     
     user_id                    用户号           否
     
     recharge_amt               充值金额         否
##### 出参:
     字段名                        描述   
                    
    user_balance                 充值后的用户余额
        
    vip                         非VIP账户成为VIP时返回1，其余情况返回0
        
    get_accumulate_points       本次充值获得的积分
    
### 18.历史账单
##### URL: http://192.168.1.196:9090//outerApi/historyOrder
##### 入参：
     字段名                      描述          是否可空
     
     user_id                    用户号           否
    
##### 出参:
     字段名                        描述   
                    
    comsu_num                  一年内的消费次数
        
    sum_amt                    一年内的消费金额总和
        
    order_all                  一年内的消费账单信息
    
### 19.充值页面加载
##### URL: http://192.168.1.196:9090//outerApi/rechargePageLoading
##### 入参：
     字段名                      描述          是否可空
     
     user_id                    用户号           否
    
##### 出参:
     字段名                        描述   
                    
    balance                    用户当前余额
    
### 20.用户是否有未完成的订单
##### URL: http://192.168.1.196:9090//outerApi/orderWithoutFinish
##### 入参：
     字段名                      描述          是否可空
     
     user_id                    用户号           否
    
##### 出参:
     字段名                        描述   
                    
    order_id                    订单号
        
    order_status(UK,UM)         订单状态
    
    （若这两个字段都没有就表示，用户完成了所有订单的支付
    若 状态UK,则redirect选菜页面,
    若 状态UM,则redirect购物车页面）
    
### 21.订单信息
##### URL: http://192.168.1.196:9090//outerApi/orderInfo
##### 入参：
     字段名                      描述          是否可空
     
     user_id                   订单号           否
    
##### 出参:
     字段名                        描述   
                    
    order_id                    订单号
        
    order_paid                  实付金额
        
    order_total_money           总计
        
    order_discount              优惠
        
    order_people_num            用餐人数
        
    order_date                  用餐日期
        
    order_board_id              桌号
        
    food_info                   菜品信息
    
### 22.联系信息
##### URL: http://192.168.1.196:9090//outerApi/ContactInfo
##### 入参：
     字段名                      描述          是否可空
         
##### 出参:
     字段名                        描述   
                    
    contact_phone                联系电话
        
    contact_email                联系邮箱
        
    contact_address              联系地址
        
    contact_workTime             工作时间
        
### 23.加载桌位信息
##### URL: http://192.168.1.196:9090//outerApi/loadBoardPage
##### 入参：
     字段名                      描述          是否可空
       
     order_id                  订单号            否
##### 出参:
     字段名                        描述   
                    
    board_type                   桌位类型
        
    order_board_date             订桌的日期
        
    order_people_num             订桌的人数
        
    order_time_intervel          订桌时间段
    
    board_id                     桌位号   
    
### 24.注销用户
##### URL: http://192.168.1.196:9090//outerApi/cancellation
##### 入参：
     字段名                      描述          是否可空
       
     order_id                  订单号            否
     
     user_id                   用户号            否
##### 出参:
     字段名                        描述   

