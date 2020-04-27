package com.example.arbeitszeitverwaltung_v3.Data;

import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.arbeitszeitverwaltung_v3.GUI.Time_ManagerFragment;
import com.example.arbeitszeitverwaltung_v3.Misc.JsonToWorkingHour;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.function.Predicate;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Database{
    private static final String URL = "http://salcher.synology.me:8888";
    private static final String URL_workinghour="/api/workingHours";

    private static final String PORT = "8888";
    private int UserId = 1;
    private static Database db;

    private Database() {
    }

    public static Database getInstance() {
        if(db==null){
            db=new Database();
        }
        return db;
    }

    public ArrayList<WorkingHour> getWorkinghours(){
        OkHttpClient client = new OkHttpClient();
        Request request=new Request.Builder().url(URL+URL_workinghour+"/"+UserId).build();
        final ArrayList<WorkingHour> workingHours= new ArrayList<>();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) {
                try {
                    String s= "{\"WorkingHours\":" +response.body().string()+"}";

                    JSONObject obj=new JSONObject(s);

                    JSONArray Jarray = obj.getJSONArray("WorkingHours");
                    JsonToWorkingHour parser=new JsonToWorkingHour();

                    for(int i=0;i<Jarray.length();i++){
                        workingHours.add(parser.toWorkingHour(Jarray.getJSONObject(i)));
                    }
                } catch (Exception e) {
                }
            }
        });
        return workingHours;
    }


    public void addWorkinghour(WorkingHour wh) throws Exception {
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss'Z'");

        OkHttpClient client = new OkHttpClient();
        JSONObject id_workindate=new JSONObject();
        id_workindate.put("id_Employee",UserId);
        id_workindate.put("workingDate", sdf.format(wh.getWorkingDate()));

        JSONObject forenoonafternoon=new JSONObject();

        if(wh.getForenoonEndTime()!=null||!wh.getForenoonInfo().isEmpty()||wh.getForenoonStartTime()!=null){
            JSONObject forenoon=new JSONObject();
            JSONObject values=new JSONObject();
            values.put("startTime",sdf.format(wh.getForenoonStartTime()));
            values.put("endTime",sdf.format(wh.getForenoonStartTime()));
            values.put("info",wh.getForenoonInfo());
            forenoonafternoon.put("forenoon",values);

        }

        if(wh.getAfternoonEndTime()!=null||!wh.getAfternoonInfo().isEmpty()||wh.getAfternoonStartTime()!=null){
            JSONObject values=new JSONObject();
            values.put("startTime",sdf.format(wh.getForenoonStartTime()));
            values.put("endTime",sdf.format(wh.getAfternoonStartTime()));
            values.put("info",wh.getForenoonInfo());
            forenoonafternoon.put("afternoon",values);
        }

        id_workindate.put("workingHours",forenoonafternoon);

        Request request=new Request.Builder().url(URL+URL_workinghour).post(RequestBody.create(id_workindate.toString(),
                MediaType.parse("application/json"))).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) {
            }
        });
    }
}
