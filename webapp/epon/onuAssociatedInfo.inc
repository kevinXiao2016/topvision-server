	<div id="versionSidePart" style="padding: 0px;">
		<div class="edge10">
			<dl class="pannel-container">
				<dd class="catvCapability" id="catvCapability1">
					<div class="catvLightRect blueTxt">
						CATV<br />@ONU.LightRectPlatform@
					</div>
				</dd>
				<dt id="oltInfoContainer">
					<div class="onuListBroad">
						<div class="onuListTitL">
							<div class="onuListTitR">
								<div class="onuListTitC">
									<p class="pT10 blueTxt">OLT-<b class="blueTxt" id = "oltName"></b></p>
								</div>
							</div>
						</div>
						<div class="onuListBody">
							<table cellpadding="0" cellspacing="0" width="100%" border="0" rules="none" class="sideTable">
								<tbody>
									<tr>
										<td class="pB2"><label class="blueTxt">@ONU.ponPort@@COMMON.maohao@</label><span id = "ponIndex">--<span></td>
									</tr>
									<tr>
										<td class="pB2"><label class="blueTxt">@ONU.rateInDirection@@COMMON.maohao@</label><span id = "inFlow">--Mbps</td>
									</tr>
									<tr>
										<td class="pB2"><label class="blueTxt">@ONU.rateOutDirection@@COMMON.maohao@</label><span id = "outFlow">--Mbps</span></td>
									</tr>
									<tr>
										<td class="pB2"><label class="blueTxt">@ONU.inlineRate@@COMMON.maohao@</label> <span id = "onuNum">0/0</span></td>
									</tr>
								</tbody>
							</table>
						</div>
						<div class="onuListFootL">
							<div class="onuListFootR">
								<div class="onuListFootC"></div>
							</div>
						</div>
					</div>
				</dt>
			</dl>
			<dl class="pannel-container">
				<dd class="catvCapability" id="catvCapability2">
					<ol class="leftFloatOl">
						<li class="onuListMiddleArr" style="margin-top:18px; width:22px; background-position:-22px 0px;"></li>
						<li class="pL5">
							<p class="blueTxt pT30">CATV</p>
							<p class="blueTxt">@ONU.receivePower@</p>
							<p id="onuCatvOrInfoRxPower">--</p>
						</li>
					</ol>
				</dd>
				<dt id="opticalInfoContainer">
					<ol class="leftFloatOl">
						<li class="pT5 pR5 leftLiExpand">
							<p class="rightBlueTxt">@ONU.receivePower@</p>
							<p class="pB5 txtRight" id = "oltPonRevPower">--</p>
							<p class="rightBlueTxt" style="margin-top:5px;margin-bottom:10px;">@downlink.prop.testDistance@@COMMON.maohao@</span><span style="color:black;" id = "testDistance">--</span></p>
							<p class="rightBlueTxt">@ONU.sendPower@</p>
							<p class="txtRight" id = "onuPonTransPower">--</p>
						</li>
						<li class="onuListMiddleArr" style="margin-top:18px;"></li>
						<li  class="pT5 pL5">
							<p class="blueTxt">@ONU.sendPower@</p>
							<p class="pB5" id = "oltPonTransPower">--</p>
							<p>&nbsp;</p>
							<p class="pB5">&nbsp;</p>
							<p class="blueTxt">@ONU.receivePower@</p>
							<p id = "onuPonRevPower">--</p>
						</li>
					</ol>
				</dt>
			</dl>
			<div class="onuListBroad" style="padding-top:10px;">
				<div class="onuListTitL">
					<div class="onuListTitR">
						<div class="onuListTitC">
							<p class="pT10"><b class="pR10">ONU <span id = "onuName">--</span><span id = "onuType">(--)</span></b><span id="modify"></span></p>
						</div>
					</div>
				</div>
				<div class="onuListBody">
					<table cellpadding="0" cellspacing="0" width="100%" border="0" rules="none" class="sideTable">
						<tr>
							<td class="pB2"><label class="blueTxt"><span id='onuLabel'></span></label><span id='onuUniq'></span></td>
							<td class="pB2"><label class="blueTxt">@ONU.cpeNum@@COMMON.maohao@</label><span id='cpeNum'></span></td>
						</tr>
						<tr>
							<td colspan="2">
								<label class="blueTxt">@ONU.address@@COMMON.maohao@</label><span id='onuLocation'></span>
							</td>
						</tr>
					</table>
					<ol class="leftFloatOl pB10 pT10 disable-state" id="operList">
					    <li class="mR5 mB5"><a href="javascript:refreshOnu();" class="normalBtn"><span><i class="miniIcoRefresh"></i>@COMMON.refresh@</span></a></li>
					    <li class="mR5 mB5"><a href="javascript:tagOnu();" class="normalBtn"><span><i class="miniIcoCmd"></i>@ONU.tag@</span></a></li>
					    <li class="mR5 mB5"><a href="javascript:unregisterOnu();" class="normalBtn"><span id='onuActive'><i class="miniIcoWrong"></i>@ONU.unregister@</span></a></li>
					    <li class="mR5 mB5"><a href="javascript:resetOnu();" class="normalBtn"><span><i class="miniIcoBack"></i>@COMMON.restore@</span></a></li>
					    <li class="mR5 mB5"><a href="javascript:deleteOnu();" class="normalBtn"><span><i class="miniIcoClose"></i>@COMMON.delete@</span></a></li>
					    <li id="attention" class="mR5 mB5"><a href="javascript:attentionOnu();" class="normalBtn"><span><i class="bmenu_eyes"></i>@ONU.attention@</span></a></li>
					    <li id="cancelAttention" class="mR5 mB5" style="display: none;"><a href="javascript:attentionOnu();" class="normalBtn"><span><i class="bmenu_eyesClose"></i>@ONU.cancelAttention@</span></a></li>
					</ol>
					
					<table class="dataTableRows mT10" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
						<thead>
							<tr>
								<th colspan="4" class="txtLeftTh">@ONU.uniPort@</th>
							</tr>
						</thead>
						<tbody id="portVlan">
						</tbody>
					</table>
					<table class="dataTableRows mT10 wlanCapability" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
						<thead>
							<tr>
								<th colspan="5" class="txtLeftTh">@ONU.wanInfo@</th>
							</tr>
						</thead>
						<tbody id="onuWan">
							
						</tbody>
					</table>
					<table class="dataTableRows mT10 wlanCapability" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
						<thead>
							<tr>
								<th colspan="5" class="txtLeftTh">@ONU.wlanInfo@</th>
							</tr>
						</thead>
						<tbody id="onuWifi">
						</tbody>
					</table>
				</div>
				<div class="onuListFootL">
					<div class="onuListFootR">
						<div class="onuListFootC"></div>
					</div>
				</div>
			</div>
		</div>
		<div class="sideLoading"><p><img src="/images/refreshing.gif" />@COMMON.loading@</p></div>
	</div>
	<div class="versionSideEnglish" id="versionSideArrow">
		<div class="versionSideArrLeft" id="arrow"></div>
		<div class="verticalTxt" id="verticalTxt"></div>
	</div>