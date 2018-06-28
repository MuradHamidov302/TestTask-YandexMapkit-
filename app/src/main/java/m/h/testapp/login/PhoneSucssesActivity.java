package m.h.testapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import m.h.testapp.R;
import m.h.testapp.map.MapActivity;

public class PhoneSucssesActivity extends AppCompatActivity {

    Button btnconfirm;
    ProgressBar progressBar;
    EditText etcomfrime;
    TextView textCounter;
    ImageButton imgbtnback;
    int pr=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_sucsses);

        etcomfrime=findViewById(R.id.etcomfrime);

        imgbtnback=findViewById(R.id.imgbtnback);
        imgbtnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });

        progressBar = (ProgressBar)findViewById(R.id.progressbar);
        textCounter = (TextView)findViewById(R.id.counter);


        CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {

                textCounter.setText("00 : " + millisUntilFinished / 1000);
                progressBar.setProgress(pr++);
            }

            public void onFinish() {

                textCounter.setText("Time Up!");
                btnconfirm.setEnabled(false);
                btnconfirm.setBackgroundResource(R.drawable.btngraybckr);
                etcomfrime.setEnabled(false);
                progressBar.setProgress(60);


            }
        };

        countDownTimer.start();



        btnconfirm=findViewById(R.id.btnconfirm);
        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MapActivity.class));
            }
        });

    }

}