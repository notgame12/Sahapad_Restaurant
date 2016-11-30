package com.sahapad.sahapad_restaurant;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private UserTable objUserTABLE;
    private FoodTABLE objFoodTABLE;
    private OrderTABLE objOrderTABLE;
    private MySQLite mySQLite;
    private EditText userEditText, passwordEditText;
    private String userString, passwordString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bind Wingget
        userEditText = (EditText) findViewById(R.id.editText);
        passwordEditText = (EditText) findViewById(R.id.editText2);

        connectedSQLite();

        testAddValue();

        synAndDelete();

        //Request SQLite
        mySQLite = new MySQLite(this);
    }//onCreate

    public void clickSignUpMain(View view){
        startActivity(new Intent(MainActivity.this,
                SingUpActivity.class));
    }

    public void clickSingInMaun(View view){
        userString = userEditText.getText().toString().trim();
        passwordString = passwordEditText.getText().toString().trim();

        //check Space
        if (userString.equals("") || passwordString.equals("")) {
            //have space
            MyAlert myAlert = new MyAlert();
            myAlert.myDialog(this, "มีช่องว่าง", "กรุณากรอกข้อมูลให้ครบ");
        }else {
            CheckUser();
        }
    }

    private void CheckUser(){
        try{
            SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(MySQLiteOpenHelper.DATABASE_NAME, MODE_PRIVATE, null);
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM userTABLE WHERE User = " + "'" + userString + "'", null);
            cursor.moveToFirst();
            String[] resultString = new String[cursor.getColumnCount()];
            for (int i=0;i<cursor.getColumnCount();i++){
                resultString[i] = cursor.getString(i);
            }
            cursor.close();

            //check password
            if (passwordString.equals(resultString[2])){
                Toast.makeText(this, "ยินดีต้อนรับ" + resultString[3], Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, ShowProduct.class);
                intent.putExtra("Result", resultString);
                startActivity(intent);
            } else {
                MyAlert myAlert = new MyAlert();
                myAlert.myDialog(this, "รหัสผ่านไม่ถูกต้อง", "กรุณาใส่รหัสผ่านอีกครั้ง");
            }   //check password
        } catch (Exception e){
            MyAlert myAlert = new MyAlert();
            myAlert.myDialog(this, "ไม่มี user นี้", "ไม่มี" + userString + "ไม่มีในฐานข้อมูลของเรา");
        }
    } //CheckUser


    private void synAndDelete(){
        SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(MySQLiteOpenHelper.DATABASE_NAME, MODE_PRIVATE, null);
        sqLiteDatabase.delete(MySQLite.user_table,null,null);
        MySynJSON mySynJSON = new MySynJSON();
        mySynJSON.execute();
    }

    public class MySynJSON extends AsyncTask<Void, Void, String>{
        @Override
        protected String doInBackground(Void... voids){
            try {
                String strURL ="http://www.csclub.ssru.ac.th/s56122201006/php_get_userTABLE.php";
                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(strURL).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();
            }catch (Exception e){
                Log.d("Sahapad","doInBack ==>" + e.toString());
                return null;
            }
        }
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            Log.d("Sahapad", "strJSON ==>" + s);
            try {
                JSONArray jsonArray = new JSONArray(s);
                for (int i=0; i<jsonArray.length(); i++){

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String strUser = jsonObject.getString(MySQLite.column_user);
                    String strPassword = jsonObject.getString(mySQLite.column_password);
                    String strName = jsonObject.getString(mySQLite.column_name);
                    mySQLite.addNewUser(strUser, strPassword, strName);
                }
                Toast.makeText(MainActivity.this,"Sunchronize mySQL to SQLite Finsh", Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Log.d("Sahapad", "onPost ==>" + e.toString());
            }
        }
    }

    private void testAddValue(){
        objUserTABLE.addNewUser("Yu", "uuy11", "Yupa");
        objFoodTABLE.addNewFood("JA", "GGwp", "Mai");
        objOrderTABLE.addOrder("New", "Newbie", "NewbieT", "Eiei");
    }//testAddValue
    private void connectedSQLite(){
        objUserTABLE = new  UserTable(this);
        objFoodTABLE = new FoodTABLE(this);
        objOrderTABLE = new OrderTABLE(this);
    }//connectedSQLite
}//Main Class