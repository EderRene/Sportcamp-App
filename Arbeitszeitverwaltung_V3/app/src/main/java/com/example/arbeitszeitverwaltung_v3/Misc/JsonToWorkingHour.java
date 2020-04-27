package com.example.arbeitszeitverwaltung_v3.Misc;

import com.example.arbeitszeitverwaltung_v3.Data.WorkingHour;


import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class JsonToWorkingHour {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public JsonToWorkingHour() {
    }

    public WorkingHour toWorkingHour(JSONObject obj) throws Exception {

        Date workingDate = sdf.parse(obj.getString("workingDate"));
        JSONObject wh=obj.getJSONObject("workingHours");
        JSONObject forenoon =wh.getJSONObject("forenoon");
        JSONObject afternoon =wh.getJSONObject("afternoon");

       Date forenoonStart= sdf.parse(forenoon.getString("startTime"));
        Date forenoonEnd= sdf.parse(forenoon.getString("endTime"));
        String info1= forenoon.getString("info");

        Date afternoonStart= sdf.parse(afternoon.getString("startTime"));
        Date afternoonEnd= sdf.parse(afternoon.getString("endTime"));
        String info2= afternoon.getString("info");

        return new WorkingHour(workingDate,info1,info2,forenoonStart,forenoonEnd,afternoonStart,afternoonEnd);

    }
}
