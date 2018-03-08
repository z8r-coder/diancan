package com.ineedwhite.diancan.biz;

import java.util.List;
import java.util.Map;

/**
 * 餐桌业务类
 */
public interface Board {
    /**
     * 预定餐桌
     * @return
     */
    public Map<String, String> getAvailableBoard(Map<String, String> paraMap);
}
