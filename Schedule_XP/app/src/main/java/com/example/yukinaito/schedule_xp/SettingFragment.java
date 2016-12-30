package com.example.yukinaito.schedule_xp;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class SettingFragment extends ListFragment{
    private static ArrayList<ModelSchedule> model;
    private SchedlueApplication schedlueApplication;
    private static final int REQUEST_CODE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        schedlueApplication = (SchedlueApplication)getActivity().getApplication();
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(
                        LAYOUT_INFLATER_SERVICE);
                final View layout = inflater.inflate(R.layout.dialog_createpattern,
                        (ViewGroup) getActivity().findViewById(R.id.layout_root));
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                ((TextView)layout.findViewById(R.id.input_textview)).setText("新たなパターンの名前を入力してください");
                ((EditText)layout.findViewById(R.id.input_pattern)).setHint("パターン名を入力");
                builder.setTitle("新規作成");
                builder.setView(layout);
                builder.setPositiveButton("作成", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ModelSchedule modelSch = new ModelSchedule();
                        modelSch.setName(((EditText)layout.findViewById(R.id.input_pattern)).getText().toString());
                        schedlueApplication.getModelSchedule().add(modelSch);
                        updateListfragment();
                    }
                });
                builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON1).setEnabled(false);
                ((EditText)layout.findViewById(R.id.input_pattern)).addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if(inputCheck(layout))
                            dialog.getButton(AlertDialog.BUTTON1).setEnabled(true);
                        else
                            dialog.getButton(AlertDialog.BUTTON1).setEnabled(false);
                    }
                });
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        ColorDrawable separate_line_color = new ColorDrawable(this.getResources().getColor(R.color.separate_line));
        getListView().setDivider(separate_line_color);
        getListView().setDividerHeight(5);
        model = schedlueApplication.getModelSchedule();
        ArrayList<String> day = new ArrayList<String>();
        for(int i = 0; i < model.size(); i++)
            day.add(model.get(i).getName());
        setListAdapter(new ArrayAdapter<String>(getActivity(), R.layout.rowdata, day));
    }

    public void updateListfragment(){
        ArrayList<String> day = new ArrayList<String>();
        for(int i = 0; i < model.size(); i++)
            day.add(model.get(i).getName());
        ArrayAdapter<String> days = new ArrayAdapter<String>(getActivity(), R.layout.rowdata, day);
        setListAdapter(days);
        days.notifyDataSetChanged();
        schedlueApplication.writeModelFile();
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id){
        Intent intent = new Intent(getActivity(), SettingModelActivity.class);
        intent.putExtra("position", pos);
        startActivityForResult(intent, REQUEST_CODE);
    }

    public boolean inputCheck(View layout){
        String end;
        end = ((EditText)layout.findViewById(R.id.input_pattern)).getText().toString();
        if(!(end.equals(""))) {
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
            updateListfragment();
    }
}
