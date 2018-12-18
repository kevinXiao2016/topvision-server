<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Zeta:HTML>
<head>
<%@include file="../include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
    module epon
    CSS css/white/disabledStyle
</Zeta:Loader>
<style type="text/css">
	.middleDl{ padding:80px 20px 0px;}
	.portTip{ position:absolute; top:8px; right:10px; background:url(/images/performance/Help.png) no-repeat; padding:1px 0px 2px 20px;}
	
	.moveTableDl dt{ text-align: center; font-weight:bold; border-bottom:1px solid #ccc; padding:8px 0; color:#0267B7; background:#EEE;}
	.moveTableDl dd{ height:224px; overflow:auto;}
	.moveTableDl dd a{ display:block; text-align:center; border-bottom:1px solid #ccc; padding:8px 0;}
	.moveTableDl dd a.select{ background:#CCE3FF url(../../images/nm3kProgressOk.gif) no-repeat 10px center; color:#0267B7;}
</style>
<script type="text/javascript">
var entityId = ${entityId},
	sniJson,//储存有所有端口的所有属性，需要优化
	vailbleGrid,
	readyGrid,
	vailbleStore,
	readyStore,
	tempPortIndex,
	vailbleList = new Array(),
	readyList = new Array();
var reg = /^([A-Za-z0-9])+$/;
/******************************************
 				 初始化区
 *****************************************/
$( DOC ).ready(function(){
	$("#trunkName").keyup(function(e){
		var text = $("#trunkName").val();
		if(!text || !reg.test(text)){///输入错误
			$("#trunkName").css({
				border : '1px solid red'
			});	
		}else{//输入正确
			$("#trunkName").css({
				border : '1px solid gray'
			});
		}
	});
	
	$("#saveBt").bind('click',function(e){			
		saveFn();
	});
	$("#cancelBt").bind('click',function(e){
		window.parent.closeWindow('addNewTrunkGroup');	
	});
//END OF $DOCUMENT	
});


	function cancelClick() {
		try {
			window.parent.getFrame("entity-" + entityId).showMSG();
		} catch (e) {}
		window.parent.closeWindow('addNewTrunkGroup');
	}

</script>
<style type="text/css">
.ruleNote{position: absolute;top:60px;left:520px;}
</style>
</head>
	<body class="openWinBody">
		<div class="edgeTB10LR20 pB5">
		    <div class="zebraTableCaption" style="padding-top:25px; padding-bottom:5px;">
		    	<div class="zebraTableCaptionTitle"><span>@TRUNK.configItem@</span></div>
		    	<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
		            <tbody>
		                <tr class="withoutBorderBottom">
		                    <td class="rightBlueTxt" width="140">@TRUNK.trunkName@:</td>
		                    <td width="160">
		                    	<input id="trunkName" class="normalInput w150" type="text" tooltip="@TRUNK.trunkNameNotNull@" maxlength="31">
		                    </td>
		                    <td class="rightBlueTxt" width="130">@TRUNK.policy@:</td>
		                    <td>
		                    	<select id='TrunkPolicyValue' class="normalSel w150">
									<option value='1'>srcMac</option>
									<option value='2'>destMac</option>
									<option value='3' selected>srcMacNDestMac</option>
									<option value='4'>srcIp</option>
									<option value='5'>destIp</option>
									<option value='6'>srcIpNDestIp</option>
								</select>
		                    </td>
		                </tr>
		            </tbody>
		        </table>
		    </div>
		</div>
		<div class="pL20 pT0 pB0" style="width:760px; height:310px;">
			<div class="zebraTableCaption" style="padding-top:32px; position:relative; top:0; left:0;">
		    	<div class="zebraTableCaptionTitle"><span>@TRUNK.selectPort@</span></div>
		    	<div class="leftPart" style="border:1px solid #ccc; width:280px; height:260px;">
		    		<dl class="moveTableDl">
		    			<dt>
		    				@TRUNK.selectPortList@
		    			</dt>
		    			<dd id="putLeftLink">
		    				
		    			</dd>
		    		</dl>
		    	</div>
		    	<a id='toRightBtn' style="position:absolute; top:112px;left:321px;" disabled="disabled" href="javascript:;" class="normalBtn"><span><i class="miniIcoArrRight"></i>@COMMON.moveIn@</span></a>
		    	<a id='toLeftBtn' style="position:absolute; top:142px;left:321px;" disabled="disabled" href="javascript:;" class="normalBtn"><span><i class="miniIcoArrLeft"></i>@COMMON.moveOut@</span></a>
		    	<div class="rightPart" style="position:absolute; top:32px; left:420px; width:280px; height:260px; border:1px solid #ccc;">
		    		<dl class="moveTableDl">
		    			<dt>
		    				@TRUNK.selectedPortList@
		    			</dt>
		    			<dd id="putRightLink">
		    				
		    			</dd>
		    		</dl>
		    	</div>
		    </div>
		</div>
		<Zeta:ButtonGroup>
			<Zeta:Button id="saveBt"  icon="miniIcoData">@COMMON.save@</Zeta:Button>
			<Zeta:Button id="cancelBt" icon="miniIcoClose">@COMMON.close@</Zeta:Button>
		</Zeta:ButtonGroup>
		
		<script type="text/javascript">
			$(function(){
				initData();
			});
			
			var aMoveData = [];//存储初始化的数据，自己加上index属性用来排序;
			var leftData = [];
			var rightData = [];
			//初始化数据;
			function initData(){
				window.top.showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.fetching, 'ext-mb-waiting');
				$.ajax({
			    	url:"/epon/trunk/loadAvailbleListFirst.tv?entityId=" + entityId,
			        method:"post",dataType:"json",cache:false,
			        success:function (json) { 
			        	window.top.closeWaitingDlg();
			        	if(!json && !json.data){return;}
			        	$.each(json.data, function(i, v){
			        		aMoveData.push({
			        			index : i,
			        			text : v.sniDisplayName,
			        			value : v.sniIndex
			        		})
			        	});
			        	$.extend(true, leftData, aMoveData);
			        	createAlink(leftData, 'putLeftLink');
			        	bindAllLinks();
			        },
			        error: function(){
			        	window.parent.showMessageDlg("@COMMON.tip@", I18N.COMMON.fetchError);
			        }
				});
			}
			function bindAllLinks(){
				bindMoveLinks();
	        	bindMoveRightBtn();
	        	bindMoveLeftBtn();
			}
			//创建a标签在指定id的<dd>标签中;
			function createAlink(arr, renderId){
				var str = '';
				$.each(arr, function(i, v){
					str += String.format('<a href="javascript:;" name="{1}">{0}</a>', v.text, v.index);
				});
				$('#'+renderId).html(str);
			}
			//绑定左右两侧a标签点击事件;
			function bindMoveLinks(){
				$('.moveTableDl dd').delegate('a','click', function(){
					var $me = $(this);
					if($me.hasClass('select')){
						$me.removeClass('select');
					}else{
						$me.addClass('select');
					}
				})
				//左侧a点击;
				$('#putLeftLink').delegate('a','click', function(){
					$('#toLeftBtn').attr({disabled: 'disabled'});
					if($('#putLeftLink a.select').length > 0){
						$('#toRightBtn').attr({disabled: false});
					}else{
						$('#toRightBtn').attr({disabled: 'disabled'});
					}
					$('#putRightLink a').removeClass('select');
				});
				//右侧a点击;
				$('#putRightLink').delegate('a','click', function(){
					$('#toRightBtn').attr({disabled: 'disabled'});
					if($('#putRightLink a.select').length > 0){
						$('#toLeftBtn').attr({disabled: false});
					}else{
						$('#toLeftBtn').attr({disabled: 'disabled'});
					}
					$('#putLeftLink a').removeClass('select');
				});
			}
			//绑定 -> 按钮
			function bindMoveRightBtn(){
				$('#toRightBtn').on('click',function(){
					$(this).attr({disabled: 'disabled'});
					//先获取选中的index;
					$('#putLeftLink a.select').each(function(){
						var index = parseInt($(this).attr('name'), 10);
						var item = getItem(index, leftData);
						leftData.removeObj(item);
						rightData.push(item);
						$(this).remove();
					});
					rightData.sort(function(a,b){
						return a.index - b.index;
					})
					createAlink(rightData, 'putRightLink');
				})
			};
			//绑定 <- 按钮;
			function bindMoveLeftBtn(){
				$('#toLeftBtn').on('click',function(){
					$(this).attr({disabled: 'disabled'});
					//先获取选中的index;
					$('#putRightLink a.select').each(function(){
						var index = parseInt($(this).attr('name'), 10);
						var item = getItem(index, rightData);
						rightData.removeObj(item);
						leftData.push(item);
						$(this).remove();
					});
					leftData.sort(function(a,b){
						return a.index - b.index;
					})
					createAlink(leftData, 'putLeftLink');
				})
			};
			//通过index获取，你是数组中的哪一个;
			function getItem(index, arr){
				var item = null;
				$.each(arr, function(i, v){
					if(index == v.index){
						item =  v;	
						return false;	
					}
				}) 
				return item;
			};
			//保存;
			function saveFn(){
				var trunkName = $("#trunkName").val();
				if(!trunkName || !reg.test(trunkName)){
					window.parent.showMessageDlg("@COMMON.tip@", I18N.TRUNK.trunkNameNotNull ,"error",function(){
						$("#trunkName").focus();
					});
					$("#trunkName").css({
						border : '1px solid red'
					});		
					return;
				}
				if(rightData.length==0){
					window.parent.showMessageDlg("@COMMON.tip@", I18N.TRUNK.plsSelectPortOne);
					return;
				}
				var policy = $("#TrunkPolicyValue").val();
				var temp = [];
				$.each(rightData, function(i, v){
					temp.push(v.value);
				})
				var members = temp.join('@');
				window.top.showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.saving, 'ext-mb-waiting');
				$.ajax({
					url:"/epon/trunk/createTrunkGroup.tv?",
			        method:"post",
			        cache:false,
			        data : 'entityId='+entityId+'&sniTrunkGroupConfigName='+trunkName+'&sniTrunkGroupConfigGroup='+members+'&sniTrunkGroupConfigPolicy='+policy,
			        success:function (response) {
			        	if('success' == response){
				        	window.parent.showMessageDlg("@COMMON.tip@", I18N.TRUNK.addOk);
							cancelClick();
			            }else{
			            	window.parent.showMessageDlg("@COMMON.tip@",I18N.TRUNK.addError);
			            }
			        },
			   		error:function () {
			   			window.parent.showMessageDlg("@COMMON.tip@", I18N.TRUNK.addError);
			    	}
				});	
			}
			
		</script>
	</body>
	
</Zeta:HTML>