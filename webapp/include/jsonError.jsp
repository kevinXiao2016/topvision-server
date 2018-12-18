<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page isErrorPage="true"%>
<%@ page import="org.json.JSONObject" %>
<%
	JSONObject json = new JSONObject();
	json.put("success", false);
	json.put("message", exception == null ? "" : exception.getMessage());
	json.put("exception", exception == null ? "" : exception.getStackTrace());
	json.write(response.getWriter());
%>
