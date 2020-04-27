package com.example.arbeitszeitverwaltung_v3.GUI;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.arbeitszeitverwaltung_v3.Data.Database;
import com.example.arbeitszeitverwaltung_v3.Data.WorkingHour;
import com.example.arbeitszeitverwaltung_v3.R;
import com.google.android.material.textfield.TextInputEditText;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class WorkingHourDialog extends AppCompatDialogFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private TextView endTimeForenoon;
    private TextView endTimeAfternoon;
    private TextView starTimeAfternoon;
    private TextView starTimeForenoon;
    private TextView tf_Forenooninfo;
    private Button btnsubmitt;
    private CheckBox ck_forenoon;
    private CheckBox ck_afternoon;
    private Date forenoonstartDate,forenoonendDate,afternoonstartDate,afternoonendDate;
    private LinearLayout forenoon,forenoontime,afternoon,afternoontime;
    private TextView messagebox;
    private Database db=null;

    private TextInputEditText tf_Afternooninfo;
    private Context context;
    private View view;
    private Date tmp;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_add_workinghour, null);
        dialogBuilder.setView(view);
        AlertDialog alertDialog = dialogBuilder.create();
        initVariables(view);
        return alertDialog;
    }

    private void initVariables(View view) {
        this.view=view;
        tf_Forenooninfo = view.findViewById(R.id.tf_Forenooninfo);
        tf_Afternooninfo = view.findViewById(R.id.tf_Afternooninfo);

        starTimeForenoon = view.findViewById(R.id.textview_Forenoon_stattime);
        starTimeAfternoon = view.findViewById(R.id.textview_Afternoon_stattime);
        endTimeAfternoon = view.findViewById(R.id.textview_Afternoon_endtime);
        endTimeForenoon = view.findViewById(R.id.textview_Forenoon_endtime);


        btnsubmitt = view.findViewById(R.id.btn_Submit);
        ck_forenoon=view.findViewById(R.id.ForenoonCheckbox);
        ck_afternoon=view.findViewById(R.id.AfternoonCheckbox);
        forenoon=view.findViewById(R.id.layoutforenoon);
        afternoon=view.findViewById(R.id.layoutafternoon);
        forenoontime=view.findViewById(R.id.layoutforenoon_time);
        afternoontime=view.findViewById(R.id.layoutafternoon_time);
        messagebox=view.findViewById(R.id.tf_messagebox);

        starTimeAfternoon.setOnClickListener(this);
        endTimeAfternoon.setOnClickListener(this);
        starTimeForenoon.setOnClickListener(this);
        endTimeForenoon.setOnClickListener(this);

        tf_Afternooninfo.setEnabled(false);
        starTimeAfternoon.setEnabled(false);
        endTimeAfternoon.setEnabled(false);

        tf_Forenooninfo.setEnabled(false);
        starTimeForenoon.setEnabled(false);
        endTimeForenoon.setEnabled(false);

        btnsubmitt.setOnClickListener((View.OnClickListener) this);
        ck_forenoon.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) this);
        ck_afternoon.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) this);
        Database db =Database.getInstance();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == btnsubmitt.getId()){
            try {
                validateData();
            } catch (Exception e) {
                messagebox.setText(e.getMessage().toString());
                return;
            }

            WorkingHour wh=new WorkingHour(Calendar.getInstance().getTime(),tf_Forenooninfo.getText().toString(),tf_Afternooninfo.getText().toString(),forenoonstartDate,forenoonendDate,afternoonstartDate,afternoonstartDate);
            try {
                db = Database.getInstance();
                db.addWorkinghour(wh);
                this.dismiss();
            } catch (Exception e) {
                messagebox.setText(e.getMessage());
            }
        }
        if(v.getId()==starTimeForenoon.getId()){
            tmp=forenoonendDate;
            getTime(starTimeForenoon,00,01,12,00,8,0);
        }
        if(v.getId()==endTimeForenoon.getId()){
            tmp=forenoonendDate;
            getTime(endTimeForenoon,00,01,12,00,8,0);
        }
        if(v.getId()==starTimeAfternoon.getId()){
            tmp=afternoonstartDate;
            getTime(starTimeAfternoon,12,00,23,59,13,0);
        }
        if(v.getId()==endTimeAfternoon.getId()){
            tmp=afternoonendDate;
            getTime(endTimeAfternoon,12,00,23,59,13,0);
        }
    }


    public void getTime(final TextView field, int minhour,int minminute, int maxhour,int maxminute, int presethour, int presetminute){
        BoundTimePickerDialog timePickerDialog = new BoundTimePickerDialog(view.getContext(), new android.app.TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {

                    field.setText(hourOfDay + ":" + minutes);
                    Calendar c=Calendar.getInstance();
                    c.setTime(Calendar.getInstance().getTime());
                    c.set(Calendar.HOUR,hourOfDay);
                    c.set(Calendar.MINUTE,minutes);


    }

        }, presethour, presetminute, true);
        timePickerDialog.setMin(minhour,minminute);
        timePickerDialog.setMax(maxhour,maxminute);
        timePickerDialog.show();
    }

    private void validateData() throws Exception {
        if(ck_forenoon.isChecked()){
            if(tf_Forenooninfo.getText().toString().isEmpty()){
                throw new Exception("Forenooninfo is empty");
            }
            if(!starTimeForenoon.getText().toString().contains(":")||!endTimeForenoon.getText().toString().contains(":")) {
                throw new Exception("Date is wrong");
            }
            getForenoonTime();
        }
        if(ck_afternoon.isChecked()){
            if(tf_Afternooninfo.getText().toString().isEmpty()){
                throw new Exception("Forenooninfo is empty");
            }
            if(!starTimeAfternoon.getText().toString().contains(":")||!endTimeAfternoon.getText().toString().contains(":")){
                throw new Exception("Date is wrong");
            }
            getAfternoonTime();
        }
        if(!ck_afternoon.isChecked()&&!ck_forenoon.isChecked()){
            throw new Exception("Nothing to insert");
        }
    }

    private void getForenoonTime() {
        Calendar c=Calendar.getInstance();
        c.setTime(Calendar.getInstance().getTime());
        c.set(Calendar.HOUR,Integer.parseInt(starTimeForenoon.getText().toString().split(":")[0]));
        c.set(Calendar.MINUTE,Integer.parseInt(starTimeForenoon.getText().toString().split(":")[1]));
        forenoonstartDate=c.getTime();
        c=Calendar.getInstance();
        c.setTime(Calendar.getInstance().getTime());
        c.set(Calendar.HOUR,Integer.parseInt(endTimeForenoon.getText().toString().split(":")[0]));
        c.set(Calendar.MINUTE,Integer.parseInt(endTimeForenoon.getText().toString().split(":")[1]));
        forenoonendDate=c.getTime();
    }
    private void getAfternoonTime() {
        Calendar c=Calendar.getInstance();
        c.setTime(Calendar.getInstance().getTime());
        c.set(Calendar.HOUR,Integer.parseInt(starTimeAfternoon.getText().toString().split(":")[0]));
        c.set(Calendar.MINUTE,Integer.parseInt(starTimeAfternoon.getText().toString().split(":")[1]));
        afternoonstartDate=c.getTime();
        c=Calendar.getInstance();
        c.setTime(Calendar.getInstance().getTime());
        c.set(Calendar.HOUR,Integer.parseInt(endTimeAfternoon.getText().toString().split(":")[0]));
        c.set(Calendar.MINUTE,Integer.parseInt(endTimeAfternoon.getText().toString().split(":")[1]));
        afternoonendDate=c.getTime();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView.getId()==ck_afternoon.getId()&&isChecked){
            tf_Afternooninfo.setEnabled(true);
            starTimeAfternoon.setEnabled(true);
            endTimeAfternoon.setEnabled(true);
        }
        if(buttonView.getId()== ck_afternoon.getId() && !isChecked){
            tf_Afternooninfo.setEnabled(false);
            starTimeAfternoon.setEnabled(false);
            endTimeAfternoon.setEnabled(false);
        }
        if (buttonView.getId()==ck_forenoon.getId()){
            tf_Forenooninfo.setEnabled(true);
            starTimeForenoon.setEnabled(true);
            endTimeForenoon.setEnabled(true);
        }
        if(isChecked==false&&buttonView.getId()==ck_forenoon.getId()){
            tf_Forenooninfo.setEnabled(false);
            starTimeForenoon.setEnabled(false);
            endTimeForenoon.setEnabled(false);
        }
    }
}
