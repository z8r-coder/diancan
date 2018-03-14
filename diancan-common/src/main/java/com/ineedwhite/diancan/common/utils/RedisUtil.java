package com.ineedwhite.diancan.common.utils;


import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.*;

import java.util.*;

/**
 * Created by rx
 */

public class RedisUtil {
    private static final Logger log = Logger.getLogger(RedisUtil.class);
    private static String REDIS_KEY = "DC_RX_";
    private static JedisPool jedisPool = null;
    private static Transaction transaction = null;

    /**
     * 初始化Redis连接池
     */
    private static void initialPool(){
        try {
            JedisPoolConfig config = new JedisPoolConfig();

            config.setTestOnBorrow(PropertiesUtil.getValue("cache.r.testonborrow").toBoolean());
            config.setMaxIdle(PropertiesUtil.getValue("cache.r.maxidle").toInt());
            config.setMinIdle(PropertiesUtil.getValue("cache.r.minidle").toInt());
            config.setSoftMinEvictableIdleTimeMillis(PropertiesUtil.getValue("cache.r.softMinEvictableIdleTime").toInt());
            config.setMaxTotal(PropertiesUtil.getValue("cache.r.maxtotal").toInt());
            config.setMaxWaitMillis(PropertiesUtil.getValue("cache.r.maxwaitmillis").toInt());

            String ip = PropertiesUtil.getStringValue("redis.ip");
            int port = PropertiesUtil.getValue("redis.port").toInt();
            jedisPool = new JedisPool(config, ip, port);
        } catch (Exception e) {
            log.error("First create JedisPool error :",e);
        }
    }
    /**
     * 在多线程环境同步初始化
     */
    private static synchronized void poolInit() {
        if (jedisPool == null) {
            initialPool();
        }
    }
    /**
     * 获取Jedis实例
     * @return Jedis
     */
    private static Jedis createJedis() {
        if (jedisPool == null) {
            poolInit();
        }
        Jedis jedis = null;
        try {
            if (jedisPool != null) {
                jedis = jedisPool.getResource();
            }
        } catch (Exception e) {
            log.error("Get jedis error : ",e);
        }
        return jedis;
    }
    /**
     * 释放jedis资源
     * @param jedis
     */
    public static void close(final Jedis jedis) {
        if (jedis != null && jedisPool !=null) {
            jedisPool.returnResource(jedis);//jedis3.0以后为jedis.close()
        }
    }


//
//    public static Jedis createJedis() {
//        Jedis jedis = null;
//        String ip = PropertiesUtil.getStringValue("redis.ip");
//        int port = PropertiesUtil.getValue("redis.port").toInt();
//        try{
//            jedis = new Jedis(ip,port);
//        }catch (Exception e){
//            log.error("实例化jedis异常",e);
//        }
//        return jedis;
//    }

//    private static void close(Jedis jedis) {
//        if (jedis != null) {
//            jedis.close();
//        }
//    }

    public static String makeKey(String key) {
        return REDIS_KEY + key;
    }

    public static void set(String key, String value) throws Exception {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value))
            return;
        Jedis jedis = createJedis();
        try {
            String redisKey = makeKey(key);
            jedis.set(redisKey, value);
        } catch (Exception ex) {
            log.warn("occurs exception", ex);
            throw ex;
        } finally {
            close(jedis);
        }
    }

    public static String getAndSet(String key, String value) throws Exception {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value))
            return "";
        Jedis jedis = createJedis();
        try {
            String redisKey = makeKey(key);
            return jedis.getSet(redisKey, value);
        } catch (Exception ex) {
            log.warn("occurs exception", ex);
            throw ex;
        } finally {
            close(jedis);
        }
    }


    /**
     * 由Redis保证的CAS操作
     */
    public static boolean compareAndSet(String lockKey, String stringKey, String oldValue, String newValue) throws Exception {
        if (StringUtils.isEmpty(lockKey) || StringUtils.isEmpty(stringKey)) {
            return false;
        }
        boolean ret = false;
        long lock = plusOne(lockKey);
        if (1 == lock) {
            String oldValueInRedis = getStr(stringKey);
            if ((null == oldValueInRedis && null == oldValue) || (null != oldValue && oldValue.equals(oldValueInRedis))) {
                set(stringKey, newValue);
                ret = true;
            }
        }
        minusOne(lockKey);
        return ret;
    }


    /**
     * 带过期时间的保存
     *
     * @param expire 过期时间单位秒
     */
    public static void setWithExpire(String key, String value, int expire) throws Exception {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value))
            return;
        Jedis jedis = createJedis();
        try {
            String redisKey = makeKey(key);
            jedis.setex(redisKey, expire, value);
        } catch (Exception ex) {
            log.warn("occurs exception", ex);
            throw ex;
        } finally {
            close(jedis);
        }
    }

    /**
     * 带过期时间的保存
     *
     * @param expire 过期时间单位秒
     */
    public static void setKeyExpire(String key, int expire) throws Exception {
        if (StringUtils.isEmpty(key))
            return;

        Jedis jedis = createJedis();
        try {
            String redisKey = makeKey(key);
            jedis.expire(redisKey, expire);
        } catch (Exception ex) {
            log.warn("occurs exception", ex);
            throw ex;
        } finally {
            close(jedis);
        }
    }

    /**
     * set not exist
     */
    public static boolean setnx(String key, String value) throws Exception {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value))
            return false;
        Jedis jedis = createJedis();
        try {
            String redisKey = makeKey(key);
            long result = jedis.setnx(redisKey, value);
            boolean ret = result == 1;
            return ret;
        } catch (Exception ex) {
            log.warn("occurs exception", ex);
            throw ex;
        } finally {
            close(jedis);
        }
    }

    /**
     * 带过期时间的保存
     *
     * @param expireAt 过期时间的时间戳,注意精确到秒！！！
     */
    public static void setKeyExpireAt(String key, long expireAt) throws Exception {
        if (StringUtils.isEmpty(key))
            return;
        Jedis jedis = createJedis();
        try {
            String redisKey = makeKey(key);
            jedis.expireAt(redisKey, expireAt);
        } catch (Exception ex) {
            log.warn("occurs exception", ex);
            throw ex;
        } finally {
            close(jedis);
        }
    }

    /**
     * 带过期时间的保存
     *
     * @param milliseconds 过期时间单位毫秒
     */
    public static void pSetWithExpire(String key, String value, int milliseconds) throws Exception {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value))
            return;
        Jedis jedis = createJedis();
        try {
            String redisKey = makeKey(key);
            jedis.psetex(redisKey, milliseconds, value);
        } catch (Exception ex) {
            log.warn("occurs exception", ex);
            throw ex;
        } finally {
            close(jedis);
        }
    }


    /**
     * 由Redis保证的原子加操作
     */
    public static long plus(String key, long value) throws Exception {
        Preconditions.checkArgument(StringUtils.isNotBlank(key), "key can not be null");

        if (value < 0) {
            return minus(key, Math.abs(value));
        }

        Jedis jedis = createJedis();
        try {
            String redisKey = makeKey(key);
            return jedis.incrBy(redisKey, value);
        } catch (Exception ex) {
            log.warn("occurs exception", ex);
            throw ex;
        } finally {
            close(jedis);
        }

    }

    public static long plusOne(String key) throws Exception {
        return plus(key, 1);
    }

    /**
     * 由Redis保证的原子减操作
     */
    public static long minus(String key, long value) throws Exception {
        Preconditions.checkArgument(StringUtils.isNotBlank(key), "key can not be null");

        if (value < 0) {
            return plus(key, Math.abs(value));
        }

        Jedis jedis = createJedis();
        try {
            String redisKey = makeKey(key);
            return jedis.decrBy(redisKey, value);
        } catch (Exception ex) {
            log.warn("occurs exception", ex);
            throw ex;
        } finally {
            close(jedis);
        }
    }

    public static long minusOne(String key) throws Exception {
        return minus(key, 1);
    }

    public static String getStr(String key) throws Exception {
        if (StringUtils.isEmpty(key))
            return null;

        Jedis jedis = createJedis();
        try {
            String redisKey = makeKey(key);
            return jedis.get(redisKey);
        } catch (Exception ex) {
            log.warn("occurs exception", ex);
            throw ex;
        } finally {
            close(jedis);
        }
    }

    /**
     * 从写redis库中获取值
     *
     * @param key redis key
     * @return redis value
     */
    public static String getStrFromMaster(String key) throws Exception {
        if (StringUtils.isEmpty(key))
            return null;

        Jedis jedis = createJedis();
        try {
            String redisKey = makeKey(key);
            return jedis.get(redisKey);
        } catch (Exception ex) {
            log.warn("occurs exception", ex);
            throw ex;
        } finally {
            close(jedis);
        }
    }

    /**
     * 获取过期时间
     *
     * @param key key
     * @return 过期时间 单位 毫秒
     */
    public static Long getExpireWithMillsFormat(String key) throws Exception {
        if (StringUtils.isEmpty(key))
            return 0l;

        Jedis jedis = createJedis();
        try {
            String redisKey = makeKey(key);
            return jedis.pttl(redisKey);
        } catch (Exception ex) {
            log.warn("occurs exception", ex);
            throw ex;
        } finally {
            close(jedis);
        }
    }

    /**
     * 获取过期时间
     *
     * @param key key
     * @return 过期时间 单位 秒
     */
    public static Long getExpireWithSecondFormat(String key) throws Exception {
        if (StringUtils.isEmpty(key))
            return 0l;
        Jedis jedis = createJedis();
        try {
            String redisKey = makeKey(key);
            return jedis.ttl(redisKey);
        } catch (Exception ex) {
            log.warn("occurs exception", ex);
            throw ex;
        } finally {
            close(jedis);
        }
    }


    public static boolean isExistInSet(String key, String value) throws Exception {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(value)) {
            return false;
        }

        Jedis jedis = createJedis();
        try {
            String redisKey = makeKey(key);
            return jedis.sismember(redisKey, value);
        } catch (Exception ex) {
            log.warn("occurs exception", ex);
            throw ex;
        } finally {
            close(jedis);
        }
    }

    public static void addIntoSet(String key, String value) throws Exception {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value))
            return;

        Jedis jedis = createJedis();
        try {
            String redisKey = makeKey(key);
            jedis.sadd(redisKey, value);
        } catch (Exception ex) {
            log.warn("occurs exception", ex);
            throw ex;
        } finally {
            close(jedis);
        }
    }

    public static void addIntoSet(String key, String[] values) throws Exception {
        if (StringUtils.isEmpty(key) || values == null || values.length == 0)
            return;

        Jedis jedis = createJedis();
        try {
            String redisKey = makeKey(key);
            jedis.sadd(redisKey, values);
        } catch (Exception ex) {
            log.warn("occurs exception", ex);
            throw ex;
        } finally {
            close(jedis);
        }
    }

    public static void addIntoSet(String key, Set<String> valueSet) throws Exception {
        if (StringUtils.isEmpty(key) || CollectionUtils.isEmpty(valueSet))
            return;

        String[] values = valueSet.toArray(new String[valueSet.size()]);
        addIntoSet(key, values);
    }


    public static Set<String> getSet(String key) throws Exception {
        if (StringUtils.isEmpty(key))
            return null;
        Jedis jedis = createJedis();
        try {
            String redisKey = makeKey(key);
            return jedis.smembers(redisKey);
        } catch (Exception ex) {
            log.warn("occurs exception", ex);
            throw ex;
        } finally {
            close(jedis);
        }
    }

    public static void deleteFromSet(String key, String value) throws Exception {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value))
            return;

        Jedis jedis = createJedis();
        try {
            String redisKey = makeKey(key);
            jedis.srem(redisKey, value);
        } catch (Exception ex) {
            log.warn("occurs exception", ex);
            throw ex;
        } finally {
            close(jedis);
        }
    }

    public static void deleteFromList(String key, String value) throws Exception {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value))
            return;

        Jedis jedis = createJedis();
        try {
            String redisKey = makeKey(key);
            jedis.lrem(redisKey, 1, value);
        } catch (Exception ex) {
            log.warn("occurs exception", ex);
            throw ex;
        } finally {
            close(jedis);
        }
    }

    public static void deleteFromSet(String key, String[] values) throws Exception {
        if (StringUtils.isEmpty(key) || values == null || values.length == 0)
            return;

        Jedis jedis = createJedis();
        try {
            String redisKey = makeKey(key);
            jedis.srem(redisKey, values);
        } catch (Exception ex) {
            log.warn("occurs exception", ex);
            throw ex;
        } finally {
            close(jedis);
        }
    }

    public static void deleteFromSet(String key, Set<String> valueSet) throws Exception {
        if (StringUtils.isEmpty(key) || CollectionUtils.isEmpty(valueSet))
            return;

        String[] values = valueSet.toArray(new String[valueSet.size()]);
        deleteFromSet(key, values);
    }

    /**
     * 在列表的右边添加元素
     */
    public static void addIntoList(String key, String value) throws Exception {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value))
            return;

        Jedis jedis = createJedis();
        try {
            String redisKey = makeKey(key);
            jedis.rpush(redisKey, value);
        } catch (Exception ex) {
            log.warn("occurs exception", ex);
            throw ex;
        } finally {
            close(jedis);
        }
    }

    public static void addIntoList(String key, String[] values) throws Exception {
        if (StringUtils.isEmpty(key) || values == null || values.length == 0)
            return;

        Jedis jedis = createJedis();
        try {
            String redisKey = makeKey(key);
            jedis.rpush(redisKey, values);
        } catch (Exception ex) {
            log.warn("occurs exception", ex);
            throw ex;
        } finally {
            close(jedis);
        }
    }

    public static void addIntoList(String key, List<String> valueList) throws Exception {
        if (StringUtils.isEmpty(key) || CollectionUtils.isEmpty(valueList))
            return;

        String[] values = valueList.toArray(new String[valueList.size()]);
        addIntoList(key, values);
    }


    public static List<String> getAllList(String key) throws Exception {
        if (StringUtils.isEmpty(key))
            return Collections.emptyList();

        return getList(key, -1);
    }

    public static List<String> getList(String key, int count) throws Exception {
        if (StringUtils.isEmpty(key) || count == 0)
            return Collections.emptyList();

        count = count == -1 ? -1 : count - 1;
        Jedis jedis = createJedis();
        try {
            String redisKey = makeKey(key);
            return jedis.lrange(redisKey, 0, count);
        } catch (Exception ex) {
            log.warn("occurs exception", ex);
            throw ex;
        } finally {
            close(jedis);
        }
    }

    public static Long getListLength(String key) throws Exception {
        if (StringUtils.isEmpty(key)) {
            return Long.valueOf(0);
        }
        Jedis jedis = createJedis();
        try {
            String redisKey = makeKey(key);
            return jedis.llen(redisKey);
        } catch (Exception ex) {
            log.warn("occurs exception", ex);
            throw ex;
        } finally {
            close(jedis);
        }
    }

    public static void addIntoHashMap(String key, Map<String, String> valueMap) throws Exception {
        if (StringUtils.isEmpty(key) || CollectionUtils.isEmpty(valueMap))
            return;

        Jedis jedis = createJedis();
        try {
            String redisKey = makeKey(key);
            jedis.hmset(redisKey, valueMap);
        } catch (Exception ex) {
            log.warn("occurs exception", ex);
            throw ex;
        } finally {
            close(jedis);
        }
    }

    public static long plusInHashMap(String key, String fieldKey, int interval) throws Exception {
        if (StringUtils.isEmpty(key) || StringUtils.isBlank(fieldKey) || interval == 0) {
            return 0L;
        }
        Jedis jedis = createJedis();
        try {
            String redisKey = makeKey(key);
            return jedis.hincrBy(redisKey, fieldKey, interval);
        } catch (Exception ex) {
            log.warn("occurs exception", ex);
            throw ex;
        } finally {
            close(jedis);
        }
    }

    public static long plusOneInHashMap(String key, String fieldKey) throws Exception {
        return plusInHashMap(key, fieldKey, 1);
    }

    public static void addIntoHashMap(String key, String fieldKey, String fieldValue) throws Exception {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(fieldKey) || StringUtils.isEmpty(fieldValue))
            return;

        Jedis jedis = createJedis();
        try {
            String redisKey = makeKey(key);
            jedis.hset(redisKey, fieldKey, fieldValue);
        } catch (Exception ex) {
            log.warn("occurs exception", ex);
            throw ex;
        } finally {
            close(jedis);
        }
    }

    public static Map<String, String> getMap(String key) throws Exception {
        if (StringUtils.isEmpty(key))
            return Collections.emptyMap();

        Jedis jedis = createJedis();
        try {
            String redisKey = makeKey(key);
            return jedis.hgetAll(redisKey);
        } catch (Exception ex) {
            log.warn("occurs exception", ex);
            throw ex;
        } finally {
            close(jedis);
        }
    }

    public static boolean isExistsInMap(String key, String fieldKey) throws Exception {
        if (StringUtils.isEmpty(key))
            return false;

        Jedis jedis = createJedis();
        try {
            String redisKey = makeKey(key);
            return jedis.hexists(redisKey, fieldKey);
        } catch (Exception ex) {
            log.warn("occurs exception", ex);
            throw ex;
        } finally {
            close(jedis);
        }
    }

    public static String getValueFromMap(String key, String fieldKey) throws Exception {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(fieldKey))
            return null;

        Jedis jedis = createJedis();
        try {
            String redisKey = makeKey(key);
            return jedis.hget(redisKey, fieldKey);
        } catch (Exception ex) {
            log.warn("occurs exception", ex);
            throw ex;
        } finally {
            close(jedis);
        }
    }

    public static void addIntoSortedSet(String key, long weight, String value) throws Exception {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            return;
        }

        Jedis jedis = createJedis();
        try {
            String redisKey = makeKey(key);
            jedis.zadd(redisKey, weight, value);
        } catch (Exception ex) {
            log.warn("occurs exception", ex);
            throw ex;
        } finally {
            close(jedis);
        }
    }

    public static Set<String> getAllSortedSet(String key) throws Exception {
        return getSortedSet(key, 0, -1);
    }

    public static Set<String> getReversedAllSortedSet(String key) throws Exception {
        return getReversedSortedSet(key, 0, -1);
    }

    public static Set<String> getSortedSet(String key, int count) throws Exception {
        return getSortedSet(key, 0, count);
    }

    public static Set<String> getReversedSortedSet(String key, int count) throws Exception {
        return getReversedSortedSet(key, 0, count);
    }

    public static Set<String> getSortedSet(String key, int start, int end) throws Exception {
        if (StringUtils.isEmpty(key)) {
            return Collections.emptySet();
        }

        Jedis jedis = createJedis();
        try {
            String redisKey = makeKey(key);
            return jedis.zrange(redisKey, start, end);
        } catch (Exception ex) {
            log.warn("occurs exception", ex);
            throw ex;
        } finally {
            close(jedis);
        }
    }

    public static Set<String> getReversedSortedSet(String key, int start, int end) throws Exception {
        if (StringUtils.isEmpty(key)) {
            return Collections.emptySet();
        }

        Jedis jedis = createJedis();
        try {
            String redisKey = makeKey(key);
            return jedis.zrevrange(redisKey, start, end);
        } catch (Exception ex) {
            log.warn("occurs exception", ex);
            throw ex;
        } finally {
            close(jedis);
        }
    }

    public static Long getSetScard(String key) throws Exception {
        if (StringUtils.isEmpty(key)) {
            return 0l;
        }

        Jedis jedis = createJedis();
        try {
            String redisKey = makeKey(key);
            return jedis.scard(redisKey);
        } catch (Exception ex) {
            log.warn("occurs exception", ex);
            throw ex;
        } finally {
            close(jedis);
        }
    }

    public static void deleteFromHashMap(String key, String fieldKey) throws Exception {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(fieldKey))
            return;

        Jedis jedis = createJedis();
        try {
            String redisKey = makeKey(key);
            jedis.hdel(redisKey, fieldKey);
        } catch (Exception ex) {
            log.warn("occurs exception", ex);
            throw ex;
        } finally {
            close(jedis);
        }
    }


    public static void batchDeleteFromHashMap(String key, List<String> fieldKeys) throws Exception {
        if (StringUtils.isEmpty(key) || CollectionUtils.isEmpty(fieldKeys))
            return;

        Jedis jedis = createJedis();
        try {
            String redisKey = makeKey(key);
            jedis.hdel(redisKey, (String[]) fieldKeys.toArray());
        } catch (Exception ex) {
            log.warn("occurs exception", ex);
            throw ex;
        } finally {
            close(jedis);
        }
    }

    public static void delete(String key) throws Exception {
        if (StringUtils.isEmpty(key))
            return;

        Jedis jedis = createJedis();
        try {
            String redisKey = makeKey(key);
            jedis.del(redisKey);
        } catch (Exception ex) {
            log.warn("occurs exception", ex);
            throw ex;
        } finally {
            close(jedis);
        }
    }


    public static Set<String> mget(List<String> keyList) {
        if (CollectionUtils.isEmpty(keyList))
            return null;
        Jedis jedis = createJedis();
        try {
            String[] keys = new String[keyList.size()];
            int i = 0;
            for (String key : keyList) {
                String redisKey = makeKey(key);
                keys[i] = redisKey;
                i++;
            }
            List<String> resultList = jedis.mget(keys);//该方法只能返回key数组对应的value的值组成的数组。
            Set<String> resultSet = new HashSet<String>(resultList.size());
            resultSet.addAll(resultList);
            if (resultSet.size() == 1) {
                Iterator<String> iterator = resultSet.iterator();
                if (iterator.next() == null) {
                    return new HashSet<String>();
                }
            }
            return resultSet;
        } catch (Exception ex) {
            log.warn("occurs exception", ex);
            return null;
        } finally {
            close(jedis);
        }
    }

    public static void mset(Map<String, String> keyValueMap) throws Exception {
        if (keyValueMap == null || keyValueMap.size() == 0) {
            return;
        }

        Jedis jedis = createJedis();
        try {
            String[] keyValueArray = new String[keyValueMap.size() * 2];
            int i = 0;
            for (String key : keyValueMap.keySet()) {
                String redisKey = makeKey(key);
                keyValueArray[i] = redisKey;
                i++;
                keyValueArray[i] = keyValueMap.get(key);
                i++;
            }
            jedis.mset(keyValueArray);
        } catch (Exception ex) {
            log.warn("occurs exception", ex);
            throw ex;
        } finally {
            close(jedis);
        }
    }

    public static String ping() throws Exception {
        Jedis jedis = createJedis();
        String PONG = null;
        try {
            PONG = jedis.ping();
        } catch (Exception ex) {
            log.warn("occurs exception", ex);
            throw ex;
        } finally {
            close(jedis);
        }
        return PONG;
    }

    public static void pipelinedWithoutReturn(PipelinedProxy pipelinedProxy) {
        Assert.notNull(pipelinedProxy, "无效参数");
        Jedis jedis = createJedis();
        try {
            Pipeline pipeline = jedis.pipelined();
            pipelinedProxy.proxy(pipeline);
            pipeline.sync();
        } finally {
            close(jedis);
        }
    }

    public static List<Object> pipelinedWithReturn(PipelinedProxy pipelinedProxy) {
        Assert.notNull(pipelinedProxy, "无效参数");
        Jedis jedis = createJedis();
        try {
            Pipeline pipeline = jedis.pipelined();
            pipelinedProxy.proxy(pipeline);
            return pipeline.syncAndReturnAll();
        } finally {
            close(jedis);
        }
    }

    public static void beginTransaction() {
        Jedis jedis = createJedis();
        Transaction transaction = jedis.multi();
    }
    public static void commitTransaction() {
        if (transaction == null) {
            return;
        }
        transaction.exec();
    }

    public static void discard() {
        if (transaction == null) {
            return;
        }
        transaction.discard();
    }

    public interface PipelinedProxy {
        void proxy(Pipeline pipeline);
    }


    public static void main(String... strings) throws Exception {
        set("hello1", "1");
        System.out.println(getStr("hello1"));
    }
}
