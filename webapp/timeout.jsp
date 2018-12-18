<%@ page language="java" contentType="text/html;charset=utf-8" %>
<script type="text/javascript">
	if (window.top.standalone) {
		alert("Session timeout!");
		window.top.close();
	} else {
		window.top.location.href = 'showlogon.tv?timeout=true';
	}
</script>
