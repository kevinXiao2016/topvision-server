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
    <h2 class="h2_bubble">Batch Topology</h2>
    <div class="edge10">
    	<h4 class="orangeTxt">1.Scan a certain segment of the device</h4>
    	<p class="pT5">Here, we take the scanning network segment 172.17.2.140-148 as an example to introduce the method of batch topology, and the topologized device will be put in the default region.</p>
    	<h6 class="pT40">Step 1 Enter the "Equipment", click"Bulk Topology", and the batch topology page will pop up</h6>
    	<img class="mT10 imgBorder" src="../images/batchTopo_en_1.jpg" alt="" />
    	
    	<h6 class="pT40">Step 2 Click "Add Network Segment", fill the Name ("NM Machine Room"), Network Segment ("172.17.2.150-155"), Region (the topologized device will join the selected region automatically, in this example, select "Default"), and Automatic scanning switch (by default, Open)</h6>
    	<p>The rule for segment addition is:</p>
		<p class="pL10 pT10">Mask: 172.168.0.1/24</p>
		<p class="pL10">Asterisk: 172.17.2.* or 172.17.*.1</p>
		<p class="pL10">Hyphen: 172.17.2.1-255 or 172.17.1-255.2</p>
    	<img class="mT10 imgBorder" src="../images/batchTopo_en_2.jpg" alt="" />
    	
    	<h6 class="pT40">Step 3 Implement the batch scanning, you can either wait for the automatic scanning page what is the configuration rule in the scanning policy to implement the automatic scanning or start manual scanning immediately. The effects of both are the same. The manual scanning can be implemented immediately only by clicking “Scan” button in the action column after each row of batch topology list.</h6>
    	<img class="mT10 imgBorder" src="../images/batchTopo_en_3.jpg" alt="" />
    	
    	<h6 class="pT40">Step 4 After the topology is successful, there will be a prompt box of "Topology Finish".</h6>
    	<img class="mT10 imgBorder" src="../images/batchTopo_en_4.jpg" alt="" />
    	
    	<h6 class="pT40">Step 5 If the expected device is not discovered after scanning, please check whether the parameter configuration is correct</h6>
    	
    </div>
</body>
</Zeta:HTML>
