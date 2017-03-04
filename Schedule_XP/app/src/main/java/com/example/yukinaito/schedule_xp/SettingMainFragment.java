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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
        modelName = ((ScheduleApplication)getActivity().getApplication()).getModelNames();

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
                        ((ScheduleApplication)getActivity().getApplication()).saveCard(new ModelCard(null, ((EditText)layout.findViewById(R.id.input_name)).getText().toString(), true));
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

        //region リスナーの登録

        //長押し時
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (position > 6) {
                    final int delete_pos = position;
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("削除");
                    builder.setMessage("選択されたモデルを削除しますか？");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //region OKをタップしたとき
                            int index = 7, count = 0;
                            while (true) {
                                if (((ScheduleApplication) getActivity().getApplication()).getModelInfo().get(index).getSaved()) {
                                    if (count == position - 7) {
                                        break;
                                    }
                                    count++;
                                }
                                index++;
                            }
                            modelName.remove(position);

                            //イベント日リストに削除するモデルが存在するか
                            boolean check = false;
                            for(int j = 0; j < ((ScheduleApplication) getActivity().getApplication()).getEventCards().size(); j++){
                                Log.d("TEST",j + " " + ((ScheduleApplication) getActivity().getApplication()).getEventCards().get(j).getIndex() + " " + ((ScheduleApplication) getActivity().getApplication()).getModelInfo().get(index).getId());
                                if(((ScheduleApplication) getActivity().getApplication()).getModelInfo().get(index).getId().equals(
                                        Integer.toString(((ScheduleApplication) getActivity().getApplication()).getEventCards().get(j).getIndex()))){
                                    check = true;
                                    break;
                                }
                            }

                            //存在する check==true
                            if(check){
                                ((ScheduleApplication) getActivity().getApplication()).getModelInfo().get(index).setSaved(false);
                                ((ScheduleApplication) getActivity().getApplication()).updateCard(
                                        ((ScheduleApplication) getActivity().getApplication()).getModelInfo().get(index).getId(),
                                        ((ScheduleApplication) getActivity().getApplication()).getModelInfo().get(index));
                            }else{
                                ((ScheduleApplication) getActivity().getApplication()).deleteCard(((ScheduleApplication) getActivity().getApplication()).getModelInfo().get(index).getId());
                            }
                            updateList();
                            //endregion
                        }
                    });
                    builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    builder.create().show();
                    return true;
                }
                return true;
            }
        });
        //endregion

        //Listの描画
        //変更必要
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
        ((ScheduleApplication)getActivity().getApplication()).startModelFragment(position);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        getActivity().setTitle("予定一覧");
        SettingModelFragment fragment = new SettingModelFragment();
        transaction.replace(R.id.content_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        //endregion
    }
}
