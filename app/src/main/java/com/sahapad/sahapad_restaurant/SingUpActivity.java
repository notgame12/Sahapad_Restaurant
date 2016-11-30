package com.sahapad.sahapad_restaurant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class SingUpActivity extends AppCompatActivity {

    //Explicit
    private EditText userEditText, passwordEditText,nameEditText;
    private String userString, passwordString, nameString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        //BindWidget
        bindWidget();

    } //oncreate
    public void clickSingUpSing(View view){
        userString = userEditText.getText().toString().trim();
        passwordString = passwordEditText.getText().toString().trim();
        nameString = nameEditText.getText().toString().trim();

        //check space
        if (userString.equals("")|| passwordString.equals("") || nameString.equals("")){
            //have space
            MyAlert myAlert = new MyAlert();
            MyAlert.myDialog(this, "มีช่องว่าง", "กรุณากรอกทุกช่อง ครับ");

        }else {
            //no space
            updateValueToServer();

        }
    }

    private void updateValueToServer() {
        String strURL = "http://www.csclub.ssru.ac.th/s56122201006/php_add_user.php";
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormEncodingBuilder()
                .add("isAdd","true")
                .add("User",userString)
                .add("Password",passwordString)
                .add("Name",nameString)
                .build();
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(strURL).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    finish();
                }catch (Exception e){
                    Log.d("Restaurant", "error" + e.toString());
                }
            }
        });
    }

    private  void bindWidget(){
        userEditText = (EditText) findViewById(R.id.editText3);
        passwordEditText = (EditText) findViewById(R.id.editText4);
        nameEditText = (EditText) findViewById(R.id.editText5);
    }
} //Main Class
