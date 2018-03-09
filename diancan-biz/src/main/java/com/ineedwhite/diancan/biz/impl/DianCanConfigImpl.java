package com.ineedwhite.diancan.biz.impl;

import com.ineedwhite.diancan.biz.DianCanConfig;
import com.ineedwhite.diancan.dao.dao.BoardDao;
import com.ineedwhite.diancan.dao.dao.CouponDao;
import com.ineedwhite.diancan.dao.dao.FoodTypeDao;
import com.ineedwhite.diancan.dao.domain.BoardDo;
import com.ineedwhite.diancan.dao.domain.CouponDo;
import com.ineedwhite.diancan.dao.domain.FoodTypeDo;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author ruanxin
 * @create 2018-03-08
 * @desc
 */
@Service
public class DianCanConfigImpl implements DianCanConfig{
    private Logger logger = Logger.getLogger(DianCanConfigImpl.class);

    @Resource
    private BoardDao boardDao;

    @Resource
    private CouponDao couponDao;

    @Resource
    private FoodTypeDao foodTypeDao;

    private static List<BoardDo> boardDoList = new ArrayList<BoardDo>();

    private static List<CouponDo> couponDoList = new ArrayList<CouponDo>();

    private static List<FoodTypeDo> foodTypeDoList = new ArrayList<FoodTypeDo>();

    /**
     * 桌位缓存信息
     */
    private static Map<Integer, BoardDo> boardDoCache = new ConcurrentHashMap<Integer, BoardDo>();

    /**
     * 卡券缓存信息
     */
    private static Map<Integer, CouponDo> couponDoCache = new ConcurrentHashMap<Integer, CouponDo>();

    /**
     * 菜系缓存信息
     */
    private static Map<Integer, FoodTypeDo> foodTypeDoCache = new ConcurrentHashMap<Integer, FoodTypeDo>();

    private static ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    @PostConstruct
    private void init() {
        logger.info("load boardConfig information start!");
        refreshConfig();

        //定时刷新页面 5分钟一次
        executorService.scheduleAtFixedRate(new Runnable() {
            public void run() {
               refreshConfig();
            }
        },5, 5, TimeUnit.MINUTES);
    }

    public boolean refreshConfig() {
        try {
            logger.info("refresh config information start!");

            //桌位缓存
            boardDoList = boardDao.findAllBoardInfo();
            Map<Integer, BoardDo> newBoardMapCache = new ConcurrentHashMap<Integer, BoardDo>();
            for (BoardDo boardDo : boardDoList) {
                newBoardMapCache.put(boardDo.getBoard_id(), boardDo);
            }
            boardDoCache = newBoardMapCache;

            //卡券缓存
            couponDoList = couponDao.findAllCoupon();
            Map<Integer, CouponDo> newCouponMapCache = new ConcurrentHashMap<Integer, CouponDo>();
            for (CouponDo couponDo : couponDoList) {
                newCouponMapCache.put(couponDo.getCoupon_id(), couponDo);
            }
            couponDoCache = newCouponMapCache;

            //菜系缓存
            foodTypeDoList = foodTypeDao.findAllFoodType();
            Map<Integer, FoodTypeDo> newFoodTypeMapCache = new ConcurrentHashMap<Integer, FoodTypeDo>();
            for (FoodTypeDo foodTypeDo : foodTypeDoList) {
                newFoodTypeMapCache.put(foodTypeDo.getFoodtype_id(), foodTypeDo);
            }
            foodTypeDoCache = newFoodTypeMapCache;

        } catch (Exception ex) {
            logger.error("refreshGateConfig exception:" + ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    public Map<Integer, BoardDo> getAllBoard() {
        return boardDoCache;
    }

    public BoardDo getBoardById(Integer boardId) {
        return boardDoCache.get(boardId);
    }

    public Map<Integer, CouponDo> getAllCouponDo() {
        return couponDoCache;
    }

    public CouponDo getCouponById(Integer couponId) {
        return couponDoCache.get(couponId);
    }

    public Map<Integer, FoodTypeDo> getAllFoodType() {
        return foodTypeDoCache;
    }

    public FoodTypeDo getFoodTypeById(Integer foodTypeId) {
        return foodTypeDoCache.get(foodTypeId);
    }
}