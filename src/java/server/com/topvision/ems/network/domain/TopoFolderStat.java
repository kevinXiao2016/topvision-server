package com.topvision.ems.network.domain;

import java.util.List;
import java.util.ArrayList;

public class TopoFolderStat extends TopoFolder {
    private static final long serialVersionUID = -6998142597495963791L;
    private List<TopoFolderNum> cols = new ArrayList<TopoFolderNum>();

    public List<TopoFolderNum> getCols() {
        return cols;
    }

    public void setCols(List<TopoFolderNum> cols) {
        this.cols = cols;
    }
}
