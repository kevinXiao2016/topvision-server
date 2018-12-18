<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.UserContext.user.language}"/>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="res"/>

/**
 * 使用公司的国际化方式
 */
var $lang={
errAlertMsg: "<fmt:message bundle="${res}" key="my97.errAlertMsg" />",
aWeekStr: ["<fmt:message bundle="${res}" key="my97.aWeekStr.wk" />", "<fmt:message bundle="${res}" key="my97.aWeekStr.Sun" />", "<fmt:message bundle="${res}" key="my97.aWeekStr.Mon" />", "<fmt:message bundle="${res}" key="my97.aWeekStr.Tue" />", "<fmt:message bundle="${res}" key="my97.aWeekStr.Wed" />", "<fmt:message bundle="${res}" key="my97.aWeekStr.Thu" />", "<fmt:message bundle="${res}" key="my97.aWeekStr.Fri" />", "<fmt:message bundle="${res}" key="my97.aWeekStr.Sat" />"],
aLongWeekStr:["<fmt:message bundle="${res}" key="my97.aLongWeekStr.wk" />","<fmt:message bundle="${res}" key="my97.aLongWeekStr.Sunday" />","<fmt:message bundle="${res}" key="my97.aLongWeekStr.Monday" />","<fmt:message bundle="${res}" key="my97.aLongWeekStr.Tuesday" />","<fmt:message bundle="${res}" key="my97.aLongWeekStr.Wednesday" />","<fmt:message bundle="${res}" key="my97.aLongWeekStr.Thursday" />","<fmt:message bundle="${res}" key="my97.aLongWeekStr.Friday" />","<fmt:message bundle="${res}" key="my97.aLongWeekStr.Saturday" />","<fmt:message bundle="${res}" key="my97.aLongWeekStr.Sunday" />"],
aMonStr: ["<fmt:message bundle="${res}" key="my97.aMonStr.Jan" />", "<fmt:message bundle="${res}" key="my97.aMonStr.Feb" />", "<fmt:message bundle="${res}" key="my97.aMonStr.Mar" />", "<fmt:message bundle="${res}" key="my97.aMonStr.Apr" />", "<fmt:message bundle="${res}" key="my97.aMonStr.May" />", "<fmt:message bundle="${res}" key="my97.aMonStr.Jun" />", "<fmt:message bundle="${res}" key="my97.aMonStr.Jul" />", "<fmt:message bundle="${res}" key="my97.aMonStr.Aug" />", "<fmt:message bundle="${res}" key="my97.aMonStr.Sep" />", "<fmt:message bundle="${res}" key="my97.aMonStr.Oct" />", "<fmt:message bundle="${res}" key="my97.aMonStr.Nov" />", "<fmt:message bundle="${res}" key="my97.aMonStr.Dec" />"],
aLongMonStr: ["<fmt:message bundle="${res}" key="my97.aLongMonStr.January" />","<fmt:message bundle="${res}" key="my97.aLongMonStr.February" />","<fmt:message bundle="${res}" key="my97.aLongMonStr.March" />","<fmt:message bundle="${res}" key="my97.aLongMonStr.April" />","<fmt:message bundle="${res}" key="my97.aLongMonStr.May" />","<fmt:message bundle="${res}" key="my97.aLongMonStr.June" />","<fmt:message bundle="${res}" key="my97.aLongMonStr.July" />","<fmt:message bundle="${res}" key="my97.aLongMonStr.August" />","<fmt:message bundle="${res}" key="my97.aLongMonStr.September" />","<fmt:message bundle="${res}" key="my97.aLongMonStr.October" />","<fmt:message bundle="${res}" key="my97.aLongMonStr.November" />","<fmt:message bundle="${res}" key="my97.aLongMonStr.December" />"],
clearStr: "<fmt:message bundle="${res}" key="my97.clearStr" />",
todayStr: "<fmt:message bundle="${res}" key="my97.todayStr" />",
okStr: "<fmt:message bundle="${res}" key="my97.okStr" />",
updateStr: "<fmt:message bundle="${res}" key="my97.updateStr" />",
timeStr: "<fmt:message bundle="${res}" key="my97.timeStr" />",
quickStr: "<fmt:message bundle="${res}" key="my97.quickStr" />",
err_1: '<fmt:message bundle="${res}" key="my97.err_1" />'
}