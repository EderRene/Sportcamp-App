package com.example.arbeitszeitverwaltung_v3.Data;


import com.example.arbeitszeitverwaltung_v3.Misc.DateHelperClass;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class WorkingHour {
    private Date workingDate;
    private String forenoonInfo;
    private String afternoonInfo;
    private Date forenoonStartTime;
    private Date forenoonEndTime;
    private Date afternoonStartTime;
    private Date afternoonEndTime;

    public WorkingHour(Date workingDate, String forenoonInfo, String afternoonInfo, Date forenoonStartTime, Date forenoonEndTime, Date afternoonStartTime, Date afternoonEndTime) {
        this.workingDate = workingDate;
        this.forenoonInfo = forenoonInfo;
        this.afternoonInfo = afternoonInfo;
        this.forenoonStartTime = forenoonStartTime;
        this.forenoonEndTime = forenoonEndTime;
        this.afternoonStartTime = afternoonStartTime;
        this.afternoonEndTime = afternoonEndTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkingHour)) return false;
        WorkingHour that = (WorkingHour) o;
        return Objects.equals(workingDate, that.workingDate) &&
                Objects.equals(forenoonInfo, that.forenoonInfo) &&
                Objects.equals(afternoonInfo, that.afternoonInfo) &&
                Objects.equals(forenoonStartTime, that.forenoonStartTime) &&
                Objects.equals(forenoonEndTime, that.forenoonEndTime) &&
                Objects.equals(afternoonStartTime, that.afternoonStartTime) &&
                Objects.equals(afternoonEndTime, that.afternoonEndTime);
    }

    public Date getWorkingDate() {
        return workingDate;
    }

    public void setWorkingDate(Date workingDate) {
        this.workingDate = workingDate;
    }

    public String getForenoonInfo() {
        return forenoonInfo;
    }

    public void setForenoonInfo(String forenoonInfo) {
        this.forenoonInfo = forenoonInfo;
    }

    public String getAfternoonInfo() {
        return afternoonInfo;
    }

    public void setAfternoonInfo(String afternoonInfo) {
        this.afternoonInfo = afternoonInfo;
    }

    public Date getForenoonStartTime() {
        return forenoonStartTime;
    }

    public void setForenoonStartTime(Date forenoonStartTime) {
        this.forenoonStartTime = forenoonStartTime;
    }

    public Date getForenoonEndTime() {
        return forenoonEndTime;
    }

    public void setForenoonEndTime(Date forenoonEndTime) {
        this.forenoonEndTime = forenoonEndTime;
    }

    public Date getAfternoonStartTime() {
        return afternoonStartTime;
    }

    public void setAfternoonStartTime(Date afternoonStartTime) {
        this.afternoonStartTime = afternoonStartTime;
    }

    public Date getAfternoonEndTime() {
        return afternoonEndTime;
    }

    public void setAfternoonEndTime(Date afternoonEndTime) {
        this.afternoonEndTime = afternoonEndTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(workingDate, forenoonInfo, afternoonInfo, forenoonStartTime, forenoonEndTime, afternoonStartTime, afternoonEndTime);
    }

    @Override
    public String toString() {
        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        String today = formatter.format(workingDate);
        return "Date: "+today+ " Working hours: "+getWholeHours();
    }

    public String allInfos() {
        return "WorkingHour{" +
                "workingDate=" + workingDate +
                ", forenoonInfo='" + forenoonInfo + '\'' +
                ", afternoonInfo='" + afternoonInfo + '\'' +
                ", forenoonStartTime=" + forenoonStartTime +
                ", forenoonEndTime=" + forenoonEndTime +
                ", afternoonStartTime=" + afternoonStartTime +
                ", afternoonEndTime=" + afternoonEndTime +
                '}';
    }

    public Double getForenoonHours(){
        if(forenoonEndTime==null||forenoonStartTime==null){
        return 0.0;
        }
        DateHelperClass dh=new DateHelperClass();
        return dh.getDifference(forenoonStartTime,forenoonEndTime);

    }

    public Double getAfternoonHours(){
        if(afternoonEndTime==null||afternoonStartTime==null){
            return 0.0;
        }
        DateHelperClass dh=new DateHelperClass();
        return dh.getDifference(afternoonStartTime,afternoonEndTime);
    }

    public Double getWholeHours(){
        return getAfternoonHours()+getForenoonHours();
    }



}
