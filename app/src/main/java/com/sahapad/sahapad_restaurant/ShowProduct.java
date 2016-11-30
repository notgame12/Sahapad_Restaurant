package com.sahapad.sahapad_restaurant;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

public class ShowProduct extends AppCompatActivity {

    //Explicit
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product);

        listView = (ListView) findViewById(R.id.listView);

        SysJSON synJSON = new SysJSON();
        synJSON.execute();

    }

    public class SysJSON extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {

            try {

                String strURL = "http://www.csclub.ssru.ac.th/s56122201006/php_get_foodTABLE.php";
                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(strURL).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();
            } catch (Exception e) {
                Log.d("Restaurant", "doIn ==> " + e.toString());
                return null;
            }
        } // doInBackground
        @Override
        protected void onPostExecute(String s) {
            try {
                Log.d("Restaurant", "Response ==> " + s);
                JSONArray jsonArray = new JSONArray(s);

                String[] iconStrings = new String[jsonArray.length()];
                String[] titleStrings = new String[jsonArray.length()];
                String[] priceStrings = new String[jsonArray.length()];

                for (int i=0;i<jsonArray.length();i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    iconStrings[i] = jsonObject.getString("Source");
                    titleStrings[i] = jsonObject.getString("Food");
                    priceStrings[i] = jsonObject.getString("Price");
                }

                productAdapter productAdapter = new productAdapter(ShowProduct.this,
                        iconStrings, titleStrings, priceStrings);
                listView.setAdapter(productAdapter);
            } catch (Exception e){
                Log.d("Restaurant", "onPost ==> " + e.toString());
            }

        }
    }
}
