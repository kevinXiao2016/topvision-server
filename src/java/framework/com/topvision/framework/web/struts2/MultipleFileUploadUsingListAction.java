package com.topvision.framework.web.struts2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

public class MultipleFileUploadUsingListAction extends ActionSupport {

    private static final long serialVersionUID = -5437134469018307804L;

    private List<File> uploads = new ArrayList<File>();

    private List<String> uploadFileNames = new ArrayList<String>();

    private List<String> uploadContentTypes = new ArrayList<String>();

    public List<File> getUpload() {
        return this.uploads;
    }

    public List<String> getUploadContentType() {
        return this.uploadContentTypes;
    }

    public List<String> getUploadFileName() {
        return this.uploadFileNames;
    }

    public void setUpload(List<File> uploads) {
        this.uploads = uploads;
    }

    public void setUploadContentType(List<String> contentTypes) {
        this.uploadContentTypes = contentTypes;
    }

    public void setUploadFileName(List<String> uploadFileNames) {
        this.uploadFileNames = uploadFileNames;
    }

    public String upload() throws Exception {

        System.out.println("\n\n upload1");
        System.out.println("files:");
        for (File u : uploads) {
            System.out.println("*** " + u + "\t" + u.length());
        }
        System.out.println("filenames:");
        for (String n : uploadFileNames) {
            System.out.println("*** " + n);
        }
        System.out.println("content types:");
        for (String c : uploadContentTypes) {
            System.out.println("*** " + c);
        }
        System.out.println("\n\n");
        return SUCCESS;
    }

}
