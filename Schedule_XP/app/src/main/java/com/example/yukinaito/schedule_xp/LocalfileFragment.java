package com.example.yukinaito.schedule_xp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class LocalfileFragment extends Fragment {
    private EditText et;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_localfile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle saveInstanceState){
        super.onViewCreated(view, saveInstanceState);
        et = (EditText)view.findViewById(R.id.edittext);
        view.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = et.getText().toString();
                try{
                    FileOutputStream out = getActivity().openFileOutput("plan.txt",getActivity().MODE_APPEND|getActivity().MODE_WORLD_READABLE);
                    out.write(str.getBytes());
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        });
        view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    FileInputStream in = getActivity().openFileInput("plan.txt");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                    String tmp;
                    et.setText("");
                    while ((tmp = reader.readLine()) != null) {
                        et.append(tmp + "\n");
                    }
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        view.findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    getActivity().deleteFile("plan.txt");
            }
        });
        view.findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {et.setText("");}
        });
    }
}
