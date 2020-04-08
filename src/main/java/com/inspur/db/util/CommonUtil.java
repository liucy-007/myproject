package com.inspur.db.util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Slf4j
public class CommonUtil {

    private CommonUtil() {
        throw new IllegalStateException("CommonUtil class");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtil.class);
    private static Integer portAgent = 8203;

    /**
     * 获取当前时间
     * @return 时间
     */
    public static String getDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(currentTime);
    }

    /**
     * timezone，即由于世界各国家与地区经度不同，地方时也有所不同，按照经度将全球划分为24个时区。
     *
     * 时区有相应的英文字母缩写，例如GMT(格林威治标准时，是东西经零度的地方),UTC(UTC的本质强调的是比GMT更为精确的世界时间标准),
     * CST等，常见的时区，具体参考：这里。
     * @return 返回标准时间
     */
    public static Date getUTCDate() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        return new Date();
    }

    /**
     * 获取北京时间
     * @return 北京时间
     */
    public static String getBeijingDate() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        long time = System.currentTimeMillis() + 1000 * 60 * 60 * 8;
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sim.format(new Date(time));
    }



}
