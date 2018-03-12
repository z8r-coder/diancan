package com.ineedwhite.diancan.common.constants;

/**
 * @author ruanxin
 * @create 2018-03-07
 * @desc
 */
public class MustNeedPara {
    public static final String[] REG_MUST_PARAM = new String[]{"user_password", "user_name", "user_phone"};

    public static final String[] LOGIN_MUST_PARAM = new String[]{"user_password", "user_phone"};

    public static final String[] GET_BOARD_PARAM = new String[]{"order_board_date", "order_board_time_interval", "order_people_number", "board_type"};

    public static final String[] RESERVE_BOARD_PARAM = new String[]{"order_board_date", "order_board_time_interval", "board_id", "user_id"};

    public static final String[] GET_FOOD_PARAM = new String[]{"food_type_id"};
}
