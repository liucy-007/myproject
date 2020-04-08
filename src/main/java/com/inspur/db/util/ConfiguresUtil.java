package com.inspur.db.util;
/**
 * @author zhangxinjie
 * @date 2018年10月1日 下午13:43:59
 * <p>
 * 配置相关操作
 */
public class ConfiguresUtil {
    public static final String VIP = "vip";
    public static final String PORT = "port";
    public static final String RULE = "rule";
    public static final String BACKEND_IP = "backendip";
    public static final String BACKEND_PORT = "backendport";
    public static final String BACKEND_WEIGHT = "weight";
    public static final String PORT_SEPARATOR = ":";
    public static final String SPACE_SEPARATOR = " ";
    public static final String EQUAL_SIGN = "=";
    public static final String L_DIRECTOR_VIRTUAL = "virtual";
    public static final String L_DIRECTOR_CHECK_TIME_OUT = "checktimeout";
    public static final String L_DIRECTOR_CHECK_INTERVAL = "checkinterval";
    public static final String L_DIRECTOR_FAILURE_COUNT = "failurecount";
    public static final String L_DIRECTOR_SCHEDULER = "scheduler";
    public static final String L_DIRECTOR_CHECK_PORT = "checkport";
    public static final String L_DIRECTOR_CHECK_TYPE = "checktype";
    public static final String L_DIRECTOR_REQUEST = "request";
    public static final String L_DIRECTOR_PROTOCOL = "protocol";
    public static final String L_DIRECTOR_REAL = "real";
    public static final String L_DIRECTOR_CHECK_TYPE_STATUS_CONNECT = "connect";
    public static final String L_DIRECTOR_CHECK_TYPE_STATUS_NEGOTIATE= "negotiate";
    public static final String PATH_OF_L_DIRECTOR_CONFIG= "F://ldirectord.txt";
    public static final String VIP_PORT = "vipPort";
    public static final String DR_GATE = "gate";


    private ConfiguresUtil() {
        throw new IllegalStateException("ConfiguresUtil class");
    }

}
