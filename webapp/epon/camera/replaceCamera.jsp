<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	LIBRARY EXT
    LIBRARY jquery
    MODULE camera
    IMPORT js.ext.ux.PagingMemoryProxy
</Zeta:Loader>
<script type="text/javascript">
	var entityId = <%= request.getParameter("entityId") %>,
		onuIndex = <%= request.getParameter("eponIndex") %>
	$(DOC).ready(function(){
		var pyhList;
		$.ajax({
			url:'/camera/loadCameraPhyList.tv',dataType:'json',async:false,
			data:{notUsePage : true,eponIndex: onuIndex,entityId : entityId},
			success:function( json ){
				pyhList = json.data;
			}
		});
		
		var phyinfoStore = new Ext.data.JsonStore({
			//url: "/camera/loadCameraPlanList.tv",
			proxy : new Ext.ux.data.PagingMemoryProxy( pyhList ),
			autoLoad : true,
			fields: ["type","mac","note"]
		});
		macCombo =  new Ext.form.ComboBox({
			renderTo:"mac",
			store : phyinfoStore,
			mode: 'local',
			editable : true,
			width : 200,
			forceSelection : true,
			valueField: 'mac',
		    displayField: 'mac',
		    listeners : {
		    	select : function(c, r, i){
		    		var type = r.data.type;
		    		typeCombo.setValue( type );
		    		$("#note").val(r.data.note);
		    	}
		    }
		});
		typeCombo =  new Ext.form.ComboBox({
			renderTo:"type",
			store : phyinfoStore,
			mode: 'local',
			editable : true,
			width : 200,
			forceSelection : true,
			valueField: 'type',
		    displayField: 'type',
		    listeners : {
		    	select : function(c, r, i){
		    		var mac = r.data.mac;
		    		macCombo.setValue(mac);
		    		$("#note").val(r.data.note);
		    	}
		    }
		});
	});
	
	function replacecamera(){
		var ip = "<%= request.getParameter("ip") %>";
		var mac = macCombo.getValue();
		var type = typeCombo.getValue();
		var note = $("#note").val();
		var cameraIndex = <%= request.getParameter("cameraIndex")%>;
		if( !(type && note && mac) ){
			return top.showMessageDlg("@COMMON.error@", "@CAMERA.fullfillPhy@",null,function(){
				macCombo.focus();
			});
		}
		top.showWaitingDlg("@COMMON.wait@","@COMMON.saving@");
		$.ajax({
			url: "/camera/replaceCamera.tv",
			data:{
				ip: ip, mac:mac, eponIndex: onuIndex,entityId : entityId,cameraIndex: cameraIndex
			},success:function(){
				top.closeWaitingDlg();
				top.nm3kRightClickTips({
					title: "@COMMON.tip@", html: "@CAMERA.relaceOk@"
				});
				cancelclick();
			},error:function(ex){
				if(ex.simpleName == "SnmpSetException"){
					if(ex.message == 1){
						return top.showMessageDlg("@COMMON.error@","@CAMERA.ipAndMacDulicate@");
					}else if(ex.message == 2){
						return top.showMessageDlg("@COMMON.error@","@CAMERA.ipRequireUnique@");
					}else if(ex.message == 3){
						return top.showMessageDlg("@COMMON.error@","@CAMERA.MacRequireUnique@");
					}
				}
				if(ex.simpleName == "RuntimeException"){
					return top.showMessageDlg("@COMMON.error@","@CAMERA.cameraFull@");
				}
				top.showMessageDlg("@COMMON.error@","@CAMERA.relaceEr@");
			}
		});
	}
	
	function cancelclick(){
		top.closeWindow("bindCamera");
	}
</script>
</head>
<body class="openWindow">
	<div class="openWinHeader">
	    <div class="openWinTip">@CAMERA.bindTip@</div>
	    <div class="rightCirIco pageCirIco"></div>
	</div>
	<div class="edgeTB10LR20 pT20">
	     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	         <tbody>
	         	<tr>
	                 <td class="rightBlueTxt w260">@CAMERA.bind2Onu@:</td>
	                 <td><%= request.getParameter("displayOnu") %></td>
	             </tr>
	             <tr class="darkZebraTr">
	                 <td class="rightBlueTxt w260">@CAMERA.cameraNo@:</td>
	                 <td><%= request.getParameter("cameraNo") %></td>
	             </tr>
	             <tr>
	                 <td class="rightBlueTxt w260">IP:</td>
	                 <td><%= request.getParameter("ip") %></td>
	             </tr>
	             <tr class="darkZebraTr">
	                 <td class="rightBlueTxt w260">@COMMON.location@:</td>
	                 <td><%= request.getParameter("location") %></td>
	             </tr>
	             <tr>
	                 <td class="rightBlueTxt w260">MAC:</td>
	                 <td>
						<div id="mac"></div>
	                 </td>
	             </tr>
	             <tr class="darkZebraTr">
	                 <td class="rightBlueTxt w260">@CAMERA.type@:</td>
	                 <td>
						<div id="type"></div>
	                 </td>
	             </tr>
	             <tr>
	                 <td class="rightBlueTxt w260">@COMMON.note@:</td>
	                 <td>
						<textarea disabled="disabled" rows="3" class="normalInput w200" id="note" style="height:40px;"></textarea>
	                 </td>
	             </tr>
	         </tbody>
	     </table>
		<div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT30 noWidthCenter">
		         <li><a href="javascript:;" class="normalBtnBig" onclick="replacecamera()"><span><i class="miniIcoSaveOK"></i>@CAMERA.replace@</span></a></li>
		         <li><a href="javascript:;" class="normalBtnBig" onclick="cancelclick()"><span>@COMMON.close@</span></a></li>
		     </ol>
		</div>
	</div>
</body>
</Zeta:HTML>

