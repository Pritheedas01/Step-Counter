package com.pritha.www.stepcount;

public class ModelData {
    String Date,Steps,Calorie,KMs;

    public ModelData(String date, String steps, String calorie, String km) {
        Date = date;
        Steps = steps;
        Calorie = calorie;
        KMs = km;
    }

    public ModelData() {
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getSteps() {
        return Steps;
    }

    public void setSteps(String steps) {
        Steps = steps;
    }

    public String getCalorie() {
        return Calorie;
    }

    public void setCalorie(String calorie) {
        Calorie = calorie;
    }

    public String getKMs() {
        return KMs;
    }

    public void setKMs(String km) {
        KMs = km;
    }
}
