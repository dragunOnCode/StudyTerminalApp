package com.example.studyterminalapp.bean;

import java.io.Serializable;

public class Resource implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer rid;

    private String resourceTitle;

    private String resourceUrl;

    private Integer classId;

    private String resourceType;

    private String videoCover;

    private String resourceDesc;

    public Resource() {
    }

    public Resource(Integer rid, String resourceTitle, String resourceUrl, Integer classId,
                    String resourceType, String videoCover, String resourceDesc) {
        this.rid = rid;
        this.resourceTitle = resourceTitle;
        this.resourceUrl = resourceUrl;
        this.classId = classId;
        this.resourceType = resourceType;
        this.videoCover = videoCover;
        this.resourceDesc = resourceDesc;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public String getResourceTitle() {
        return resourceTitle;
    }

    public void setResourceTitle(String resourceTitle) {
        this.resourceTitle = resourceTitle;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getVideoCover() {
        return videoCover;
    }

    public void setVideoCover(String videoCover) {
        this.videoCover = videoCover;
    }

    public String getResourceDesc() {
        return resourceDesc;
    }

    public void setResourceDesc(String resourceDesc) {
        this.resourceDesc = resourceDesc;
    }
}
