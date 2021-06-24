package com.emanuelef.remote_capture.model;

public class JsonModelClass {
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    private String domain;

    public JsonModelClass() {
    }

    public JsonModelClass(String domain, String ip) {
        this.domain = domain;
        this.ip = ip;
    }

    private String ip;

}
