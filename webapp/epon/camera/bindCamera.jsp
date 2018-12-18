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
		onuIndex = <%= request.getParameter("onuIndex") %>;
	$(DOC).ready(function(){
		var planlist, pyhList;
		$.ajax({
			url:'/camera/loadCameraPlanList.tv',dataType:'json',async:false,
			data:{notUsePage : true,eponIndex: onuIndex,entityId : entityId},
			success:function( json ){
				planlist = json.data;
			}
		});
		$.ajax({
			url:'/camera/loadCameraPhyList.tv',dataType:'json',async:false,
			data:{notUsePage : true,eponIndex: onuIndex,entityId : entityId},
			success:function( json ){
				pyhList = json.data;
			}
		});
		var planStore = new Ext.data.JsonStore({
			//url: "/camera/loadCameraPlanList.tv",
			//proxy : new Ext.ux.data.PagingMemoryProxy( planlist ),
			//autoLoad : true,
			data : planlist,
			fields: ["cameraNo","location","ip"]
		});
		var $list = [];
		var $newList = [];
		for(var i=planlist.length;i>0;i--){
			var n = planlist[i-1];
			if(!$list.contains(n.location)){
				$list.add(n.location);
				$newList.add(n);
			}
		}
		var planStore2 = new Ext.data.JsonStore({
			//url: "/camera/loadCameraPlanList.tv",
			//proxy : new Ext.ux.data.PagingMemoryProxy( planlist ),
			//autoLoad : true,
			data : $newList,
			fields: ["cameraNo","location","ip"]
		});
		var phyinfoStore = new Ext.data.JsonStore({
			//url: "/camera/loadCameraPlanList.tv",
			//proxy : new Ext.ux.data.PagingMemoryProxy( pyhList ),
			//autoLoad : true,
			data:pyhList,
			fields: ["type","mac","note"]
		});
		var $list = [];
		var $newList = [];
		for(var i=pyhList.length;i>0;i--){
			var n = pyhList[i-1];
			if(!$list.contains(n.type)){
				$list.add(n.type);
				$newList.add(n);
			}
		}
		var phyinfoStore2 = new Ext.data.JsonStore({
			//url: "/camera/loadCameraPlanList.tv",
			//proxy : new Ext.ux.data.PagingMemoryProxy( pyhList ),
			//autoLoad : true,
			data:$newList,
			fields: ["type","mac","note"]
		});
		cameraNoCombo =  new Ext.form.ComboBox({
			renderTo:"cameraNoCt",
			store : planStore,
			mode: 'local',
			editable : true,
			width : 200,
			forceSelection : true,
			valueField: 'cameraNo',
		    displayField: 'cameraNo',
		    listeners : {
		    	select : function(c, r, i){
		    		var ip = r.data.ip;
		    		ipCombo.setValue(ip);
		    		var location = r.data.location;
		    		locaCombo.setValue(location);
		    	}
		    }
		});
		ipCombo = new Ext.form.ComboBox({
			renderTo:"ip",
			store : planStore,
			mode: 'local',
			width : 200,
			editable : true,
			forceSelection : true,
			valueField: 'ip',
		    displayField: 'ip',
		    listeners : {
		    	select : function(c, r, i){
		    		var cameraNo = r.data.cameraNo;
		    		cameraNoCombo.setValue(cameraNo);
		    		var location = r.data.location;
		    		locaCombo.setValue(location);
		    	}
		    }
		});
		locaCombo = new Ext.form.ComboBox({
			renderTo:"location",
			store : planStore2,
			mode: 'local',
			width : 200,
			editable : true,
			forceSelection : true,
			valueField: 'location',
		    displayField: 'location',
	    	listeners : {
		    	select : function(c, r, i){
		    		var ip = r.data.ip;
		    		ipCombo.setValue(ip);
		    		var cameraNo = r.data.cameraNo;
		    		cameraNoCombo.setValue(cameraNo);
		    	}
		    }
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
			store : phyinfoStore2,
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
	
	function newcamera(){
		var ip = ipCombo.getValue();
		var cameraNo = cameraNoCombo.getValue();
		var location = locaCombo.getValue();
		var mac = macCombo.getValue();
		var type = typeCombo.getValue();
		var note = $("#note").val();
		if( !(ip && cameraNo && location) ){
			return top.showMessageDlg("@COMMON.error@", "@CAMERA.fullfillPlan@",null,function(){
				ipCombo.focus();
			});
		}
		if( !(type && note && mac) ){
			return top.showMessageDlg("@COMMON.error@", "@CAMERA.fullfillPhy@",null,function(){
				macCombo.focus();
			});
		}
		top.showWaitingDlg("@COMMON.wait@","@COMMON.saving@");
		$.ajax({
			url: "/camera/saveCameraConfig.tv",
			data:{
				ip:ip, mac:mac, eponIndex: onuIndex,entityId : entityId
			},success:function(){
				top.closeWaitingDlg();
				top.nm3kRightClickTips({
					title: "@COMMON.tip@",
					html: "@CAMERA.bindOk@"
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
				top.showMessageDlg("@COMMON.error@","@CAMERA.bindEr@");
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
	                 <td>
						<div id="cameraNoCt"></div>
	                 </td>
	             </tr>
	             <tr>
	                 <td class="rightBlueTxt w260">IP:</td>
	                 <td>
						<div id="ip"></div>
	                 </td>
	             </tr>
	             <tr class="darkZebraTr w260">
	                 <td class="rightBlueTxt">@COMMON.location@:</td>
	                 <td>
						<div id="location"></div>
	                 </td>
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
		         <li><a href="javascript:;" class="normalBtnBig" onclick="newcamera()"><span><i class="miniIcoSave"></i>@BUTTON.apply@</span></a></li>
		         <li><a href="javascript:;" class="normalBtnBig" onclick="cancelclick()"><span><i class="miniIcoForbid"></i>@BUTTON.cancel@</span></a></li>
		     </ol>
		</div>
	</div>
</body>
</Zeta:HTML>

