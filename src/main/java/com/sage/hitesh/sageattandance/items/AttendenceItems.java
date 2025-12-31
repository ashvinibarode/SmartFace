package com.lucky.attendenceapplication.items;

public class AttendenceItems {
    String u_id,name;

    public AttendenceItems(String u_id, String name) {
        this.u_id = u_id;
        this.name = name;
    }

    public String getU_id() {
        return u_id;
    }

    public String getName() {
        return name;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
