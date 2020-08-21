package com.example.c302_p09_mcafe;

import androidx.appcompat.app.AppCompatActivity;
import cz.msebera.android.httpclient.Header;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DisplayMenuItemsActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<MenuCategoryItem> adapter;
    private ArrayList<MenuCategoryItem> list;
    private AsyncHttpClient client;

    String catID, loginId, apikey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_menu_items);

        listView = (ListView) findViewById(R.id.listViewItems);
        list = new ArrayList<MenuCategoryItem>();
        adapter = new ArrayAdapter<MenuCategoryItem>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        Intent i = getIntent();
        catID = i.getStringExtra("catID");
        loginId = i.getStringExtra("loginId");
        apikey = i.getStringExtra("apikey");

        client = new AsyncHttpClient();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MenuCategoryItem selected = list.get(position);

                //TODO: make Intent to DisplayMenuItemsActivity passing the categoryId
                Intent i = new Intent(DisplayMenuItemsActivity.this,ViewMenuItemActivity.class);
                i.putExtra("menuID",selected.getId());
                i.putExtra("catID", selected.getCategoryId());
                i.putExtra("des",selected.getDescription());
                i.putExtra("price",selected.getUnitPrice());
                i.putExtra("loginId",loginId);
                i.putExtra("apikey",apikey);
                startActivity(i);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        list.clear();

        RequestParams params = new RequestParams();
        params.add("categoryId", catID);
        params.add("loginId", loginId);
        params.add("apikey", apikey);

        client.post("http://10.0.2.2/C302_P09_mCafe/getMenuItemsByCategory.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObj = response.getJSONObject(i);

                        String id = jsonObj.getString("menu_item_id");
                        String categoryID = jsonObj.getString("menu_item_category_id");
                        String description = jsonObj.getString("menu_item_description");
                        Double price = Double.parseDouble(jsonObj.getString("menu_item_unit_price"));

                        MenuCategoryItem menuCategoryItem = new MenuCategoryItem(id, categoryID, description, price);
                        list.add(menuCategoryItem);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter = new ArrayAdapter<MenuCategoryItem>(DisplayMenuItemsActivity.this, android.R.layout.simple_list_item_1, list);
                listView.setAdapter(adapter);


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.submain, menu);
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
        else if (id == R.id.menu_addmenuitem){
            Intent i = new Intent(getApplicationContext(),AddMenuItemActivity.class);
            i.putExtra("catID",catID);
            i.putExtra("loginId",loginId);
            i.putExtra("apikey",apikey);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}