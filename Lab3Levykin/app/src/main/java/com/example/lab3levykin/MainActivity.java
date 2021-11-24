package com.example.lab3levykin;
//"40b15f581e4e441fb4c62458211310";
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String key;
    EditText txt;
    ListView loadlist;
    ArrayList<Keys> lstNetwork = new ArrayList<>();
    ArrayAdapter<Keys> adpNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt = findViewById(R.id.editcity);

        StaticDB.database = new DB(this, "jsondb", null, 1);
    }

    public void onBtnQueryClick(View v)
    {
        Intent intent = new Intent(this, Result.class);
        intent.putExtra("apikey", key);
        intent.putExtra("city",txt.getText().toString());
        startActivity(intent);
    }

    public void onBtnKeyClick(View v)
    {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        View customLayout = getLayoutInflater().inflate(R.layout.dialog, null);
        bld.setTitle("Настройки ключа");
        bld.setView(customLayout);

        EditText textKey = customLayout.findViewById(R.id.editKey);
        Button accept = customLayout.findViewById(R.id.btnApply);
        Button cancel = customLayout.findViewById(R.id.btnExit);
        Button save = customLayout.findViewById(R.id.btnSet);
        Button load = customLayout.findViewById(R.id.btnLoad);
        Dialog dlg = bld.create();
        dlg.show();

        save.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) { //сохранить ключ
               int nid = StaticDB.database.getMaxIDForKey() + 1;
               StaticDB.database.addKey(nid, textKey.getText().toString());
               Toast.makeText(getApplicationContext(), "Saved!", Toast.LENGTH_LONG).show();
           }
        });
        load.setOnClickListener //загрузить ключи
                (view -> OnNetworkSettingsLoad(view, "загрузить", textKey));
        accept.setOnClickListener(view -> {
            key = String.valueOf(textKey.getText());
            dlg.cancel();
        });
        cancel.setOnClickListener(view -> dlg.cancel());
    }

    private void OnNetworkSettingsLoad(View view, String load, EditText key) { //загрузить ключи
        View customLayout = getLayoutInflater().inflate(R.layout.dialogload, null);
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setTitle("Загрузить ключ");
        bld.setView(customLayout);
        Dialog dlg = bld.create();
        dlg.show();

        loadlist = customLayout.findViewById(R.id.list_keys);
        adpNetwork = new ArrayAdapter<Keys>
                (this, android.R.layout.simple_list_item_1, lstNetwork);

        loadlist.setAdapter(adpNetwork);

        loadlist.setOnItemClickListener((parent, _view, position, id) -> {
            Keys n = adpNetwork.getItem(position);
            key.setText(n.Key);
            dlg.cancel();
        });
        lstNetwork.clear();
        StaticDB.database.getAllKeys(lstNetwork);
        adpNetwork.notifyDataSetChanged();
    }

    public void onBtnHistoryClick(View v)
    {
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }
}