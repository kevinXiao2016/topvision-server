<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.topvision.platform.SystemConstants" %>
<tr>
	<td><fmt:message bundle="${report}" key="report.analysisTime" />:</td>
	<td><select style="width:206px" id="reportCycle" name="reportCycle" onchange="cycleChanged(this)">
		<option value="0"><fmt:message bundle="${report}" key="report.custom" /></option>
        <option value="1"><fmt:message bundle="${report}" key="report.last1H" /></option>
        <option value="2"><fmt:message bundle="${report}" key="report.last12H" /></option>
        <option value="3"><fmt:message bundle="${report}" key="report.last24H" /></option>
        <option value="4"><fmt:message bundle="${report}" key="report.today" /></option>
        <option value="5"><fmt:message bundle="${report}" key="report.yesterday" /></option>
        <option value="6"><fmt:message bundle="${report}" key="report.curWeek" /></option>
        <option value="7"><fmt:message bundle="${report}" key="report.lastWeek" /></option>
		<option value="8"><fmt:message bundle="${report}" key="report.curMonth" /></option>
        <option value="9"><fmt:message bundle="${report}" key="report.lastMonth" /></option>
        <option value="10"><fmt:message bundle="${report}" key="report.curQuarter" /></option>
        <option value="11"><fmt:message bundle="${report}" key="report.curYear" /></option>
		</select>
	</td>	
</tr>

<tr id="startTimeArea"><Td><fmt:message bundle="${report}" key="report.startTime" />:</Td>
<td>
	<div><input type=text name="startDate" class=iptxt readonly id="calInput1" 
		style="width:90px" value="<s:property value="startDate"/>"><img id="imgCal1" hspace=5 style="cursor:pointer;" 
		src="../images/calendar.gif" align="absmiddle" onClick="showCalendar(1)"><select id="startTime" name="startTime" style="width:90px">
		<option value="0">00:00</option>
		<option value="1">01:00</option>
		<option value="2">02:00</option>
		<option value="3">03:00</option>
		<option value="4">04:00</option>
		<option value="5">05:00</option>
		<option value="6">06:00</option>
		<option value="7">07:00</option>
		<option value="8">08:00</option>
		<option value="9">09:00</option>
		<option value="10">10:00</option>
		<option value="11">11:00</option>
		<option value="12">12:00</option>
		<option value="13">13:00</option>
		<option value="14">14:00</option>
		<option value="15">15:00</option>	
		<option value="16">16:00</option>
		<option value="17">17:00</option>
		<option value="18">18:00</option>
		<option value="19">19:00</option>
		<option value="20">20:00</option>	
		<option value="21">21:00</option>
		<option value="22">22:00</option>
		<option value="23">23:00</option>
	</select>
	</div>
</td></tr>

<tr id="endTimeArea"><td><fmt:message bundle="${report}" key="report.endTime" />:</td>
<td>
	<div><input type=text name="endDate" class=iptxt readonly id="calInput2" style="width:90px"
		value="<s:property value="endDate"/>"><img id="imgCal2" hspace=5 style="cursor:pointer;" 
		src="../images/calendar.gif" align="absmiddle" onClick="showCalendar(2)"><select id="endTime" name="endTime" style="width:90px">
		<option value="0">00:00</option>
		<option value="1">01:00</option>
		<option value="2">02:00</option>
		<option value="3">03:00</option>
		<option value="4">04:00</option>
		<option value="5">05:00</option>
		<option value="6">06:00</option>
		<option value="7">07:00</option>
		<option value="8">08:00</option>
		<option value="9">09:00</option>
		<option value="10">10:00</option>
		<option value="11">11:00</option>
		<option value="12">12:00</option>
		<option value="13">13:00</option>
		<option value="14">14:00</option>
		<option value="15">15:00</option>	
		<option value="16">16:00</option>
		<option value="17">17:00</option>
		<option value="18">18:00</option>
		<option value="19">19:00</option>
		<option value="20">20:00</option>	
		<option value="21">21:00</option>
		<option value="22">22:00</option>
		<option value="23">23:00</option>
	</select>		
	</div>	
</td></tr>