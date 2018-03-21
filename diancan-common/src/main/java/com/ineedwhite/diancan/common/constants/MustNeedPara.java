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

    public static final String[] RESERVE_BOARD_PARAM = new String[]{"order_id", "order_board_date", "order_board_time_interval", "board_id", "user_id"};

    public static final String[] GET_FOOD_PARAM = new String[]{"food_type_id", "food_page"};

    public static final String[] GET_USR_INFO = new String[]{"user_id"};

    public static final String[] ADD_FOOD = new String[]{"food_id", "food_num"};

    public static final String[] SHOPPING_CART = new String[] {"order_id"};

    public static final String[] GET_COUPON_LIST = new String[]{"user_id", "order_id"};

    public static final String[] USE_COUPON = new String[] {"coupon_id", "order_id"};

    public static final String[] SHOPPING_CARD_ADD_MINUS = new String[] {"food_id", "food_num", "order_id"};

    public static final String[] CHECK_OUT = new String[]{"order_id"};

    public static final String[] GET_USER_COUPON = new String[]{"user_id"};

    public static final String[] GET_USER_DETAIL_INFO = new String[]{"user_id"};

    public static final String[] MOD_USER_INFO = new String[]{"user_id", "user_name", "user_phone"};

    public static final String[] RECHARGE = new String[]{"user_id", "recharge_amt"};

    public static final String[] HISTORY_ORDER = new String[]{"user_id"};

    public static final String[] RECHARGE_PAGE_LOADING = new String[]{"user_id"};

    public static final String[] ORDER_WITHOUT_FINISH = new String[]{"user_id"};

    public static final String[] ORDER_INFO = new String[]{"user_id"};

    public static final String[] LOAD_BOARD_PAGE = new String[]{"order_id"};

    public static final String[] CANCELLATION = new String[]{"user_id"};
}
