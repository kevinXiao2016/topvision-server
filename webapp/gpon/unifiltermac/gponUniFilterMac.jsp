<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module gpon
</Zeta:Loader>
<style type="text/css">
	body,html{ height: 100%;}
    #openLayer{ width: 520px; position:absolute; left:50%; top:100px; margin-left:-260px; z-index:9;}
    #openLayerBg{ width:100%; height:100%; position: absolute; top:0; left:0; background:#F7F7F7; z-index:8;
	 filter: alpha(opacity=80);
	 -webkit-opacity: 0.8;
	    -moz-opacity: 0.8;
	     -ms-opacity: 0.8;
	      -o-opacity: 0.8;
	         opacity: 0.8;
	}
	.question{ padding:1px 0px 1px 20px; background:url(/images/performance/Help.png) no-repeat;}
</style>
<script type="text/javascript">
    var uniId = '${uniId}';
    var onuId = '${onuId}';
    var uniNo = '${uniNo}';
    //注意:如果是从设备快照进入页面，则uniId为空。这个时候，加载完端口号的下拉框的时候，必须去reload一下数据;
    var withoutUniId = (uniId == "") ? true : false;
    
    var MAX_MAC_RECORD = 16; //一个端口下最多可以添加16条;
    var cm,columnModels,sm,store,grid,viewPort,tbar,bbar;
    
    Ext.onReady(function(){
        columnModels = [
            {header: "MAC", width:100, dataIndex: "macAddressString", align: "center", sortable: false},
            {header: "@COMMON.manu@", width:200, dataIndex: "macAddressString", align: "center", sortable: false, fixed:true, renderer:renderManu}
        ];
        cm = new Ext.grid.ColumnModel({
            defaults : {
                menuDisabled : true
            },
            columns: columnModels
        });
        store = new Ext.data.JsonStore({
            url: "/gpon/unifiltermac/loadGponUniFilterMacList.tv",
            root: "data",
            totalProperty: "rowCount",
            remoteSort: true,
            fields: ["macAddressString","uniNo","uniId","uniIndex"]
        }); 
        store.baseParams = {
        	uniId : window.uniId
        };
        store.load();
        //下拉框 添加 刷新
        tbar = new Ext.Toolbar({
       		renderTo: 'topPart',
            items : [{
        		xtype: 'tbspacer',
        		width: 10
	        },{
	        	xtype: 'tbtext',
	        	cls : 'blueTxt',
	        	text : '@onu/ONU.portNum@@COMMON.maohao@'
	        },{
	        	xtype: "component",
	        	html: '<select id="portSel" class="normalSel w80" onchange="changePortNum()"></select>'
	        },{
        		xtype: 'tbspacer',
        		width: 5
	        },{
                text : "@BUTTON.add@",
                iconCls : "bmenu_new",
                handler : showAddMac
            },'->',{
            	xtype : "component",
            	cls : 'pR10',
            	html : String.format('<p class="question">@GPON.onePortMaxAdd@</p>', MAX_MAC_RECORD)
            }]
        });
        grid = new Ext.grid.GridPanel({
        	renderTo: 'putGrid',
        	height: 350,
        	border: true,
            stripeRows:true,
            bodyCssClass: "normalTable",
            region: "center",
            store: store,
            cm: cm,
            viewConfig: { forceFit: true }
        });
        
        createPort();
    });//end document.ready;
    function renderManu(v, o, r){
    	var uniId = r.data.uniId
    	return String.format('<a href="javascript:;" onclick="deleteClick(\'{0}\',\'{1}\')">@COMMON.delete@</a>', uniId, v);
    }
    function deleteClick(uniId, mac){ 
    	top.showConfirmDlg('@COMMON.tip@','@onu/ONU.confirmDel2@',function(type){
    		if(type == "no"){
    			return;
    		}
    		$.ajax({
        	    url: '/gpon/unifiltermac/deleteGponUniFilterMac.tv',
        	    data : {
        	    	uniId : uniId,
        	    	mac : V.formatMac(mac)
        	    },
        	    type: 'POST',
        	    dateType: 'json',
        	    success: function(json) {
        	    	store.reload();
        	    	top.afterSaveOrDelete({
        	            title: '@COMMON.tip@',
        	            html: '<b class="orangeTxt">@onu/SERVICE.deleteOk@</b>'
        	        });
        	    },
        	    error: function() {
        	    	window.parent.showMessageDlg("@COMMON.tip@", "@onu/SERVICE.deleteError@");
        	    },
        	    cache: false
        	  }); 
    	});
    }
    function createPort(){
    	$.ajax({
    	    url: '/onu/loadUniList.tv',
    	    data : {
    	    	onuId : window.onuId
    	    },
    	    type: 'POST',
    	    dateType: 'json',
    	    success: function(json) {
    	    	if(json && json.length && json.length > 0){
    	    		var str = '';
    	    		$.each(json, function(i, v){
    	    			str += String.format('<option value="{0}">{1}</option>',v.uniId, v.uniNo);
    	    		});
    	    		$("#portSel").html(str);
    	    		$("#portSel").val(window.uniId);
    	    		//  如果是从设备快照进入页面，则没有传入uniId;
    	    		//  这个时候需要加载完端口号后，重新去加载一下store;
    	    		//  因为只有这个时候，才知道第一个端口号的uniId;
    	    		if(withoutUniId){
    	    			changePortNum();
    	    		}
    	    	}else{
    	    		window.parent.showMessageDlg("@COMMON.tip@", "@GPON.addPortNumFail@");	
    	    	}
    	    },
    	    error: function() {
    	    	window.parent.showMessageDlg("@COMMON.tip@", "@GPON.addPortNumFail@");
    	    },
    	    cache: false
    	  });
    }
    
    function fetchData(){
    	window.top.showWaitingDlg( '@COMMON.waiting@', '@onu/EPON.loading@', 'ext-mb-waiting');
    	$.ajax({
    	    url: '/gpon/unifiltermac/refreshGponUniFilterMac.tv',
    	    data : {
    	    	uniId : $("#portSel").val()
    	    },
    	    type: 'POST',
    	    dateType: 'json',
    	    success: function(json) {
    	    	window.top.closeWaitingDlg();
    	    	store.reload();
    	    	top.afterSaveOrDelete({
    	            title: '@COMMON.tip@',
    	            html: '<b class="orangeTxt">@onu/VLAN.fetchAgainOk@</b>'
    	        });
    	    },
    	    error: function() {
    	    	window.top.closeWaitingDlg();
    	    	window.parent.showMessageDlg("@COMMON.tip@", "@onu/VLAN.fetchAgainError@");
    	    },
    	    cache: false
    	  });
    }
    function showAddMac(){
    	//1个端口下最多16条;
    	if(store.getCount() >= MAX_MAC_RECORD){
    		var tip = String.format("@GPON.onePortMaxAdd@", MAX_MAC_RECORD);
    		window.parent.showMessageDlg("@COMMON.tip@", tip);
    		return;
    	}
    	
    	var $openLayer = $("#openLayer");
    	if( $openLayer.length < 1 ){
    		createAddMacLayer();
    	}else{
    		var portNo = $("#portSel").find("option:selected").text();
    		$("#putUniPort").text(portNo);
    		$("#macInput").val("");
    		openLayer();
    	}
    }
    function openLayer(){
    	$("#openLayer, #openLayerBg").fadeIn();
    }
    function closeLayer(){
    	$("#openLayer, #openLayerBg").fadeOut();
    }
    function createAddMacLayer(){
    	var tpl = new Ext.XTemplate(
	    	'<div id="openLayer">',
			'	<div class="zebraTableCaption">',
		 	'		<div class="zebraTableCaptionTitle"><span class="blueTxt"><label class="blueTxt">@BUTTON.add@</label></span></div>',
			'	     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">',
			'	         <tbody>',
			'	             <tr>',
			'	                <td class="rightBlueTxt" width="150">',
			'	                	@onu/ONU.portNum@@COMMON.maohao@',
			'					</td>',
			'					<td id="putUniPort">{uniNo}</td>',
			'				</tr>',
			'	             <tr class="darkZebraTr">',
			'	                <td class="rightBlueTxt" width="150">',
			'	                	MAC:',
			'					</td>',
			'					<td>',
			'						<input id="macInput" type="text" class="w200 normalInput" toolTip="@onu/ACL.macTip@" />',
			'					</td>',
			'				</tr>',
			'			</tbody>',
			'		</table>',
			'		<div class="noWidthCenterOuter clearBoth">',
			'		     <ol class="upChannelListOl pB0 pT30 noWidthCenter">',
			'		         <li><a href="javascript:;" class="normalBtnBig" id="modifyIpRoute" onclick="addMacClick()"><span><i class="miniIcoAdd"></i>@BUTTON.add@</span></a></li>',
			'		         <li><a href="javascript:;" class="normalBtnBig" onclick="closeLayer()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>',
			'		     </ol>',
			'		</div>',
			'	</div>',
			'</div>',
			'<div id="openLayerBg"></div>'
		);
    	var obj = {
    		uniNo : $("#portSel").find("option:selected").text()
    	}
    	tpl.append(document.body, obj);
    }
    function addMacClick(){
    	var $macInput = $("#macInput"),
    	    macV = $macInput.val(),
    	    $portSel = $("#portSel"),
    	    uniId = $portSel.val();
    	
    	if( !V.isMac(macV) ){
    		$macInput.focus();
    		return;
    	}
    	window.top.showWaitingDlg('@COMMON.waiting@', '@COMMON.saving@', 'ext-mb-waiting');
    	$.ajax({
    	    url: '/gpon/unifiltermac/addGponUniFilterMac.tv',
    	    data : {
    	    	uniId : uniId,
    	    	mac : V.formatMac(macV)
    	    },
    	    type: 'POST',
    	    dateType: 'json',
    	    success: function(json) {
    	    	window.top.closeWaitingDlg();
    	    	closeLayer();
    	    	store.reload();	
    	    	if(json && json.success && json.success=="true"){
    	    		top.afterSaveOrDelete({
    	    	        title: '@COMMON.tip@',
    	    	        html: '<b class="orangeTxt">@onu/EPON.addOk@</b>'
    	    	    });
    	    	}else{
    	    		window.parent.showMessageDlg("@COMMON.tip@", "@onu/EPON.addError@<br />@GPON.canNotAdd@<br />@GPON.canNotAddSameMac@");
    	    	}
    	    },
    	    error: function() {
    	    	window.top.closeWaitingDlg();
    	    	closeLayer();
    	    	window.parent.showMessageDlg("@COMMON.tip@", "@onu/EPON.addError@");
    	    },
    	    cache: false
    	  });
    }
    function changePortNum(){
    	store.baseParams = {
            	uniId : $("#portSel").val()
            };
            store.load();
    }
    function cancelClick(){
    	top.closeWindow("modalDlg");
    }
</script>
</head>
    <body class="openWinBody">
    	<div id="topPart"></div>
    	<div id="putGrid" class="pL5 pR5 pT5"></div>
    	<div id="bottomPart">
    		<div class="noWidthCenterOuter clearBoth">
		        <ol class="upChannelListOl pB0 pT10 noWidthCenter">
		            <li><a onclick="fetchData()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoEquipment"></i>@COMMON.fetch@</span></a></li>
		            <li><a onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
		        </ol>
		    </div>
    	</div>
    </body>
</Zeta:HTML>