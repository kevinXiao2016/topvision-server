<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@include file="/include/ZetaUtil.inc"%>
 <Zeta:Loader>
    library ext
    library jquery
    library zeta
    module resources
    import system.newUser
    plugin DropdownTree
</Zeta:Loader>


<style type="text/css">
#w800{ width:790px; overflow:hidden; position: relative; top:0; left:0; height:385px;}
#w1600{ width:1600px; position:absolute; top:0; left:0;}
#step0, #step1{width:800px; overflow:hidden; position:absolute; top:0px; left:0px;}
#step1{ left:800px;}
</style>
<script type="text/javascript" src="../js/jquery/nm3kPassword.js"></script>
<script type="text/javascript">
	var checkPasswdComplex = true;
</script>

</head>
<body class="openWinBody">
	<form id="userForm" name="userForm">
		<input type=hidden id="departmentId" name="userEx.departmentId" value="0" />
		<input type=hidden id="placeId" name="userEx.placeId" value="0" />
		<input type=hidden id="roleIds" name="userEx.roleIds" />
		<!-- <input type=hidden id="regionId" name="userEx.userGroupId" value="-1" /> -->
		<input type="hidden" id="rootRegionIds" name="userEx.userGroupIdsStr" value="-1" />
		<input type="hidden" id="curRegionId" name="userEx.userGroupId" value="-1" />
	

	<div class="openWinHeader">
	    <div class="openWinTip">
	       <p><b class="orangeTxt">@SYSTEM.user@</b></p>
	       <p><span id="newMsg">@SYSTEM.createUser@</span></p>
	    </div>
	    <div class="rightCirIco userCirIco">
	    </div>
	</div>
	<div id="w800">
		<div id="w1600">
		<div id="step0">
			<div class="edgeTB10LR20">
			    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
			        <tbody>
			            <tr>
			                <td class="rightBlueTxt" width="200">
			                   <label for="userName">@SYSTEM.userName@: <font color=red>*</font></label>
			                </td>
			                <td width="300">
			                	<input style="width:290px;" id="userName" name="userEx.userName" value='' class="normalInput"
			                	toolTip='@SYSTEM.userNameFamatError@' 
								type=text maxlength=32 />
			                </td>
			                 <td><span id="nameExist" style="display:none;" class="orangeTxt">@SYSTEM.userNameExist@</span>
	               			 </td>
			            </tr>
			            <tr class="darkZebraTr">
			                <td class="rightBlueTxt">
			                    <label for="passwd">@SYSTEM.passWord@<font color=red>*</font></label>
			                </td>
			                <td id="putPass">
								<script type="text/javascript">
									var pass1 = new nm3kPassword({
										id : "userEx.passwd",
										renderTo : "putPass",
										toolTip : '@SYSTEM.createUser.note1@',
										width : 248,
										maxlength : 16,
										firstShowPassword : true
									})
									pass1.init();
									$("#putPass").find(":input").css("ime-mode","disabled");
								</script>
			                </td>
							<td></td>			           
			            </tr>
			        </tbody>
			    </table>
			   	<p class="pT10 pB5"><label for="roleName">@SYSTEM.pleaseSelectUserRole@: <font color=red>*</font></label></p>
			   <div id="roleTree" class="clear-x-panel-body threeFeBg" style="width:99%; border:1px solid #D0D0D0"></div>	
			   <div class="noWidthCenterOuter clearBoth">
				    <ol class="upChannelListOl pB0 pT20 noWidthCenter">
				        <li>
				            <a id="nextBt"  href="javascript:;" class="normalBtnBig">
				                <span>
				                    <i class="miniIcoArrRight">
				                    </i>
				                  		@SYSTEM.nextStep@
				                </span>
				            </a>
				        </li>
				         <li>
				            <a id=""  href="javascript:;" class="normalBtnBig"  onclick="cancelClick()">
				                <span>
				                	<i class="miniIcoForbid">
				                	</i>
				                  	@COMMON.cancel@
				                </span>
				            </a>
				        </li>
				    </ol>
				</div>	   
			</div>
		</div>
		
		<div id="step1">
			<div class="edgeTB10LR20" style="padding:5px 20px;">
			    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
			        <tbody>
			            <tr>
			                <td class="rightBlueTxt" width="200">
			                    <label for="familyName">@SYSTEM.name@: </label>
			                </td>
			                <td>
			                    <input style="width:290px" id=familyName name="userEx.familyName" value='' class=normalInput 
								type=text maxlength=24 toolTip='@SYSTEM.pleaseInputRealName@' />
			                </td>
			            </tr>
			            <tr>
			            	<td class="rightBlueTxt">
			                    <label for="root_regions">@SYSTEM.rootRegion@: <font color=red>*</font></label>
			                </td>
			                <td><div id="root_regions"></div></td>
			            </tr>
			            <tr>
			            	<td class="rightBlueTxt">
			                    <label for="cur_root_regions">@SYSTEM.currentregion@: <font color=red>*</font></label>
			                </td>
			                <td><div id="cur_root_regions"></div></td>
			            </tr>
			            <%-- <tr class="darkZebraTr">
			                <td class="rightBlueTxt">
			                    <label for="regionName">@SYSTEM.Region@: <font color=red>*</font></label>
			                </td>
			                <td>
			                   <input style="width: 232px" id="regionName" disabled="disabled"
							name="userEx.userGroupName"
							value='<s:property value="userEx.userGroupName"/>' class="normalInputDisabled floatLeft"
							type="text" toolTip='@SYSTEM.pleaseSelectCusRegion@' />
							<a  id="selectRegionBt" onclick="selectRegionClick();" href="javascript:;" class="nearInputBtn"><span>@COMMON.select@</span></a>
			                </td>
			            </tr> --%>
			             <tr>
			                <td class="rightBlueTxt" width="200">
			                    <label for="departmentName">@SYSTEM.department@: </label>
			                </td>
			                <td>
			                    <input style="width: 232px" id="departmentName"  disabled="disabled"
								name="userEx.departmentName"
								value='<s:property value="userEx.departmentName"/>' class="normalInputDisabled floatLeft"
								type=text toolTip='@SYSTEM.pleaseSelectCusDepartment@' />
								<a id="selectDepartmentBt" onclick="selectDepartmentClick();" href="javascript:;" class="nearInputBtn"><span>@COMMON.select@</span></a>
			                </td>
			            </tr>
			            <tr class="darkZebraTr">
			                <td class="rightBlueTxt">
			                    <label for="placeName">@SYSTEM.post@: </label>
			                </td>
			                <td>
			                   <input style="width: 232px" id="placeName" toolTip='@SYSTEM.pleaseSelectCusPost@'
								name="userEx.placeName" value='<s:property value="userEx.placeName"/>'  disabled="disabled"
								class="normalInputDisabled floatLeft" type="text" />
								<a  id="selectPostBt" onclick="selectPostClick();" href="javascript:;" class="nearInputBtn"><span>@COMMON.select@</span></a>
			                </td>
			            </tr>
			             <tr>
			                <td class="rightBlueTxt" width="200">
			                    <label for="workPhone">@SYSTEM.workTelephone@: </label>
			                </td>
			                <td>
			                    <input style="width:290px" id="workPhone" name="userEx.workTelphone" value='' class="normalInput" 
									type=text maxlength=16 />
			                </td>
			            </tr>
			            <tr class="darkZebraTr">
			                <td class="rightBlueTxt">
			                    <label for="homePhone">@SYSTEM.homeTelephone@: </label>
			                </td>
			                <td>
			                   <input style="width:290px" id="homePhone" name="userEx.homeTelphone" value='' class="normalInput" 
								type=text maxlength=16 />
			                </td>
			            </tr>
			             <tr>
			                <td class="rightBlueTxt" width="200">
			                    <label for="mobile">@SYSTEM.cellphone@: </label>
			                </td>
			                <td>
			                    <input style="width:290px" id=mobile name="userEx.mobile" value='' class="normalInput" 
									type=text maxlength=16 /><font color=orange> @SYSTEM.confirmSMS@</font>
			                </td>
			            </tr>
			            <tr class="darkZebraTr">
			                <td class="rightBlueTxt">
			                    <label for="email">@SYSTEM.email@: </label>
			                </td>
			                <td>
			                   <input style="width:290px" id=email name="userEx.email" value='' class="normalInput" 
								type=text maxlength=32 /><font color=orange> @SYSTEM.confirmEmail@</font>
			                </td>
			            </tr>
			        </tbody>
			    </table>
			    <div class="noWidthCenterOuter clearBoth">
				    <ol class="upChannelListOl pB0 pT5 noWidthCenter">
				        <li>
				            <a id="prevBt"  href="javascript:;" class="normalBtnBig">
				                <span>
				                    <i class="miniIcoArrLeft">
				                    </i>
				                    @SYSTEM.upStep@
				                </span>
				            </a>
				        </li>		        
				        <li>
				            <a  id="finishButton" href="javascript:;" class="normalBtnBig" 
				             <s:if test="checkPasswdComplex"> 	onclick="okClick(true)"	  </s:if>
							  <s:else> 	onclick="okClick(false)" </s:else>>
				                <span>
								  	<i class="miniIcoAdd"></i>
				                    @COMMON.create@
				                </span>
				            </a>
				        </li>
				         <li>
				            <a id=""  href="javascript:;" class="normalBtnBig"  onclick="cancelClick()">
				                <span>
					            	<i class="miniIcoForbid"></i>
				                  	@COMMON.cancel@
				                </span>
				            </a>
				        </li>
				    </ol>
				</div>	   
			</div>
		</div>
	</div>
	</div>
	<script type="text/javascript">
	$(function(){
		$("#nextBt").click(function(){
			var userName = Zeta$('userName').value;
			var passwd = Zeta$('userEx.passwd').value;
			var roles = tree.getChecked();
			//对名称进行重复性校验
			if(repeatFlag){
				$("input#userName").focus();
				return;
			}
			if(!validate(userName, passwd, roles)){return;}
			$("#w1600").animate({left:-800},'fast');		
		});//点击下一步;
		
		$("#prevBt").click(function(){
			$("#w1600").animate({left:0},'normal');		
		});//点击上一步;
	});//end document.ready;
	</script>

</form>
</body>
</Zeta:HTML>