package com.example.mybase;

/**
 * @author WZ
 * @date 2019/5/23.
 * 严格要求自己，对每一行代码负责
 * description：
 */
public class Flag {
    //登录
    public static final String CACHE_LOGIN_DATA = "CACHE_LOGIN_DATA";
    /**
     * 搜索关键字
     */
    public static final String CACHE_SEARCH_KEY = "CACHE_SEARCH_KEY";

    /**
     * 公共配置区域 商品标签
     */
    public static final String CACHE_CONFIG_KEY = "CACHE_CONFIG_KEY";

    public static double APP_SIZE = 0;

    public enum Event {
        JUMP_LOGIN,
        JUMP_TAB1,
        JUMP_TAB2,
        SELECT_ADDRESS,
        SELECT_ADDRESS_MORE,
        SELECT_BANK_CARD,
        WECHAT_PAY
    }
}
