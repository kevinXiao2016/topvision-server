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
    <h2 class="h2_bubble">批量拓扑</h2>
    <div class="edge10">
    	<h4 class="orangeTxt">1、扫描某一个网段内的设备</h4>
    	<p class="pT5">下面以扫描网段为172.17.2.140-148为例介绍批量拓扑的方法，拓扑出来的设备放入默认地域。</p>
    	<h6 class="pT40">第一步：进入“设备管理”，点击“批量拓扑”，弹出批量拓扑页面。</h6>
    	<img class="mT10 imgBorder" src="../images/batchTopo_zh_1.jpg" alt="" />
    	
    	<h6 class="pT40">第二步：点击“添加网段”，填写网段名称（“网管机房”），网段（“172.17.2.140-148”）、地域（拓扑出的设备会自动加入所选地域，此例选择“默认地域”），自动扫描开关（默认为开启）</h6>
    	<p>添加网段的规则为：</p>
		<p class="pL10 pT10">掩码172.168.0.1/24</p>
		<p class="pL10">星号172.17.2.* 或 172.17.*.1</p>
		<p class="pL10">中划线172.17.2.1-255或172.17.1-255.2</p>
    	<img class="mT10 imgBorder" src="../images/batchTopo_zh_2.jpg" alt="" />
    	
    	<h6 class="pT40">第三步： 执行批量扫描，可以等待自动扫描页就是扫描策略里面配置的规则来执行自动扫描，也可以立即执行手动扫描，两者实现的效果是一样的，执行手动扫描只需要点击批量拓扑列表每行后面操作列中的“扫描”按钮即可立即执行</h6>
    	<img class="mT10 imgBorder" src="../images/batchTopo_zh_3.jpg" alt="" />
    	
    	<h6 class="pT40">第四步：拓扑成功后，有“拓扑发现结束”的提示框</h6>
    	<img class="mT10 imgBorder" src="../images/batchTopo_zh_4.jpg" alt="" />
    	
    	<h6 class="pT40">第五步： 如果扫描后没有将期望出现的设备扫描出来，请查看参数配置是否正确</h6>
    	
    </div>
</body>
</Zeta:HTML>
