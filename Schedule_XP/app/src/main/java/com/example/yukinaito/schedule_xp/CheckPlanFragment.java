package com.example.yukinaito.schedule_xp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CheckPlanFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_addcheckplan, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle saveInstanceState) {
        super.onViewCreated(view, saveInstanceState);
        view.findViewById(R.id.button_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CheckPlanActivity.class);
                intent.putExtra("select",0);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.button_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CheckPlanActivity.class);
                intent.putExtra("select",1);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.button_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CheckPlanActivity.class);
                intent.putExtra("select",2);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.button_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CheckPlanActivity.class);
                intent.putExtra("select",3);
                startActivity(intent);
            }
        });
    }
}
