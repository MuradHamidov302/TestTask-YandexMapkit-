package m.h.testapp.login;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import m.h.testapp.R;
import m.h.testapp.map.MapActivity;
import m.h.testapp.retrofit.BASE_URL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhoneConfirmActivity extends AppCompatActivity {

    Button btnconfirm;
    ProgressBar progressBar;
    EditText etcomfrime;
    TextView textCounter;
    ImageButton imgbtnback;
    int pr=1;

    private ProgressDialog progressDialog;

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

//progresbar timer run method call--------
        progressBar = (ProgressBar)findViewById(R.id.progressbar);
        textCounter = (TextView)findViewById(R.id.counter);
        ProgresBarRun();

        //btn confrme code send------------------------------------------------
        Intent intent = getIntent();
       final String phoneI = intent.getStringExtra("phone");

        etcomfrime=findViewById(R.id.etcomfrime);
        btnconfirm=findViewById(R.id.btnconfirm);
        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isEmpty(etcomfrime)) {
                    String confirm_code=etcomfrime.getText().toString();
                   LoginConfirmMethod(phoneI,confirm_code);

                }else {
                    etcomfrime.requestFocus();
                    etcomfrime.setError("empty");
                }

            }
        });

    }


    //checkempty method-------------------------------------
    private boolean isEmpty(EditText edittext) {
        if (edittext.getText().toString().trim().length() > 0)
            return false;

        return true;
    }
    //------------------------------------------------


    //progresbar timer run method --------
    void ProgresBarRun(){
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
    }
    //--------------------------------------------------



    //log confirm method----------------------------------------------------

    private void LoginConfirmMethod(String phoneNum,String confirmCode) {

        progressDialog = new ProgressDialog(PhoneConfirmActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        //call request----------------------------------------------------
        Call<ResponseLogin> call = BASE_URL.retrofitServicesLogin.phoneconfirme(phoneNum,confirmCode);
        Log.w("request login", call.request().toString());


        call.enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                progressDialog.dismiss();

                if (response.isSuccessful()) {
                    ResponseLogin responseLogin=response.body();
                    Toast.makeText(getApplicationContext(),"true",Toast.LENGTH_SHORT).show();
                    String token=responseLogin.getToken();
                        AlertDialog.Builder builder = new AlertDialog.Builder(PhoneConfirmActivity.this);
                        builder.setMessage("Good Login.\ntoken : "+token+"\nClick Ok go map Activity")
                                .setCancelable(false)
                                .setPositiveButton("OK ",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        startActivity(new Intent(getApplicationContext(),MapActivity.class));
                                    }
                                });
                        AlertDialog alert=builder.create();
                        alert.show();

                } else {
                    Toast.makeText(getApplicationContext(), "false" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"fail error",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
        //----------------------------------------------------------------


    }



}