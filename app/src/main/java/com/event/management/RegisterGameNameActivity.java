package com.event.management;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class RegisterGameNameActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    Button submit;
    SharedPreferences sp;

    EditText editText;
    List<EditText> allEds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_game_name);
        sp = getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);
        linearLayout = findViewById(R.id.register_game_name_edittext_layout);
        submit = findViewById(R.id.register_game_name_submit);

        //Log.d("RESPONSE",sp.getString(ConstantSp.EVENTMAXALLOW, ""));

        /*for (int i = 0; i < Integer.parseInt(sp.getString(ConstantSp.EVENTMAXALLOW, "")); i++) {
            EditText editText = new EditText(RegisterGameNameActivity.this);
            editText.setId(editText.generateViewId());
            editText.setHeight(150);
            editText.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            editText.setHint("Enter Name");
            linearLayout.addView(editText);
        }*/

        for (int i = 0; i < Integer.parseInt(sp.getString(ConstantSp.EVENTMAXALLOW, "")); i++) {
            editText = new EditText(RegisterGameNameActivity.this);
            editText.setId(editText.generateViewId());
            editText.setHeight(150);
            editText.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            editText.setHint("Enter Name");
            linearLayout.addView(editText);
            allEds.add(editText);
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int iEmpty = 0;
                int price = 0;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < allEds.size(); i++) {
                    if (allEds.get(i).getText().toString().trim().equals("")) {
                        iEmpty += 1;
                    } else {
                        sb.append(allEds.get(i).getText().toString() + ",");
                        price += Integer.parseInt(sp.getString(ConstantSp.EVENTCOLLEGEPRICE, ""));
                    }
                }
                if (sp.getString(ConstantSp.EVENTMAXALLOW, "").equals(String.valueOf(iEmpty))) {
                    new ToastIntentClass(RegisterGameNameActivity.this, "Please Enter Atleast One Name");
                } else {
                    sb.setCharAt(sb.length() - 1, ' ');
                    //Log.d("RESPONSE", sb.toString().trim());
                    sp.edit().putString(ConstantSp.EVENTNAMES,sb.toString().trim()).commit();
                    sp.edit().putString(ConstantSp.EVENTPRICES,String.valueOf(price)).commit();
                }
            }
        });

    }
}