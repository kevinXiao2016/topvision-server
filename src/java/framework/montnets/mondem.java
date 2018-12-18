/***********************************************************************
 * $Id: mondem.java,v 1.1 2013-3-11-上午11:28:39 Victor Exp $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011-2010 Victor All rights reserved.
 ***********************************************************************/
package montnets;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.framework.EnvironmentConstants;

/**
 * 短信猫的接口，由于dll是厂家给的，包名不能改变
 * 
 * @author Victor
 * @created @2013-3-11-上午11:28:39
 * 
 */
public class mondem {
    private static Logger logger = LoggerFactory.getLogger(mondem.class);
    // load native dll
    static {
        try {
            System.load(EnvironmentConstants.getEnv(EnvironmentConstants.DLL_HOME) + File.separatorChar + "mondem.dll");
        } catch (UnsatisfiedLinkError ule) {
            logger.error(
                    "native lib 'mondem.dll' not found in " + "'java.library.path': "
                            + EnvironmentConstants.getEnv(EnvironmentConstants.DLL_HOME) + File.separatorChar
                            + "mondem.dll", ule);
        }
    }

    public native int SetModemType(int ComNo, int ModemType);

    public native int GetModemType(int ComNo);

    public native int InitModem(int PortNo);

    public native int SendMsg(int PortNo, String strHeader, String strMsg);

    public native String[] ReadMsgEx(int PortNo);

    public native int CloseModem(int PortNo);

    public native int GetPortMax();

    public native int GetStatus(int PortNo);

    public native int GetSndCount(int PortNo);

    public native int GetRecCount(int PortNo);

    public native int ClrSndBuf(int PortNo);

    public native int ClrRecBuf(int PortNo);

    public native int SetReceive(int Type);

    public native int CancelSend(int Count);

    public native int SetDelayTime(int PortNo, int DelayTime);

    public native String[] WapPushCvt(String strTitle, String strUrl);

    public native int SetThreadMode(int Mode);

    // public native int MonInitModem(String strDev,int num);
    // public native int MonSendMsg(int Chno,String strHeader,String strMsg);
    // public native String[] MonGetMsg(int Chno);
    // public native int MonCloseModem();
}