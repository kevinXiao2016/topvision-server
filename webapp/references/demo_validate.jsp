<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<Zeta:Loader>
	library jquery
	module demo
</Zeta:Loader>
<style type="text/css">
 label.error{ display:none !important;} 
</style>
</head>
<body class="openWinBody">
	<form id="form1" name="form1" >
	<div class="edge10">
		<div class="zebraTableCaption">
   			<div class="zebraTableCaptionTitle"><span>@demo.validator@</span></div>		
			<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	             <tr>
	             	<td class="rightBlueTxt w200">@demo.showTips@：</td>
	             	<td>
	             		<input type="text" id="input1" name="input1" class="normalInput" toolTip="@demo.validateTip@" />
	                </td>
	             </tr>
	             <tr class="darkZebraTr">
	             	<td class="rightBlueTxt">@demo.nessary@：</td>
	                <td>
	             		<input type="text" id="input2" name="input2" class="normalInput"  toolTip="@demo.NOTNULL@"  />
	                </td>
	             </tr>
	             <tr>
	             	<td class="rightBlueTxt">Email:</td>
	             	<td>
	             		<input type="text" id="input3" name="input3" class="normalInput"  toolTip="@demo.accpetEmail@"  />
	                </td>
	             </tr>
	             <tr class="darkZebraTr">
	             	<td class="rightBlueTxt">
	             		url:
	             	</td>
	                <td>
	             		<input type="text" id="input4" name="input4" class="normalInput"  toolTip="@demo.acceptHttp@"  />
	                </td>
	             </tr>
	             <tr>
	             	<td class="rightBlueTxt">@demo.charLength@:</td>
	             	<td>
	             		<input type="text" id="input5" name="input5" class="normalInput"  toolTip="@demo.between38@"  />
	                </td>
	             </tr>
	             <tr class="darkZebraTr">
	             	<td class="rightBlueTxt">@demo.number@：</td>
	                <td>
	             		<input type="text" id="input6" name="input6" class="normalInput"  toolTip="@demo.acceptNumber@"  />
	                </td>
	             </tr>
	             <tr>
	             	<td class="rightBlueTxt">@demo.integer@：</td>
	             	<td>
	             		<input type="text" id="input7" name="input7" class="normalInput"  toolTip="@demo.acceptInteger@"  />
	                </td>
	             </tr>
	             <tr class="darkZebraTr">
	             	<td class="rightBlueTxt">@demo.range255@：</td>
	             	<td>
	             		<input type="text" id="input8" name="input8" class="normalInput"  toolTip="@demo.range255Tip@"  />
	             	</td>
	             </tr>
	              <tr>
	             	<td class="rightBlueTxt">@demo.mustEqual@：</td>
	             	<td>
	             		<input type="text" id="input9" name="input9" class="normalInput"  toolTip="@demo.mustEqual@"  />
	             	</td>
	             </tr>
	             <tr class="darkZebraTr">
	             	<td class="rightBlueTxt">@demo.date@：</td>
	             	<td>
	             		<input type="text" id="input10" name="input10" class="normalInput"  toolTip="@demo.acceptISO@"  />
	             	</td>
	             </tr>
	              <tr>
	             	<td class="rightBlueTxt">@demo.int@：</td>
	             	<td>
	             		<input type="text" id="input11" name="input11" class="normalInput"  toolTip="@demo.mustInt@"  />
	             	</td>
	             </tr>
	             <tr class="darkZebraTr">
	             	<td class="rightBlueTxt">@demo.mustJpg@：</td>
	             	<td>
	             		<input type="text" id="input12" name="input12" class="normalInput"  toolTip="@demo.eg@"  />
	             	</td>
	             </tr>
	              <tr>
	             	<td class="rightBlueTxt">
	             		字符长度大于4（自定义）:
	             	</td>
	             	<td>
	             		<input type="text" id="input13" name="input13" class="normalInput"  toolTip="字符长度大于4"  />
	             	</td>
	             </tr>
	            <!--  <tr class="darkZebraTr">
	             	<td class="rightBlueTxt">
	             	
	             	</td>
	             	<td>
	             	
	             	</td>
	             </tr>
	              <tr>
	             	<td class="rightBlueTxt">
	             	
	             	</td>
	             	<td>
	             	
	             	</td>
	             </tr>
	             <tr class="darkZebraTr">
	             	<td class="rightBlueTxt">
	             	
	             	</td>
	             	<td>
	             	
	             	</td>
	             </tr>
	              <tr>
	             	<td class="rightBlueTxt">
	             	
	             	</td>
	             	<td>
	             	
	             	</td>
	             </tr> -->
	             <tr class="darkZebraTr">
	             	<td class="rightBlueTxt">
	             	
	             	</td>
	                <td>
	             		<a id="okBtn" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSaveOK"></i>@demo.commit@</span></a>
	                </td>
	             </tr>
			</table>		
		</div>
	</div>
	</form>
<script type="text/javascript" src="/js/jquery/Nm3kMsg.js"></script>
<script type="text/javascript">
	$(function(){
		 //正整数;
		jQuery.validator.addMethod("positiveInteger", function(value, element) {
			var reg = new RegExp("^[0-9]*[1-9][0-9]*$");	
			return this.optional(element) || reg.test(value);
		},"@demo.mustInt@");
		
		//jpg或png后缀;
		jQuery.validator.addMethod("jpgPng", function(value, element) {
		var reg = new RegExp("(.*)(\.jpg|\.png)$","i");
			return this.optional(element) || reg.test(value);
		},"@demo.mustJpg@"); 
		
		//字符长度大于某个数;
		jQuery.validator.addMethod("bigX", function(value, element, para) {  
			var v = false;		
			if($(element).val().length > para){	v = true;}
		    return this.optional(element) || v;    
		}, jQuery.format("输入内容长度大于{0}")); 
		
		$("#form1").validate({			
			//onfocusout :false,
			rules:{
				input2:{required:true,minlength:1},
				input3:{required:true,email:true},
				input4:{required:true,url:true},
				input5:{required:true, rangelength:[3,8]},
				input6:{required:true,number:true},
				input7:{required:true,digits:true},
				input8:{required:true,range:[0,255],digits:true},
				input9:{required:true,equalTo:"#input8"},
				input10:{required:true,dateISO:true},
				input11:{required:true,positiveInteger:true},
				input12:{required:true, jpgPng:true},
				input13:{required:true, bigX:4}
			},
			highlight: function(element, errorClass) {  		
				$(element).addClass("normalInputRed");
			},
			unhighlight: function(element, errorClass) {  
				$(element).removeClass("normalInputRed");
			}
		});//end validate;
		
		//点击提交按钮;
		var msg;
		$("#okBtn").click(function(){
			if($("#form1").valid()){				
				alert("@demo.commitOk@");
			}else{
				var num = $("#form1").validate().numberOfInvalids();			
				$(":text.normalInputRed").eq(0).focus();
				var text = "@demo.txt@";
				
				if($("#msg").length == 0){
					 msg= new Nm3kMsg({
				         html: text,
				         title: "@demo.validateOk@",
				         timeLoading : true,
				         id: "msg",
				         unique: true
				     });
				     msg.init();
				}else{
					msg.update({
						title: "@demo.validateOk@",
						html: text						
					})					
				}
			}
		});
	});//end document.ready;	 
</script>
	
</body>
</Zeta:HTML>
