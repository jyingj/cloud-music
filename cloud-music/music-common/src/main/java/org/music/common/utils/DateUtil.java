package org.music.common.utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtil {
	private static Logger logger = LoggerFactory.getLogger(DateUtil.class);
	private String currentMonday;
	private String currentSunday;
	private String lastMonday;
	private String lastSunday;
	private String nextMonday;
	private String nextSunday;

	public DateUtil(String currentDate, String format) {
		DateFormat timeformat = new SimpleDateFormat(format);
		Calendar cal = Calendar.getInstance();
		Date date = null;
		currentDate=formatDateStringPre(currentDate, format);
		try {			
			if (7 == getWeekdayOfDateTime(currentDate, format)) {
				date = timeformat.parse(dayHandel(currentDate, format, -1));
				setCurrentSunday(currentDate);
			} else {
				Date date2 = timeformat
						.parse(dayHandel(currentDate, format, +7));
				cal.setTime(date2);
				cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
				setCurrentSunday(dateToString(cal.getTime(), format));
				date = timeformat.parse(currentDate);
			}
			cal.setTime(date);
			cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			setCurrentMonday(dateToString(cal.getTime(), format));
			cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			setLastSunday(dayHandel(getCurrentMonday(), format, -1));
			setLastMonday(dayHandel(getCurrentMonday(), format, -7));
			setNextMonday(dayHandel(getCurrentSunday(), format, +1));
			setNextSunday(dayHandel(getCurrentSunday(), format, +7));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}


	/**
	 * 时间相减
	 * @param time1 比较开始时间
	 * @param time2 比较结束时间
	 * @return
	 */
	public static String timeMinus(Date time1, Date time2) {		
		long time3 = time1.getTime();
		long time4 = time2.getTime();
		if(time4<time3){
			logger.debug("比较结束时间不能小于比较开始时间");
			return "";
		}
		long s = (time4 - time3) / 1000;
		String result =s/(24*3600)+"天"+ s %(24*3600)/ 3600 + "小时" + s%(24*3600) % 3600 / 60 + "分" + s%(24*3600) % 3600 % 60	+ "秒";
		return result;
	}
	
	/**
	 * 毫秒转时间
	 * @param s
	 * @return
	 */
	public static String millionSecondToTime(long s){
		s = s / 1000;
		String result =s/(24*3600)+"天"+ s %(24*3600)/ 3600 + "小时" + s%(24*3600) % 3600 / 60 + "分" + s%(24*3600) % 3600 % 60	+ "秒";
		result=result.replaceAll("(0天0小时0分)|(0天0小时)|(0天)", "");
		return result;		
	}

	/**
	 * 获取时间差 单位：秒
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static long getSlacktime(Date time1,Date time2){
		long time3 = time1.getTime();
		long time4 = time2.getTime();
		if(time4<time3){
//			logger.debug("比较结束时间不能小于比较开始时间");
			return -1;
		}
		long s = (time4 - time3) / 1000;		
		return s;
	}
	
	/**
	 * 获取时间差 单位：天
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static long getDaysByDates(Date time1,Date time2){
		long time3 = time1.getTime();
		long time4 = time2.getTime();
		long s = Math.abs(time4 - time3) / (24*3600000);
		return s;
	}
	
	/**
	 * 获取yyyy-MM-dd是星期几
	 * 
	 * @param datetime
	 * @param format
	 *            格式'yyyy-MM-dd'
	 * @return
	 */
	public static int getWeekdayOfDateTime(String datetime, String format) {
		DateFormat df = new SimpleDateFormat(format);
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(df.parse(datetime));
		} catch (Exception e) {
			e.printStackTrace();
		}
		int weekday = c.get(Calendar.DAY_OF_WEEK) - 1;
		return weekday == 0 ? 7 : weekday;
	}
	
	/**
	 * 获取yyyy-MM-dd是星期几
	 * 
	 * @param datetime
	 * @param format
	 *            格式'yyyy-MM-dd'
	 * @return
	 */
	public static String getWeekdayOfDateTimeStr(String datetime) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(df.parse(datetime));
		} catch (Exception e) {
			e.printStackTrace();
		}
		int weekday = c.get(Calendar.DAY_OF_WEEK) - 1;
		int day= weekday == 0 ? 7 : weekday;
		if(day==1){
			return "星期一";
		}
		if(day==2){
			return "星期二";
		}
		if(day==3){
			return "星期三";
		}
		if(day==4){
			return "星期四";
		}
		if(day==5){
			return "星期五";
		}
		if(day==6){
			return "星期六";
		}
		if(day==7){
			return "星期日";
		}
		return "未知";
	}
	

	/**
	 * 获取系统日期
	 * 
	 * @param format
	 *            格式'yyyy-MM-dd HH:mm:ss'
	 * @return
	 */
	public static String getCurrentDate(String format) {
		SimpleDateFormat timeformat = new SimpleDateFormat(format);
		Date date = Calendar.getInstance().getTime();
		return timeformat.format(date).toString();

	}

	/**
	 * 日期转为字符串
	 * 
	 * @param date
	 *            日期
	 * @param format
	 *            格式 'yyyyMMdd'
	 * @return
	 */
	public static String dateToString(Date date, String format) {
		// 字符串转Date
		SimpleDateFormat timeformat = new SimpleDateFormat(format);
		return timeformat.format(date).toString();
	}

	/**
	 * 字符串转日期
	 * 
	 * @param dateStr
	 *            要转为日期的字符串 如:'20091010'
	 * @param format
	 *            格式'yyyyMMdd'
	 * @return
	 * @throws ParseException
	 */
	public static Date stringToDate(String dateStr, String format) {
		dateStr=dateStr==null?"":dateStr.trim();
		format=format==null?"":format.trim();
		if(dateStr.length()==0){	
			logger.error("要格式化的字符串(dateStr)为空！");
			return null;
		}
		if(format.length()==0){	
			logger.error("format为空！");
			return null;
		}
		dateStr=formatDateString(dateStr,format);
		SimpleDateFormat timeformat = new SimpleDateFormat(format);
		try {
			return timeformat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static Date stringToDatePrivate(String dateStr, String format) {
		SimpleDateFormat timeformat = new SimpleDateFormat(format);
		try {
			return timeformat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 格式化日期字符串
	 * 
	 * @param dateStr
	 * @param format
	 * @return
	 */
	public static String formatDateString(String dateStr, String format) {
		dateStr=dateStr==null?"":dateStr.trim();
		format=format==null?"":format.trim();
		if(dateStr.length()==0){	
			logger.error("要格式化的字符串(dateStr)为空！");
			return "";
		}
		if(format.length()==0){	
			logger.error("format为空！");
			return "";
		}
		String result="";
		if(format.indexOf("年")!=-1){
			result=formatDateStringPre(dateStr, "yyyy") + "年";
		}
		if(format.indexOf("月")!=-1){
			result+=formatDateStringPre(dateStr, "MM") + "月";
		}
		if(format.indexOf("日")!=-1){
			result+=formatDateStringPre(dateStr, "dd") + "日";
		}
		if(format.indexOf("时")!=-1){
			result+=formatDateStringPre(dateStr, "HH") + "时";
		}
		if(format.indexOf("分")!=-1){
			result+=formatDateStringPre(dateStr, "mm") + "分";
		}
		if(format.indexOf("秒")!=-1){
			result+=formatDateStringPre(dateStr, "ss") + "秒";
		}
		if(!"".equals(result)){
			return result;
		}else{
			return formatDateStringPre(dateStr, format);
		}				
	}

	/**
	 * 格式化日期字符串
	 * 
	 * @param dateStr
	 * @param format
	 * @return
	 */
	private static String formatDateStringPre(String dateStr, String format) {
		Date date = null;
		SimpleDateFormat timeformat = new SimpleDateFormat(format);
		String alexDate="";
		String alexTime="";
		String tempdateStr="";
		String finadateStr=dateStr;
		if (dateStr.indexOf(":")!=-1){//处理2010-10-10 12:13:14
			alexDate=dateStr.substring(0,dateStr.indexOf(" ")).trim();
			alexTime=dateStr.substring(dateStr.indexOf(" ")).trim().replace(":","");						
		}else{
			if(dateStr.replaceAll("-", "").length()>8){
				alexDate=dateStr.replaceAll("-", "").substring(0,8).trim();
				alexTime=dateStr.replaceAll("-", "").substring(8).trim();
			}else{
				alexDate=dateStr.trim();
			}
			
		}				
		String[] tempDateStr=alexDate.split("-");//预防并处理这种情况2010-1-1 转为20100101
		if(tempDateStr.length>1){
			for(int i=0;i<tempDateStr.length;i++){
				tempdateStr=tempdateStr+new DecimalFormat("00").format(Integer.valueOf(tempDateStr[i]));
			}			
			dateStr=(tempdateStr+alexTime).trim();
		}else{
			if(dateStr.replaceAll("-", "").length()>8){
				dateStr=(alexDate+alexTime).trim();	
			}else{
				dateStr=(alexDate+" "+alexTime).trim();	
			}
					
		}				
		
		if (dateStr.length() == 6) {
			date = stringToDatePrivate(dateStr+"01 000000", "yyyyMMdd HHmmss");
			String formatDate = timeformat.format(date).toString();
			return formatDate;			
		}
		if (dateStr.length() == 4) {		
			date = stringToDatePrivate(dateStr+"0101 000000", "yyyyMMdd HHmmss");
			String formatDate = timeformat.format(date).toString();
			return formatDate;
		}
		if (dateStr.length() == 8) {
			date = stringToDatePrivate(dateStr+" 000000", "yyyyMMdd HHmmss");
			String formatDate = timeformat.format(date).toString();
			return formatDate;
		}
		if (dateStr.length() == 10) {
			date = stringToDatePrivate(dateStr+"0000", "yyyyMMddHHmmss");
			String formatDate = timeformat.format(date).toString();
			return formatDate;
		}
		if (dateStr.length() == 12) {
			date = stringToDatePrivate(dateStr+"00", "yyyyMMddHHmmss");
			String formatDate = timeformat.format(date).toString();
			return formatDate;
		}
		if (dateStr.length() == 14) {
			date = stringToDatePrivate(dateStr, "yyyyMMddHHmmss");
			String formatDate = timeformat.format(date).toString();
			return formatDate;
		}
		if (dateStr.indexOf(":")!=-1) {
			date = stringToDatePrivate(dateStr, format.replaceAll("-", ""));			
			String formatDate = timeformat.format(date).toString();
			return formatDate;
		}
		timeformat = new SimpleDateFormat(format);
		date=stringToDatePrivate(finadateStr,format);		
		String formatDate = timeformat.format(date).toString();
		return formatDate;		
	}

	/**
	 * 获取指定月份的第一天
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static String getFirstDayOfSpecifyDate(String dateStr, String format) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(stringToDatePrivate(formatDateStringPre(dateStr, format), format));
		cal.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		return dateToString(cal.getTime(), format);
	}

	/**
	 * 获取指定月份的最后一天
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static String getLastDayOfSpecifyDate(String dateStr, String format) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(stringToDatePrivate(formatDateStringPre(dateStr, format), format));
		cal.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		cal.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
		return dateToString(cal.getTime(), format);
	}

	/**
	 * 年加减
	 * 
	 * @param yearNum
	 * @return
	 * @throws ParseException
	 */
	public static String yearHandel(String dateStr, String format, int yearNum) {
		try {
			SimpleDateFormat timeformat = new SimpleDateFormat(format);
			Date date = timeformat.parse(formatDateStringPre(dateStr, format));
			Calendar a = Calendar.getInstance();
			a.setTime(date);
			a.add(Calendar.YEAR, yearNum);
			return timeformat.format(a.getTime());

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}
	
	/**
	 * 年加减
	 * 
	 * @param yearNum
	 * @return
	 * @throws ParseException
	 */
	public static Date yearHandel(Date date, int yearNum) {
		try {
			Calendar a = Calendar.getInstance();
			a.setTime(date);
			a.add(Calendar.YEAR, yearNum);
			return a.getTime();
		} catch (Exception e) {
			e.printStackTrace();
			return date;
		}
		
	}

	/**
	 * 月加减
	 * 
	 * @param monNum
	 * @return
	 * @throws ParseException
	 */
	public static String monthHandel(String dateStr, String format, int monNum) {
		try {
			SimpleDateFormat timeformat = new SimpleDateFormat(format);
			Date date = timeformat.parse(formatDateStringPre(dateStr, format));
			Calendar a = Calendar.getInstance();
			a.setTime(date);
			a.add(Calendar.MONTH, monNum);
			return timeformat.format(a.getTime());
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}

	/**
	 * 日加减
	 * 
	 * @param dayNum
	 * @return
	 * @throws ParseException
	 */
	public static String dayHandel(String dateStr, String format, int dayNum) {
		try {
			SimpleDateFormat timeformat = new SimpleDateFormat(format);
			Date date = timeformat.parse(formatDateStringPre(dateStr, format));
			Calendar a = Calendar.getInstance();
			a.setTime(date);
			a.add(Calendar.DATE, dayNum);
			return timeformat.format(a.getTime());
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 日加减
	 * 
	 * @param dayNum
	 * @return
	 * @throws ParseException
	 */
	public static Date dayHandel(Date date, int dayNum) {
		try {
			Calendar a = Calendar.getInstance();
			a.setTime(date);
			a.add(Calendar.DATE, dayNum);
			return a.getTime();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getCurrentMonday() {
		return currentMonday;
	}

	public void setCurrentMonday(String currentMonday) {
		this.currentMonday = currentMonday;
	}

	public String getCurrentSunday() {
		return currentSunday;
	}

	public void setCurrentSunday(String currentSunday) {
		this.currentSunday = currentSunday;
	}

	public String getLastMonday() {
		return lastMonday;
	}

	public void setLastMonday(String lastMonday) {
		this.lastMonday = lastMonday;
	}

	public String getLastSunday() {
		return lastSunday;
	}

	public void setLastSunday(String lastSunday) {
		this.lastSunday = lastSunday;
	}

	public String getNextMonday() {
		return nextMonday;
	}

	public void setNextMonday(String nextMonday) {
		this.nextMonday = nextMonday;
	}

	public String getNextSunday() {
		return nextSunday;
	}

	public void setNextSunday(String nextSunday) {
		this.nextSunday = nextSunday;
	}


}