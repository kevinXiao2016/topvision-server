/***********************************************************************
 * $ TelnetIO.java,v1.0 2013-2-26 15:06:02 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.telnet;

import java.net.Socket;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Vector;
import java.awt.*;

/**
 * @author jay
 * @created @2013-2-26-15:06:02
 */
public class TelnetIO {
    /**
     * State variable for telnetnegotiation reader
     */
    private byte neg_state = 0;

    /**
     * constants for the negotiation state
     */
    private final static byte STATE_DATA = 0;
    private final static byte STATE_IAC = 1;
    private final static byte STATE_IACSB = 2;
    private final static byte STATE_IACWILL = 3;
    private final static byte STATE_IACDO = 4;
    private final static byte STATE_IACWONT = 5;
    private final static byte STATE_IACDONT = 6;
    private final static byte STATE_IACSBIAC = 7;
    private final static byte STATE_IACSBDATA = 8;
    private final static byte STATE_IACSBDATAIAC = 9;

    /**
     * What IAC SB <xx> we are handling right now
     */
    private byte current_sb;

    /**
     * IAC - init sequence for telnet negotiation.
     */
    private final static byte IAC = (byte) 255;
    /**
     * [IAC] End Of Record
     */
    private final static byte EOR = (byte) 239;
    /**
     * [IAC] WILL
     */
    private final static byte WILL = (byte) 251;
    /**
     * [IAC] WONT
     */
    private final static byte WONT = (byte) 252;
    /**
     * [IAC] DO
     */
    private final static byte DO = (byte) 253;
    /**
     * [IAC] DONT
     */
    private final static byte DONT = (byte) 254;
    /**
     * [IAC] Sub Begin
     */
    private final static byte SB = (byte) 250;
    /**
     * [IAC] Sub End
     */
    private final static byte SE = (byte) 240;
    /**
     * Telnet option: echo text
     */
    private final static byte TELOPT_ECHO = (byte) 1;  /* echo on/off */
    /**
     * Telnet option: End Of Record
     */
    private final static byte TELOPT_EOR = (byte) 25;  /* end of record */
    /**
     * Telnet option: Negotiate About Window Size
     */
    private final static byte TELOPT_NAWS = (byte) 31;  /* NA-WindowSize*/
    /**
     * Telnet option: Terminal Type
     */
    private final static byte TELOPT_TTYPE = (byte) 24;  /* terminal type */
    private final static byte[] TELOPT_BTTYPE = "vt100".getBytes();  /* terminal type */

//    private final static byte[] IACWILL  = { IAC, WILL };
//    private final static byte[] IACWONT  = { IAC, WONT };
//    private final static byte[] IACDO    = { IAC, DO	};
    //    private final static byte[] IACDONT  = { IAC, DONT };
    private final static byte[] IACSB = {IAC, SB};
    private final static byte[] IACSE = {IAC, SE};

    /**
     * Telnet option qualifier 'IS'
     */
    private final static byte TELQUAL_IS = (byte) 0;

    /**
     * Telnet option qualifier 'SEND'
     */
    private final static byte TELQUAL_SEND = (byte) 1;

    /**
     * What IAC DO(NT) request do we have received already ?
     */
    private byte[] receivedDX;

    /**
     * What IAC WILL/WONT request do we have received already ?
     */
    private byte[] receivedWX;
    /**
     * What IAC DO/DONT request do we have sent already ?
     */
    private byte[] sentDX;
    /**
     * What IAC WILL/WONT request do we have sent already ?
     */
    private byte[] sentWX;

    private Socket socket = null;
    private BufferedInputStream is = null;
    private BufferedOutputStream os = null;

    /**
     * Connect to the remote host at the specified port.
     *
     * @param address the symbolic host address
     * @param port    the numeric port
     * @see #disconnect
     * @throws java.io.IOException     IOException
     */
    public void connect(String address, int port) throws IOException {
        socket = new Socket(address, port);
        is = new BufferedInputStream(socket.getInputStream());
        os = new BufferedOutputStream(socket.getOutputStream());
        neg_state = 0;
        receivedDX = new byte[256];
        sentDX = new byte[256];
        receivedWX = new byte[256];
        sentWX = new byte[256];
//        byte bufff[] = new byte[256];
    }

    /**
     * Disconnect from remote host.
     *
     * @see #connect
     * @throws java.io.IOException  IOException
     */
    public void disconnect() throws IOException {
        if (socket != null)
            socket.close();
    }

    /**
     * Connect to the remote host at the default telnet port (23).
     *
     * @param address the symbolic host address
     * @throws java.io.IOException        IOException
     */
    public void connect(String address) throws IOException {
        connect(address, 23);
    }

    /**
     * Returns bytes available to be read.  Since they haven't been
     * negotiated over, this could be misleading.
     * Most useful as a boolean value - "are any bytes available" -
     * rather than as an exact count of "how many ara available."
     *
     * @throws IOException on problems with the socket connection
     * @return int
     */
    public int available() throws IOException {
        return is.available();
    }

    /**
     * Read data from the remote host. Blocks until data is available.
     * Returns an array of bytes.
     *
     * @see #send
     * @return byte[]
     * @throws java.io.IOException     IOException
     */
    public byte[] receive() throws IOException {
        int count = is.available();
        byte buf[] = new byte[count];
        count = is.read(buf);
        if (count < 0) throw new IOException("Connection closed.");
        if (count > 0) {
            buf = negotiate(buf, count);
        }
        return buf;
    }

    /**
     * Send data to the remote host.
     *
     * @param buf array of bytes to send
     * @see #receive
     * @throws java.io.IOException IOException
     */
    public void send(byte[] buf) throws IOException {
        if (os != null) {
            os.write(buf);
            os.flush();
        } else {
            throw new IOException("telnet has not login.");
        }
    }

    /**
     * send data to remote host
     *
     * @param b of byte to send
     * @throws java.io.IOException IOException
     */
    public void send(byte b) throws IOException {
        os.write(b);
        os.flush();
    }

    /**
     * Handle an incoming IAC SB <type> <bytes> IAC SE
     *
     * @param type    type of SB
     * @param sbdata  byte array as <bytes>
     * @param sbcount nr of bytes. may be 0 too.
     * @throws java.io.IOException IOException
     */
    private void handle_sb(byte type, byte[] sbdata, int sbcount) throws IOException {
        switch (type) {
            case TELOPT_TTYPE:
                if (sbcount > 0 && sbdata[0] == TELQUAL_SEND) {
                    send(IACSB);
                    send(TELOPT_TTYPE);
                    send(TELQUAL_IS);
                    send(TELOPT_BTTYPE);
                    send(IACSE);
                }//end if
        }//end switch
    }

    /**
     * wo faengt buf an bei buf[0] oder bei buf[1]
     * @param buf buf
     * @param count count
     * @return byte[]
     * @throws java.io.IOException    IOException
     */
    @SuppressWarnings("unused")
    private byte[] negotiate(byte buf[], int count) throws IOException {
        byte nbuf[] = new byte[count];
        byte sbbuf[] = new byte[count];
        byte sendbuf[] = new byte[3];
        byte b, reply;
        int sbcount = 0;
        int boffset = 0, noffset = 0;
        Vector<String> vec;

        while (boffset < count) {
            b = buf[boffset++];
            /* of course, byte is a signed entity (-128 -> 127)
            * but apparently the SGI Netscape 3.0 doesn't seem
            * to care and provides happily values up to 255
            */
            if (b >= 128)
                b = (byte) ((int) b - 256);
            switch (neg_state) {
                case STATE_DATA:
                    if (b == IAC) {
                        neg_state = STATE_IAC;
                    } else {
                        nbuf[noffset++] = b;
                    }
                    break;
                case STATE_IAC:
                    switch (b) {
                        case IAC:
                            neg_state = STATE_DATA;
                            nbuf[noffset++] = IAC;
                            break;
                        case WILL:
                            neg_state = STATE_IACWILL;
                            break;
                        case WONT:
                            neg_state = STATE_IACWONT;
                            break;
                        case DONT:
                            neg_state = STATE_IACDONT;
                            break;
                        case DO:
                            neg_state = STATE_IACDO;
                            break;
                        case EOR:
                            neg_state = STATE_DATA;
                            break;
                        case SB:
                            neg_state = STATE_IACSB;
                            sbcount = 0;
                            break;
                        default:
                            neg_state = STATE_DATA;
                            break;
                    }
                    break;
                case STATE_IACWILL:
                    switch (b) {
                        case TELOPT_ECHO:
                            reply = DO;
                            vec = new Vector<String>(2);
                            vec.addElement("NOLOCALECHO");
                            break;
                        case TELOPT_EOR:
                            reply = DO;
                            break;
                        default:
                            reply = DONT;
                            break;
                    }
                    if (reply != sentDX[b + 128] || WILL != receivedWX[b + 128]) {
                        sendbuf[0] = IAC;
                        sendbuf[1] = reply;
                        sendbuf[2] = b;
                        send(sendbuf);
                        sentDX[b + 128] = reply;
                        receivedWX[b + 128] = WILL;
                    }
                    neg_state = STATE_DATA;
                    break;
                case STATE_IACWONT:
                    switch (b) {
                        case TELOPT_ECHO:
                            vec = new Vector<String>(2);
                            vec.addElement("LOCALECHO");
                            reply = DONT;
                            break;
                        case TELOPT_EOR:
                            reply = DONT;
                            break;
                        default:
                            reply = DONT;
                            break;
                    }
                    if (reply != sentDX[b + 128] || WONT != receivedWX[b + 128]) {
                        sendbuf[0] = IAC;
                        sendbuf[1] = reply;
                        sendbuf[2] = b;
                        send(sendbuf);
                        sentDX[b + 128] = reply;
                        receivedWX[b + 128] = WILL;
                    }
                    neg_state = STATE_DATA;
                    break;
                case STATE_IACDO:
                    switch (b) {
                        case TELOPT_ECHO:
                            reply = WILL;
                            vec = new Vector<String>(2);
                            vec.addElement("LOCALECHO");
                            break;
                        case TELOPT_TTYPE:
                            reply = WILL;
                            break;
                        case TELOPT_NAWS:
                            vec = new Vector<String>(2);
                            vec.addElement("NAWS");
                            Dimension size = null;
                            receivedDX[b] = DO;
                            if (size == null) {
                                // this shouldn't happen
                                send(IAC);
                                send(WONT);
                                send(TELOPT_NAWS);
                                reply = WONT;
                                sentWX[b] = WONT;
                                break;
                            }
                            reply = WILL;
                            sentWX[b] = WILL;
                            sendbuf[0] = IAC;
                            sendbuf[1] = WILL;
                            sendbuf[2] = TELOPT_NAWS;
                            send(sendbuf);
                            send(IAC);
                            send(SB);
                            send(TELOPT_NAWS);
                            send((byte) (size.width >> 8));
                            send((byte) (size.width & 0xff));
                            send((byte) (size.height >> 8));
                            send((byte) (size.height & 0xff));
                            send(IAC);
                            send(SE);
                            break;
                        default:
                            reply = WONT;
                            break;
                    }
                    if (reply != sentWX[128 + b] || DO != receivedDX[128 + b]) {
                        sendbuf[0] = IAC;
                        sendbuf[1] = reply;
                        sendbuf[2] = b;
                        send(sendbuf);
                        sentWX[b + 128] = reply;
                        receivedDX[b + 128] = DO;
                    }
                    neg_state = STATE_DATA;
                    break;
                case STATE_IACDONT:
                    switch (b) {
                        case TELOPT_ECHO:
                            reply = WONT;
                            vec = new Vector<String>(2);
                            vec.addElement("NOLOCALECHO");
                            break;
                        case TELOPT_NAWS:
                            reply = WONT;
                            break;
                        default:
                            reply = WONT;
                            break;
                    }
                    if (reply != sentWX[b + 128] || DONT != receivedDX[b + 128]) {
                        send(IAC);
                        send(reply);
                        send(b);
                        sentWX[b + 128] = reply;
                        receivedDX[b + 128] = DONT;
                    }
                    neg_state = STATE_DATA;
                    break;
                case STATE_IACSBIAC:
                    if (b == IAC) {
                        sbcount = 0;
                        current_sb = b;
                        neg_state = STATE_IACSBDATA;
                    } else {
                        neg_state = STATE_DATA;
                    }
                    break;
                case STATE_IACSB:
                    switch (b) {
                        case IAC:
                            neg_state = STATE_IACSBIAC;
                            break;
                        default:
                            current_sb = b;
                            sbcount = 0;
                            neg_state = STATE_IACSBDATA;
                            break;
                    }
                    break;
                case STATE_IACSBDATA:
                    switch (b) {
                        case IAC:
                            neg_state = STATE_IACSBDATAIAC;
                            break;
                        default:
                            sbbuf[sbcount++] = b;
                            break;
                    }
                    break;
                case STATE_IACSBDATAIAC:
                    switch (b) {
                        case IAC:
                            neg_state = STATE_IACSBDATA;
                            sbbuf[sbcount++] = IAC;
                            break;
                        case SE:
                            handle_sb(current_sb, sbbuf, sbcount);
                            current_sb = 0;
                            neg_state = STATE_DATA;
                            break;
                        case SB:
                            handle_sb(current_sb, sbbuf, sbcount);
                            neg_state = STATE_IACSB;
                            break;
                        default:
                            neg_state = STATE_DATA;
                            break;
                    }
                    break;
                default:
                    neg_state = STATE_DATA;
                    break;
            }
        }
        buf = new byte[noffset];
        System.arraycopy(nbuf, 0, buf, 0, noffset);
        return buf;
    }
}