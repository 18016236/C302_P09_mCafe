package com.example.c302_p09_mcafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.*;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<MenuCategory> adapter;
    private ArrayList<MenuCategory> list;
    private AsyncHttpClient client;

    String loginID, apikey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listViewMenuCategories);
        list = new ArrayList<MenuCategory>();
        adapter = new ArrayAdapter<MenuCategory>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        client = new AsyncHttpClient();

        //TODO: read loginId and apiKey from SharedPreferences
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        loginID = pref.getString("loginID","");
        apikey = pref.getString("apiKey","");

        // TODO: if loginId and apikey is empty, go back to LoginActivity
        if (loginID.equalsIgnoreCase("") || apikey.equalsIgnoreCase("")){
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        }
		//TODO: Point X - call getMenuCategories.php to populate the list view
        RequestParams params = new RequestParams();
        params.add("loginId", loginID);
        params.add("apikey", apikey);

        client.post("http://10.0.2.2/C302_P09_mCafe/getMenuCategories.php",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                try{
                    Log.i("JSON Results: ",response.toString());

                    for (int i =0; i< response.length(); i++){
                        JSONObject jsonObj = response.getJSONObject(i);

                        String categoryID = jsonObj.getString("menu_item_category_id");
                        String description = jsonObj.getString("menu_item_category_description");

                        MenuCategory menuCategory = new MenuCategory(categoryID,description);
                        list.add(menuCategory);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter = new ArrayAdapter<MenuCategory>(MainActivity.this, android.R.layout.simple_list_item_1, list);
                listView.setAdapter(adapter);

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MenuCategory selected = list.get(position);

				//TODO: make Intent to DisplayMenuItemsActivity passing the categoryId
                Intent i = new Intent(MainActivity.this,DisplayMenuItemsActivity.class);
                i.putExtra("catID",selected.getCategoryId());
                i.putExtra("loginId",loginID);
                i.putExtra("apikey",apikey);
                startActivity(i);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_logout) {
            // TODO: Clear SharedPreferences
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            pref.edit().clear().apply();
            // TODO: Redirect back to login screen
            Intent i = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
