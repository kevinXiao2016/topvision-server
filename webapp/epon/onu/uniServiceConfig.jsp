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
	    module onu
	    CSS css/white/disabledStyle
	    IMPORT js/jquery/Nm3kTabBtn
	    IMPORT js.zetaframework.ZetaBindTempldate
</Zeta:Loader>
<style type="text/css">
    body,html{height:100%;}
    #mask{ width:100%; height:100%; overflow:hidden; z-index:99; position:absolute; display:none;}
	#rulerDiv{ position:absolute; top:0px; left:0px; z-index:999; border:1px solid #ccc; background:#F7F7F7; width:480px; height:280px; padding:0px;
	           -webkit-border-radius:3px;
	              -moz-border-radius:3px;
	               -ms-border-radius:3px;
	                -o-border-radius:3px;
	                   border-radius:3px;}
	.scrollBtn{ cursor:pointer;}
	#putAggrInnerGrid{width:100%; clear:both;}
	.dataTable tr.selectedTr{ background: #CCE3FF;}
	.cursorPointer:hover{ background: #fff;}
</style>
	
<script type="text/javascript">
	var clickItem = 0; //点击了顶部第几个选项卡;
	var onuId = "${onuId}";
	var entityId = "${entityId}";
	var translat;
	var zetaDataBind;
	var outDirEnable,outCir,pir,inDirEnable,inCir,cbs,ebs,uniIndex,profileId,bindProfAttr;
	var uniPortString,uniId,uniIndex;
	var vlanMode,profileId;
	var rulerStore;
	var rulerTranslate;//规则列表-转换;
	var rulerAggr;//规则列表-聚合;
	var rulerAggrInner; //规则列表,聚合点击添加后,内部有个规则列表;
	var aggEditStore;
	var rulerTrunk; //规则列表-trunk;
	var updateFlag = false;
	var aggVlanData = {
			aggCvlanArr : [],
			aggSvlanArr : []
		};

	//显示聚合规则的修改
	var updateFlag = false;
	
	function _reset(data){
		uniPortString = data.uniPortVlanPortString;
		uniIndex = data.uniIndex;
		profileId = data.profileId;
		vlanMode = data.vlanMode;
		bindProfAttr = data.bindProfAttr;
		inDirEnable = data.uniPortInRateLimitEnable;
		inCir = data.uniPortInCIR;
		cbs = data.uniPortInCBS;
		ebs = data.uniPortInEBS;
		if (inDirEnable != 1) {
			$('#inCir').attr({disabled:"disabled"});
			$('#cbs').attr({disabled:"disabled"});
			$('#ebs').attr({disabled:"disabled"});
		}else{
			$('#inCir').removeAttr("disabled");
			$('#cbs').removeAttr("disabled");
			$('#ebs').removeAttr("disabled");
		}
		outDirEnable = data.uniPortOutRateLimitEnable;
		outCir = data.uniPortOutCIR;
		pir = data.uniPortOutPIR;
		if (outDirEnable != 1) {
			$('#outCir').attr({disabled:"disabled"});
			$('#pir').attr({disabled:"disabled"});
		}else{
			$('#outCir').removeAttr("disabled");
			$('#pir').removeAttr("disabled");
		}
		zetaDataBind.reset({
			uniId: data.uniId,
			uniIndex: data.uniIndex,
			portEnable :{
				portEnable : data.uniAdminStatus== 1?true:false
			},
			univlan : {
				pvid : data.vlanPVid,
				vlanMode : data.vlanMode,
				//profileId : data.profileId,
				uniPortVlanPortString : data.uniPortVlanPortString
			},
			uniportRate : {
				inDirEnable:data.uniPortInRateLimitEnable == 1?true:false,
				inCir:data.uniPortInCIR,
				cbs:data.uniPortInCBS,
				ebs:data.uniPortInEBS,
				outDirEnable:data.uniPortOutRateLimitEnable== 1?true:false,
				pir:data.uniPortOutPIR,
				outCir:data.uniPortOutCIR
			},
			portWorkMode : {
				autonegotiation: data.uniAutoNegotiationEnable== 1?true:false,
				flowControlEnable: data.flowCtrl== 1?true:false,
				uniAutoType: data.topUniAttrAutoNegotiationAdvertisedTechAbilityInteger == -1?1:data.topUniAttrAutoNegotiationAdvertisedTechAbilityInteger
			},
			untagPri : {
				priority : data.uniUSUtgPri
			},
			macLearn : {
				macLearnMaxNum : data.macLearnMaxNum
			},
			perfManage : {
				perfEnable:data.perfStats15minuteEnable == 1?true:false
			}
		});
	}
	function registerBind(){
		window.zetaDataBind = new ZetaBindTempldate({
			portEnable :{
				title : "@ONU.portEnable@",
				url : "/onu/setPortEnabled.tv"
			},
			univlan : {
				title : "@ONU.uniVlanInfo@",
				url : "/onu/setUniVlan.tv",
				validate : function(data){
					if(!V.isInRange(data.pvid,[1,4094])){
						$("#pvid").focus();
						return false;
					}
					return true;
				}
			},
			uniportRate : {
				title:"@ONU.uniSpeedDec@",
				url:"/onu/setPortRateLimit.tv",
				validate : function(data){
					 if(data.inDirEnable){
						if(!V.isInRange(data.inCir, [1, 1000000])){
							$("#inCir").focus();
							return false;
						}
						if(!V.isInRange(data.cbs,[0, 16383])){
							$("#cbs").focus();
							return false;
						}
						if(!V.isInRange(data.ebs, [0, 16383])){
							$("#ebs").focus();
							return false;
						}
					 }
					 if(data.outDirEnable){
						 if(!V.isInRange(data.outCir, [0, 1000000])){
							 $("#outCir").focus();
							 return false;
						 }
						 if(!V.isInRange(data.pir, [0, 1000000])){
							 $("#pir").focus();
							 return false;
						 }
						 if(parseInt(data.pir) < parseInt(data.outCir)){
							 $("#pir").focus();
							 return false;
						 }
					 }
					 return true;
				}
			},
			portWorkMode : {
				title:"@ONU.workMode@",
				url:"/onu/setPortWorkMode.tv"
			},
			untagPri : {
				title: "@ONU.uniUSUtgPriSimple@",
				url:"/onu/setUniUSUtgPri.tv"
			},
			macLearn : {
				title:"@ONU.macLearn@",
				url:"/onu/setUniMacLearnNum.tv",
				validate :function(data){
					if(!V.isInRange(data.macLearnMaxNum,[0,64])){
						$("#macLearnMaxNum").focus();
						return false;
					}
					return true;
				}
			},
			perfManage : {
				title:"@ONU.stasitcEnable@",
				url:"/onu/setUniPerfStatus.tv"
			}
		});
		var uniList = ${uniList};
			
			var html =  '<div class="edge10">';
				html += 	'<p class="flagP"><span class="flagInfo">@ONU.uniConfig@</span></p>';
				html +=     '<table class="dataTable zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">';
				html +=         '<thead><tr><th>@COMMON.port@</th><th>PVID</th><th>@univlanprofile/PROFILE.vlanMode@</th></tr></thead>';
				html +=         '<tbody>';
				$.each(uniList,function(i,v){
					var mode = $("#vlanMode option[value="+(v.vlanMode == 0 ?v.vlanMode: (v.vlanMode))+"]").text();
					html +=             String.format('<tr class="cursorPointer" onclick="showUniServiceConfig({3},this,{0});"><td align="center">{0}</td><td align="center">{1}</td><td align="center">{2}</td></tr>', v.uniNo, v.bindPvid, mode, v.uniId
);
		});
				html +=         '</tbody>';
				html +=     '</table>';
				html += '</div>'
			$('#putTree').html(html)
			$("#putTree tbody tr:eq(0)").trigger('click');
		}
			 
		function showUniServiceConfig(uniId,element,uniNo){
			 $('#putTree tr').removeClass('selectedTr');
			 $(element).addClass("selectedTr");
			 
			 $.ajax({
				url:"/onu/loadUniServiceConfig.tv",
				cache:false,dataType:'json',
				data: {
					onuId:onuId, 
					uniId:uniId,
					entityId:entityId
				},
				success:function(data){
					_reset(data);
					enableClick();
					vlanProfile(data.profileId,data.profileName);
					WIN.uniNo = uniNo
					WIN.uniId = uniId;
					changeMode();
					changeMode2();
				}
			 });
		 }	 
		 
		 $(function(){
			 registerBind();
			 var aSrc = window.location.href.split("?");
			 if(aSrc.length == 2){
				var pro = aSrc[1];
				if(pro.indexOf('&') != -1){ //有&符号;
					var andArr = pro.split("&");
					$.each(andArr, function(n, v){
						if(/^clickItem=/.test(v)){
							v = v.replace(/^clickItem=/,'');
							window.clickItem = v;
						}
					});
				}
			 }
			 
			 var tab1 = new Nm3kTabBtn({
				    renderTo : "putTabBtn",
				    callBack : "tabFn",
				    selectedIndex : window.clickItem,
				    tabs     : ["@COMMON.all@","@ONU.portEnable@", "@ONU.uniVlanInfo@", "@ONU.uniSpeedDec@", "@ONU.workMode@", "@ONU.uniUSUtgPriSimple@", "@ONU.macLearn@", "@ONU.stasitcEnable@"]
				 });
			 tab1.init();
			 if(window.clickItem > 0){
				 tabFn(window.clickItem);
			 }
			 $('#putTabBtn a:eq(2)').trigger('click');
			 
			 var tb = new Ext.Toolbar({
				    style   : 'z-index:9999; position:relative;', //由于nm3kToolTip的层级特别高，toolbar需要盖住nm3kToolTip,所以z-index必须更高;
			        region  : 'north', 
			        height  : 35,
			        items   : [{
			        	cls     : 'mL10 mR5',
			        	iconCls : 'bmenu_data',
			        	text    : '@BUTTON.save@',
			        	handler : saveClick
			        },'-',{
			        	cls     : 'mL5 mR5',
			        	iconCls : 'bmenu_equipment',
			        	text    : '@BUTTON.fetch@',
			        	handler : refreshClick
			        },'-',{
			        	cls     : 'mL5',
			        	iconCls : 'bmenu_positon', 
			        	text    : '@ONU.applyToOtherUni@',
			        	handler : applyToOtherPorts
			        }/* ,'-',{
			        	cls     : 'mL5',
			        	iconCls : 'bmenu_compare', 
			        	text    : 'applyToOtherOnu',
			        	handler : applyToOtherOnu
			        } */]
			 });
			 
			 new Ext.Viewport({
				 cls      : 'viewPortWithSplit',
				 layout   : 'border',
				 defaults : {
					 border : false 
				 },
				 items  : [ tb,
				 {
					 width      : 200,
					 split      : true,
					 region     : 'west',
					 minWidth   : 200,
					 maxWidth   : 350,
					 contentEl  : 'putTree',
					 autoScroll : true
				 },{
					 id         : 'centerComponent',
					 region     : 'center',
					 contentEl  : 'centerContainer',
					 autoScroll : true,
					 listeners  : {
						 resize : function(){
							 upDateToolTip();
						 }
					 } 
				 }],
				 listeners : {
					 afterRender : function(){
						 Ext.getCmp('centerComponent').body.on('scroll',function(e){
							 upDateToolTip();
						 })
					 }
				 }
			 });
			 function upDateToolTip(){
				 //var aPos = Ext.getCmp('centerComponent').body.getScroll(),
			     var $normalInputFocus = $(".normalInputFocus"),
			         $nm3kToolTip = $("#nm3kToolTip");
				 
			     $nm3kToolTip.css({zIndex: 9998}); //因为tbar是9999，不能高于tbar;
				 if( $nm3kToolTip.length ==1  && $nm3kToolTip.is(":visible") && $(".normalInputFocus").length == 1){
					var leftPos = $normalInputFocus.offset().left;
					var h = $("#nm3kToolTip").height();
					var topPos = $normalInputFocus.offset().top - h;
					$("#nm3kToolTip").css({"left":leftPos, "top":topPos});
				 }else{
					 $nm3kToolTip.css({display: 'none'});
				 }
			 }
			 
			 $(".scrollBtn").click(function(){
				 var $me = $(this),
				     id = $me.attr("id"),
				     alt = $me.attr("alt");
				 if(alt === "on"){
					 $me.attr({src : '../../images/speOff.png', alt : 'off'});
				 }else if(alt === "off"){
					 $me.attr({src : '../../images/speOn.png', alt : 'on'});
				 }
				 if(id =='autonegotiation' || id == 'flowControlEnable'){
					 enableClick();
				 }
			 })
			var inEnable = '${oltUniPortRateLimit.uniPortInRateLimitEnable}';
			if (inEnable != 1) {
				$('#inCIR').attr({disabled:"disabled"});
				$('#inCBS').attr({disabled:"disabled"});
				$('#inEBS').attr({disabled:"disabled"});
			}
		var outEnable = '${oltUniPortRateLimit.uniPortOutRateLimitEnable}';
		if (outEnable != 1) {
			$('#outCIR').attr({disabled:"disabled"});
			$('#outPIR').attr({disabled:"disabled"});
		}
			 
		 });//end document.ready;
		 function getRulerStore(modeUpdate){
			 if(modeUpdate == 2){
				rulerStore = new Ext.data.JsonStore({
					url : '/epon/uniportvlan/loadTranslationRuleList.tv',
					fields : ["vlanIndex","translationNewVid"],
					baseParams : {
						entityId : entityId,
						uniIndex : uniIndex,
						uniId : uniId
					}
				}); 
			 }
			 if(modeUpdate == 3){
				 rulerStore = new Ext.data.JsonStore({
					url : '/epon/uniportvlan/loadAggregationRule.tv',
					fields : ["aggregationVidListAfterSwitch", "portAggregationVidIndex"],
					baseParams : {
						entityId : entityId,
						uniIndex : uniIndex,
						uniId : uniId
					}
				});
			 }
 			 if(modeUpdate == 4){
 				rulerStore = new Ext.data.JsonStore({
 					url : '/epon/uniportvlan/loadTrunkRuleList.tv',
 					fields : ["trunkId"],
 					baseParams : {
 						entityId : entityId,
 						uniIndex : uniIndex,
 						uniId : uniId
 					}
 				});
			 }
			rulerStore.load();
		 }
		 //点击顶部，切换显示单个div;
		 function tabFn(index){
			 window.clickItem = index;
			 switch(index){
				case 0:
					$(".jsTabBody").css("display","block");
					break;
				default:
					$(".jsTabBody").css("display","none");
					$(".jsTabBody").eq(index-1).fadeIn();
					break;
			}
		 }
		 
		 function reload(){
			//window.clickItem
			var index = location.href.lastIndexOf("&uniId");
      		if(index == -1){
	      		location.href= location.href+"&uniId="+uniId + "&clickItem=" + window.clickItem;
      		}else{
      			location.href= location.href.substring(0,index)+"&uniId="+uniId + "&clickItem=" + window.clickItem;
      		}
		 }
		 
		 //切换模式;
		 function changeMode(){
			 var modeUpdate = $("#vlanMode").val(),
			 	 pvid = $("#pvid").val(),
			     $showProfile = $("#showProfile"),
			     $putRulerTranslate = $("#putRulerTranslate");
			 
			 if(vlanMode != modeUpdate){
				 window.parent.showConfirmDlg("@COMMON.tip@", "@ONU.changeVlanMode@", function(type) {
						if (type == 'no') {//点击否，退回原来的模式;
							$("#vlanMode").val(vlanMode);
							return;
						}
						window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
						$.ajax({
							url : '/epon/uniportvlan/modifyVlanMode.tv',
							data : {
								entityId : entityId,
								uniIndex : uniIndex,
								uniId : uniId,
								uniPvid : pvid,
								vlanMode : modeUpdate
							},
							dataType : 'json',
							success : function(result) {
								if(result.success){
									top.afterSaveOrDelete({
					       				title: "@COMMON.tip@",
					       				html: '<b class="orangeTxt">@ONU.changeVlanSuccess@</b>'
					       			});
									reload();
								}else{
									window.parent.showMessageDlg("@COMMON.tip@", "@ONU.changeVlanFail@");
								}
							},
							error : function(json) {
								window.parent.showMessageDlg("@COMMON.tip@", "@ONU.changeVlanFail@");
							},
							cache : false
						});
					});
			 };//end if;
			 
		 }
	function changeMode2(){
		$('#aggTip').hide();
		var modeUpdate = $("#vlanMode").val(),
	 	 pvid = $("#pvid").val(),
	     $showProfile = $("#showProfile"),
	     $putRulerTranslate = $("#putRulerTranslate");
		
		if(rulerTranslate){//如果有转换的规则列表(case 2里面那个grid);
			rulerTranslate.destroy();
			rulerTranslate = null;
		 }
		 if(rulerAggr){
			 rulerAggr.destroy();
			 rulerAggr = null;
		 }
		 if(rulerAggrInner){
			 rulerAggrInner.destroy();
			 rulerAggrInner = null;
		 }
		 if(rulerTrunk){
			 rulerTrunk.destroy();
			 rulerTrunk = null;
		 }
		 $putRulerTranslate.empty();
		 switch(modeUpdate){
			 case '0':
			 case '1':
				 $showProfile.text('@univlanprofile/UNIVLAN.unRelated@');
				 $putRulerTranslate.empty();
				 rulerTranslate = null;
				 break;
			 case '2':
					if(!rulerTranslate){//如果没有规则列表,则创建一个gridpanel;
						 getRulerStore(modeUpdate);
						 rulerTranslate = new Ext.grid.GridPanel({
							bbar    : [{
								text    : '@univlanprofile/RULE.addRule@',
								iconCls : 'bmenu_new',
								handler : function(){
									addRuler('transparent');
								}
							}],
							cls     : 'normalTable',
							store   : rulerStore,
							title   : "@univlanprofile/RULE.ruleList@",
							height  : 200,
							border  : true,
							colModel : new Ext.grid.ColumnModel([
							    {header : "@univlanprofile/RULE.originalId@", align : 'center', dataIndex : 'vlanIndex'},
							    {header : "@univlanprofile/RULE.targetId@", align : 'center', dataIndex : 'translationNewVid'},
							    {header : "@COMMON.manu@", width:160, fixed:true, align : 'center',dataIndex :'op',  renderer : opeartionRender}
							]),
							viewConfig : {
								forceFit : true
							},
							renderTo : 'putRulerTranslate'
						});		
					}else{//如果已经有规则列表，则load一下rulerStore;
						
					}
					break;
			 case '3': //聚合的规则列表;
					if(!rulerAggr){
						getRulerStore(modeUpdate);
						rulerAggr = new Ext.grid.GridPanel({
							bbar    : [{
								text    : '@univlanprofile/RULE.addRule@',
								iconCls : 'bmenu_new',
								handler : function(){
									addRuler('aggr');
								}
							}],
							cls     : 'normalTable',
							store   : rulerStore,
							title   : "@univlanprofile/RULE.ruleList@",
							height  : 200,
							border  : true,
							colModel : new Ext.grid.ColumnModel([
							    {header : "@univlanprofile/RULE.originalAgg@", align : 'center', dataIndex : 'aggregationVidListAfterSwitch'},
	 							{header : "@univlanprofile/RULE.targetAgg@", align : 'center', dataIndex : 'portAggregationVidIndex'},
	 							{header : "@COMMON.manu@", width:160, fixed:true, align : 'center',dataIndex :'op', renderer : aggrOpeartionRender}
							]),
							viewConfig : {
								forceFit : true
							},
							renderTo : 'putRulerTranslate'
						});
						$('#aggTip').show();
					}
					break;
			 case '4'://trunk 模式;
					if(!rulerTrunk){
						getRulerStore(modeUpdate);
						rulerTrunk = new Ext.grid.GridPanel({
							bbar    : [{
								text    : '@univlanprofile/RULE.addRule@',
								iconCls : 'bmenu_new',
								handler : function(){
									addRuler('trunk');
								}
							}],
							cls     : 'normalTable',
							store   : rulerStore,
							title   : "@univlanprofile/RULE.ruleList@",
							height  : 200,
							border  : true,
							colModel : new Ext.grid.ColumnModel([
								{header : "Trunk Id", align : 'center', dataIndex : 'trunkId'}, 
								{header : "@COMMON.manu@", width:200, fixed:true, align : 'center',dataIndex :'op', renderer : trunkOpeartionRender}
							]),
							viewConfig : {
								forceFit : true
							},
							renderTo : 'putRulerTranslate'
						});	
					}
				 break;
		 }
	}
	function aggrOpeartionRender(value, cellmate, record){
		var aggrSvid = record.data.portAggregationVidIndex;
		return String.format("<a href='javascript:;' onClick='deleteAggRule({0})'>@COMMON.delete@</a> / <a href='javascript:;' onClick='addRuler(\"aggrEdit\",{0})'>@COMMON.modify@</a>",aggrSvid);
	}
	function trunkOpeartionRender(value, cellmate, record){
		var trunkId = record.data.trunkId;
		return String.format("<a href='javascript:;' onClick='deleteTrunkRule({0})'>@COMMON.delete@</a>",trunkId); 
	}
	//删除trunk规则
	function deleteTrunkRule(trunkId){
		window.parent.showConfirmDlg("@COMMON.tip@", "@univlanprofile/RULE.deleteRule@", function(type) {
			if (type == 'no') {
				return;
			}
			var trunkData = getStoreData();
			trunkData.remove(trunkId);
			window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
			$.ajax({
				url : '/epon/uniportvlan/deleteTrunkRule.tv',
				type : 'POST',
				data : {
					entityId : entityId,
					uniIndex : uniIndex,
					uniId : uniId,
					trunkList : trunkData.join(",")
				},
				dataType :　'json',
				success : function(result) {
					if(result.success){
						top.afterSaveOrDelete({
		       				title: "@COMMON.tip@",
		       				html: '<b class="orangeTxt">@univlanprofile/PROFILE.deleteSuccess@</b>'
		       			});
						rulerStore.reload();
					}else{
						window.parent.showMessageDlg("@COMMON.tip@", "@univlanprofile/PROFILE.deleteFailed@");
					}
				},
				error : function(json) {
					window.parent.showMessageDlg("@COMMON.tip@", "@univlanprofile/PROFILE.deleteFailed@");
				},
				cache : false
			});
		});
	}
	//删除AGG规则
	function deleteAggRule(aggrSvid){
		window.parent.showConfirmDlg("@COMMON.tip@", "@univlanprofile/RULE.deleteRule@", function(type) {
			if (type == 'no') {
				return;
			}
			window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
			$.ajax({
				url : '/epon/uniportvlan/deleteAggregationRule.tv',
				type : 'POST',
				data : {
					entityId : entityId,
					uniIndex : uniIndex,
					uniId : uniId,
					aggrSvid : aggrSvid
				},
				dataType :　'json',
				success : function(result) {
					if(result.success){
						top.afterSaveOrDelete({
		       				title: "@COMMON.tip@",
		       				html: '<b class="orangeTxt">@univlanprofile/PROFILE.deleteSuccess@</b>'
		       			});
						rulerStore.reload();
					}else{
						window.parent.showMessageDlg("@COMMON.tip@", "@univlanprofile/PROFILE.deleteFailed@");
					}
				},
				error : function(json) {
					window.parent.showMessageDlg("@COMMON.tip@", "@univlanprofile/PROFILE.deleteFailed@");
				},
				cache : false
			});
		});
	}
	function opeartionRender(value, cellmate, record){
			var vlanIndex = record.data.vlanIndex;
			return String.format("<a href='javascript:;' onClick='deleteTranslateRule({0})'>@COMMON.delete@</a>",vlanIndex); 
	}
	function deleteTranslateRule(vlanIndex){
		window.parent.showConfirmDlg("@COMMON.tip@", "@univlanprofile/RULE.deleteRule@", function(type) {
			if (type == 'no') {
				return;
			}
			window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
			$.ajax({
				url : '/epon/uniportvlan/deleteTranslationRule.tv',
				type : 'POST',
				data : {
					entityId : entityId,
					uniIndex : uniIndex,
					uniId : uniId,
					vlanIndex : vlanIndex
				},
				dataType :　'json',
				success : function(result) {
					if(result.success){
						top.afterSaveOrDelete({
		       				title: "@COMMON.tip@",
		       				html: '<b class="orangeTxt">@univlanprofile/PROFILE.deleteSuccess@</b>'
		       			});
						rulerStore.reload();
					}else{
						window.parent.showMessageDlg("@COMMON.tip@", "@univlanprofile/PROFILE.deleteFailed@");
					}
				},
				error : function(json) {
					window.parent.showMessageDlg("@COMMON.tip@", "@univlanprofile/PROFILE.deleteFailed@");
				},
				cache : false
			});
		});
	}
	//添加规则,通常只要一个参数,编辑聚合规则的时候,会传递第二个参数;
	function addRuler(para){
		var $rulerDiv = $("#rulerDiv");
		if($rulerDiv.length === 0){
			$('body').append('<div id="rulerDiv"></div>');
		}else{
			$rulerDiv.empty().show();
		}
		switch(para){
		case 'transparent':
			//转换规则最多8条;
			if(rulerStore.getCount() < 8){
				$("#rulerDiv").css({width:480, height:280});
				addTransRuler();
			}else{
				$("#rulerDiv").css({display: 'none'});
				window.top.showMessageDlg("@COMMON.tip@", "@univlanprofile/RULE.totalLimit@");
			}
			break;
		case 'aggr':
			$("#rulerDiv").css({width:800, height:460});
			addAggrRuler();
			break;
		case 'aggrEdit':
			$("#rulerDiv").css({width:800, height:460});
			editAggrRule(arguments[1]);
			break;
		case 'trunk':
			if(rulerStore.getCount() < 8){
				$("#rulerDiv").css({width:480, height:280});
				addTrunkRuler();
			}else{
				$("#rulerDiv").css({display: 'none'});
				window.top.showMessageDlg("@COMMON.tip@", "@univlanprofile/RULE.totalLimit@");
			}
			break;
		}
		
	}
	//添加转换的规则;
	function addTransRuler(){
		var tpl = new Ext.XTemplate([
		    '<div class="rulerTitle"><b>@univlanprofile/RULE.addRule@</b><label onclick="closeRulerDiv()"></label></div>',                         
			'<div class="edge10 pT40">',
				'<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">',
				    '<tbody>',
				        '<tr>',
				            '<td class="rightBlueTxt" width="150">@univlanprofile/RULE.originalId@:</td>',
				            '<td><input type="text" class="normalInput w180" maxlength="4" tooltip="@univlanprofile/RULE.vlanInputTip@" id="beforeVlan" /></td>',
				        '</tr>',
				        '<tr class="darkZebraTr">',
					        '<td class="rightBlueTxt">@univlanprofile/RULE.targetId@:</td>',
				            '<td><input type="text" class="normalInput w180" maxlength="4" tooltip="@univlanprofile/RULE.vlanInputTip@" id="afterVlan" /></td>',
				        '</tr>',
				    '</tbody>',
				'</table>',
				'<div class="noWidthCenterOuter clearBoth">',
			        '<ol class="upChannelListOl pB0 pT40 noWidthCenter">',
			            '<li><a href="javascript:;" class="normalBtnBig" onclick="saveTransRuler()"><span><i class="miniIcoAdd"></i>@BUTTON.add@</span></a></li>',
			            '<li><a href="javascript:;" class="normalBtnBig" onclick="closeRulerDiv()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>',
			        '</ol>',
			    '</div>',
			'</div>'
		]);
		var $rulerDiv = Ext.get("rulerDiv");
		tpl.overwrite($rulerDiv, {});
		$rulerDiv.center();
		$("#mask").css({display:'block'});
	}
	//添加聚合的规则;
	function addAggrRuler(){
		if(rulerStore.getCount() < 8){
			var tpl = new Ext.XTemplate([
       			'<div class="rulerTitle"><b>@univlanprofile/RULE.addRule@</b><label onclick="closeRulerDiv()"></label></div>',                         
       			'<div class="edge10 pT10">',
       				'<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">',
       				    '<tbody>',
       				        '<tr class="withoutBorderBottom">',
       				            '<td width="130" class="rightBlueTxt">@univlanprofile/RULE.originalAgg@</td>',
       				            '<td><input type="text" id="cVlanInput" class="normalInput w180" toolTip="@univlanprofile/RUlE.batchAddTip@"/></td>',
       					        '<td width="130" class="rightBlueTxt">@univlanprofile/RULE.targetAgg@:</td>',
       				            '<td><input type="text" id="sVlanInput" class="normalInput w180" toolTip="@univlanprofile/RULE.vlanInputTip@"/></td>',
       				            '<td class=""><a href="javascript:;" class="normalBtn" onclick="addAggRule()"> <span><i class="miniIcoAdd"></i>@BUTTON.add@</span></a></td>',
       				        '</tr>',
       				    '</tbody>',
       				'</table>',
       				'<div id="putAggrInnerGrid"></div>',
       	   			'<div class="noWidthCenterOuter clearBoth">',
       	   		        '<ol class="upChannelListOl pB0 pT10 noWidthCenter">',
       	   		            '<li><a href="javascript:;" class="normalBtnBig" onclick="saveAggrRuler()"><span><i class="miniIcoAdd"></i>@BUTTON.add@</span></a></li>',
       	   		            '<li><a href="javascript:;" class="normalBtnBig" onclick="closeRulerDiv()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>',
       	   		        '</ol>',
       	   		    '</div>',
       	   		'</div>'
          		]);
       		var $rulerDiv = Ext.get("rulerDiv");
       		tpl.overwrite($rulerDiv, {});
       		$rulerDiv.center();
       		$("#mask").css({display:'block'});
       		
       		var rulerAggrInnerCm = new Ext.grid.ColumnModel([ 
            	   	{header : "@univlanprofile/RULE.originalAgg@", width : 120, align : 'center',dataIndex : 'ruleCvlan'}, 
              	 	{header : "@univlanprofile/RULE.targetAgg@", width : 120, align : 'center',dataIndex : 'ruleSvlan'}, 
              	 	{header : "@COMMON.manu@", width : 100, align : 'center',dataIndex :'op', renderer : function(){
              	 		return String.format("<a href='javascript:;' onClick='deleteAggraInnerRule()'>@COMMON.delete@</a>");
              	 	}}
       		]);
       		
       		//规则添加相关
       		aggEditStore = new Ext.data.JsonStore({
       			data : [],
       			fields : ["ruleCvlan", "ruleSvlan"]
       		});
       		
       		rulerAggrInner =  new Ext.grid.GridPanel({
       			title : "@univlanprofile/RUlE.aggDataList@",
       			height : 320,
       			border : true,
       			cls : 'normalTable',
       			store : aggEditStore,
       			colModel : rulerAggrInnerCm,
       			viewConfig : {
       				forceFit : true
       			},
       			renderTo : 'putAggrInnerGrid',
       			sm : new Ext.grid.RowSelectionModel({
       				singleSelect : true
       			})
       		});
			//记录已经配置的聚合VLAN数据
			collectAggVlanData();
		}else{
			window.top.showMessageDlg("@COMMON.tip@", "@univlanprofile/RULE.totalAggLimit@");
			return;
		}
		
	}
	//修改聚合的规则;
	function editAggrRule(num){
		var tpl = new Ext.XTemplate([
   			'<div class="rulerTitle"><b>@ONU.modifyRule@</b><label onclick="closeRulerDiv()"></label></div>',                         
   			'<div class="edge10 pT10">',
   				'<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">',
   				    '<tbody>',
   				        '<tr class="withoutBorderBottom">',
   				            '<td width="130" class="rightBlueTxt">@univlanprofile/RULE.originalAgg@</td>',
   				            '<td><input type="text" id="cVlanInput" class="normalInput w180" toolTip="@univlanprofile/RUlE.batchAddTip@"/></td>',
   					        '<td width="140" class="rightBlueTxt">@univlanprofile/RULE.targetAgg@:</td>',
   				            '<td><input type="text" id="sVlanInput" disabled="disabled" class="normalInput w180" toolTip="@univlanprofile/RULE.vlanInputTip@" value="{afterNum}" /></td>',
   				         	'<td class=""><a href="javascript:;" class="normalBtn" onclick="addAggRule()"> <span><i class="miniIcoAdd"></i>@BUTTON.add@</span></a></td>',
   				        '</tr>',
   				    '</tbody>',
   				'</table>',
   				'<div id="putAggrInnerGrid"></div>',
   	   			'<div class="noWidthCenterOuter clearBoth">',
   	   		        '<ol class="upChannelListOl pB0 pT10 noWidthCenter">',
			   	   		  '<li><a href="javascript:;" class="normalBtnBig" onclick="saveAggrRuler()"><span><i class="miniIcoAdd"></i>@BUTTON.save@</span></a></li>',
				          '<li><a href="javascript:;" class="normalBtnBig" onclick="closeRulerDiv()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>',
   	   		        '</ol>',
   	   		    '</div>',
   	   		'</div>'
      		]);
   		var $rulerDiv = Ext.get("rulerDiv");
   		tpl.overwrite($rulerDiv, {afterNum:num});
   		$rulerDiv.center();
   		$("#mask").css({display:'block'});
   		
   		var rulerAggrInnerCm = new Ext.grid.ColumnModel([ 
        	   	{header : "@univlanprofile/RULE.originalAgg@", width : 120, align : 'center',dataIndex : 'ruleCvlan'}, 
          	 	{header : "@univlanprofile/RULE.targetAgg@", width : 120, align : 'center',dataIndex : 'ruleSvlan'}, 
          	 	{header : "@COMMON.manu@", width : 100, align : 'center',dataIndex :'op', renderer : function(){
          	 		return String.format("<a href='javascript:;' onClick='deleteAggraInnerRule()'>@COMMON.delete@</a>");
          	 	}}
   		]);
   		
   		//规则添加相关
		aggEditStore = new Ext.data.JsonStore({
			data : [],
			fields : ["ruleCvlan", "ruleSvlan"]
		});
   		
   		rulerAggrInner =  new Ext.grid.GridPanel({
   			title : "@univlanprofile/RUlE.aggDataList@",
   			height : 320,
   			border : true,
   			cls : 'normalTable',
   			store : aggEditStore,
   			colModel : rulerAggrInnerCm,
   			viewConfig : {
   				forceFit : true
   			},
   			renderTo : 'putAggrInnerGrid',
   			sm : new Ext.grid.RowSelectionModel({
   				singleSelect : true
   			})
   		});
   		
   		//获取要修改的数据
		var selectedAgg = rulerAggr.getSelectionModel().getSelected();
		var aggVidList = selectedAgg.data.aggregationVidListAfterSwitch;
		var aggVidIndex = selectedAgg.data.portAggregationVidIndex;
		var aggRuleArr = [];
		$.each(aggVidList, function(index, aggVid){
			var rule = {ruleCvlan:aggVid, ruleSvlan:aggVidIndex};
			aggRuleArr.push(rule);
		})
		//显示修改页面
		$('#cVlanInput').val("");
		$('#sVlanInput').val(aggVidIndex).attr("disabled", true);
		//加载要修改的数据
		aggEditStore.loadData(aggRuleArr);
		//记录已经配置的聚合VLAN数据
		collectAggVlanData();
		//标记操作为修改
		updateFlag = true;
	}
	
	//添加trunk的规则;
	function addTrunkRuler(){
		var tpl = new Ext.XTemplate([
			'<div class="rulerTitle"><b>@univlanprofile/RULE.addRule@</b><label onclick="closeRulerDiv()"></label></div>',                         
			'<div class="edge10 pT60">',
				'<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">',
				    '<tbody>',
				        '<tr class="withoutBorderBottom">',
				            '<td class="rightBlueTxt" width="150">Trunk ID:</td>',
				            '<td><input type="text" id="trunkId" class="normalInput w180" maxlength="4" tooltip="@univlanprofile/RULE.vlanInputTip@"></td>',
				        '</tr>',
				    '</tbody>',
				'</table>',
      			'<div class="noWidthCenterOuter clearBoth">',
      		        '<ol class="upChannelListOl pB0 pT40 noWidthCenter">',
      		            '<li><a href="javascript:;" class="normalBtnBig" onclick="saveTrunkRuler()"><span><i class="miniIcoAdd"></i>@BUTTON.add@</span></a></li>',
      		            '<li><a href="javascript:;" class="normalBtnBig" onclick="closeRulerDiv()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>',
      		        '</ol>',
      		     '</div>',
      		 '</div>'
      		]);
		var $rulerDiv = Ext.get("rulerDiv");
		tpl.overwrite($rulerDiv, {});
		$rulerDiv.center();
		$("#mask").css({display:'block'});
	}
	
	//输入校验: 输入的整数否在范围内
	function checkInput(value,compareStart,compareEnd){
		var reg = /^[1-9]\d*$/;	
		if (reg.exec(value) && parseInt(value) <= compareEnd && parseInt(value) >= compareStart) {
			return true;
		} else {
			return false;
		}
	}
	
	//向grid中添加聚合规则
	function addAggRule(){
		var cVlanData = $('#cVlanInput').val().trim();
		var sVlanData = $('#sVlanInput').val().trim();
		if(aggEditStore.getCount() < 8){
			if(!checkCvlanInput(cVlanData)){
				$('#cVlanInput').focus();
				return;
			}
			var cvlanList = changeToArray(cVlanData);
			if(!checkInput(sVlanData,1,4094)){
				$('#sVlanInput').focus();
				return;
			}
			if(aggEditStore.getCount() + cvlanList.length > 8){
				window.top.showMessageDlg("@COMMON.tip@", "@univlanprofile/RULE.totalAggLimit@");
				return;
			}
			//先判断Vlan id在其它规则里有没有出现过
			var cVlanRepeat = false;
			var existsCvlan = aggVlanData.aggCvlanArr;
			$.each(cvlanList,function(index, newCvlan){
				if($.inArray(newCvlan, existsCvlan) > -1){
					cVlanRepeat = true;
					return false;
				}
			});
			//原VLAN　ID不能在其它规则中出现
			if(cVlanRepeat){
				window.top.showMessageDlg("@COMMON.tip@", "@univlanprofile/RULE.originalAggUsed@",null,function(){
					$('#cVlanInput').focus();
				});
				return;
			}
			//添加时还需要校验SVLAN
			if(!updateFlag){
				var sVlanRepeat = false;
				var existsSvlan = aggVlanData.aggSvlanArr;
				//防止string类型与Number类型的比较
				var svlanValue = parseInt(sVlanData, 10);
				if($.inArray(svlanValue, existsSvlan) > -1){
					sVlanRepeat = true;
				}
				//转换后VLAN ID不能在其它规则中出现
				if(sVlanRepeat){
					window.top.showMessageDlg("@COMMON.tip@", "@univlanprofile/RULE.targetAggUsed@",null,function(){
						$('#sVlanInput').focus();
					});
					return;
				}
			}
			var addRecord = new Array();
			$.each(cvlanList,function(i,item){
				var toAdd = {ruleCvlan:item, ruleSvlan:sVlanData};
				addRecord.push(toAdd);
				//记录新添加的aggCvlan
				aggVlanData.aggCvlanArr.push(item);
			});
			aggEditStore.loadData(addRecord,true);
			//添加成功后将cVlan输入框置空
			$('#cVlanInput').val("").focus();
			if(!updateFlag){
				//添加成功后将sVlan输入置为不可用
				$('#sVlanInput').attr("disabled", true);
			}
		}else{
			window.top.showMessageDlg("@COMMON.tip@", "@univlanprofile/RULE.totalAggLimit@");
			return;
		}
	}
	
	//删除添加聚合的列表的规则;
	function deleteAggraInnerRule(){
		var select = rulerAggrInner.getSelectionModel().getSelected();
		aggEditStore.remove(select);
		//删除配置规则时从记录中删除cvlan
		aggVlanData.aggCvlanArr.remove(select.data.ruleCvlan);
		if(!updateFlag && aggEditStore.getCount() == 0){
			$('#sVlanInput').attr("disabled", false);
		}
	}
	
	function checkCvlanInput(v){
		var reg = /^([0-9]{1,4}[,-]{0,1})+$/;
	    if (reg.exec(v)) {
	    	var tmp = v.replace(new RegExp('-', 'g'), ',');
	    	var tmpA = tmp.split(',');
	    	var tmpAl = tmpA.length;
			for(var i=0; i<tmpAl; i++){
				if(parseInt(tmpA[i]) > 4094 || parseInt(tmpA[i]) < 1){
					return false;
				}
			}
	    	return true;
	    }
	    return false;
	}
	
	//添加聚合的规则 点击保存按钮;
	function saveAggrRuler(){
		if(aggEditStore.getCount() > 0){
			var cVlan = new Array();
			var sVlan;
			aggEditStore.each(function(rec){
				cVlan.push(rec.data.ruleCvlan);
				sVlan = rec.data.ruleSvlan;
			});
			var actionUrl = '/epon/uniportvlan/addAggregationRule.tv';
			if(updateFlag){
				actionUrl = '/epon/uniportvlan/modifyAggregationRule.tv';
			}
			window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
			$.ajax({
				url : actionUrl,
				type : 'POST',
				data : {
					entityId : entityId,
					uniIndex : uniIndex,
					uniId : uniId,
					aggrCvids : cVlan.join(","),
					aggrSvid : sVlan
				},
				dataType :　'json',
				success : function(result) {
					if(result.success){
						top.afterSaveOrDelete({
		       				title: "@COMMON.tip@",
		       				html: '<b class="orangeTxt">@univlanprofile/RULE.saveSuccess@</b>'
		       			});
						rulerStore.reload();
						closeRulerDiv();
					}else{
						window.top.showMessageDlg("@COMMON.tip@", "@univlanprofile/RULE.saveFailed@");
					}
				},
				error : function() {
					window.top.showMessageDlg("@COMMON.tip@", "@univlanprofile/RULE.saveFailed@");
				},
				cache : false
			}); 
		}else{
			window.top.showMessageDlg("@COMMON.tip@", "@univlanprofile/RULE.noRuleAdded@");
			return;
		}
	}
	
	function changeToArray(v){
		var re = new Array();
		var t = v.split(",");
		var tl = t.length;
		for(var i=0; i<tl; i++){
			var tt = t[i];
			var ttI = tt.indexOf("-");
			if(ttI > 0){
				var ttt = tt.split("-");
				if(ttt.length == 2){
					var low = parseInt(parseInt(ttt[0]) > parseInt(ttt[1]) ? ttt[1] : ttt[0]);
					var tttl = Math.abs(parseInt(ttt[0]) - parseInt(ttt[1]));
					for(var u=0; u<tttl + 1; u++){
						re.push(low + u);
					}
				}
			}else if(ttI == -1){
				re.push(parseInt(tt));
			}
		}
		var rel = re.length;
		if(rel > 1){
			var o = {};
			for(var k=0; k<rel; k++){
				o[re[k]] = true;
			}
			re = new Array();
			for(var x in o){
				if (x > 0 && o.hasOwnProperty(x)) {
					re.push(parseInt(x)); 
				} 
			}
			re.sort(function(a, b){
				return a - b;
			});
		}
		return re;
	}
	
	//收集已经配置的聚合规则中VLAN数据
	function collectAggVlanData(){
		//每次使用前将保存的数据重置,防止受到之前数据的影响
		aggVlanData.aggCvlanArr = [];
		aggVlanData.aggSvlanArr = [];
		//收集当前数据
		rulerStore.each(function(rec){
			var aggVidList = rec.data.aggregationVidListAfterSwitch;
			$.each(aggVidList, function(index, cVlan){
				aggVlanData.aggCvlanArr.push(cVlan);
			})
			aggVlanData.aggSvlanArr.push(rec.data.portAggregationVidIndex);
		});
	}

	//添家trunk规则 点击保存按钮;
	function saveTrunkRuler(){
		var trunkId = $("#trunkId").val().trim();
		var trunkData = getStoreData();
		if(!V.isInRange(trunkId,[1, 4094])){
			$("#trunkId").focus();
			return;
		}
		if($.inArray(trunkId, trunkData) > -1){
			window.top.showMessageDlg("@COMMON.tip@", "@univlanprofile/RULE.trunkIdUsed@",null,function(){
				$("#trunkId").focus();
			});
			return;
		}
		trunkData.push(trunkId);
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@");
		$.ajax({
			url : '/epon/uniportvlan/addTrunkRule.tv',
			type : 'POST',
			data : {
				entityId : entityId,
				uniIndex : uniIndex,
				uniId : uniId,
				trunkList : trunkData.join(",")
			},
			dataType :　'json',
			success : function(result) {
				if(result.success){
					top.afterSaveOrDelete({
	       				title: "@COMMON.tip@",
	       				html: '<b class="orangeTxt">@univlanprofile/PROFILE.addSuccess@</b>'
	       			});
					closeRulerDiv();
					rulerStore.reload();
				}else{
					window.parent.showMessageDlg("@COMMON.tip@", "@univlanprofile/PROFILE.addFailed@");
				}
			},
			error : function(json) {
				window.parent.showMessageDlg("@COMMON.tip@", "@univlanprofile/PROFILE.addFailed@");
			},
			cache : false
		});
	}
	//添加透传的规则 点击保存按钮;
	function saveTransRuler(){
		var beforeVlan = $("#beforeVlan").val().trim();
		var afterVlan = $("#afterVlan").val().trim();
		if(!V.isInRange(beforeVlan,[1, 4094])){
			$("#beforeVlan").focus();
			return;
		}
		if(!V.isInRange(afterVlan,[1, 4094])){
			$("#afterVlan").focus();
			return;
		}
		//Vlan ID 不能在其它规则中出现
		var cVlanRepeat = false;
		var sVlanRepeat = false;
		rulerStore.each(function(rec){
			if(beforeVlan == rec.data.cVlanData){
				cVlanRepeat = true;
				return false;
			}
			if(afterVlan == rec.data.ruleSvlan){
				sVlanRepeat = true;
				return false;
			}
		});
		//原VLAN　ID不能在其它规则中出现
		if(cVlanRepeat){
			window.top.showMessageDlg("@COMMON.tip@", "@univlanprofile/RULE.originalVlanUsed@",null,function(){
				$("#beforeVlan").focus();
			});
			return;
		}
		//转换后VLAN ID不能在其它规则中出现
		if(sVlanRepeat){
			window.top.showMessageDlg("@COMMON.tip@", "@univlanprofile/RULE.targetVlanUsed@",null,function(){
				$("#afterVlan").focus();
			});
			return;
		}
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
		$.ajax({
			url : '/epon/uniportvlan/addTranslateRule.tv',
			type : 'POST',
			data : {
				entityId : entityId,
				uniIndex : uniIndex,
				uniId : uniId,
				vlanIndex : beforeVlan,
				translationNewVid : afterVlan
			},
			dataType :　'json',
			success : function(result) {
				if(result.success){
					top.afterSaveOrDelete({
	       				title: "@COMMON.tip@",
	       				html: '<b class="orangeTxt">@univlanprofile/RULE.saveSuccess@</b>'
	       			});
					closeRulerDiv();
					rulerStore.reload();
				}else{
					window.top.showMessageDlg("@COMMON.tip@", "@univlanprofile/RULE.saveFailed@");
				}
			},
			error : function() {
				window.top.showMessageDlg("@COMMON.tip@", "@univlanprofile/RULE.saveFailed@");
			},
			cache : false
		}); 
	    
	}
	//关闭添加规则的div
	function closeRulerDiv(){
		var $rulerDiv = $("#rulerDiv");
		$rulerDiv.hide();
		$("#mask").css({display:'none'});
	}
	
	function saveClick(){
		var portEnable = $("#portEnable").attr("alt") == "on" ? 1: 2;
		var perfEnable = $("#perfEnable").attr("alt") == "on" ? 1: 2;
		if(portEnable == 1){
			zetaDataBind.getConfig("portEnable").title = '@ONU.portEnable@';
		}else{
			zetaDataBind.getConfig("portEnable").title = '@ONU.portDisable@';
		}
		if(perfEnable == 1){
			zetaDataBind.getConfig("perfManage").title = '@ONU.stasitcEnable@';
		}else{
			zetaDataBind.getConfig("perfManage").title = '@ONU.stasitcDisable@';
		}
		zetaDataBind.commit({
			onuId : ${onuId},
			entityId : ${entityId}
		},null,null,function(){
			reload();
		});
	}
	
	function refreshClick(){
		 window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetch@...", 'waitingMsg', 'ext-mb-waiting');
		  $.ajax({
		      url:'/onu/refreshOnu.tv',
		      data: {
		      	onuId : onuId,
		      	entityId : entityId
		      }, 
		      dateType:'json',
		      success:function(json){
		      		setTimeout(function(){
			      		window.top.closeWaitingDlg();
			      		top.afterSaveOrDelete({
			 				title: '@COMMON.tip@',
			 				html: '<b class="orangeTxt">@COMMON.fetchOk@</b>'
			 			});
		      			reload();
		      		},5000);
		      },
		      error:function(){
		    	  window.top.showMessageDlg("@COMMON.tip@", "@COMMON.fetchEr@");
		      },
		      cache:false
		  });
	}
	
	//获取Store中的数据
	function getStoreData(){
		var trunkData = [];
		rulerStore.each(function(rec){
			trunkData.push(rec.data.trunkId);
		});
		return trunkData;
	}
	
	 //应用到其他端口;
	function applyToOtherPorts(){
		window.top.createDialog('modalDlg', "UNI "+uniPortString+"@ONU.applyOther@", 800, 500, '/onu/applyServiceConfig.tv?onuId=${onuId}&entityId=${entityId}&sourceIndex='+uniIndex, null, true, true, reload);
	}
	
	//应用到其他ONU;
	 function applyToOtherOnu(){
		 //window.top.createDialog('modalDlg', 'applyToOtherOnu', 1200, 600, '/epon/onu/applyToOtherOnu.jsp?onuId=${onuId}', null, true, true);
	 }
	
	
	function enableChange(id) {
		if (id == 'inCheck') {
			if ($('#inDirEnable').is(':checked')) {
				$('#inCir').removeAttr("disabled");
				$('#cbs').removeAttr("disabled");
				$('#ebs').removeAttr("disabled");
				$("#inCir").val(inCir);
				$("#cbs").val(cbs);
				$("#ebs").val(ebs);
			} else {
				$('#inCir').attr({disabled:"disabled"});
				$('#cbs').attr({disabled:"disabled"});
				$('#ebs').attr({disabled:"disabled"});
				$("#inCir").val(-1);
				$("#cbs").val(-1);
				$("#ebs").val(-1);
			}
		} else if (id == 'outCheck') {
			if ( $('#outDirEnable').is(':checked') ) {
				$('#outCir').removeAttr("disabled");
				$('#pir').removeAttr("disabled");
				$("#outCir").val(outCir);
				$("#pir").val(pir);
			} else {
				$('#outCir').attr({disabled:"disabled"});
				$('#pir').attr({disabled:"disabled"});
				$("#outCir").val(-1);
				$("#pir").val(-1);
			}
		} 
	}
	/*
	*  端口工作模式 分3种情况
	*  1开启  直接disabled;
	*  1关闭 2开启  全双工可选，半双工不可选(共3项);
	*  1关闭 2关闭  所有都可选(共6项);
	*/
	function enableClick(){
		var oldValue = $("#uniAutoType").val();
		var mode1 = $("#autonegotiation").attr("alt"),
		    mode2 = $("#flowControlEnable").attr("alt"),
		    opt1  = ['<option value="0">@UNI.mode0@</option>',
		             '<option value="1">@UNI.mode1@</option>',
					 '<option value="2">@UNI.mode2@</option>',
					 '<option value="3">@UNI.mode3@</option>',
					 '<option value="4">@UNI.mode4@</option>',
					 '<option value="5">@UNI.mode5@</option>',
					 '<option value="6">@UNI.mode6@</option>'].join(''),
			opt2  = ['<option value="0">@UNI.mode0@</option>',
			         '<option value="1">@UNI.mode1@</option>',
			         '<option value="3">@UNI.mode3@</option>',
			         '<option value="5">@UNI.mode5@</option>'].join('');
		
		if(mode1 == "on"){
			$('#uniAutoType').attr({disabled:"disabled"});
		}else{
			$('#uniAutoType').removeAttr("disabled");
			var $opt = $("#uniAutoType"); 
			$opt.empty();
			switch(mode2){
			case "on":
				$opt.append(opt2);
				break;
			case "off":
				$opt.append(opt1);
				break;
			}
			$("#uniAutoType").val(oldValue);
		}
	}
	function vlanProfile(profileId,profileName){
		if(profileId > 0){
			$("#profileName").text(profileName);
		}else{
			$("#profileName").text("@univlanprofile/UNIVLAN.unRelated@");
		}
	}
	function showVlanProfile(){
		window.top.createDialog('relateprofile', "@univlanprofile/UNIVLAN.viewProfile@", 800, 500, "/epon/univlanprofile/showRelateProfile.tv?entityId="+entityId+"&uniIndex="+uniIndex+"&profileIndex="+profileId+"&bindProfAttr="+bindProfAttr+"&uniId="+uniId, null, true, true);
	}
	</script>
	</head>
    <body class="whiteToBlack">
    	<div id="putToolBar"></div>
    	<div id="putTree">
    		<div class="edge10 pB0">
	    		<!-- <ul id="tree">
	    		</ul> -->
	    		
    		</div>
    	</div>
    	<div id="centerContainer">
	    	<div id="putTabBtn" class="edge10"></div>
	    	<div class="edge10 pB0 clearBoth jsTabBody">
		    	<table class="dataTableRows zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
					<thead>
						<tr>
							<th colspan="6" class="txtLeftTh">@ONU.portEnable@</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td width="160" class="rightBlueTxt">@ONU.portEnable@:</td>
							<td colspan="5">
								<img id="portEnable" src="../../images/speOff.png" alt="on" class="scrollBtn"/>
							</td>
			    		</tr>
			    	</tbody>
		    	</table>
	    	</div>
	    	<div class="edge10 pB0 clearBoth jsTabBody">
		    	<table class="dataTableRows zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
					<thead>
						<tr>
							<th colspan="6" class="txtLeftTh">@univlanprofile/UNIVLAN.uniVlanInfo@</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td width="160" class="rightBlueTxt">@epon/VLAN.portNum@:</td>
							<td>
								<div class="w180" id="uniPortVlanPortString"></div>	
							</td>
							<td width="160" class="rightBlueTxt">PVID:</td>
							<td>
								<div class="w180">
									<input type="text" id="pvid" class="normalInput w60" value="" maxlength="4" tooltip="@univlanprofile/RULE.vlanInputTip@" />
								</div>
							</td>
							<td width="160" class="rightBlueTxt">@univlanprofile/PROFILE.vlanMode@:</td>
							<td>
								<div class="w180">
									<select id="vlanMode" class="normalSel w160" onchange="changeMode()" >
										<option value="0">@univlanprofile/PROFILE.modeTransparent@</option>
										<option value="1">@univlanprofile/PROFILE.modeTag@</option>
										<option value="2">@univlanprofile/PROFILE.modeTranslate@</option>
										<option value="3">@univlanprofile/PROFILE.modeAgg@</option>
										<option value="4">Trunk</option>
									</select>
								</div>
							</td>
			    		</tr>
			    		<tr>
			    			<td class="rightBlueTxt">
			    				@univlanprofile/UNIVLAN.profile@:
			    			</td>
			    			<td colspan="5" id="profileName">
			    			</td>
			    		</tr>
			    	</tbody>
			    	<tbody>
			    		<tr>
			    			<td colspan="6">
			    				<div class="noWidthCenterOuter clearBoth">
	     							<ol class="upChannelListOl pB5 pT5 noWidthCenter">
										<li>
											<a href="javascript:" class="normalBtn" onclick="showVlanProfile()"><span><i class="miniIcoTwoArr"></i>@univlanprofile/UNIVLAN.viewProfile@</span></a>		
										</li>		
			    					</ol>
			    				</div>
			    			</td>
			    		</tr>
			    	</tbody>
		    	</table>
		    	<div id="putRulerTranslate" class="pT10">
		    		
		    	</div>
		    	<div id="aggTip" class="clearBoth yellowTip mT5" style="display:none; background:#f7f7f7; border-color:#d0d0d0;">
		    		<p class="pB0"><b class="orangeTxt">@univlanprofile/RULE.addRule@@COMMON.tip@@COMMON.maohao@</b></p>
					<p><b class="orangeTxt">@univlanprofile/UNIVLAN.aggTip1@</b></p> 
					<p><b class="orangeTxt">@univlanprofile/UNIVLAN.aggTip2@</b></p>
					<p><b class="orangeTxt">@univlanprofile/UNIVLAN.aggTip3@</b></p>
				</div>
	    	</div>
	    	<div class="edge10 pB0 clearBoth jsTabBody">
	    		<table class="dataTableRows zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
					<thead>
						<tr>
							<th colspan="6" class="txtLeftTh">@ONU.uniSpeedDec@</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td class="rightBlueTxt"><input type="checkbox" id="inDirEnable" name="ck1" onclick="enableChange('inCheck')" /></td>
							<td colspan="5">
								<label class="blueTxt" for="inDirEnable">@epon/SERVICE.inRateRestrictEn@</label>
							</td>
			    		</tr>
			    		<tr>
							<td width="160" class="rightBlueTxt">CIR:</td>
							<td>
								<div class="w180">
									<input class="normalInput w60" id= "inCir" type="text" value="-1" tooltip="@SERVICE.uniPortInCIRTip@" /> Kbps
								</div>
							</td>
							<td width="160" class="rightBlueTxt">CBS:</td>
							<td>
								<div class="w180">
									<input class="normalInput w60" id= "cbs" type="text" value="-1"  tooltip="@SERVICE.uniPortInCBSTip@"/> KBytes
								</div>
							</td>
							<td width="160" class="rightBlueTxt">EBS:</td>
							<td>
								<div class="w180">
									<input class="normalInput w60" id="ebs" type="text" value="-1"  tooltip="@SERVICE.uniPortInEBSTip@" /> KBytes
								</div>
							</td>
			    		</tr>
			    		<tr>
							<td class="rightBlueTxt"><input type="checkbox" id="outDirEnable" onclick="enableChange('outCheck')" name="ck2" /></td>
							<td colspan="5">
								<label class="blueTxt" for="outDirEnable">@epon/SERVICE.outRateRestrictEn@</label>
							</td>
			    		</tr>
			    		<tr>
							<td class="rightBlueTxt">PIR:</td>
							<td>
								<div class="w180">
									<input class="normalInput w60" id= "pir" type="text" value="0"   tooltip="@SERVICE.rangePIR@" /> Kbps
								</div>
							</td>
							<td class="rightBlueTxt">CIR:</td>
							<td>
								<div class="w180">
									<input class="normalInput w60" id= "outCir" type="text" value="0"   tooltip="@SERVICE.rangeCIR@"/> KBytes
								</div>
							</td>
							<td colspan="2"></td>
			    		</tr>
			    	</tbody>
		    	</table>
	    	</div>
	    	<div class="edge10 pB0 clearBoth jsTabBody">
		    	<table class="dataTableRows zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
					<thead>
						<tr>
							<th colspan="6" class="txtLeftTh">
			    				@ONU.workMode@
							</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td width="160" class="rightBlueTxt">@UNI.autoNegEn@:</td>
							<td>
								<div class="w180">
									<img src="../../images/speOff.png" id="autonegotiation" alt="on" class="scrollBtn">
								</img>
								</div>
							</td>
							<td width="160" class="rightBlueTxt">@UNI.flowControlEn@:</td>
							<td>
								<div class="w180">
									<img src="../../images/speOff.png" id="flowControlEnable" alt="on" class="scrollBtn">
								</img>
								</div>
							</td>
							<td width="160" class="rightBlueTxt">
								@UNI.workMode@:
							</td>
							<td>
								<div class="w180">
									<select id="uniAutoType" class="normalSel w160">
										<option value="0">@UNI.mode0@</option>
										<option value="1">@UNI.mode1@</option>
										<option value="2">@UNI.mode2@</option>
										<option value="3">@UNI.mode3@</option>
										<option value="4">@UNI.mode4@</option>
										<option value="5">@UNI.mode5@</option>
										<option value="6">@UNI.mode6@</option>
									</select>
								</div>
							</td>
			    		</tr>
			    		<tr>
			    			<td colspan="6">
			    				<p class="pL20 pT10 blueTxt"><b class="orangeTxt">@UNI.autoNegNote@</p>
			    				<p class="pL20 pB10 blueTxt"><b class="orangeTxt">@UNI.autoNegNote2@</p>
			    			</td>
			    		</tr>
			    	</tbody>
		    	</table>
	    	</div>
	    	<div class="edge10 pB0 clearBoth jsTabBody">
		    	<table class="dataTableRows zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
					<thead>
						<tr>
							<th colspan="6" class="txtLeftTh">@ONU.uniUSUtgPriSimple@</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td width="260" class="rightBlueTxt">@ONU.uniUSUtgPri@:</td>
							<td>
								<div class="w180">
									<select id="priority" class="normalSel w80">
										<option value="255">@ONU.noPri@</option>
										<option value="0">0</option>
										<option value="1">1</option>
										<option value="2">2</option>
										<option value="3">3</option>
										<option value="4">4</option>
										<option value="5">5</option>
										<option value="6">6</option>
										<option value="7">7</option>
									</select>
								</div>
							</td>
							<td width="160" class="rightBlueTxt"></td>
							<td>
								<div class="w180"></div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="edge10 pB0 clearBoth jsTabBody">
		    	<table class="dataTableRows zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
					<thead>
						<tr>
							<th colspan="6" class="txtLeftTh">@ONU.macLearn@</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td width="160" class="rightBlueTxt">@ONU.macMaxLearn@:</td>
							<td colspan="5">
								<input id="macLearnMaxNum" value ="" maxlength="2" type="text" class="normalInput w60 floatLeft mR5" tooltip="@epon/SERVICE.uniMacAddrLearnMaxNumTip@" />
							</td>
			    		</tr>
					</tbody>
				</table>
			</div>
			<div class="edge10 pB10 clearBoth jsTabBody">
		    	<table class="dataTableRows zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
					<thead>
						<tr>
							<th colspan="6" class="txtLeftTh">@ONU.stasitcEnable@</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td width="160" class="rightBlueTxt">@ONU.stasitcEnable@:</td>
							<td colspan="5">
								<img id="perfEnable" src="../../images/speOff.png" alt="on" class="scrollBtn"/>
							</td>
			    		</tr>
					</tbody>
				</table>
			</div>
			<div class="noWidthCenterOuter clearBoth">
				<ol class="upChannelListOl pB5 pT0 noWidthCenter">
					<li>
						<a href="javascript:;" class="normalBtnBig" onclick="saveClick()"><span><i class="miniIcoData"></i>@BUTTON.save@</span></a>		
					</li>		
				</ol>
			</div>
		</div>
		<div id="mask" class="fakeMask"></div>
	</body>
</Zeta:HTML> 


