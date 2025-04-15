package com.example.englishapp;

public class Duc_CauHoi {
    private String noiDungCauHoi;
    private String luaChon_1;
    private String luaChon_2;
    private String luaChon_3;
    private String luaChon_4;
    private String dapAnDung;

    // Constructor
    public Duc_CauHoi(String noiDungCauHoi, String luaChon_1, String luaChon_2, String luaChon_3, String luaChon_4, String dapAnDung) {
        this.noiDungCauHoi = noiDungCauHoi;
        this.luaChon_1 = luaChon_1;
        this.luaChon_2 = luaChon_2;
        this.luaChon_3 = luaChon_3;
        this.luaChon_4 = luaChon_4;
        this.dapAnDung = dapAnDung;
    }

    // Getters
    public String getNoiDungCauHoi() {
        return noiDungCauHoi;
    }

    public String getLuaChon_1() {
        return luaChon_1;
    }

    public String getLuaChon_2() {
        return luaChon_2;
    }

    public String getLuaChon_3() {
        return luaChon_3;
    }

    public String getLuaChon_4() {
        return luaChon_4;
    }

    public String getDapAnDung() {
        return dapAnDung;
    }
}
