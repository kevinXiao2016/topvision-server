package com.topvision.ems.cmc.util;

import java.util.ArrayList;
import java.util.List;

public class RealtimeCmUtil {

    public static final String[] JUMPSTATUS_ALLSHOW={"1","2","5","6","10","11","21","22","23","25","26","27"};
    public static final String[] JUMPSTATUS_ONLINE={"6","21","26","27"};
    public static final String[] JUMPSTATUS_OFFLINE={"1"};
    public static final String[] JUMPSTATUS_ONLINING={"2","5","10","11","22","23","25"};
    public static final String STATUS_ELSE="100";//其他不常见状态
    
	/**
	 * @param 页面跳转传值映射
	 * @return List<String>
	 */
	public static List<String> cmStatusTrans(String status){
		List<String>statusList=new ArrayList<String>();
		if(status=="JUMPSTATUS_ALLSHOW"){
			for(int i=0;i<JUMPSTATUS_ALLSHOW.length;i++){
				statusList.add(JUMPSTATUS_ALLSHOW[i]);
			}
			return statusList;
		}else if(status=="JUMPSTATUS_ONLINE"){
			for(int i=0;i<JUMPSTATUS_ONLINE.length;i++){
				statusList.add(JUMPSTATUS_ONLINE[i]);
			}
			return statusList;
		}else if(status=="JUMPSTATUS_OFFLINE"){
			for(int i=0;i<JUMPSTATUS_OFFLINE.length;i++){
				statusList.add(JUMPSTATUS_OFFLINE[i]);
			}
			return statusList;
		}else{
			for(int i=0;i<JUMPSTATUS_ONLINING.length;i++){
				statusList.add(JUMPSTATUS_ONLINING[i]);
			}
			return statusList;
		}
	}
	/**
	 * 将状态和值一一对应
	 * @param
	 * @return List<String>
	 */	
	public static String mapMirror(String statusString){
		switch(statusString){
		case "offline":
			return "1";
		case "init(r2)":
			return "2";
		case "init(i)":
			return "5";
		case "online":
			return "6";
		case "init(d)":
			return "10";
		case "init(io)":
			return "11";
		case "online(d)":
			return "21";
		case "init6(s)":
			return "22";
		case "init6(a)":
			return "23";
		case "init6(r)":
			return "25";
		case "p-online":
			return "26";
		case "w-online":
			return "27";
		}
		return "100";
	}
	

	/**
	 * 数组转list
	 * @param 状态数组
	 * @return List<String>
	 */
	public static List<String> mapMirrorList(String[] statusList){
		List<String>list=new ArrayList<String>();
		for(int i=0;i<statusList.length;i++){
			list.add(statusList[i]);
		}
		return list;
	}	
	
	/**
	 * @param1 查询页面的状态参数
	 * @param2 cmts页面跳转的参数
	 * @return String[]
	 */
	public static String[] judgeStatusPara(String chooseStatus,String cmStatus){
		String[]jumpStatus={"JUMPSTATUS_ALLSHOW"};//如果没有选择，则初始默认为全部状态展示
		String statusArray[] = null;
		if (chooseStatus != null) {
			statusArray = chooseStatus.split(",");
		}
		if (chooseStatus == null && cmStatus.length() > 0) {
			jumpStatus[0] = cmStatus;// 其他页面跳转时传递参数
		}
		return statusArray == null ? jumpStatus : statusArray;
	}
}
