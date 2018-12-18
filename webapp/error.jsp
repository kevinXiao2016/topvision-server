<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module platform
</Zeta:Loader>
</head>
<body>
<div style="margin:5 auto;" align="center">
    <table>
        <tr>
            <td>
                <img src="/images/face-tired.png">
            </td>
            <td align="left">
                <p style="margin: 10px;">@errorTitle@</p>
                <p style="margin: 10px;">@errorCause1@</p>
                <p style="margin: 10px;">@errorCause2@</p>
                <p style="margin: 10px;">@errorCause3@</p>
            </td>
        </tr>
    </table>
</div>
</body>
</Zeta:HTML>
