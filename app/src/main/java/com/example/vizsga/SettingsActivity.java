package com.example.vizsga;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import java.util.Calendar;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    private static final int MENU_ITEM_ID_SETTINGS = R.id.settings;
    private static final int MENU_ITEM_ID_SINGLE_BUTTON = R.id.action_single_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        RadioButton radioButtonEnglish = findViewById(R.id.radioButton);
        RadioButton radioButtonHungarian = findViewById(R.id.radioButton4);

        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "en");

        if (language.equals("en")) {
            radioButtonEnglish.setChecked(true);
        } else if (language.equals("hu")) {
            radioButtonHungarian.setChecked(true);
        } else {
            radioButtonEnglish.setChecked(true);
        }

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioButton) {
                setLocale("en");
            } else if (checkedId == R.id.radioButton4) {
                setLocale("hu");
            }
        });

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
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = new Configuration();
        config.locale = locale;
        resources.updateConfiguration(config, resources.getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();

        Intent intent = new Intent(this, SettingsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
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
            Intent intent = new Intent(SettingsActivity.this, AccountActivity.class);
            startActivity(intent);
            return true;
        } else if (id == MENU_ITEM_ID_SETTINGS) {
            Intent intent = new Intent(SettingsActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
