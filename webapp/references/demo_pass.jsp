<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>测试页面</title>
<link rel="stylesheet" type="text/css" href="css/gui.css" />
<link rel="stylesheet" type="text/css" href="css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="css/white/xtheme.css" />
<link rel="stylesheet" type="text/css" href="css/white/mytheme.css" />
<style type="text/css">
/* label.error{ display:inline !important;} */
</style>
</head>
<body class="openWinBody">
	<form id="form1" name="form1" >
	<div class="edge10">
		<div class="zebraTableCaption">
   			<div class="zebraTableCaptionTitle"><span>密码框demo</span></div>		
			<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">	            
	             <tr>
	             	<td class="rightBlueTxt w200">
	             		普通密码：
	             	</td>
	             	<td id="test1">
	             		
	             		
	                </td>	                
	             </tr>	
	             <tr>
	             	<td class="rightBlueTxt">
	             		密码设置宽度：
	             	</td>
	             	<td id="test2">
	             		
	             		
	                </td>
	             </tr>	
	             <tr>
	             	<td class="rightBlueTxt ">
	             		首先显示密码框：
	             	</td>
	             	<td id="test3">
	             		
	             		
	                </td>
	             </tr>	
	             <tr>
	             	<td class="rightBlueTxt ">
	             		初始有值并不能填写：
	             	</td>
	             	<td>
	             		<div  id="test4"></div>
	                </td>
	             </tr>	  
	             <tr>
	             	<td></td>
	             	<td><span id="openBtn">开启编辑</span></td>
	             </tr>
	             <tr>
	             	<td class="rightBlueTxt ">
	             	</td>
	             	<td id="test4">
							<a id="okBtn" href="javascript:;" class="normalBtnBig"><span>验证</span></a>
							<div style="clear:both"></div>						          		
	                </td>
	             </tr>	        
			</table>		
			
		</div>
	</div>
	</form>


	

<script type="text/javascript" src="js/jquery/jquery.js"></script>
<script type="text/javascript" src="js/jquery/jquery.validate.js"></script>
<script type="text/javascript" src="js/jquery/jquery.livequery.js"></script>
<script type="text/javascript" src="js/jquery/nm3kToolTip.js"></script>
<script type="text/javascript" src="js/jquery/nm3kPassword.js"></script>
<script type="text/javascript" src="js/jquery/dragMiddle.js"></script>
<script type="text/javascript">
	

		
	
	
</script>
<script type="text/javascript">
	$(function(){
		var pass1 = new nm3kPassword({
			id : "pass1",
			renderTo : "test1",
			toolTip : "不能为空，不能少于5个字符"
		})
		pass1.init();
		
		var pass2 = new nm3kPassword({
			id : "pass2",
			renderTo : "test2",
			toolTip : "不能为空，不能少于2个字符",
			width : 300
		})
		pass2.init();
		
		var pass3 = new nm3kPassword({
			id : "pass3",
			renderTo : "test3",
			toolTip : "不能为空，不能少于2个字符",
			firstShowPassword : true
		})
		pass3.init(); 
		
		var pass4 = new nm3kPassword({
			id : "pass4",
			renderTo : "test4",
			toolTip : "不能为空，不能少于2个字符",
			value : 'abcde',
			disabled : true
		})
		pass4.init(); 
		
		$("#openBtn").click(function(){
			if($(this).text() == "开启编辑"){
				$(this).text("关闭编辑")
				pass4.setDisabled(false);
			}else{
				$(this).text("开启编辑")
				pass4.setDisabled(true);
			}
		})
		
		
		
		/////////////验证部分;
		$("#form1").validate({
			onfocusout: false,
			rules:{
				pass1:{required:true,minlength:5},
				pass2:{required:true,minlength:2},
				pass3:{required:true,minlength:2},
				pass4:{required:true,minlength:2}
			},
			highlight: function(element, errorClass) {  		
				$(element).addClass("normalInputRed");
			},
			unhighlight: function(element, errorClass) {  
				$(element).removeClass("normalInputRed");
			}
		});//end validate;
		$("#okBtn").click(function(){
			if($("#form1").valid()){
				var str = $("#pass1").val() +"\n"+ $("#pass2").val() +"\n"+ $("#pass3").val() +"\n"+ $("#pass4").val();
				alert("验证成功!得到的值分别是：\n" + str);			
			}else{
				alert("验证不成功");
				$(".normalInputRed:eq(0)").focus();
			}
		}); 
		
	});//end document.ready;	
</script>
	
</body>
</html>
