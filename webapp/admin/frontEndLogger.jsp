<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    LIBRARY ext
    LIBRARY jquery
    LIBRARY zeta
    IMPORT js/TopvisionSocket/TopvisionSocket
</Zeta:Loader>
<style type="text/css">
body,html{ width:100%; height:100%; overflow:hidden;}
</style>
<script type="text/javascript">
    $(function(){
    	$('#openSwitch').on('click', function() {
    		$.ajax({
    			url: '/admin/openFrontEndLog.tv',
    	        cache: false, 
    	        timeout: 10000,
    	        method:'post',
    	        success: function(response) {
    	          $('#switchStatus').text(true);  
    	          $('#openSwitch').attr('disabled', true);
    	          $('#closeSwitch').attr('disabled', false);
    	        },
    	        error: function(){
    	        }
    		});
    	})
    	
    	$('#closeSwitch').on('click', function() {
            $.ajax({
                url: '/admin/closeFrontEndLog.tv',
                cache: false, 
                timeout: 10000,
                method:'post',
                success: function(response) {
                  $('#switchStatus').text(false);  
                  $('#openSwitch').attr('disabled', false);
                  $('#closeSwitch').attr('disabled', true);
                },
                error: function(){
                }
            });
        })
    })
</script>
</head>
<body class="whiteToBlack">
    <p>当前开启状态：<span id="switchStatus">${frontEndLogSwitch}</span> </p>
    <button id="openSwitch">开启</button>
    <button id="closeSwitch">关闭</button>
</body>
</Zeta:HTML>
