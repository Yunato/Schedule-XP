package com.example.yukinaito.schedule_xp;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class SettingMainFragment extends ListFragment {
    private ArrayList<String> modelName;
    private ArrayAdapter<String> drawItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_listandfbutton, container, false);
        view.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //region ダイアログの生成
                LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View layout = inflater.inflate(R.layout.dialog_createmodel, (ViewGroup)getActivity().findViewById(R.id.layout_root));

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("新規作成");
                builder.setView(layout);
                builder.setPositiveButton("作成", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //region "作成"のタップ時
                        //変更必要
                        modelName.add(((EditText)layout.findViewById(R.id.input_name)).getText().toString());
                        updateList();
                        //endregion
                    }
                });
                builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //region "キャンセル"のタップ時
                        //endregion
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                ((EditText)layout.findViewById(R.id.input_name)).addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        //入力チェック 空欄でないかどうか
                        if(((EditText)layout.findViewById(R.id.input_name)).getText().toString().trim().length() != 0)
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                        else
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    }
                });
                //endregion
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        //ListViewの区切り線
        ColorDrawable separate_line_color = new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.separate_line));
        getListView().setDivider(separate_line_color);
        getListView().setDividerHeight(5);

        //Listの描画
        //変更必要
        modelName = new ArrayList<>();
        modelName.add("日曜日");
        drawItem = new ArrayAdapter<>(getActivity(), R.layout.rowdata, modelName);

        updateList();
    }

    //Listの更新
    public void updateList(){
        setListAdapter(drawItem);
        //画面更新
        drawItem.notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id){
        //region モデルの編集画面へ遷移
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        getActivity().setTitle("予定一覧");
        SettingModelFragment fragment = new SettingModelFragment();
        transaction.replace(R.id.content_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        //endregion
    }
}
