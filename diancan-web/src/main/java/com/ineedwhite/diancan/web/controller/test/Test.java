package com.ineedwhite.diancan.web.controller.test;

import com.ineedwhite.diancan.biz.*;
import com.ineedwhite.diancan.biz.utils.OrderUtils;
import com.ineedwhite.diancan.common.utils.DateUtil;
import com.ineedwhite.diancan.common.utils.RedisUtil;
import com.ineedwhite.diancan.dao.dao.FoodDao;
import com.ineedwhite.diancan.dao.dao.OrderDao;
import com.ineedwhite.diancan.dao.dao.TestDao;
import com.ineedwhite.diancan.dao.domain.FoodDo;
import com.ineedwhite.diancan.dao.domain.OrderDo;
import com.ineedwhite.diancan.dao.domain.TestDo;
import org.junit.runner.RunWith;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import java.util.*;

/**
 * @author ruanxin
 * @create 2018-03-05
 * @desc
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context.xml"})
public class Test {
    @Autowired
    private TestDao testDao;

    @Autowired
    private UserService user;

    @Autowired
    private BoardService board;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private FoodTypeService foodType;

    @Autowired
    private FoodService food;

    @Autowired
    private DianCanConfigService dianCanConfig;

    @Autowired
    private OrderService orderService;

    @org.junit.Test
    public void testMysql() {
        List<TestDo> list =  testDao.queryAll();
        for (TestDo testDo : list) {
            System.out.println(testDo.getTest_author());
        }
    }

    /**
     * 注册功能
     */
    @org.junit.Test
    public void register() {
        Map<String, String> paraMap = new HashMap<String, String>();
        paraMap.put("user_password", "15884812382");
        paraMap.put("user_name", "ruanxin");
        paraMap.put("user_phone", "15884812382");
        Map<String, String> map = user.register(paraMap);
        System.out.println(map);
    }

    /**
     * 登陆
     */
    @org.junit.Test
    public void login() {
        Map<String, String> paraMap = new HashMap<String, String>();
        paraMap.put("user_phone", "15884812382");
        paraMap.put("user_password", "15884812382");
        Map<String, String> map = user.login(paraMap);
        System.out.println(map);
    }

    /**
     * 获取可用桌位号
     */
    @org.junit.Test
    public void getAvailBoard() {
        Map<String, String> paraMap = new HashMap<String, String>();
        paraMap.put("order_board_date", "20180306");
        paraMap.put("order_board_time_interval", "0");
        paraMap.put("board_type", "1");
        paraMap.put("order_people_number", "2");
        Map<String, String> map = board.getAvailableBoard(paraMap);
        System.out.println(map);
    }


    @org.junit.Test
    public void orderInit() {
        String orderId = UUID.randomUUID().toString().replace("-", "");
        OrderDo orderDo = new OrderDo();
        orderDo.setOrder_id(orderId);
        orderDo.setBoard_id(3);
        orderDo.setOrder_people_number(2);

        String time = DateUtil.getCurrDateStr(DateUtil.DEFAULT_PAY_FORMAT);
        orderDo.setOrder_date(time);
        orderDao.insertOrderInfo(orderDo);
    }

    @org.junit.Test
    public void getFoodType(){
        Map<String, String> foodMap = foodType.getAllFoodType();
        System.out.println(foodMap);
    }

    @org.junit.Test
    public void reserveBoard() throws Exception {
        Map<String, String> paraMap = new HashMap<String, String>();
        paraMap.put("user_id", "3dc9214c9c0b4e569b646cdf514b746d");
        paraMap.put("order_board_date", "20180310");
        paraMap.put("order_board_time_interval", "0");
        paraMap.put("board_id", "8");
        Map<String, String> map = board.reserveBoard(paraMap);
        System.out.println(map);
    }

    @org.junit.Test
    public void getFoodByType() throws Exception {
        Map<String, String> paraMap = new HashMap<String, String>();
        paraMap.put("food_type_id","1");
        paraMap.put("food_page", "3");
        Map<String, String> map = food.getFoodByType(paraMap);
        System.out.println(map);
    }

    @org.junit.Test
    public void getUsrByUsrId() {
        Map<String, String> paraMap = new HashMap<String, String>();
        paraMap.put("user_id", "a5a2583d6d6a4c96ba8de0827abc1d87");
        Map<String, String> map = user.userInfo(paraMap);
        System.out.println(map);
    }

    @org.junit.Test
    public void addFoodToShoppingCart() {
        try {
            Map<String, String> paraMap = new HashMap<String, String>();
            paraMap.put("order_id", "ecd4d560e134415eba5f35f926ca99b7");
            paraMap.put("food_id", "2");
            paraMap.put("food_num", "2");
            orderService.addFoodToShoppingCart(paraMap);
            System.out.println(OrderUtils.getCacheOrder("ecd4d560e134415eba5f35f926ca99b7"));
            List<String> listFoodId = OrderUtils.getFoodIdList("ecd4d560e134415eba5f35f926ca99b7");
            List<String> listFoodNum = OrderUtils.getFoodNumList("ecd4d560e134415eba5f35f926ca99b7");
            System.out.println(listFoodId.get(0) + ":" + listFoodNum.get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
