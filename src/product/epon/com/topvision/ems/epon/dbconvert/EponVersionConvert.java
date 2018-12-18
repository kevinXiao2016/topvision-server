/***********************************************************************
 * $Id: EponVersionConvert.java,v1.0 2012-8-2 下午03:00:28 $
 * 
 * @author: RodJohn
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.dbconvert;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.platform.dbconvert.VersionConvert;
import com.topvision.platform.service.DataManageParser;
import com.topvision.platform.service.DataModule;
import com.topvision.platform.util.DataBaseConstants;

/**
 * @author RodJohn
 * @created @2012-8-2-下午03:00:28
 * 
 */
@Service("eponVersionConvert")
public class EponVersionConvert extends VersionConvert implements DataModule {
    @Autowired
    private DataManageParser dataManageParser;

    public void convertFrom165to170(List<String> sql) {
        for (int i = 0; i < sql.size(); i++) {
            if (sql.get(i).startsWith("INSERT INTO `oltonuattribute`")) {
                sql.set(i, sql.get(i).replaceAll("topOnuCapDeregister", "CapDeregister"));
                sql.set(i, sql.get(i).replaceAll("topOnuCapAddrMaxQuantity", "CapAddrMaxQuantity"));
                sql.set(i, sql.get(i).replaceAll("topOnuPonPerfStats15minuteEnable", "PonPerfStats15minuteEnable"));
                sql.set(i, sql.get(i).replaceAll("topOnuPonPerfStats24hourEnable", "PonPerfStats24hourEnable"));
                sql.set(i, sql.get(i).replaceAll("topOnuTemperatureDetectEnable", "TemperatureDetectEnable"));
                sql.set(i, sql.get(i).replaceAll("topOnuCurrentTemperature", "CurrentTemperature"));
                sql.set(i, sql.get(i).replaceAll("topOnuRstpBridgeMode", "RstpBridgeMode"));
                sql.set(i, sql.get(i).replaceAll("topOnuVoipEnable", "VoipEnable"));
                sql.set(i, sql.get(i).replaceAll("topOnuCatvEnable", "CatvEnable"));
            }
            if (sql.get(i).startsWith("INSERT INTO `oltuniattribute`")) {
                sql.set(i, sql.get(i).replaceAll("uniAutoNegotiationLocalTechAbility", "uniAutoNegLocalTechAbility"));
                sql.set(i, sql.get(i).replaceAll("topUniAttrFlowCtrl", "FlowCtrl"));
                sql.set(i, sql.get(i).replaceAll("topUniAttrPerfStats15minuteEnable", "PerfStats15minuteEnable"));
                sql.set(i, sql.get(i).replaceAll("topUniAttrPerfStats24hourEnable", "PerfStats24hourEnable"));
                sql.set(i, sql.get(i).replaceAll("topUniAttrLastChangeTime", "LastChangeTime"));
                sql.set(i, sql.get(i).replaceAll("topUniAttrIsolationEnable", "IsolationEnable"));
                sql.set(i, sql.get(i).replaceAll("topUniAttrMacAddrLearnMaxNum", "MacAddrLearnMaxNum"));
                sql.set(i,
                        sql.get(i).replaceAll("topUniAttrAutoNegotiationAdvertisedTechAbility",
                                "AutoNegAdvertisedTechAbility"));
                sql.set(i, sql.get(i).replaceAll("topUniAttrMacAddrClearByPort", "MacAddrClearByPort"));
                sql.set(i, sql.get(i).replaceAll("topUniAttrMacAge", "macAge"));
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.DataModule#processModule(java.util.List,
     * java.lang.String, java.lang.String)
     */
    @SuppressWarnings("unused")
    @Override
    public List<String> processModule(List<String> sqlList, String nowVersion, String fileVersion) {
        if (true) {
            String[] tables = DataBaseConstants.EPON_TABLE.split(" ");
            Object[] result = new Object[tables.length];
            for (int i = 0; i < tables.length; i++) {
                List<String> insertList = new ArrayList<String>();
                result[i] = insertList;
                for (String sql : sqlList) {
                    if (getTableNameFromInsertSql(sql).equals(tables[i])) {
                        insertList.add(sql);
                    }
                }
                /*
                 * String[] result = new String[78]; for(String sql : sqlList){
                 * if(sql.startsWith("INSERT INTO `oltattribute`")){ result[0] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltpowerrelation`")){ result[1] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltpowerattribute`")){ result[2] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltpowerstatus`")){ result[3] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltfanrelation`")){ result[4] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltfanattribute`")){ result[5] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltfanstatus`")){ result[6] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltslotrelation`")){ result[7] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltslotattribute`")){ result[8] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltslotstatus`")){ result[9] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltsnirelation`")){ result[10] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltsniattribute`")){ result[11] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltponrelation`")){ result[12] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltponattribute`")){ result[13] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltonurelation`")){ result[14] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltonuattribute`")){ result[15] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltonuponrelation`")){ result[16] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltonuponattribute`")){ result[17] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltunirelation`")){ result[18] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltuniattribute`")){ result[19] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltacllisttable`")){ result[20] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltaclportacllisttable`")){ result[21] = sql;
                 * }else if(sql.startsWith("INSERT INTO `oltaclruleentry`")){ result[22] = sql;
                 * }else if(sql.startsWith("INSERT INTO `oltauthentication`")){ result[23] = sql;
                 * }else if(sql.startsWith("INSERT INTO `oltigmpentityentry`")){ result[24] = sql;
                 * }else if(sql.startsWith("INSERT INTO `oltigmpproxyparaentry`")){ result[25] =
                 * sql; }else
                 * if(sql.startsWith("INSERT INTO `oltigmpcontrolledmulticastpackageentry`")){
                 * result[26] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltigmptopmcoltconfigmgmtobjects`")){ result[27]
                 * = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltigmptopmcsniconfigmgmtobjects`")){ result[28]
                 * = sql; }else if(sql.startsWith("INSERT INTO `oltigmptopmconuentry`")){ result[29]
                 * = sql; }else if(sql.startsWith("INSERT INTO `oltigmptopmconuvlantransentry`")){
                 * result[30] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltigmptopmcuniconfigentry`")){ result[31] = sql;
                 * }else if(sql.startsWith("INSERT INTO `oltigmpmcforwardingslottable`")){
                 * result[32] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltigmpmcforwardingporttable`")){ result[33] =
                 * sql; }else if(sql.startsWith("INSERT INTO `oltigmpmcforwardingonutable`")){
                 * result[34] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltigmpcontrolledmulticastuserauthorityentry`")){
                 * result[35] = sql; }else if(sql.startsWith("INSERT INTO `oltmonitorvalue`")){
                 * result[36] = sql; }else if(sql.startsWith("INSERT INTO `oltonuupgrade`")){
                 * result[37] = sql; }else if(sql.startsWith("INSERT INTO `oltoperatorhistory`")){
                 * result[38] = sql; }else if(sql.startsWith("INSERT INTO `oltopertype`")){
                 * result[39] = sql; }else if(sql.startsWith("INSERT INTO `olttrapconfig`")){
                 * result[40] = sql; }else if(sql.startsWith("INSERT INTO `oltperfthreshold`")){
                 * result[41] = sql; }else if(sql.startsWith("INSERT INTO `perfstatcycle`")){
                 * result[42] = sql; }else if(sql.startsWith("INSERT INTO `perfstatsglobalset`")){
                 * result[43] = sql; }else if(sql.startsWith("INSERT INTO `perfstats15table`")){
                 * result[44] = sql; }else if(sql.startsWith("INSERT INTO `perfstats24table`")){
                 * result[45] = sql; }else if(sql.startsWith("INSERT INTO `oltpononuauthmode`")){
                 * result[46] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltpononuautoauthmode`")){ result[47] = sql;
                 * }else if(sql.startsWith("INSERT INTO `oltvlanconfig`")){ result[48] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltvlanglobalinfo`")){ result[49] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltvlantranslation`")){ result[50] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltvlantrunk`")){ result[51] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltportvlan`")){ result[52] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltponqinq`")){ result[53] = sql; }else
                 * if(sql.startsWith("INSERT INTO `olttopvlanagg`")){ result[54] = sql; }else
                 * if(sql.startsWith("INSERT INTO `olttopvlanqinq`")){ result[55] = sql; }else
                 * if(sql.startsWith("INSERT INTO `olttopvlantrans`")){ result[56] = sql; }else
                 * if(sql.startsWith("INSERT INTO `olttopvlantrunk`")){ result[57] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltvlanagg`")){ result[58] = sql; }else
                 * if(sql.startsWith("INSERT INTO `onuportvlan`")){ result[59] = sql; }else
                 * if(sql.startsWith("INSERT INTO `onuvlanagg`")){ result[60] = sql; }else
                 * if(sql.startsWith("INSERT INTO `onuvlantranslation`")){ result[61] = sql; }else
                 * if(sql.startsWith("INSERT INTO `onuvlantrunk`")){ result[62] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltsnimacaddressmanagement`")){ result[63] = sql;
                 * }else if(sql.startsWith("INSERT INTO `onublockauthen`")){ result[64] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltsniredirect`")){ result[65] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltsnistorminfo`")){ result[66] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltponstorminfo`")){ result[67] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltunistorminfo`")){ result[68] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltvifpriipconfig`")){ result[69] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltvifsubipconfig`")){ result[70] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltstpglobalsettable`")){ result[71] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltstpporttable`")){ result[72] = sql; }else
                 * if(sql.startsWith("INSERT INTO `olttopalarmcodemask`")){ result[73] = sql; }else
                 * if(sql.startsWith("INSERT INTO `olttopalarminstancemask`")){ result[74] = sql;
                 * }else if(sql.startsWith("INSERT INTO `oltslatable`")){ result[75] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltportmirrorconfig`")){ result[76] = sql; }else
                 * if(sql.startsWith("INSERT INTO `oltporttrunkconfig`")){ result[77] = sql; } }
                 */
            }
            return makeList(result);
        }
        return null;
    }

    public List<String> makeList(Object[] result) {
        List<String> rList = new ArrayList<String>();
        for (Object object : result) {
            @SuppressWarnings("unchecked")
            List<String> strings = (List<String>) object;
            if (strings.size() > 0) {
                rList.addAll(strings);
            }
        }
        return rList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.util.VersionConvert#initialize()
     */
    @Override
    @PostConstruct
    public void initialize() {
        setModule();
        dataManageParser.registDataModule(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.util.VersionConvert#destroy()
     */
    @Override
    @PreDestroy
    public void destroy() {
        dataManageParser.unRegistDataModule(this);
    }

    /**
     * @return the dataManageParser
     */
    public DataManageParser getDataManageParser() {
        return dataManageParser;
    }

    /**
     * @param dataManageParser
     *            the dataManageParser to set
     */
    public void setDataManageParser(DataManageParser dataManageParser) {
        this.dataManageParser = dataManageParser;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.DataModule#processModule(java.util.List)
     */
    @Override
    public List<String> processModule(List<String> sqlList) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.util.VersionConvert#setStartVersion()
     */
    @Override
    public void setStartVersion() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.util.VersionConvert#setEndVersion()
     */
    @Override
    public void setEndVersion() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.util.VersionConvert#setPriority()
     */
    @Override
    public void setPriority() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.util.VersionConvert#setModule()
     */
    @Override
    public void setModule() {
        this.module = "EPON";
    }

}
