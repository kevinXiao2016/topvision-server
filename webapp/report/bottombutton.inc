<%@ page language="java" pageEncoding="UTF-8"%>
<tr id=footer><td align=center height=40px>
<button type=button 
	class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
	onMouseOut="this.className='BUTTON75'" onMouseDown="this.className='BUTTON_PRESSED75'"
	onclick="createPdfClick()"><fmt:message bundle="${report}" key="report.genPDF" /></button>&nbsp;<button type=button 
	class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
	onMouseOut="this.className='BUTTON75'" onMouseDown="this.className='BUTTON_PRESSED75'"
	onclick="onPrintviewClick()"><fmt:message bundle="${report}" key="report.printPreview" /></button>&nbsp;
<button type=submit class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
	onMouseOut="this.className='BUTTON75'" onMouseDown="this.className='BUTTON_PRESSED75'"
	onclick="onPrintClick()"><fmt:message bundle="${report}" key="report.print" /></button>&nbsp;<button id="optionBt1"
	class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
	onMouseOut="this.className='BUTTON75'" onMouseDown="this.className='BUTTON_PRESSED75'"
	onclick="optionClick()"><fmt:message bundle="${report}" key="report.options" /> »</button>
</td></tr>	