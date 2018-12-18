<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
	window.top.showMessageDlg(I18N.COMMON.tip, I18N.NETWORK.uploadImageSuccess);
	window.parent.refreshClick(<s:property value="uploadZip"/>);
</script>
