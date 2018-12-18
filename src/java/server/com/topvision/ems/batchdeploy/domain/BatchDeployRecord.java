/***********************************************************************
 * $Id: BatchDeployRecord.java,v1.0 2013年12月13日 下午2:37:04 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.batchdeploy.domain;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.facade.batchdeploy.domain.BatchRecordSupport;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.common.FileUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Bravin
 * @created @2013年12月13日-下午2:37:04
 *
 */
public class BatchDeployRecord implements AliasesSuperType, Serializable {
    private static final long serialVersionUID = -3052664172148227407L;
    private static final Logger logger = LoggerFactory.getLogger(BatchDeployRecord.class);
    private Integer batchDeployId;
    private Long entityId;
    private String entityName;
    private String operator;
    private Integer typeId;
    private Date startTime;
    private Long duration;
    private List<? extends BatchRecordSupport> success;
    private byte[] successBlob;
    private List<? extends BatchRecordSupport> failures;
    private byte[] failuresBlob;
    private Integer sucessCount;
    private Integer failureCount;
    private List<?> matchList;
    private String comment;

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public byte[] getSuccessBlob() {
        return successBlob;
    }

    public void setSuccessBlob(byte[] successBlob) {
        this.successBlob = successBlob;
        ObjectInputStream is = null;
        try {
            is = new ObjectInputStream(new SerialBlob(successBlob).getBinaryStream());
            this.success = (List<BatchRecordSupport>) is.readObject();
        } catch (IOException e) {
            logger.error("", e);
        } catch (SQLException e) {
            logger.error("", e);
        } catch (ClassNotFoundException e) {
            logger.error("", e);
        } finally {
            FileUtils.closeQuitely(is);
        }
    }

    public byte[] getFailuresBlob() {
        return failuresBlob;
    }

    public void setFailuresBlob(byte[] failuresBlob) {
        this.failuresBlob = failuresBlob;
        ObjectInputStream is = null;
        try {
            is = new ObjectInputStream(new SerialBlob(failuresBlob).getBinaryStream());
            this.failures = (List<BatchRecordSupport>) is.readObject();
        } catch (IOException e) {
            logger.error("", e);
        } catch (SQLException e) {
            logger.error("", e);
        } catch (ClassNotFoundException e) {
            logger.error("", e);
        } finally {
            FileUtils.closeQuitely(is);
        }
    }

    public void setMatches(String matches) {
        String[] split = matches.split("_");
        this.matchList = Arrays.asList(split);
    }

    public String getMatches() {
        StringBuilder sb = new StringBuilder();
        if (matchList == null || matchList.isEmpty()) {
            return null;
        } else {
            for (Object match : matchList) {
                sb.append("_").append(match);
            }
            return sb.substring(1);
        }
    }


    public Integer getBatchDeployId() {
        return batchDeployId;
    }

    public void setBatchDeployId(Integer batchDeployId) {
        this.batchDeployId = batchDeployId;
    }

    public Integer getSucessCount() {
        return sucessCount;
    }

    public void setSucessCount(Integer sucessCount) {
        this.sucessCount = sucessCount;
    }

    public Integer getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(Integer failureCount) {
        this.failureCount = failureCount;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public List<? extends BatchRecordSupport> getSuccess() {
        return success;
    }

    public void setSuccess(List success) {
        this.success = success;
        this.sucessCount = success.size();
        ByteArrayOutputStream bos = null;
        ObjectOutputStream ois = null;
        try {
            bos = new ByteArrayOutputStream();
            ois = new ObjectOutputStream(bos);
            ois.writeObject(success);
            this.successBlob = bos.toByteArray();
        } catch (IOException e) {
            logger.error("", e);
        } finally {
            FileUtils.closeQuitely(bos);
            FileUtils.closeQuitely(ois);
        }
    }

    public List<? extends BatchRecordSupport> getFailures() {
        return failures;
    }

    public void setFailures(List failures) {
        this.failures = failures;
        this.failureCount = failures.size();
        ByteArrayOutputStream bos = null;
        ObjectOutputStream ois = null;
        try {
            bos = new ByteArrayOutputStream();
            ois = new ObjectOutputStream(bos);
            ois.writeObject(failures);
            this.failuresBlob = bos.toByteArray();
        } catch (IOException e) {
            logger.error("", e);
        } finally {
            FileUtils.closeQuitely(bos);
            FileUtils.closeQuitely(ois);
        }
    }

    public List<?> getMatchList() {
        return matchList;
    }

    public void setMatchList(List<?> matchList) {
        this.matchList = matchList;
    }

    public String getStartTimeString() {
        return DateUtils.getDateFormat().format(startTime);
    }

    @Override
    public String toString() {
        return "BatchDeployRecord [batchDeployId=" + batchDeployId + ", entityId=" + entityId + ", operator="
                + operator + ", typeId=" + typeId + ", startTime=" + startTime + ", duration=" + duration
                + ", success=" + success + ", successBlob=" + java.util.Arrays.toString(successBlob) + ", failures="
                + failures + ", failuresBlob=" + java.util.Arrays.toString(failuresBlob) + ", sucessCount="
                + sucessCount + ", failureCount=" + failureCount + ", matchList=" + matchList + ", comment=" + comment
                + "]";
    }

}
