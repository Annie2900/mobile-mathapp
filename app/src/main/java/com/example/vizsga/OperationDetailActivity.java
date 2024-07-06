package com.example.vizsga;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import java.util.Calendar;
import java.util.Random;

public class OperationDetailActivity extends AppCompatActivity {

    private static final int MENU_ITEM_ID_SINGLE_BUTTON = R.id.action_single_button;
    private static final long DELAY_MILLIS = 3000;
    private static final int MENU_ITEM_ID_SETTINGS = R.id.settings;

    private TextView resultTextView;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation_detail);

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

        LinearLayout mainLayout = findViewById(R.id.main2);
        if (hourOfDay >= 6 && hourOfDay < 18) {
            mainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.babyblue));
        } else {
            mainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.babybluedark));
        }

        resultTextView = findViewById(R.id.textView_result);

        if (getIntent().hasExtra("operation")) {
            Operation operation = getIntent().getParcelableExtra("operation");
            if (operation != null) {
                displayOperation(operation);
            }
        }

        handler = new Handler(Looper.getMainLooper());
        Button giveUpButton = findViewById(R.id.giveup_btn);
        giveUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int answer = Integer.parseInt(resultTextView.getText().toString());
                String sorryAnswerText = getString(R.string.giveUp, answer);
                Toast.makeText(OperationDetailActivity.this, sorryAnswerText, Toast.LENGTH_SHORT).show();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        navigateToMainActivity();
                    }
                }, DELAY_MILLIS);
            }
        });
        Button submitButton = findViewById(R.id.submit_btn);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });

        RadioButton radioButton1 = findViewById(R.id.radioButton1);
        RadioButton radioButton2 = findViewById(R.id.radioButton2);
        RadioButton radioButton3 = findViewById(R.id.radioButton3);

        radioButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton2.setChecked(false);
                radioButton3.setChecked(false);
            }
        });

        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton1.setChecked(false);
                radioButton3.setChecked(false);
            }
        });

        radioButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton1.setChecked(false);
                radioButton2.setChecked(false);
            }
        });


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
            Intent intent = new Intent(OperationDetailActivity.this, AccountActivity.class);
            startActivity(intent);
            return true;
        } else if (id == MENU_ITEM_ID_SETTINGS) {
            Intent intent = new Intent(OperationDetailActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayOperation(Operation operation) {
        int a = operation.getA();
        int b = operation.getB();
        int result = 0;

        displayNumber(a, R.id.imageView1);

        clearOperationImageView(R.id.imageView3);

        switch (operation.getOp()) {
            case "+":
                ((ImageView) findViewById(R.id.imageView3)).setImageResource(R.drawable.plus);
                findViewById(R.id.imageView3).setVisibility(View.VISIBLE);
                break;
            case "-":
                ((ImageView) findViewById(R.id.imageView3)).setImageResource(R.drawable.minus);
                findViewById(R.id.imageView3).setVisibility(View.VISIBLE);
                break;
            case "*":
                ((ImageView) findViewById(R.id.imageView3)).setImageResource(R.drawable.multiply);
                findViewById(R.id.imageView3).setVisibility(View.VISIBLE);
                break;
            case "/":
                ((ImageView) findViewById(R.id.imageView3)).setImageResource(R.drawable.division);
                findViewById(R.id.imageView3).setVisibility(View.VISIBLE);
                break;
            default:
                findViewById(R.id.imageView3).setVisibility(View.GONE);
                break;
        }

        displayNumber(b, R.id.imageView4);

        ((ImageView) findViewById(R.id.imageView6)).setImageResource(R.drawable.equal);
        findViewById(R.id.imageView6).setVisibility(View.VISIBLE);

        switch (operation.getOp()) {
            case "+":
                result = a + b;
                break;
            case "-":
                result = a - b;
                break;
            case "*":
                result = a * b;
                break;
            case "/":
                if (b != 0) {
                    result = a / b;
                } else {
                    Toast.makeText(OperationDetailActivity.this, "Cannot divide by zero", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                // default
                break;
        }
        resultTextView.setText(String.valueOf(result));
        displayRadioButtonOptions(result);
    }

    private void clearOperationImageView(int imageViewId) {
        ImageView imageView = findViewById(imageViewId);
        if (imageView != null) {
            imageView.setImageDrawable(null);
        }
    }

    private void displayNumber(int number, int startImageViewId) {
        String numberString = String.valueOf(number);
        int imageViewId = startImageViewId;

        int numDigits = numberString.length();

        for (int i = startImageViewId; i < startImageViewId + 3; i++) {
            ImageView imageView = findViewById(i);
            if (imageView != null) {
                imageView.setVisibility(View.GONE);
            }
        }

        for (int i = 0; i < numDigits; i++) {
            char digitChar = numberString.charAt(i);
            int digit = Character.getNumericValue(digitChar);
            int drawableId = getDrawableIdForDigit(digit);

            ImageView imageView = findViewById(imageViewId);
            if (imageView != null) {
                imageView.setImageResource(drawableId);
                imageView.setVisibility(View.VISIBLE);
            }

            imageViewId++;
        }
    }

    private int getDrawableIdForDigit(int digit) {
        switch (digit) {
            case 0:
                return R.drawable.zero;
            case 1:
                return R.drawable.one;
            case 2:
                return R.drawable.two;
            case 3:
                return R.drawable.three;
            case 4:
                return R.drawable.four;
            case 5:
                return R.drawable.five;
            case 6:
                return R.drawable.six;
            case 7:
                return R.drawable.seven;
            case 8:
                return R.drawable.eight;
            case 9:
                return R.drawable.nine;
            default:
                return R.drawable.zero;
        }
    }

    private int correctAnswer;
    private int incorrectAnswer1;
    private int incorrectAnswer2;

    private void displayRadioButtonOptions(int result) {
        Random random = new Random();
        correctAnswer = result;
        incorrectAnswer1 = result + random.nextInt(5) - 2;
        incorrectAnswer2 = result + random.nextInt(5) - 2;

        while (incorrectAnswer1 == correctAnswer || incorrectAnswer2 == correctAnswer || incorrectAnswer1 == incorrectAnswer2) {
            incorrectAnswer1 = result + random.nextInt(5) - 2;
            incorrectAnswer2 = result + random.nextInt(5) - 2;
        }

        int[] positions = {1, 2, 3};
        shuffleArray(positions);

        switch (positions[0]) {
            case 1:
                setImagesForOption(R.id.radioButton1, R.id.imageRight1, R.id.imageRight2, correctAnswer);
                ((RadioButton) findViewById(R.id.radioButton1)).setText(String.valueOf(correctAnswer));
                break;
            case 2:
                setImagesForOption(R.id.radioButton2, R.id.imageRight3, R.id.imageRight4, correctAnswer);
                ((RadioButton) findViewById(R.id.radioButton2)).setText(String.valueOf(correctAnswer));
                break;
            case 3:
                setImagesForOption(R.id.radioButton3, R.id.imageRight5, R.id.imageRight6, correctAnswer);
                ((RadioButton) findViewById(R.id.radioButton3)).setText(String.valueOf(correctAnswer));
                break;
        }

        switch (positions[1]) {
            case 1:
                setImagesForOption(R.id.radioButton1, R.id.imageRight1, R.id.imageRight2, incorrectAnswer1);
                ((RadioButton) findViewById(R.id.radioButton1)).setText(String.valueOf(incorrectAnswer1));
                break;
            case 2:
                setImagesForOption(R.id.radioButton2, R.id.imageRight3, R.id.imageRight4, incorrectAnswer1);
                ((RadioButton) findViewById(R.id.radioButton2)).setText(String.valueOf(incorrectAnswer1));
                break;
            case 3:
                setImagesForOption(R.id.radioButton3, R.id.imageRight5, R.id.imageRight6, incorrectAnswer1);
                ((RadioButton) findViewById(R.id.radioButton3)).setText(String.valueOf(incorrectAnswer1));
                break;
        }

        switch (positions[2]) {
            case 1:
                setImagesForOption(R.id.radioButton1, R.id.imageRight1, R.id.imageRight2, incorrectAnswer2);
                ((RadioButton) findViewById(R.id.radioButton1)).setText(String.valueOf(incorrectAnswer2));
                break;
            case 2:
                setImagesForOption(R.id.radioButton2, R.id.imageRight3, R.id.imageRight4, incorrectAnswer2);
                ((RadioButton) findViewById(R.id.radioButton2)).setText(String.valueOf(incorrectAnswer2));
                break;
            case 3:
                setImagesForOption(R.id.radioButton3, R.id.imageRight5, R.id.imageRight6, incorrectAnswer2);
                ((RadioButton) findViewById(R.id.radioButton3)).setText(String.valueOf(incorrectAnswer2));
                break;
        }
    }


    private void setImagesForOption(int radioButtonId, int imageView1Id, int imageView2Id, int number) {
        String numberString = String.valueOf(number);
        ImageView imageView1 = findViewById(imageView1Id);
        ImageView imageView2 = findViewById(imageView2Id);

        if (numberString.length() == 1) {
            // Single digit
            int digit = Character.getNumericValue(numberString.charAt(0));
            imageView1.setImageResource(getDrawableIdForDigit(digit));
            imageView1.setVisibility(View.VISIBLE);
            imageView2.setVisibility(View.GONE);
        } else if (numberString.length() == 2) {
            // Two digits
            int firstDigit = Character.getNumericValue(numberString.charAt(0));
            int secondDigit = Character.getNumericValue(numberString.charAt(1));
            imageView1.setImageResource(getDrawableIdForDigit(firstDigit));
            imageView2.setImageResource(getDrawableIdForDigit(secondDigit));
            imageView1.setVisibility(View.VISIBLE);
            imageView2.setVisibility(View.VISIBLE);
        }
    }

    private void shuffleArray(int[] array) {
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(OperationDetailActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToResultActivity(boolean isCorrect) {
        Intent intent = new Intent(OperationDetailActivity.this, ResultActivity.class);
        intent.putExtra("isCorrect", isCorrect);
        intent.putExtra("correctAnswer", correctAnswer);
        intent.putExtra("correctAnswerDrawable", getDrawableIdForDigit(correctAnswer));
        startActivity(intent);
        finish();
    }

    private void checkAnswer() {
        RadioButton selectedRadioButton = findViewById(getSelectedRadioButtonId());

        if (selectedRadioButton != null) {
            int selectedAnswer = Integer.parseInt(selectedRadioButton.getText().toString());

            boolean isCorrect = (selectedAnswer == correctAnswer);

            navigateToResultActivity(isCorrect);
        } else {
            Toast.makeText(OperationDetailActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
        }
    }

    private int getSelectedRadioButtonId() {
        RadioButton radioButton1 = findViewById(R.id.radioButton1);
        RadioButton radioButton2 = findViewById(R.id.radioButton2);
        RadioButton radioButton3 = findViewById(R.id.radioButton3);

        if (radioButton1.isChecked()) {
            return R.id.radioButton1;
        } else if (radioButton2.isChecked()) {
            return R.id.radioButton2;
        } else if (radioButton3.isChecked()) {
            return R.id.radioButton3;
        }

        return -1;
    }
}