package com.ineedwhite.diancan.biz;

import java.util.List;
import java.util.Map;

/**
 * 餐桌业务类
 */
public interface BoardService {
    /**
     * 获取可预定餐桌
     * @return
     */
    public Map<String, String> getAvailableBoard(Map<String, String> paraMap);

    /**
     * 预定餐桌
     * @param paraMap
     * @return
     */
    public Map<String, String> reserveBoard(Map<String, String> paraMap) throws Exception;

    /**
     * 加载订桌页面
     * @param paraMap
     * @return
     */
    public Map<String, String> loadBoardPage(Map<String, String> paraMap) throws Exception;
}
