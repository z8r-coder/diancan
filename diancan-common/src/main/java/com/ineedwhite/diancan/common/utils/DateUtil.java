package com.ineedwhite.diancan.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Date;

/**
 * Created by rx on 18/3/8.
 * 时间处理类
 */
public class DateUtil {
    /**
     * 时间格式
     */
    public final static String TIME_FORMAT                    = "HH:mm:ss:SS";
    public final static String ZHIFU_TIME                     = "yyyyMMdd";
    /**
     * 时间格式
     */
    public final static String TIME_SHORT_FORMAT              = "HH:mm:ss";

    /**
     * 缺省短日期格式
     */
    public final static String DEFAULT_SHORT_DATE_FORMAT      = "yyyy-MM-dd";
    /**
     * 日期格式
     */
    public final static String SHORT_DATE_FORMAT = "yyyy-MM-dd HH:mm";

    /**
     * 缺省中文短日期格式
     */
    public final static String DEFAULT_SHORT_DATE_FORMAT_ZH   = "yyyy年M月d日";

    /**
     * 缺省长日期格式
     */
    public final static String DEFAULT_LONG_TIME_FORMAT       = DEFAULT_SHORT_DATE_FORMAT
                                                                           + " " + TIME_FORMAT;

    public final static DateTimeFormatter DTF_DEFAULT_LONG_TIME_FORMAT   = DateTimeFormat
        .forPattern(DEFAULT_LONG_TIME_FORMAT);

    public final static String DEFAULT_SHORT_TIME_FORMAT      = DEFAULT_SHORT_DATE_FORMAT
                                                                           + " "
                                                                           + TIME_SHORT_FORMAT;

    public final static DateTimeFormatter DTF_DEFAULT_SHORT_TIME_FORMAT  = DateTimeFormat
        .forPattern(DEFAULT_SHORT_TIME_FORMAT);

    /**
     * Java能支持的最小日期字符串（yyyy-MM-dd）类型
     */
    public final static String JAVA_MIN_SHORT_DATE_STR        = "1970-01-01";

    /**
     * Java能支持的最小日期字符串（yyyy-MM-dd HH:mm:ss:SS）
     */
    public final static String JAVA_MIN_LONG_DATE_STR         = "1970-01-01 00:00:00:00";

    /**
     * 支付传输默认时间格式
     */
    public final static String DEFAULT_PAY_FORMAT             = "yyyyMMddHHmmss";

    /**
     * 带分割符时间格式
     */
    public final static String DEFAULT_SPLIT_PAY_FORMAT       = "yyyy-MM-dd HH:mm:ss";

    /**
     * 带毫秒格式
     */
    public final static String DEFAULT_PAY_MILLISECOND_FORMAT = "yyyyMMddHHmmssSSS";

    /**
     * Java能支持的最小的Timestamp
     */
    public final static Timestamp JAVA_MIN_TIMESTAMP             = convertStrToTimestamp(
        JAVA_MIN_LONG_DATE_STR);
    /**
     * 默认的时间段显示格式
     */
    public final static String DEFAULT_PERIOD_FORMAT          = "{0}天{1}小时{2}分钟";

    /**
     * Java能支持的最大日期字符串（yyyy-MM-dd）
     */
    public final static String JAVA_MAX_SHORT_DATE_STR        = "9999-12-31";
    public static final long BASE_START_DAY = 1420041600000l;          //基准时间为2015-01-01日 00:00:00
    private static long                   DAY_UNIT_OF_SECONDS            = 24 * 60 * 60l;
    public static final long ONE_DAY_MILLIS = DAY_UNIT_OF_SECONDS
            * 1000;
    private static long                   HOUR_UNIT_OF_SECONDS           = 60 * 60l;
    private static long                   MINUTE_UNIT_OF_SECONDS         = 60l;

    /**
     * 日期相加
     * @param tu    时间单位 @see com.rrx.common.utils.DateUtil.TimeUnit
     * @param delta 相加的值，不能超过Integer的最大值和最小值
     */
    public static Date add(TimeUnit tu, long delta) {
        if (delta == 0)
            return new Date();

        int deltaInt = 0;
        if (delta > Integer.MIN_VALUE && delta < Integer.MAX_VALUE)
            deltaInt = (int) delta;
        if (delta > Integer.MAX_VALUE)
            deltaInt = Integer.MAX_VALUE;
        if (delta < Integer.MIN_VALUE)
            deltaInt = Integer.MIN_VALUE;

        DateTime dt = new DateTime();
        switch (tu) {
            case yy:
                dt = dt.plusYears(deltaInt);
                break;
            case MM:
                dt = dt.plusMonths(deltaInt);
                break;
            case dd:
                dt = dt.plusDays(deltaInt);
                break;
            case HH:
                dt = dt.plusHours(deltaInt);
                break;
            case mm:
                dt = dt.plusMinutes(deltaInt);
                break;
            case ss:
                dt = dt.plusSeconds(deltaInt);
                break;
        }

        return dt.toDate();
    }

    /**
     * 获取当天起始时间
     * 格林尼治时间比北京时间少8个小时，需要减去8小时的毫秒
     */
    public static long getStartTimeStampToday() {
        return BASE_START_DAY
               + ((System.currentTimeMillis() - BASE_START_DAY) / ONE_DAY_MILLIS) * ONE_DAY_MILLIS;
    }

    public static Date getStartDateToday() {
        return new Date(getStartTimeStampToday());
    }

    /**
     * 获取明天的起始时间戳
     */
    public static long getStartTimeStampTomorrow() {
        return BASE_START_DAY + ((System.currentTimeMillis() - BASE_START_DAY) / ONE_DAY_MILLIS + 1)
                                * ONE_DAY_MILLIS;
    }

    public static Date getStartDateTomorrow() {
        return new Date(getStartTimeStampTomorrow());
    }

    /**
     * 获取当天结束时间毫秒
     * 格林尼治时间比北京时间少8个小时，需要减去8小时的毫秒
     */
    public static long getEndTimeStampToday() {
        return BASE_START_DAY + ((System.currentTimeMillis() - BASE_START_DAY) / ONE_DAY_MILLIS + 1)
                                * ONE_DAY_MILLIS
               - 1;
    }

    public static Date getEndDateToday() {
        return new Date(getEndTimeStampToday());
    }

    /**
     * 获取当天结束时间字符串
     * 格林尼治时间比北京时间少8个小时，需要减去8小时的毫秒
     */
    public static String getEndTimeToday() {
        return new DateTime(getEndTimeStampToday()).toString(DEFAULT_SHORT_TIME_FORMAT);
    }

    /**
     * 获取指定日期结束时间字符串
     * 格林尼治时间比北京时间少8个小时，需要减去8小时的毫秒
     */
    public static String getEndDateOfDay(Date date) {
        return new DateTime(
            BASE_START_DAY + ((date.getTime() - BASE_START_DAY) / ONE_DAY_MILLIS + 1)
                             * ONE_DAY_MILLIS
                            - 1).toString(DEFAULT_SHORT_TIME_FORMAT);
    }

    /**
     * 获取几天后结束时间字符串
     * 格林尼治时间比北京时间少8个小时，需要减去8小时的毫秒
     */
    public static long getEndTimeStampOfPlusDay(int plusDays) {
        return BASE_START_DAY
               + ((System.currentTimeMillis() - BASE_START_DAY) / ONE_DAY_MILLIS + plusDays + 1)
                 * ONE_DAY_MILLIS
               - 1;
    }

    public static String getEndTimeOfPlusDay(int plusDays) {
        return new DateTime(getEndTimeStampOfPlusDay(plusDays)).toString(DEFAULT_SHORT_TIME_FORMAT);
    }

    /**
     * 获取昨天结束时间字符串
     * 格林尼治时间比北京时间少8个小时，需要减去8小时的毫秒
     */
    public static String getTimeEndOfYesterday() {
        return new DateTime(
            BASE_START_DAY + ((System.currentTimeMillis() - BASE_START_DAY) / ONE_DAY_MILLIS)
                             * ONE_DAY_MILLIS
                            - 1).toString(DEFAULT_SHORT_DATE_FORMAT);
    }

    public static String getTimeEndOfYesterday(String format) {
        return new DateTime(
                BASE_START_DAY + ((System.currentTimeMillis() - BASE_START_DAY) / ONE_DAY_MILLIS)
                        * ONE_DAY_MILLIS
                        - 1).toString(format);
    }

    public static boolean compareTime(Date time) {
        DateTime dt1 = new DateTime();
        return dt1.isAfter(time.getTime());
    }

    public static boolean compareTime(String time) {
        return compareTime(time, getCurrDateStrWithDefaultLongTimeFormat(),
            DTF_DEFAULT_LONG_TIME_FORMAT);
    }

    public static boolean compareTime(String time1, String time2) {
        return compareTime(time1, time2, DTF_DEFAULT_LONG_TIME_FORMAT);
    }

    public static boolean compareTime(String time1, String time2, String dateFormat) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern(dateFormat);
        return compareTime(time1, time2, dtf);
    }

    public static boolean compareTime(String time1, String time2, DateTimeFormatter dtf) {
        //时间解析
        DateTime dt1 = DateTime.parse(time1, dtf);
        DateTime dt2 = DateTime.parse(time2, dtf);

        return dt1.isAfter(dt2);
    }

    public static String convertDateToStr(Date date, String dateFormat) {
        if (date == null || StringUtils.isBlank(dateFormat)) {
            return "";
        }
        DateTime dt = new DateTime(date);
        return dt.toString(DateTimeFormat.forPattern(dateFormat));
    }

    public static String covertDateToStr(long time) {
        return convertDateToStr(time, DEFAULT_SHORT_TIME_FORMAT);
    }

    public static String convertDateToStr(long time, String dateFormat) {
        return convertDateToStr(new Date(time), dateFormat);
    }

    /**
     * 校验时间格式
     * @param date 日期
     * @param lenient 是否严格检查 true 宽松检查， false 严格检查
     * @return
     */
    public static boolean defaultDateFormatValidate(String date, boolean lenient) {
        if(date.length() != 14 && date.length() != 13) {
            return false;
        }
        if(!lenient) {
            try {
                DateUtil.convertStrToDate(String.valueOf(date), DEFAULT_PAY_FORMAT);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            //检验是否能转为Long类型，防止抛异常
            for(int i=0; i<date.length(); i++) {
                if(date.charAt(i) < 48 || date.charAt(i) > 57) {
                    return false;
                }
            }
            Long dateVal = Long.valueOf(date);
            int hook = date.length() == 14 ? 1 : 10;
            int year = (int) (dateVal*hook/(10000000000L));
            int month = (int) ((dateVal*hook/100000000)%100);
            int dat = (int) ((dateVal*hook/1000000)%100);
            int hour = (int) ((dateVal*hook/10000)%100);
            int min = (int) ((dateVal*hook/100)%100);
            int sed = (int) ((dateVal)%(100/hook));
            if(year < 1700 || month > 12 || month == 0 || dat > 31 || dat == 0 || hour > 23
                    || min > 59 || sed > 59) {
                return false;
            }
        }
        return true;
    }

    public static Date convertStrToDate(String time, String dateFormat) {
        DateTimeFormatter format = DateTimeFormat.forPattern(dateFormat);
        //时间解析
        DateTime dt1 = DateTime.parse(time, format);
        return dt1.toDate();
    }

    public static Date convertStrToDateWithDefaultDayFormat(String time) {
        return convertStrToDate(time, DEFAULT_PAY_FORMAT);
    }

    private static Timestamp convertStrToTimestamp(String time) {
        if (time == null) {
            return null;
        }

        DateTimeFormatter format = DateTimeFormat.forPattern(DEFAULT_LONG_TIME_FORMAT);
        DateTime dt1 = DateTime.parse(time, format);
        return new Timestamp(dt1.getMillis());
    }

    public static String convertToPeriod(long period) {

        return MessageFormat.format(DEFAULT_PERIOD_FORMAT, (period / DAY_UNIT_OF_SECONDS),
            (period % DAY_UNIT_OF_SECONDS / HOUR_UNIT_OF_SECONDS),
            (period % HOUR_UNIT_OF_SECONDS / MINUTE_UNIT_OF_SECONDS));
    }

    public static double dateDiffDays(Date startDate, Date endDate) {
        return (double) (endDate.getTime() - startDate.getTime()) / (60 * 60 * 24 * 1000);
    }

    public static String getCurrDateStrWithDefaultLongTimeFormat() {
        return getCurrDateStr(DEFAULT_LONG_TIME_FORMAT);
    }

    /**
     * 获取当前时间精确到秒级别的默认格式时间
     * @return
     */
    public static String getCurrDateStrWithDefaultPayFormat() {
        return getCurrDateStr(DEFAULT_PAY_FORMAT);
    }

    /**
     * 获取当前时间精确到毫秒级别的默认格式时间
     * @return
     */
    public static String getCurrDateStrWithDefaultPayMillisecondFormat() {
        return getCurrDateStr(DEFAULT_PAY_MILLISECOND_FORMAT);
    }

    public static String getCurrDateStr(String dateFormat) {
        return convertDateToStr(new Date(), dateFormat);
    }

    public static Timestamp getCurrTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static Long getUnixTimestamp() {
        return System.currentTimeMillis() / 1000;
    }

    public static String toBeginDate(String fieldValue) {
        if (StringUtils.isBlank(fieldValue)) {
            return "";
        }
        if (fieldValue.length() > 10) {
            return fieldValue;
        }

        fieldValue += " 00:00:00";
        return fieldValue;
    }

    public static String toEndDate(String fieldValue) {
        if (StringUtils.isBlank(fieldValue)) {
            return "";
        }

        if (fieldValue.length() > 10) {
            return fieldValue;
        }

        fieldValue += " 23:59:59";
        return fieldValue;
    }

    public static String getStandardDatetimeStr(String time) {
        if (StringUtils.isBlank(time)) {
            return "";
        }

        DateTime dt1 = DateTime.parse(time, DTF_DEFAULT_LONG_TIME_FORMAT);
        return dt1.toString();
    }

    public static String getTimeDiff(String startTime, String endTime) {

        DateTime dt1 = DateTime.parse(startTime, DTF_DEFAULT_LONG_TIME_FORMAT);
        DateTime dt2 = DateTime.parse(endTime, DTF_DEFAULT_LONG_TIME_FORMAT);

        String result = "";

        //默认为毫秒，除以1000是为了转换成秒
        long interval = (dt1.getMillis() - dt2.getMillis()) / 1000; //秒
        long day = interval / (DAY_UNIT_OF_SECONDS); //天
        long hour = interval % (DAY_UNIT_OF_SECONDS) / HOUR_UNIT_OF_SECONDS; //小时
        long minute = interval % HOUR_UNIT_OF_SECONDS / MINUTE_UNIT_OF_SECONDS; //分钟
        long second = interval % MINUTE_UNIT_OF_SECONDS; //秒
        if (day > 0)
            result = result + day + "天";
        if (hour > 0)
            result = result + hour + "时";
        if (minute > 0)
            result = result + minute + "分";
        if (second > 0)
            result = result + second + "秒";

        return result;

    }

    public static long getSecondDiff(String startTime, String endTime) {
        DateTime dt1 = DateTime.parse(startTime, DTF_DEFAULT_SHORT_TIME_FORMAT);
        DateTime dt2 = DateTime.parse(endTime, DTF_DEFAULT_SHORT_TIME_FORMAT);

        return (dt1.getMillis() - dt2.getMillis()) / 1000l;
    }
    public enum TimeUnit {
        yy("时间单位－年"), MM("时间单位－月"), dd("时间单位－日"), HH("时间单位－时"), mm("时间单位－分"), ss("时间单位－秒");

        public String desc;

        TimeUnit(String desc) {
            this.desc = desc;
        }

    }


    /**
     * 获取今年的1月1日
     * @return
     */
    public static String getCurrYearBegin() {
        String currTime = getCurrDateStr(DEFAULT_PAY_FORMAT);
        String currYear = currTime.substring(0, 4);
        currYear = currYear + "0101";
        return currYear;
    }
    public static String getNextYearBegin() {
        String currTime = getCurrDateStr(DEFAULT_PAY_FORMAT);
        String currYear = currTime.substring(0, 4);
        String nextYear = String.valueOf(Integer.parseInt(currYear) + 1);
        nextYear = nextYear + "0101";
        return nextYear;
    }

    /**
     * 获取当前时间后几个小时的时间
     */
    public static String getTimeByHour(int hour) {
        return new DateTime((new Date()).getTime() + (HOUR_UNIT_OF_SECONDS * 1000 * hour)).toString(DEFAULT_SHORT_TIME_FORMAT);
    }

    public static String getTimeAfterMinutes(String time, int minutes) {
        long dateTime = convertStrToDate(time, DEFAULT_SPLIT_PAY_FORMAT).getTime();
        return new DateTime(dateTime + (60 * 1000 * minutes)).toString(DEFAULT_SPLIT_PAY_FORMAT);
    }

    public static String getTimeAfterMinutes(Date dateTime, int minutes) {
        return new DateTime(dateTime.getTime() + (60 * 1000 * minutes)).toString(DEFAULT_SPLIT_PAY_FORMAT);
    }

    public static void main(String[] args) {
        System.out.println(getCurrYearBegin());
        System.out.println(getNextYearBegin());
    }
}
