	<div id="topPart">
		<!-- 查询DIV -->
		<div id="query-container">
			<div id="simple-toolbar-div">
				<table class="queryTable">
					<tr>
						<td><input type="text" class="normalInput" id="queryContent"
							placeHolder="@ONU.queryTip@" maxlength="63" /></td>
						<td><a id="simple-query" href="javascript:;"
							class="normalBtn" onclick="simpleQuery()"><span><i
									class="miniIcoSearch"></i>@COMMON.query@</span></a></td>
						<td><a href="#" id="advance-query-link"
							onclick="showAdvanceQuery()">@ONU.advanceSearch@</a></td>
					</tr>
				</table>
			</div>
			<div id="advance-toolbar-div" style="display: none;">
				<table class="queryTable">
					<tr>
						<td class="rightBlueTxt">@COMMON.alias@:</td>
						<td class="pR10">
							<input type="text" class="normalInput" style="width: 150px" id="nameInput" maxlength="63" />
						</td>
						<td class="rightBlueTxt w50">MAC/SN:</td>
						<td class="pR10">
							<input type="text" class="normalInput w150" id="macInput" />
						</td>
						<td class="rightBlueTxt w80 noPonPattern">@ONU.ponPattern@:</td>
						<td class="noPonPattern">
							<select class="normalSel w150" id='ponPattern'>
								<option value="-1">@COMMON.select@</option>
								<option value="G">GPON</option>
								<option value="E">EPON</option>
							</select>
						</td>
						<td class="rightBlueTxt w80 noOnuTag">@ONU.tag@:</td>
						<td class="noOnuTag">
							<input type="text" style="width: 150px" id="onuTag" />
						</td>
						<td class="rightBlueTxt w80 ">ONU TYPE:</td>
						<td class="pR10">
							<input type="text" style="width: 150px" id="typeSelect" />
						</td>
						<td class="" colspan="1"></td>
						<td class="" valign="middle" rowspan="3" >
							<ul class="leftFloatUl">
								<li>
									<a href="javascript:;" class="normalBtn"style="margin-right: 5px;" onclick="onSeachClick()"><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a></li>
								<li>
									<a href="javascript:;" style="position: relative;top:4px;" onclick="showSimpleQuery()"><span>@ccm/CCMTS.CmList.quickSearch@</span></a>
								</li>
							</ul>
						</td>
					</tr>
					<tr>
						<td class="rightBlueTxt">OLT:</td>
						<td class="pR10">
							<div style="width: 152px" id="oltContainer"></div>
						</td>
						<td class="rightBlueTxt">@COMMON.slot@:</td>
						<td class="pR10">
							<input type="text" class="" style="width: 150px" id="slotSelect" />
						</td>
						<td class="rightBlueTxt">@COMMON.pon@:</td>
						<td class="pR10">
							<input type="text" class="" style="width: 124px" id="ponSelect" />
						</td>
						<td class="rightBlueTxt pR10">@COMMON.status@:</td>
						<td class="">
							<select type="text" class="normalSel"  id="statusSelect">
							<option value="-1">@COMMON.all@</option>
							<option value="1">@COMMON.online@</option>
							<option value="2">@COMMON.offline@</option>
						</select></td>
					</tr>
					<tr class="noPartition">
						<td class="rightBlueTxt">@ONU.partition@:</td>
						<td colspan="7"><div id="partition"></div></td>
					</tr>
				</table>
			</div>
		</div>
	</div>