package com.example.c302_p09_mcafe;

import androidx.appcompat.app.AppCompatActivity;
import cz.msebera.android.httpclient.Header;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

public class AddMenuItemActivity extends AppCompatActivity {

    EditText etItem, etprice;
    Button btnAdd;
    private AsyncHttpClient client;
    String loginID, apikey, catID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_item);
        client = new AsyncHttpClient();

        etItem = findViewById(R.id.etItem);
        etprice = findViewById(R.id.etPrice);
        btnAdd = findViewById(R.id.btnAdd);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        loginID = pref.getString("loginID","");
        apikey = pref.getString("apiKey","");

        Intent i = getIntent();
        catID = i.getStringExtra("catID");

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestParams params = new RequestParams();
                params.add("menu_item_category_id",catID);
                params.add("menu_item_description",etItem.getText().toString());
                params.add("menu_item_unit_price",etprice.getText().toString());
                params.add("loginId", loginID);
                params.add("apikey", apikey);

                client.post("http://10.0.2.2/C302_P09_mCafe/addMenuItem.php", params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            Log.i("JSON Results: ", response.toString());
                            Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                            finish();
                        } catch (JSONException e){
                            e.printStackTrace();

                        }
                    }
                });

            }
        });



    }
}