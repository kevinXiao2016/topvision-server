<meta http-equiv="Pragma" CONTENT="no-cache" />
<meta http-equiv="Cache-Control" CONTENT="no-cache" />
<meta http-equiv="Expires" CONTENT="-1" />
<%@ page import="com.topvision.platform.SystemConstants" %>
<%@ page import="com.topvision.platform.service.ThemeManager" %>
<%@ page import="com.topvision.platform.domain.UserContext" %>
<%
	SystemConstants sc = SystemConstants.getInstance();
	String lang = sc.getStringParam("language", "zh_CN");

	String cssStyleName = ThemeManager.getDefaultTheme();
	UserContext uc = null;
	if (session != null) {
		uc = (UserContext) session.getAttribute(UserContext.KEY);
		if (uc != null && uc.getStyleName() != null) {
			cssStyleName = uc.getStyleName();   
		}
	}
%>
<script type="text/javascript">
var Topvision = {version : 1.0, lang : '<%= lang %>'};
var userName = '<%= uc == null ? "" : uc.getUser().getUserName() %>';
var cssStyleName = '<%= cssStyleName %>';
var chkSelModel = false;
var animCollapse = false;
var stripeRows = false;	
var useArrows = <%= "apple".equals(cssStyleName) || "vista".equals(cssStyleName) || "live".equals(cssStyleName) %>;
var trackMouseOver = <%= "vista".equals(cssStyleName) %>;
var enableMenuScrolling = false;
var displayInputTip = <%= uc == null ? true : uc.isDisplayInputTip() %>;
document.oncontextmenu = function(){
	return false;
}
</script>
<link rel="stylesheet" type="text/css" href="/css/gui.css" />
<link rel="stylesheet" type="text/css" href="/css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/mytheme.css" />
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/ext/ext-lang-<%= lang %>.js"></script>
