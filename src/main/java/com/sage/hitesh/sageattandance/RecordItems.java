package com.sage.hitesh.sageattandance;

import android.util.Log;

public class RecordItems {
    String name,absents,mobile,address,leaves,id,present;
    public RecordItems(String name, String absents, String present, String mobile, String address, String leaves, String id) {
        this.name = name;
        this.absents = absents;
        this.present = present;
        this.mobile = mobile;
        this.address = address;
        this.leaves = leaves;
        this.id = id;
    }

    public RecordItems() {}

    public RecordItems(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAbsents(String absents) {
        this.absents = absents;
    }

    public String getP() {
        return p;
    }

    String p="";
    public void setPresent(String present) {
        this.p = present;
        Log.d("Firebase Test", "setPresent:"+ this.p);
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLeaves(String leaves) {
        this.leaves = leaves;
    }

    public String getName() {
        return name;
    }

    public String getAbsents() {
        return absents;
    }

    public String getMobile() {
        return mobile;
    }

    public String getAddress() {
        return address;
    }

    public String getLeaves() {
        return leaves;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

