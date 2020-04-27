package com.example.arbeitszeitverwaltung_v3.GUI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.arbeitszeitverwaltung_v3.Data.WorkingHour;
import com.example.arbeitszeitverwaltung_v3.R;

import java.util.ArrayList;

public class File_Fragment extends Fragment {

    private ListView lv_files;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_files, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lv_files = view.findViewById(R.id.listview_file);
        ArrayList<String> files = new ArrayList<String>();
        files.add("Vertrag.docx");
        files.add("Profile.img");
        files.add("programm.exe");
       ArrayAdapter adapterListView
                = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, files);
        lv_files.setAdapter(adapterListView);
    }
}
