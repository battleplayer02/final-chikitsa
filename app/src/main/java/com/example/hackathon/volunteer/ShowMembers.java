package com.example.hackathon.volunteer;
import java.io.Serializable;

public class ShowMembers implements Serializable{

    String eid;
    String id;
    String pid;

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPmobile() {
        return pmobile;
    }

    public void setPmobile(String pmobile) {
        this.pmobile = pmobile;
    }

    public String getAssignwork() {
        return assignwork;
    }

    public void setAssignwork(String assignwork) {
        this.assignwork = assignwork;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    String pname;
    String pmobile;
    String assignwork;
    String address;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}