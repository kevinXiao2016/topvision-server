<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library jquery
	module demo
</Zeta:Loader>
<script type="text/javascript">
	var $output;
	/***********************************************
			程序入口
	***********************************************/
	$( DOC ).ready( initialize );
	//  所有的处理建议都在 页面准备好( domReady )之后进行
	function initialize(){
		$output = $("#output");
		addListener();
	}
	
	/***********************************************
					添加全局事件监听
	***********************************************/
	function addListener(){
		$("#test").click(function(){
			$.ajax({
				url : '/entity/newEntity.tv',cache:false,
				success:function(){
					//TODO 请求执行正常后的处理流程
				},error:function(e, status){
					if( status == RESPONSE_STATUS.NOT_FOUND ){  // 404 请求未找到
						//TODO  action不存在!这个部分可以统一做
					} else if( e.simpleName == "NullPointerException" ){
						//TODO 进行空指针异常的处理
					} else if( e.simpleName == "SnmpNoResponseException" ){
						//TODO 进行snmp无响应异常的处理
					} else if(e.type == "java.sql.SQLException"){  
						//TODO 处理SQL异常
					} else{  // 其他所有的异常
						//TODO 
					}
					//异常的所有信息以及异常类型都在  异常对象e 中
					$output.append(" exception type : " + e.type + "<br>");
					$output.append(" exception message : " + e.message + "<br>");
					$output.append(" status : " + status  + "<br>");
					$output.append(" stackTrace : " + e.stackTrace[0].className+"..." + "<br>");
					$output.append(" others ... " + "<br>");
				}
			});			
		})
	}
</script>
</head>
<body>
	<button class="BUTTON75" id="test">@demo.test@</button>
	<div id="output"></div>
</body>
</Zeta:HTML>