<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2012-8-6
  Time: 9:05:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <script type="text/javascript" src="../js/jquery/jquery.js"></script>
    <script type="text/javascript">
    function changeLanguage(lang){
        $.ajax({
            url: '/include/changeLanguage.tv?userId='+userId+'&lang='+lang+'&r=' + Math.random(),
            type: 'POST',
            dataType:'json',
            success: function() {
            }
        });
        window.parent.location.reload(); ;
    }
    </script>
  </head>
  <body>Place your content here</body>
</html>