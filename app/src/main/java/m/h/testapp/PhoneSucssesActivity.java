package m.h.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PhoneSucssesActivity extends AppCompatActivity {

    Button btnconfirm;
    ProgressBar progressBar;
    TextView textCounter;
    ImageButton imgbtnback;
    MyCountDownTimer myCountDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_sucsses);

        imgbtnback=findViewById(R.id.imgbtnback);
        imgbtnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });

        progressBar = (ProgressBar)findViewById(R.id.progressbar);
        textCounter = (TextView)findViewById(R.id.counter);
        progressBar.setProgress(100);
        myCountDownTimer = new MyCountDownTimer(60, 1);
        myCountDownTimer.start();



        btnconfirm=findViewById(R.id.btnconfirm);
        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MapActivity.class));
            }
        });

    }

public class MyCountDownTimer extends CountDownTimer {

    public MyCountDownTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        textCounter.setText(String.valueOf(millisUntilFinished));
        int progress = (int) (millisUntilFinished-1);
        progressBar.setProgress(progress);
    }

    @Override
    public void onFinish() {
        textCounter.setText("00:00");
        progressBar.setProgress(0);
    }

}
}