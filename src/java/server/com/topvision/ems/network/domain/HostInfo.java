package com.topvision.ems.network.domain;

import java.util.List;

import com.topvision.ems.resources.domain.Disk;
import com.topvision.ems.resources.domain.Software;
import com.topvision.framework.domain.BaseEntity;

public class HostInfo extends BaseEntity {
    private static final long serialVersionUID = 8941540899706307668L;
    List<RunProcess> processes;
    List<Software> softwares;
    List<Disk> Disks;

    /**
     * @return the disks
     */
    public List<Disk> getDisks() {
        return Disks;
    }

    /**
     * @return the processes
     */
    public List<RunProcess> getProcesses() {
        return processes;
    }

    /**
     * @return the softwares
     */
    public List<Software> getSoftwares() {
        return softwares;
    }

    /**
     * @param disks
     *            the disks to set
     */
    public void setDisks(List<Disk> disks) {
        Disks = disks;
    }

    /**
     * @param processes
     *            the processes to set
     */
    public void setProcesses(List<RunProcess> processes) {
        this.processes = processes;
    }

    /**
     * @param softwares
     *            the softwares to set
     */
    public void setSoftwares(List<Software> softwares) {
        this.softwares = softwares;
    }
}
