package com.example.vizsga;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class ResultActivity extends AppCompatActivity {

    private static final int MENU_ITEM_ID_SINGLE_BUTTON = R.id.action_single_button;
    private static final int MENU_ITEM_ID_SETTINGS = R.id.settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_result);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setLogo(R.drawable.numbers);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

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
        Intent intent = getIntent();
        boolean isCorrect = intent.getBooleanExtra("isCorrect", false);
        int correctAnswer = intent.getIntExtra("correctAnswer", 0);

        ImageView imageView7 = findViewById(R.id.imageView7);
        ImageView imageView8 = findViewById(R.id.imageView8);
        imageView7.setVisibility(View.GONE);
        imageView8.setVisibility(View.GONE);

        String correctAnswerString = String.valueOf(correctAnswer);
        if (correctAnswerString.length() == 1) {
            imageView7.setImageResource(getDrawableIdForDigit(correctAnswer));
        } else if (correctAnswerString.length() == 2) {
            imageView7.setImageResource(getDrawableIdForDigit(correctAnswerString.charAt(0) - '0'));
            imageView8.setImageResource(getDrawableIdForDigit(correctAnswerString.charAt(1) - '0'));
        }

        TextView textView4 = findViewById(R.id.textView4);
        TextView textView5 = findViewById(R.id.textView5);
        textView4.setVisibility(View.GONE);
        textView5.setVisibility(View.GONE);

        new Handler().postDelayed(() -> {
            imageView7.setVisibility(View.VISIBLE);
            if (correctAnswerString.length() == 2) {
                imageView8.setVisibility(View.VISIBLE);
            }

            if (isCorrect) {
                textView4.setVisibility(View.VISIBLE);
                textView5.setVisibility(View.GONE);
            } else {
                textView4.setVisibility(View.GONE);
                textView5.setVisibility(View.VISIBLE);
            }
        }, 2000);

        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToMainActivity();
            }
        });
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(ResultActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.main_in, R.anim.splash_out);
        finish();
    }

    private int getDrawableIdForDigit(int digit) {
        switch (digit) {
            case 0: return R.drawable.zero;
            case 1: return R.drawable.one;
            case 2: return R.drawable.two;
            case 3: return R.drawable.three;
            case 4: return R.drawable.four;
            case 5: return R.drawable.five;
            case 6: return R.drawable.six;
            case 7: return R.drawable.seven;
            case 8: return R.drawable.eight;
            case 9: return R.drawable.nine;
            default: return R.drawable.zero;
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
            Intent intent = new Intent(ResultActivity.this, AccountActivity.class);
            startActivity(intent);
            return true;
        } else if (id == MENU_ITEM_ID_SETTINGS) {
            Intent intent = new Intent(ResultActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
