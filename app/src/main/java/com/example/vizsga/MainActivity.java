package com.example.vizsga;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OperationAdapter.OnItemClickListener {

    RecyclerView recyclerView;
    private static final int MENU_ITEM_ID_SETTINGS = R.id.settings;
    private static final int MENU_ITEM_ID_SINGLE_BUTTON = R.id.action_single_button;
    private List<Operation> operationList = new ArrayList<>();
    private OperationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.red));

        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        LinearLayout mainLayout = findViewById(R.id.main);

        if (hourOfDay >= 6 && hourOfDay < 18) {
            mainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.babyblue));
        } else {
            mainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.babybluedark));
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setLogo(R.drawable.numbers);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new OperationAdapter(operationList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(this);

        parseJson();
    }

    private void parseJson() {
        try {
            JSONObject jsonObject = new JSONObject(Utils.OWM_JSON);
            JSONArray jsonArray = jsonObject.getJSONArray("json2");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonOperation = jsonArray.getJSONObject(i);
                int a = jsonOperation.getInt(Utils.OWM_A);
                int b = jsonOperation.getInt(Utils.OWM_B);
                String op = jsonOperation.getString(Utils.OWM_OP);

                Operation operation = new Operation(a, b, op);
                operationList.add(operation);
            }

            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            Log.e("MainActivity", "Error parsing JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == MENU_ITEM_ID_SINGLE_BUTTON) {
            Intent intent = new Intent(MainActivity.this, AccountActivity.class);
            startActivity(intent);
            return true;
        } else if (id == MENU_ITEM_ID_SETTINGS) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(MainActivity.this, OperationDetailActivity.class);
        intent.putExtra("operation", operationList.get(position));
        startActivity(intent);
        overridePendingTransition(R.anim.main_in, R.anim.splash_out);
    }
}
