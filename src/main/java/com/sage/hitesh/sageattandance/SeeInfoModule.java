package com.sage.hitesh.sageattandance;

import android.util.Log;

import java.util.Map;

public class SeeInfoModule {
    public SeeInfoModule() {}

    public SeeInfoModule(String name, String address, String mobile, String id, String absentCount, String presentCont, String leaveCount,
                         Map<String, Object> attendance,Map<String,Object>leave) {
        this.name = name;
        this.address = address;
        this.mobile = mobile;
        this.id = id;
        this.absentCount = absentCount;
        this.presentCont = presentCont;
        this.leaveCount = leaveCount;
        this.attendance = attendance; // todo added this
        this.leave=leave;
    }

    String name,address,mobile,id,present;
    String absentCount, presentCont, leaveCount;
    Map<String, Object> attendance,leave;

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAbsentCount(String absentCount) {
        this.absentCount = absentCount;
    }

    public void setPresentCont(String presentContt) {
        this.present = presentContt;
    }

    public void setLeaveCount(String leaveCount) {
        this.leaveCount = leaveCount;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getMobile() {
        return mobile;
    }

    public String getId() {
        return id;
    }

    public String getAbsentCount() {
        return absentCount;
    }

    public String getPresentCont() {
        return present;
    }

    public String getLeaveCount() {
        return leaveCount;
    }
    // todo added this here
    public void setAttendance(Map<String, Object> attendance) {
        this.attendance = attendance;
    }
    public Map<String, Object> getAttendance() {
        return attendance;
    }
    // todo end of added


    public void setLeave(Map<String, Object> leave) {
        this.leave = leave;
    }
    public Map<String, Object> getleave() {
        return leave;
    }

}
