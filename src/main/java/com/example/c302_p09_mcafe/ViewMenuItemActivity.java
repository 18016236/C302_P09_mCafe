package com.example.c302_p09_mcafe;

import androidx.appcompat.app.AppCompatActivity;
import cz.msebera.android.httpclient.Header;

import android.content.Intent;
import android.os.Bundle;
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

public class ViewMenuItemActivity extends AppCompatActivity {

    Button btnUpdate, btnDelete;
    EditText etDes, etPrice;

    String id,catID,des,loginId, apikey;
    Double price;
    private AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_menu_item);

        client = new AsyncHttpClient();

        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        etDes = findViewById(R.id.etViewDes);
        etPrice = findViewById(R.id.etViewprice);

        Intent i = getIntent();

        id = i.getStringExtra("menuID");
        catID = i.getStringExtra("catID");
        des = i.getStringExtra("des");
        price = i.getDoubleExtra("price",0.0);
        loginId = i.getStringExtra("loginId");
        apikey = i.getStringExtra("apikey");

        etPrice.setText(price + "");
        etDes.setText(des);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestParams params = new RequestParams();
                params.add("menu_item_id", id);
                params.add("menu_item_category_id",catID);
                params.add("menu_item_description",etDes.getText().toString());
                params.add("menu_item_unit_price",etPrice.getText().toString());
                params.add("loginId", loginId);
                params.add("apikey", apikey);


                client.post("http://10.0.2.2/C302_P09_mCafe/updateMenuItemById.php",params,new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try{
                            Log.i("JSON Results: ",response.toString());

                            Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(ViewMenuItemActivity.this, "Record successfully updated", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestParams params = new RequestParams();
                params.add("menu_item_id",id);
                params.add("loginId",loginId);
                params.add("apikey",apikey);

                client.post("http://10.0.2.2/C302_P09_mCafe/deleteMenuItemById.php",params, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            Toast.makeText(ViewMenuItemActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
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