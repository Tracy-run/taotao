package com.demo.taotao.common.dateutil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUitls {


    /**
     *  获取当天的日期 YYYY-MM-dd
     * @return
     */
    public static String getCurrentDate(){
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd");
        return format.format(new Date());
    }

    /**
     *  获取当天的日期 YYYY-MM-dd MM:HH:ss
     * @return
     */
    public static String getCurrentTime(){
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
        return format.format(new Date());
    }


    /**
     * 获取昨天的日期
     * @return
     */
    public static String getYesterday(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,-1);
        Date time = cal.getTime();
        return new SimpleDateFormat("YYYY-MM-dd").format(time);
    }

    /**
     * 获取昨天的时间
     * @return
     */
    public static String getYesterdayTime(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,-1);
        Date time = cal.getTime();
        return new SimpleDateFormat("YYYY-MM-dd hh:mm:ss").format(time);
    }


    /**
     * 获取N 天前的时间
     * @param n
     * @return
     */
    public static String getNDaysBeforeTime(int n){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,-n);
        Date time = cal.getTime();
        return new SimpleDateFormat("YYYY-MM-dd hh:mm:ss").format(time);
    }


    /**
     * 判断date是周末？？  是：true  否：false
     * @param date
     * @return
     */
    public static boolean isWeekend(String date){
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date ddate = format.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(ddate);
            if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 秒转小时
     * @param time
     * @return
     */
    public static Double secondToHour(Long time){
        return BigDecimalUtil.add(time/3600, (time%3600)/3600.00);
    }


    /**
     * 日期差值
     * @param startDay yyyy-MM-dd
     * @param endDay yyyy-MM-dd
     * @return
     */
    public static int calenderDiffDay(String startDay,String endDay){
        int result =0;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = format.parse(startDay);
            Date endDate = format.parse(endDay);

            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            long time1 = cal.getTimeInMillis();
            cal.setTime(endDate);
            long time2 = cal.getTimeInMillis();
            long betweenDays=(time2-time1)/(1000*3600*24);

            return Integer.parseInt(String.valueOf(betweenDays>0? betweenDays:0));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    /**
     * 计算月份差
     * @param startDate
     * @param endDate
     * @return
     */
    public static int calenderDiffMonth(String startDate, String endDate) {
        int result = 0;
        try {
            SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM");
            Date start = sfd.parse(startDate);
            Date end = sfd.parse(endDate);
            int startYear = getYear(start);
            int startMonth = getMonth(start);
            int startDay = getDay(start);
            int endYear = getYear(end);
            int endMonth = getMonth(end);
            int endDay = getDay(end);
            if (startDay > endDay) { // 1月17 大于 2月28
                if (endDay == getDaysOfMonth(getYear(new Date()), 2)) { // 也满足一月
                    result = (endYear - startYear) * 12 + endMonth - startMonth;
                } else {
                    result = (endYear - startYear) * 12 + endMonth - startMonth
                            - 1;
                }
            } else {
                result = (endYear - startYear) * 12 + endMonth - startMonth;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }


    /**
     * 返回日期的天，1-12,即yyyy-MM-dd中的dd
     * @param date
     * @return
     */
    public static int getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DATE);
    }

    /**
     * 返回日期的月份，1-12,即yyyy-MM-dd中的MM
     *
     * @param date
     * @return
     */
    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 返回日期的年,即yyyy-MM-dd中的yyyy
     *
     * @param date
     *            Date
     * @return int
     */
    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 根据年月获取总天数
     * @param year
     * @param month
     * @return
     */
    public static int getDaysOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }


    public static void main(String [] args){
        System.out.println(getCurrentTime());
    }


}
