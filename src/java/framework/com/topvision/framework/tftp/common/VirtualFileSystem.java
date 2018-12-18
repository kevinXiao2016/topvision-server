/**
 * (c) Melexis Telecom and/or Remote Operating Services B.V.
 * 
 * Distributable under LGPL license
 * See terms of license at gnu.org
 */
package com.topvision.framework.tftp.common;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;

/**
 * @author marco
 * 
 *         To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go
 *         to Window>Preferences>Java>Code Generation.
 */
public interface VirtualFileSystem {
    public File expand(String location) throws FileNotFoundException;
    public InputStream getInputStream(VirtualFile file) throws FileNotFoundException;

    public OutputStream getOutputStream(VirtualFile file) throws FileNotFoundException;
}
