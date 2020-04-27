package com.example.arbeitszeitverwaltung_v3.GUI;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.arbeitszeitverwaltung_v3.Data.Database;
import com.example.arbeitszeitverwaltung_v3.Data.WorkingHour;
import com.example.arbeitszeitverwaltung_v3.MainActivity;
import com.example.arbeitszeitverwaltung_v3.Misc.JsonToWorkingHour;
import com.example.arbeitszeitverwaltung_v3.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Predicate;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Time_ManagerFragment extends Fragment implements View.OnClickListener {

    private TextView date1, date2;
    private Calendar calendar = null;
    private DatePickerDialog pickerDialog = null;
    private ListView listViewWorkingHours = null;
    private Database db = null;
    private Date fromDate, toDate;
    private FloatingActionButton btn_float_add;

    ArrayList<WorkingHour> workingHours = null;
    private ArrayAdapter adapterListView = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        db = Database.getInstance();
        return inflater.inflate(R.layout.fragment_time_manager, container, false);
}

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        date1 = getView().findViewById(R.id.date1_time_manager);
        date2 = getView().findViewById(R.id.date2_time_manager);
        btn_float_add=getView().findViewById(R.id.btn_float);
        btn_float_add.setOnClickListener(this);
        listViewWorkingHours = this.getActivity().findViewById(R.id.listviewworkinghours);
        calendar = Calendar.getInstance();
        date1.setText("From Date");

        date2.setText("To Date");

        date1.setOnClickListener(this);
        date2.setOnClickListener(this);


        setWorkinghours();
        listViewWorkingHours.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayWorkingHourDialog((WorkingHour) listViewWorkingHours.getItemAtPosition(position));
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

    }

    private void displayWorkingHourDialog(WorkingHour selectedItem) {

        WorkingHourDialog whd = new WorkingHourDialog();
        whd.show(getActivity().getSupportFragmentManager(), "null");
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == date1.getId()) {
            getDateFromPicker(3, date1, 1);

        }
        if (v.getId() == date2.getId()) {
            getDateFromPicker(0, date2, 2);
        }
        if(v.getId()==btn_float_add.getId()){
            showAddPopup();
        }
    }

    private void showAddPopup() {
        WorkingHourDialog whd = new WorkingHourDialog();
        whd.show(getActivity().getSupportFragmentManager(), "null");
    }


    private void getDateFromPicker(int minusDays, final TextView tv, final int dateset) {
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, (-minusDays));

        int month = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.DAY_OF_MONTH);

        pickerDialog = new DatePickerDialog(getView().getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                c.set(year, month, dayOfMonth, 0, 0);
                if (dateset == 2) {
                    toDate = c.getTime();
                } else {
                    fromDate = c.getTime();
                }
                setWorkinghours();
                tv.setText(dayOfMonth + "." + (month + 1) + "." + year);

            }

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        pickerDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        setWorkinghours();
    }

    private void setWorkinghours() {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                    workingHours = db.getWorkinghours();
                    adapterListView
                            = new ArrayAdapter<WorkingHour>(getContext(), android.R.layout.simple_list_item_1, workingHours);
                    listViewWorkingHours.setAdapter(adapterListView);
                    adapterListView.notifyDataSetChanged();
            }
        });
    }

}