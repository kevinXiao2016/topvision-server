<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="../include/cssStyle.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
    module resources
</Zeta:Loader>
</head>
	<body class="openWinBody">
		<div class="openWinHeader">
		    <div class="openWinTip">
		    	<p>@SYSTEM.timeoutTip1@</p>
		    	<p>@SYSTEM.timeoutTip2@</p>
		    </div>
		    <div class="rightCirIco clock2CirIco"></div>
		</div>
		<div class="edgeTB10LR20 pT40">
		     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
		         <tbody>
		         	 <tr>
		         		<td class="rightBlueTxt" width="218">
		         			@SYSTEM.userNameHeader@:
		         		</td>
		         		<td>
		         			<input type="text" class="normalInputDisabled w130" value="${userName}" readonly="readonly" />
		         		</td>
		         	 </tr>
		             <tr class="darkZebraTr">
		                 <td class="rightBlueTxt">
		                     	@SYSTEM.userSessionTime@: 
		                 </td>
		                 <td>
		                     <input id="sessionTime" value="${timeout}" type="text" class="normalInput w130" style="ime-mode:disabled" toolTip="@SYSTEM.sessionTimeTip@" /> 分钟
		                 </td>
		             </tr>
		         </tbody>
		     </table>
			<div class="noWidthCenterOuter clearBoth">
			     <ol class="upChannelListOl pB0 pT80 noWidthCenter">
			         <li><a href="javascript:;" onclick="save();" class="normalBtnBig"><span><i class="miniIcoData"></i>@COMMON.save@</span></a></li>
			         <li><a href="javascript:;" class="normalBtnBig" onclick="closeFn()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
			     </ol>
			</div>
		</div>
	</body>
	<script type="text/javascript">
	function closeFn(){
		top.closeWindow("setSessionTime");
	}
	function save(){
		var $sessionTime= $("#sessionTime"),
		    $timeout = $sessionTime.val(),
		    pass = V.isSystemTimeout($timeout);
		if( !pass ){
			$sessionTime.focus();
			return;	
		} 
		if( $timeout == 0 ){//关闭session其实是将session设置为特别大的数值;
					
		}
		$.ajax({
			url:"/system/updateUserSession.tv",cache:false,
			data:{userId:${userId},timeout : $timeout},
			success:function(){
				top.afterSaveOrDelete({
					title : "@COMMON.tip@",
					html : "@COMMON.updateSuccessfully@"
				});
				var activeId = top.getActiveFrameId();
				if(activeId == "userList"){//如果是用户管理页面，则刷新那个页面;
					try{
						top.frames["frameuserList"].onRefreshClick();	
					}catch(err){}
				};
				closeFn();
			},error:function(){
				top.afterSaveOrDelete({
					title : "@COMMON.tip@",
					html : "@COMMON.updatefailure@"
				});
			}
		});
	}
	</script>
</Zeta:HTML>