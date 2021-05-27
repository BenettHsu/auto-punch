package cn.xuben99.util;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Slf4j
public class DateUtil {

    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private final String SHORT_DATE_PATTERN = "yyyy-MM-dd";

    public static String getCurrentStr(){
        return format(new Date());
    }
    public static Date getCurrentDate(){
        return new Date();
    }

    public static String getCurrentHourPoint(){
        return getHourPointByDateOffset(0,new Date());
    }

    public static String getHourPointByDateOffset(int i,Date date){
        Calendar instance = new Calendar.Builder().setInstant(date).build();
        setHourMinuteSecondByOffset(instance,i,0,0);
        instance.set(Calendar.MINUTE,0);
        instance.set(Calendar.SECOND,0);
        return format(instance.getTime());
    }
    public static String getHourPointByCurrentOffset(int i){
        return getHourPointByDateOffset(0,new Date());
    }

    public static String getTodayStart(){
        return getStartByTodayOffset(0);
    }

    public static String getStartByTodayOffset(int i){
        return format(setHourMinuteSecondIsZero(new Date(),0,0,i).getTime());
    }
    public static String getStartByDateOffset(int i,Date date){
        return format(setHourMinuteSecondIsZero(date,0,0,i).getTime());
    }

    public static String getTodayEnd(){
        return getEndByTodayOffset(0);
    }

    public static String getEndByTodayOffset(int i){
        return format(setHourMinuteSecondIsMax(new Date(),0,0,i).getTime());
    }
    public static String getEndByDateOffset(int i,Date date){
        return format(setHourMinuteSecondIsMax(date,0,0,i).getTime());
    }

    public static Calendar setHourMinuteSecondIsZero(Date date,int yearOffset,int monthOffset,int dayOffset){
        Calendar instance = new Calendar.Builder().setInstant(date).build();
        setYearMonthDayByOffset(instance,yearOffset,monthOffset,dayOffset);
        instance.set(Calendar.HOUR_OF_DAY,0);
        instance.set(Calendar.MINUTE,0);
        instance.set(Calendar.SECOND,0);
        return instance;
    }

    public static Calendar setHourMinuteSecondIsMax(Date date,int yearOffset,int monthOffset,int dayOffset){
        Calendar instance = new Calendar.Builder().setInstant(date).build();
        setYearMonthDayByOffset(instance,yearOffset,monthOffset,dayOffset);
        instance.set(Calendar.HOUR_OF_DAY,23);
        instance.set(Calendar.MINUTE,59);
        instance.set(Calendar.SECOND,59);
        return instance;
    }

    public static void setYearMonthDayByOffset(Calendar instance,int yearOffset,int monthOffset,int dayOffset){
        instance.add(Calendar.YEAR,yearOffset);
        instance.add(Calendar.MONTH,monthOffset);
        instance.add(Calendar.DAY_OF_MONTH,dayOffset);
    }

    public static void setHourMinuteSecondByOffset(Calendar instance,int hourOffset,int minuteOffset,int secondOffset){
        instance.add(Calendar.HOUR_OF_DAY,hourOffset);
        instance.add(Calendar.MINUTE,minuteOffset);
        instance.add(Calendar.SECOND,secondOffset);
    }

    public static String format(Date date){
        return format(date,DEFAULT_DATE_PATTERN);
    }

    public static String format(Date date, String pattern){
        return new SimpleDateFormat(pattern).format(date);
    }

    public static Date parse(String str) throws ParseException {
        return parse(str,DEFAULT_DATE_PATTERN);
    }

    public static Date parse(String str, String pattern) throws ParseException {
        return new SimpleDateFormat(pattern).parse(str);
    }

}
