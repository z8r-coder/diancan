package com.ineedwhite.diancan.common.utils;

import com.ineedwhite.diancan.common.ErrorCodeEnum;
import com.ineedwhite.diancan.common.constants.BizOptions;
import com.ineedwhite.diancan.common.constants.DcException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author ruanxin
 * @create 2018-03-07
 * @desc
 */
public class BizUtils {

    private final static Logger logger = Logger.getLogger(BizUtils.class);
    /**
     * 检查必须的入参是否满足不可空
     * @param paramMap 参数列表
     */
    public static void checkMustParam(Map<String, String> paramMap, String[] mustParaArr) {
        if (mustParaArr == null || mustParaArr.length == 0) {
            return ;
        }

        for (String param : mustParaArr) {
            if (StringUtils.isEmpty(param)) {
                continue;
            }
            if (StringUtils.isEmpty(paramMap.get(param))) {
                logger.warn("checkMustParam failed for:" + param);
                throw new DcException(ErrorCodeEnum.DC00001, param);
            }
        }
    }
    /**
     * 把HttpServletRequest对象的getParameterMap返回对象转换为java.utils.Map对象
     * @param requestPara
     * @return
     */
    public static Map<String, String> getMapFromRequestMap(Map<String, String[]> requestPara) {
        Map<String, String> paraMap = new HashMap<String, String>();
        Iterator<Map.Entry<String, String[]>> it = requestPara.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String[]> entry = it.next();
            String key = entry.getKey();
            String[] values = entry.getValue();
            if (values != null && values.length >= 1) {
                paraMap.put(key, values[0]);
            }
        }

        return paraMap;
    }

    /**
     * 设置返回MAP
     * @param retMap
     * @param rspCode
     * @param rspMsg
     */
    public static void setRspMap(Map<String, String> retMap, String rspCode, String rspMsg) {
        retMap.put("rspCode", rspCode);
        retMap.put("rspMsg", rspMsg);
    }

    public static void setRspMap(Map<String, String> retMap, ErrorCodeEnum errorCodeEnum) {
        retMap.put("rspCode", errorCodeEnum.getCode());
        retMap.put("rspMsg", errorCodeEnum.getDesc());
    }

    public static void setRspMap(Map<String, String> retMap, DcException dcEx) {
        retMap.put("rspCode", dcEx.getErrorCode());
        retMap.put("rspMsg", dcEx.getErrorMsg());
    }

    /**
     * 将map转换为字符串
     * @param map  要转换的map
     * @return 转换后的字符串
     */
    public static String convertToString(Map<String, String> map) {
        if (CollectionUtils.isEmpty(map)) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        for (String key : map.keySet()) {
            result.append(BizOptions.AMPERSAND);
            result.append(key);
            result.append(BizOptions.EQUAL);
            String value = map.get(key);
            if(value == null) {
                value = "";
            }
            result.append(value);
        }

        return result.substring(1);
    }

    /**
     * 通过手机号获取卡号
     * @param phone
     * @return
     */
    public static String getUsrCardNo(String phone) {
        if (phone == null || phone.length() != 11) {
            throw new DcException(ErrorCodeEnum.DC00021);
        }
        return phone.substring(phone.length() - 6, phone.length());
    }

    /**
     * change map to bean
     * @param map
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T map2Bean(Map<String, String> map, Class<T> clazz) throws Exception {
        T bean = clazz.newInstance();
        BeanUtils.populate(bean, map);
        return bean;
    }

    /**
     * change bean to map
     * @param bean
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> Map<String, String> bean2Map(T bean) throws Exception {
        Map<String, String> retMap =  BeanUtils.describe(bean);
        retMap.remove("class");
        return retMap;
    }

    /**
     * 获取请求的IP
     *
     * @param request 请求
     */
    public static String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if (ip.indexOf(",") > 0) {
            ip = ip.substring(0, ip.indexOf(","));
        }

        return ip;
    }
}
