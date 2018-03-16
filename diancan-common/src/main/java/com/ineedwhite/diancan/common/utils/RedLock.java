package com.ineedwhite.diancan.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;


/**
 * 分部式锁服务-提供悲观锁，以桌子号中心
 * 基于Redis实现
 *
 * Created by rx on 18/3/9.
 */
public class RedLock {
    private static final Logger log = Logger.getLogger(RedLock.class);
    public static final int RETRY_COUNT = 3;

    public static boolean lockDefaultTime(String boardId) {
        return lock(boardId, ExpireTimeConfig.SHORT_LOCK_SECONDS);
    }

    public static boolean lock(String boardId, int expireSeconds) {
        String lockKey = getLockKey(boardId);
        try {
            for (int i = 0; i < RETRY_COUNT; i++) {  // 3次重试
                // value 设置为到期时间
                String value = String.valueOf(System.currentTimeMillis() + expireSeconds * 1000L);
                boolean ret = RedisUtil.setnx(lockKey, value);
                if (ret == false) {
                    // 死锁检测 ------------------------------- begin
                    String lockKeyValue = RedisUtil.getStr(lockKey);
                    // Case1: 锁到期了，但没释放。
                    if (lockKeyValue != null) {
                        // Case1-STEP1: 比较现在V.S.锁的到期时间
                        long now = System.currentTimeMillis();
                        long oldLockTimeOut = Long.parseLong(lockKeyValue);
                        long newLockTimeOut = System.currentTimeMillis() + expireSeconds * 1000L;
                        // Case1-STEP2: 比较锁是否已经失效
                        if (now > oldLockTimeOut + 1*1000L) { // 1秒的delay factor
                            log.error("死锁检测-检测到锁已经失效!");
                            // Case1-STEP3: 获取上一个锁的到期时间，并设置现在的锁到期时间。
                            String oldTimeOutFromRedis = RedisUtil.getAndSet(lockKey, String.valueOf(newLockTimeOut));
                            if (StringUtils.equals(String.valueOf(oldLockTimeOut), oldTimeOutFromRedis)) {
                                // Case1-STEP4: 获取成功，设置失效时间.
                                RedisUtil.setKeyExpire(lockKey, expireSeconds);
                                log.info("获取锁成功-死锁检测生效");
                                return true;
                            }
                        }
                    }
                    else {
                        Thread.sleep(50);
                        continue;
                    }
                    // 死锁检测 ------------------------------- end

                    log.info("获取锁-失败");
                    return ret;
                }
                else {
//                    log.info("获取锁-成功");
                    RedisUtil.setKeyExpire(lockKey, expireSeconds);
//                    log.info("设置锁超时时间-成功");
                    return true;
                }
            }
        } catch(Exception ex) {
            log.error("异常-redis设置锁异常", ex);
            return false;
        }

        return false;
    }

    public static void unLock(String boardId) {
        String lockKey = getLockKey(boardId);
//        log.info("信息-释放锁-mercId={} mercOrderId={}", mercId, mercOrderId);
        try {
            RedisUtil.delete(lockKey);
//            log.info("信息-释放锁-成功");
        }
        catch (Exception ex) {
            log.error("异常-释放锁异常", ex);
        }
    }

    public static boolean isLocked(String boardId) throws Exception {
        String lockKey = getLockKey(boardId);
        String value = RedisUtil.getStr(lockKey);
        return !StringUtils.isEmpty(value);
    }

    private static String getLockKey(String boardId){
        if (StringUtils.isBlank(boardId)) {
            throw new IllegalArgumentException("boardId is not allow to be null");
        }
        return String.format("lock_%s", boardId);
    }

    public static class ExpireTimeConfig {
        // 默认锁定时间
        public static final int DEFAULT_LOCK_SECONDS  = 180;

        public static final int SHORT_LOCK_SECONDS = 5;
    }

}
