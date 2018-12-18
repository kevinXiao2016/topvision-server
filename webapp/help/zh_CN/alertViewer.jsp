<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    css help.help
    module platform
</Zeta:Loader>
</head>
<body class="openWinBody edge10">
    <h2 class="h2_bubble">告警查看器</h2>
    <div class="edge10">
    	<h4 class="orangeTxt">1、告警确认全局配置</h4>
    	<p class="pT5">可以通过全局配置，来设置已经清除的告警状态是否自动确认。</p>
    	<p>配置<b class="blueTxt"> 开启 </b>时，系统会<b class="blueTxt"> 自动确认 </b>已处于清除状态的当前告警</p>
    	<p>配置<b class="blueTxt"> 关闭  </b>时，用户需<b class="blueTxt"> 手动确认 </b>已处于清除状态的当前告警</p>
    	<h6 class="pT40">第一步：点击系统右上角“全局管理视图”按钮</h6>
    	<img class="mT10 imgBorder" src="../images/alertView_zh_1.jpg" alt="" />
    	<h6 class="pT40">第二步：在全局管理视图页面点击“告警确认全局配置”按钮</h6>
    	<img class="mT10 imgBorder" src="../images/alertView_zh_2.jpg" alt="" />
    	<h6 class="pT40">第三步：在弹出的页面中，通过点击“开启”或“关闭”按钮，来设置告警是否为自动确认。</h6>
    	<img class="mT10 imgBorder" src="../images/alertView_zh_3.jpg" alt="" />
    </div>
</body>
</Zeta:HTML>
