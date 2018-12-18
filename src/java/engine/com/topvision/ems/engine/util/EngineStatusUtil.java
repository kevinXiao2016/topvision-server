package com.topvision.ems.engine.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

import com.topvision.framework.EnvironmentConstants;

public class EngineStatusUtil {
	public static String getMemUsage() {
		long totalMemory = Runtime.getRuntime().totalMemory();
		long freeMemory = Runtime.getRuntime().freeMemory();

		BigDecimal totalBd = new BigDecimal(Long.toString(totalMemory));
		BigDecimal usedBd = new BigDecimal(Long.toString(totalMemory- freeMemory));
		double usage = usedBd.divide(totalBd, 4, BigDecimal.ROUND_HALF_UP).doubleValue()*100;
		DecimalFormat df = new DecimalFormat("#.00");
		return (df.format(usage) + "%");
	}
	
	public static String getRunTime(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		long thisTime = calendar.getTimeInMillis();
		long startTime = Long.valueOf(EnvironmentConstants.getEnv(EnvironmentConstants.START_TIME));
		long runTime = thisTime - startTime;
		String runTimeStr = formatLongToTimeStr(runTime);
		return runTimeStr;
	}
	
	public static int getThreadNumber(){
		// 获得线程总数
		ThreadGroup parentThread = Thread.currentThread().getThreadGroup();
		while(parentThread.getParent() != null){
			parentThread = parentThread.getParent();
		}
		int totalThread = parentThread.activeCount();
		return totalThread;
	}
	
	private static String formatLongToTimeStr(Long l) {
        int hour = 0;
        int minute = 0;
        int second = 0;
 
        second = l.intValue() / 1000;
 
        if (second > 60) {
            minute = second / 60;
            second = second % 60;
        }
        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        return (getTwoLength(hour) + ":" + getTwoLength(minute)  + ":"  + getTwoLength(second));
    }
    
    private static String getTwoLength(final int data) {
        if(data < 10) {
            return "0" + data;
        } else {
            return "" + data;
        }
    } 
}
